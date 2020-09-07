package com.example.nest;

import android.content.Context;
import android.content.SharedPreferences;

//保存简单数据
public class MySharedPreferences {

    //创建一个SharedPreferences    类似于创建一个数据库，库名为 data
    public static SharedPreferences share(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    //phoneNum
    public static Object getPhoneNum(Context context){
        return share(context).getString("phoneNum",null);
    }
    public static boolean setPhoneNum(String phoneNum, Context context){
        SharedPreferences.Editor e = share(context).edit();
        e.putString("phoneNum",phoneNum);
        Boolean bool = e.commit();
        return bool;
    }

    //password
    public static String getPassword(Context context){
        return share(context).getString("password",null);
    }
    //这里使用的是 apply() 方法保存，将不会有返回值
    public static void setPassword(String password, Context context){
        SharedPreferences.Editor e = share(context).edit();
        e.putString("password",password);
        e.apply();
    }

    //sign in status
    public static String getSignInStatus(Context context){
        return share(context).getString("SignInStatus",null);
    }
    public static void setSignInStatus(String SignInStatus, Context context){
        SharedPreferences.Editor e = share(context).edit();
        e.putString("SignInStatus",SignInStatus);
        e.apply();
    }


}

