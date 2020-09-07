package com.example.nest;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

public class MyStatusBar {
    protected boolean useStatusBarColor = true;

    protected boolean useThemestatusBarColor = false;

    protected Window window = null;

    protected void setStatusBar(Activity paramActivity) {
        if (Build.VERSION.SDK_INT >= 21) {
            paramActivity.getWindow().getDecorView().setSystemUiVisibility(1280);
            if (this.useThemestatusBarColor) {
                paramActivity.getWindow().setStatusBarColor(ContextCompat.getColor((Context)paramActivity, R.color.colorPrimary));
            } else {
                paramActivity.getWindow().setStatusBarColor(0);
            }
        } else if (Build.VERSION.SDK_INT >= 19) {
            WindowManager.LayoutParams layoutParams = paramActivity.getWindow().getAttributes();
            layoutParams.flags = 0x4000000 | layoutParams.flags;
        } else {
            Toast.makeText((Context)paramActivity, "低于4.4版本不存在沉浸式状态栏", Toast.LENGTH_SHORT).show();
        }
        if (Build.VERSION.SDK_INT >= 23 && this.useStatusBarColor)
            paramActivity.getWindow().getDecorView().setSystemUiVisibility(9216);
    }
}
