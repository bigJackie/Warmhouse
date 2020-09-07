package com.example.nest;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;


import androidx.appcompat.widget.AppCompatEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于手机号格式“188 8888 8888”
 * Created by Alex on 2017/2/28.
 */
public class PhoneEditText extends AppCompatEditText implements TextWatcher {


    public PhoneEditText(Context context) {
        super(context);
    }

    public PhoneEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhoneEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
        if (s == null || s.length() == 0) return;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(s.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        if (!TextUtils.isEmpty(sb.toString().trim()) && !sb.toString().equals(s.toString())) {
            setText(sb.toString());
            setSelection(sb.length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 获取电话号码
     * @return
     */
    public String getPhoneText() {
        String str = getText().toString();
        return replaceBlank(str);
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符
     *
     * @param str
     *
     * @return
     */
    private String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}