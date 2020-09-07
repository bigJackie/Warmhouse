package com.example.nest;

import android.content.Context;

public class PrefUtils {
    private static final int MIN_CLICK_DELAY_TIME = 300;

    public static final String PREF_NAME = "config";

    private static long lastClickTime;

    public static boolean getBoolean(Context paramContext, String paramString, boolean paramBoolean) {
        return paramContext.getSharedPreferences("config", 0).getBoolean(paramString, paramBoolean);
    }

    public static boolean isFastClick() {
        boolean bool;
        long l = System.currentTimeMillis();
        if (l - lastClickTime > 300L) {
            bool = true;
        } else {
            bool = false;
        }
        lastClickTime = l;
        return bool;
    }

    public static void setBoolean(Context paramContext, String paramString, boolean paramBoolean) {
        paramContext.getSharedPreferences("config", 0).edit().putBoolean(paramString, paramBoolean).commit();
    }
}
