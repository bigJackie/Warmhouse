package com.example.nest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.nest.home.HomePage;

public class SplashScreen extends AppCompatActivity {
    private ConstraintLayout splashPage;

    private String sign_in_status;

    private void jumpNextPage() {
        if (!PrefUtils.getBoolean(this, "is_user_guide_showed", false)) {
            startActivity(new Intent(this, GuidePages.class));
        } else {
            if (sign_in_status.equals("1")) startActivity(new Intent(this, MainFragment.class));
            else startActivity(new Intent(this, SigninPage.class));
        }
        finish();
    }

    private void startAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0F, 1.0F);
        alphaAnimation.setDuration(2000L);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation param1Animation) {
                SplashScreen.this.jumpNextPage();
            }

            public void onAnimationRepeat(Animation param1Animation) {}

            public void onAnimationStart(Animation param1Animation) {}
        });
        this.splashPage.startAnimation(alphaAnimation);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.splash_screen);
        getWindow().addFlags(1024);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.splashPage = findViewById(R.id.splash_page);
        startAnimation();
        sign_in_status = (String) MySharedPreferences.getSignInStatus(SplashScreen.this);
        if (sign_in_status == null) sign_in_status = "0";
    }
    //双击退出
    private long firstTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                System.exit(0);
            } else {
                Toast.makeText(SplashScreen.this, "再点一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
