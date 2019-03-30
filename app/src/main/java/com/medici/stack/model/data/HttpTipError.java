package com.medici.stack.model.data;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.io.Serializable;

/**
 * 网络请求失败的显示字符资源
 */
public class HttpTipError implements Serializable {
    private boolean httpMsgError;
    private int strRes;
    private String message;

    private HttpTipError(int strRes) {
        this.strRes = strRes;
    }

    private HttpTipError(@NonNull String message) {
        httpMsgError = true;
        this.message = message;
    }

    public static HttpTipError newError(@StringRes int strRes){
        HttpTipError error = new HttpTipError(strRes);
        return error;
    }

    public static HttpTipError newError(@NonNull String message){
        HttpTipError error = new HttpTipError(message);
        return error;
    }

    public boolean isHttpMsgError() {
        return httpMsgError;
    }

    @StringRes
    public int getStrRes() {
        return strRes;
    }

    public String getMessage() {
        return message;
    }
}
