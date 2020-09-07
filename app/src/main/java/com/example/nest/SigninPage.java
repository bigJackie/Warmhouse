package com.example.nest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class SigninPage extends AppCompatActivity {
    private Button forgetPWDBtn;

    private ImageButton inVisibleBtn;

    MyStatusBar myStatusBar = new MyStatusBar();

    private Button otherSigninBtn;

    public static PhoneEditText phoneText_in;

    private EditText pwdText;

    private ImageButton signinBtn;

    private Button signupBtn;

    private ImageButton visibleBtn;

    private String sign_in_url = "http://120.79.100.43/nest/NestAppUserSignIn.php";

    private String phoneNum, password;

    public static String  isUserChecked;

    private void bindView() {
        this.phoneText_in = (PhoneEditText)findViewById(R.id.phoneText);
        this.pwdText = (EditText)findViewById(R.id.pwdText);
        this.visibleBtn = (ImageButton)findViewById(R.id.visibleButton);
        this.inVisibleBtn = (ImageButton)findViewById(R.id.inVisibleButton);
        this.forgetPWDBtn = (Button)findViewById(R.id.forgetPWDButton);
        this.signupBtn = (Button)findViewById(R.id.signupButton);
        this.otherSigninBtn = (Button)findViewById(R.id.otherSigninButton);
        this.signinBtn = (ImageButton)findViewById(R.id.signinButton);
        isUserChecked = "2";
    }

    private void initView() {
        this.visibleBtn.setVisibility(View.INVISIBLE);
        this.inVisibleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                SigninPage.this.inVisibleBtn.setVisibility(View.INVISIBLE);
                SigninPage.this.visibleBtn.setVisibility(View.VISIBLE);
                SigninPage.this.pwdText.setInputType(144);
                SigninPage.this.pwdText.setSelection(SigninPage.this.pwdText.getText().length());
            }
        });
        this.visibleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                SigninPage.this.inVisibleBtn.setVisibility(View.VISIBLE);
                SigninPage.this.visibleBtn.setVisibility(View.INVISIBLE);
                SigninPage.this.pwdText.setInputType(129);
                SigninPage.this.pwdText.setSelection(SigninPage.this.pwdText.getText().length());
            }
        });
        this.forgetPWDBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (PrefUtils.isFastClick()) {
                    SigninPage signinActivity = SigninPage.this;
                    signinActivity.startActivity(new Intent((Context)signinActivity, ForgetPWDPage.class));
                }
            }
        });
        this.signupBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (PrefUtils.isFastClick()) {
                    SigninPage signinActivity = SigninPage.this;
                    signinActivity.startActivity(new Intent((Context)signinActivity, SignupPage.class));
                }
            }
        });
        this.otherSigninBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                PrefUtils.isFastClick();
            }
        });
        this.signinBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                phoneNum = phoneText_in.getText().toString().replaceAll(" ","");
                password = pwdText.getText().toString();
                if (TextUtils.isEmpty(phoneNum)){
                    Toast.makeText(SigninPage.this, "请填写手机号码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(SigninPage.this, "请填写登录密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!NetworkChecker.isNetworkConnected(SigninPage.this)){
                    Toast.makeText(SigninPage.this, "网络错误", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!phoneNum.matches("^1[3-9][0-9]\\d{4,8}$") || (phoneNum.length()!=11)){
                    Toast.makeText(SigninPage.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                } else if (PrefUtils.isFastClick()) {
                    HttpPost.usersign_in_post(sign_in_url, phoneNum, password);
                }
            }
        });
    }

    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.signin_page);
        getWindow().addFlags(1024);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.myStatusBar.setStatusBar((Activity)this);
        bindView();
        initView();

        if (((String) MySharedPreferences.getPhoneNum(SigninPage.this) != null)) phoneText_in.setText((String) MySharedPreferences.getPhoneNum(SigninPage.this));

        Handler existHandler = new Handler(){
            public void handleMessage(Message msg)
            {
                Toast.makeText(SigninPage.this, "该手机号未注册请先注册", Toast.LENGTH_SHORT).show();
            }
        };

        Handler errortHandler = new Handler(){
            public void handleMessage(Message msg)
            {
                Toast.makeText(SigninPage.this, "登录密码错误", Toast.LENGTH_SHORT).show();
            }
        };


        new Thread(){
            @Override
            public void run(){
                while (true)
                {
                    if (isUserChecked.equals("0")){
                        isUserChecked = "2";
                        existHandler.sendEmptyMessage(0);
                    } else if (isUserChecked.equals("1")){
                        MySharedPreferences.setPhoneNum(phoneNum, SigninPage.this);
                        MySharedPreferences.setPassword(password, SigninPage.this);
                        MySharedPreferences.setSignInStatus("1", SigninPage.this);
                        startActivity(new Intent(SigninPage.this, MainFragment.class));
                        finish();
                        break;
                    } else if (isUserChecked.equals("-1")){
                        isUserChecked = "2";
                        errortHandler.sendEmptyMessage(0);
                    }
                }
            }
        }.start();
    }

    //HOME返回桌面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
