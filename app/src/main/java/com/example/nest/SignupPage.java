package com.example.nest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SignupPage extends MsgVerification {
    private Button backBtn;

    private ImageButton inVisibleBtn;

    MyStatusBar myStatusBar = new MyStatusBar();

    private PhoneEditText phoneText;

    private EditText pwdText;

    private Button signupBtn;

    private  Button verificationButton;

    private EditText verificationText;

    private ImageButton visibleBtn;

    private  String phoneNum, password;

    public static String isUserExist = "2";

    private String resgister_url = "http://120.79.100.43/nest/NestAppRegister.php";

    private String userchecker_url = "http://120.79.100.43/nest/NestAppUserChecker.php";

    private void bindView() {
        this.phoneText = (PhoneEditText)findViewById(R.id.phoneText_up) ;
        this.verificationText = (EditText)findViewById(R.id.verificationText);
        this.pwdText = (EditText)findViewById(R.id.pwdText_up);
        this.verificationText = (EditText) findViewById(R.id.verificationText);
        this.signupBtn = (Button)findViewById(R.id.signupButton);
        this.verificationButton = (Button)findViewById(R.id.verificationButton);
        this.backBtn = (Button)findViewById(R.id.backButton);
        this.visibleBtn = (ImageButton)findViewById(R.id.visibleButton);
        this.inVisibleBtn = (ImageButton)findViewById(R.id.inVisibleButton);
    }

    private void initView() {
        this.visibleBtn.setVisibility(View.INVISIBLE);
        this.inVisibleBtn.setVisibility(View.INVISIBLE);
        this.inVisibleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                SignupPage.this.inVisibleBtn.setVisibility(View.INVISIBLE);
                SignupPage.this.visibleBtn.setVisibility(View.VISIBLE);
                SignupPage.this.pwdText.setInputType(144);
                SignupPage.this.pwdText.setSelection(SignupPage.this.pwdText.getText().length());
            }
        });
        this.visibleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                SignupPage.this.inVisibleBtn.setVisibility(View.VISIBLE);
                SignupPage.this.visibleBtn.setVisibility(View.INVISIBLE);
                SignupPage.this.pwdText.setInputType(129);
                SignupPage.this.pwdText.setSelection(SignupPage.this.pwdText.getText().length());
            }
        });
        this.backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                SignupPage.this.finish();
            }
        });
        this.verificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取验证码
                getCode(phoneText.getText().toString().replaceAll(" ",""));
            }
        });
        this.signupBtn.setOnClickListener(view -> {
            String code = verificationText.getText().toString().trim();
            if (TextUtils.isEmpty(phoneText.getText().toString().replaceAll(" ",""))) {
                Toast.makeText(SignupPage.this, "请填写手机号码", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(code)) {
                Toast.makeText(SignupPage.this, "请填写验证码", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(pwdText.getText().toString())) {
                Toast.makeText(SignupPage.this, "请填写登录密码", Toast.LENGTH_SHORT).show();
                return;
            } else if (pwdText.getText().toString().length() < 8 || pwdText.getText().toString().length() > 16) {
                Toast.makeText(SignupPage.this, "密码长度应在8-16位之间", Toast.LENGTH_SHORT).show();
                return;
            } else if (!NetworkChecker.isNetworkConnected(SignupPage.this)){
                Toast.makeText(SignupPage.this, "网络错误", Toast.LENGTH_SHORT).show();
                return;
            }
            // 验证 验证码是否正确
            phoneNum = phoneText.getText().toString().replaceAll(" ","");
            verifyCode(phoneNum, code);
        });
    }

    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.signup_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.myStatusBar.setStatusBar((Activity)this);
        bindView();
        initView();

        Handler existHandler = new Handler(){
            public void handleMessage(Message msg)
            {
                Toast.makeText(SignupPage.this, "该手机号已经注册了,无需重复注册", Toast.LENGTH_SHORT).show();
            }
        };

        Handler succeedHandler = new Handler(){
            public void handleMessage(Message msg)
            {
                Toast.makeText(SignupPage.this, "账号注册成功", Toast.LENGTH_SHORT).show();
            }
        };


        new Thread(){
        @Override
        public void run(){
            while (true)
            {
                if (isUserExist.equals("0")){
                    //获取输入框中的内容
                    password = pwdText.getText().toString();
                    //替换键值对，这里的键必须和接口中post传递的键一致
                    HttpPost.register_post(resgister_url, phoneNum, password);
                    SigninPage.phoneText_in.setText(phoneNum);
                    startActivity(new Intent(SignupPage.this, SigninPage.class));
                    succeedHandler.sendEmptyMessage(0);
                    finish();
                    break;
                } else if (isUserExist.equals("1")){
                    isUserExist = "2";
                    existHandler.sendEmptyMessage(0);
                    System.out.println(isUserExist);
                }
            }
        }
        }.start();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

    @Override
    protected boolean checkPhoneNum(String phone) {
        if (!TextUtils.isEmpty(phone) && phone.matches("^1[3-9][0-9]\\d{4,8}$") && (phone.length()==11)) {
            return true;
        } else {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    @Override
    protected void onSendCodeSucceeded() {
        Toast.makeText(this, "已成功发送验证码", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSendCodeFailed() {
        Toast.makeText(this, "发送验证码失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onVerifySucceeded() {
        HttpPost.userchecker_post(userchecker_url, phoneNum);
    }

    @Override
    protected void onVerifyFailed() {
        Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
    }
}
