package com.example.nest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.os.SystemClock;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.nest.fragment.MyFragmentPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainFragment extends AppCompatActivity {

    //UI Objects
    private FloatingActionButton home_btn, home_btn_l1, home_btn_l2, home_btn_r1, home_btn_r2;
    private ViewPager2 viewPager;

    private MyFragmentPagerAdapter mAdapter;

    private boolean hasBtnHide = false;

    //几个代表页面的常量
    public static final int PAGE_HOME = 0;
    public static final int PAGE_COMMUNITY = 1;
    public static final int PAGE_HEALTH = 2;
    public static final int PAGE_PERSON = 3;
    public static int PAGE_FOCUS = 0;

    private static final long CLICK_INTERVAL_TIME = 300;
    private static long lastClickTime = 0;

    private int[] btn_res;
    private int res_temp;

    public static Fragment[] fragmentArray;
    private Fragment fragment_temp;


    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);

        //状态栏透明
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.main_fragment);
        getWindow().addFlags(1024);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mAdapter = new MyFragmentPagerAdapter(this);
        bindView();
        initView();
    }

    private void bindView() {
        home_btn = findViewById(R.id.home_btn);
        home_btn_l1 = findViewById(R.id.home_btn_childl1);
        home_btn_l2 = findViewById(R.id.home_btn_childl2);
        home_btn_r1 = findViewById(R.id.home_btn_childr1);
        home_btn_r2 = findViewById(R.id.home_btn_childr2);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);

        btn_res = new int[]{R.drawable.home_btn_animate, R.drawable.community_btn_animate, R.drawable.health_btn_animate, R.drawable.person_btn_animate, R.drawable.guide_btn_animate,
                            R.drawable.community_btn_animate, R.drawable.community_btn_animate, R.drawable.health_btn_animate, R.drawable.person_btn_animate, R.drawable.guide_btn_animate,
                            R.drawable.health_btn_animate, R.drawable.community_btn_animate, R.drawable.health_btn_animate, R.drawable.person_btn_animate, R.drawable.guide_btn_animate,
                            R.drawable.person_btn_animate, R.drawable.community_btn_animate, R.drawable.health_btn_animate, R.drawable.person_btn_animate, R.drawable.guide_btn_animate,};
        fragmentArray = new Fragment[20];
    }

    private void initView() {
        childBtnHide();
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0 :
                        PAGE_FOCUS = 0;
                        btnSetRes(PAGE_FOCUS);
                        switch_animation();
                        break;
                    case 1 :
                        PAGE_FOCUS = 1;
                        btnSetRes(PAGE_FOCUS);
                        switch_animation();
                        break;
                    case 2 :
                        PAGE_FOCUS = 2;
                        btnSetRes(PAGE_FOCUS);
                        switch_animation();
                        break;
                    case 3 :
                        PAGE_FOCUS = 3;
                        btnSetRes(PAGE_FOCUS);
                        switch_animation();
                        break;
                }
            }
        });

        viewPager.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    long currentTimeMillis = SystemClock.uptimeMillis();
                    //两次点击间隔时间小于300ms代表双击
                    if (currentTimeMillis - lastClickTime < CLICK_INTERVAL_TIME) {
                        if (!hasBtnHide) {
                            hide_animation();
                            hasBtnHide = true;
                        }
                        else {
                            display_animation();
                            hasBtnHide = false;
                        }
                    }
                    lastClickTime = currentTimeMillis;
                }
                childBtnHide();
                return false;
            }
        });

        home_btn.setOnLongClickListener(v -> {
            switch (PAGE_FOCUS){
                case 0:
                    guide_animation();
                    home_guide_init();
                    break;
                case 1:
                    guide_animation();
                    community_guide_init();
                    break;
                case 2:
                    guide_animation();
                    health_guide_init();
                    break;
                case 3:
                    guide_animation();
                    person_guide_init();
                    break;
            }
            return false;
        });

        home_btn.setOnClickListener(v -> {
            long currentTimeMillis = SystemClock.uptimeMillis();
            //两次点击间隔时间小于300ms代表双击
            if (currentTimeMillis - lastClickTime < CLICK_INTERVAL_TIME) {
                refresh_animation();
            }
            lastClickTime = currentTimeMillis;
        });
    }

    private void btnExchange(int ex, int page){
        res_temp = btn_res[page * 5];
        btn_res[page * 5] = btn_res[page*5 + ex];
        btn_res[page*5 + ex] = res_temp;
        fragment_temp = fragmentArray[page * 5];
        fragmentArray[page * 5] = fragmentArray[page*5 + ex];
        fragmentArray[page*5 + ex] = fragment_temp;
    }

    private void btnSetRes(int page){
        home_btn.setImageResource(btn_res[page * 5]);
        home_btn_l1.setImageResource(btn_res[page*5 + 1]);
        home_btn_l2.setImageResource(btn_res[page*5 + 2]);
        home_btn_r1.setImageResource(btn_res[page*5 + 3]);
        home_btn_r2.setImageResource(btn_res[page*5 + 4]);
    }

    private void home_guide_init() {
        home_btn.setOnClickListener(v -> mAdapter.homeContainer.changeChildFragment(fragmentArray[0]));
        home_btn_l1.setOnClickListener(v -> {
            mAdapter.homeContainer.changeChildFragment(fragmentArray[1]);
            btnExchange(1, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });
        home_btn_l2.setOnClickListener(v -> {
            mAdapter.homeContainer.changeChildFragment(fragmentArray[2]);
            btnExchange(2, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });

        home_btn_r1.setOnClickListener(v -> {
            mAdapter.homeContainer.changeChildFragment(fragmentArray[3]);
            btnExchange(3, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });
        home_btn_r2.setOnClickListener(v -> {
            mAdapter.homeContainer.changeChildFragment(fragmentArray[4]);
            btnExchange(4, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });
    }

    private void community_guide_init() {
        home_btn.setOnClickListener(v -> {

        });
        home_btn_l1.setOnClickListener(v -> {
            btnExchange(1, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });
        home_btn_l2.setOnClickListener(v -> {
            btnExchange(2, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });

        home_btn_r1.setOnClickListener(v -> {
            btnExchange(3, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });
        home_btn_r2.setOnClickListener(v -> {
            btnExchange(4, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });
    }

    private void health_guide_init() {
        home_btn_l1.setOnClickListener(v -> {
            btnExchange(1, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });
        home_btn_l2.setOnClickListener(v -> {
            btnExchange(2, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });

        home_btn_r1.setOnClickListener(v -> {
            btnExchange(3, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });
        home_btn_r2.setOnClickListener(v -> {
            btnExchange(4, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });
    }

    private void person_guide_init() {
        home_btn_l1.setOnClickListener(v -> {
            btnExchange(1, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });
        home_btn_l2.setOnClickListener(v -> {
            btnExchange(2, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });

        home_btn_r1.setOnClickListener(v -> {
            btnExchange(3, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });
        home_btn_r2.setOnClickListener(v -> {
            btnExchange(4, PAGE_FOCUS);
            btnSetRes(PAGE_FOCUS);
            refresh_animation();
        });
    }

    private void childBtnHide() {
        home_btn_l1.setEnabled(false);
        home_btn_l2.setEnabled(false);
        home_btn_r1.setEnabled(false);
        home_btn_r2.setEnabled(false);
        home_btn_l1.setVisibility(View.INVISIBLE);
        home_btn_l2.setVisibility(View.INVISIBLE);
        home_btn_r1.setVisibility(View.INVISIBLE);
        home_btn_r2.setVisibility(View.INVISIBLE);
    }

    private void childBtnDisplay() {
        home_btn_l1.setEnabled(true);
        home_btn_l2.setEnabled(true);
        home_btn_r1.setEnabled(true);
        home_btn_r2.setEnabled(true);
        home_btn_l1.setVisibility(View.VISIBLE);
        home_btn_l2.setVisibility(View.VISIBLE);
        home_btn_r1.setVisibility(View.VISIBLE);
        home_btn_r2.setVisibility(View.VISIBLE);
    }

    private void guide_animation() {
        childBtnDisplay();
        TranslateAnimation translateAnimation01 = new TranslateAnimation(300, 0, 0, 0);
        TranslateAnimation translateAnimation02 = new TranslateAnimation(125, 0, 220, 0);
        TranslateAnimation translateAnimation03 = new TranslateAnimation(-300, 0, 0, 0);
        TranslateAnimation translateAnimation04 = new TranslateAnimation(-125, 0, 220, 0);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(AnimationSet.RESTART);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.2f, 1.0f, 0.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AnimationSet animationSet01 = new AnimationSet(true);
        animationSet01.addAnimation(alphaAnimation);
        animationSet01.addAnimation(scaleAnimation);
        animationSet01.addAnimation(translateAnimation01);
        animationSet01.setInterpolator(new LinearInterpolator());
        animationSet01.setDuration(200);
        AnimationSet animationSet02 = new AnimationSet(true);
        animationSet02.addAnimation(alphaAnimation);
        animationSet02.addAnimation(scaleAnimation);
        animationSet02.addAnimation(translateAnimation02);
        animationSet02.setInterpolator(new LinearInterpolator());
        animationSet02.setDuration(200);
        AnimationSet animationSet03 = new AnimationSet(true);
        animationSet03.addAnimation(alphaAnimation);
        animationSet03.addAnimation(scaleAnimation);
        animationSet03.addAnimation(translateAnimation03);
        animationSet03.setInterpolator(new LinearInterpolator());
        animationSet03.setDuration(200);
        AnimationSet animationSet04 = new AnimationSet(true);
        animationSet04.addAnimation(alphaAnimation);
        animationSet04.addAnimation(scaleAnimation);
        animationSet04.addAnimation(translateAnimation04);
        animationSet04.setInterpolator(new LinearInterpolator());
        animationSet04.setDuration(200);
        home_btn_l1.startAnimation(animationSet01);
        home_btn_l2.startAnimation(animationSet02);
        home_btn_r1.startAnimation(animationSet03);
        home_btn_r2.startAnimation(animationSet04);
    }

    private void hide_animation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 300);
        translateAnimation.setDuration(700);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                home_btn.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        home_btn.startAnimation(translateAnimation);
    }

    private void display_animation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 300, 0);
        translateAnimation.setDuration(700);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        translateAnimation.hasEnded();
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                home_btn.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        home_btn.startAnimation(translateAnimation);
    }

    private void switch_animation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -50);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setRepeatCount(3);
        translateAnimation.setDuration(200);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                hasBtnHide = false;
                home_btn.setEnabled(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        home_btn.startAnimation(translateAnimation);
    }

    private void refresh_animation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1, 0.7f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        alphaAnimation.setRepeatCount(1);
        scaleAnimation.setRepeatCount(1);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(500);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setInterpolator(new LinearInterpolator());
        animationSet.setRepeatMode(AnimationSet.RESTART);
        animationSet.setFillAfter(true);
        animationSet.setFillBefore(false);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new Handler().postDelayed(() -> childBtnHide(), 10);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        home_btn.startAnimation(animationSet);
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


