package com.example.nest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;
import java.util.List;

public class GuidePages extends AppCompatActivity {

    private static int[] mImageIds = new int[] { R.drawable.guide, R.drawable.guide, R.drawable.guide };

    private final String[] USER_PERMISIONS = new String[] { "android.permission.CALL_PHONE", "android.permission.CAMERA", "android.permission.ACCESS_FINE_LOCATION", "android.permission.WRITE_EXTERNAL_STORAGE" };

    private Button mBtnStart;

    private int mDotDistance;

    private LinearLayout mDotGroup;

    private List<ImageView> mImageList;

    private ViewPager mVpGuide;

    private View mWhiteDot;

    MyStatusBar transparentComponent = new MyStatusBar();

    private void bindView() {
        this.mVpGuide = findViewById(R.id.vp_guide);
        this.mBtnStart = findViewById(R.id.btn_start);
        this.mBtnStart.setVisibility(View.INVISIBLE);
        this.mDotGroup = findViewById(R.id.dot_group);
        this.mWhiteDot = findViewById(R.id.white_dot);
    }

    private void initView() {
        this.mImageList = new ArrayList<>();
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageIds[i]);
            this.mImageList.add(imageView);
        }
        this.mDotDistance = this.mDotGroup.getChildAt(1).getLeft() - this.mDotGroup.getChildAt(0).getLeft();
        this.mDotGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                mDotGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mDotDistance = mDotGroup.getChildAt(1).getLeft()- mDotGroup.getChildAt(0).getLeft();
            }
        });
        this.mVpGuide.setAdapter(new GuideAdapter());
        this.mVpGuide.addOnPageChangeListener(new GuidePageChangeListener());
        this.mBtnStart.setOnClickListener(param1View -> {
            PrefUtils.setBoolean(GuidePages.this, "is_user_guide_showed", true);
            if (PrefUtils.isFastClick()) {
                GuidePages guideActivity = GuidePages.this;
                guideActivity.startActivity(new Intent(guideActivity, SigninPage.class));
            }
            GuidePages.this.finish();
        });
    }

    private void requestForPermissions() {
        ActivityCompat.requestPermissions((Activity)this, this.USER_PERMISIONS, 1);
        if (ContextCompat.checkSelfPermission((Context)this, "android.permission.CALL_PHONE") != 0)
            Toast.makeText((Context)this, "请授权", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.guide_page);
        getWindow().addFlags(1024);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.transparentComponent.setStatusBar(this);
        bindView();
        initView();
        requestForPermissions();
    }



    class GuideAdapter extends PagerAdapter {
        public void destroyItem(ViewGroup param1ViewGroup, int param1Int, Object param1Object) {
            param1ViewGroup.removeView(GuidePages.this.mImageList.get(param1Int));
        }

        public int getCount() {
            return GuidePages.this.mImageList.size();
        }

        public Object instantiateItem(ViewGroup param1ViewGroup, int param1Int) {
            param1ViewGroup.addView(GuidePages.this.mImageList.get(param1Int));
            return GuidePages.this.mImageList.get(param1Int);
        }

        public boolean isViewFromObject(View param1View, Object param1Object) {
            return (param1View == param1Object);
        }
    }

    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
        public void onPageScrollStateChanged(int param1Int) {}

        public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {
            param1Int1 = (int)(GuidePages.this.mDotDistance * (param1Float + param1Int1));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)GuidePages.this.mWhiteDot.getLayoutParams();
            layoutParams.leftMargin = param1Int1;
            GuidePages.this.mWhiteDot.setLayoutParams(layoutParams);
        }

        public void onPageSelected(int param1Int) {
            if (param1Int == GuidePages.mImageIds.length - 1) {
                GuidePages.this.mBtnStart.setVisibility(View.VISIBLE);
                GuidePages.this.mDotGroup.setVisibility(View.INVISIBLE);
                GuidePages.this.mWhiteDot.setVisibility(View.INVISIBLE);
                return;
            }
            GuidePages.this.mBtnStart.setVisibility(View.INVISIBLE);
            GuidePages.this.mDotGroup.setVisibility(View.VISIBLE);
            GuidePages.this.mWhiteDot.setVisibility(View.VISIBLE);
        }
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
