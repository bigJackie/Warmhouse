package com.example.nest;


import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpPost {

    // constructor
    public HttpPost() {
    }

    public static void register_post(String url, String phoneNum, String password){
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("phoneNum",phoneNum)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String result = response.body().string();
                    //处理UI需要切换到UI线程处理
                }
            }
        });
    }

    public static void userchecker_post(String url, String phoneNum){
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("phoneNum",phoneNum)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String result = response.body().string();
                    //处理UI需要切换到UI线程处理
                    SignupPage.isUserExist = result;
                }
            }
        });
    }

    public static void usersign_in_post(String url, String phoneNum, String password){
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("phoneNum",phoneNum)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String result = response.body().string();
                    //处理UI需要切换到UI线程处理
                    SigninPage.isUserChecked = result;
                    System.out.println(result);
                }
            }
        });
    }

    public void getAsyn(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //...
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String result = response.body().string();
                    //处理UI需要切换到UI线程处理
                }
            }
        });
    }

}