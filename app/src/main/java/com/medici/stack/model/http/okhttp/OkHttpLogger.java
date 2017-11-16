package com.medici.stack.model.http.okhttp;

import com.cnbi.ic9.util.tool.LogUtil;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * desc:
 * author：李宗好
 * time: ${time} 10:37
 * ic_email_icon：lzh@cnbisoft.com
 */
public class OkHttpLogger implements HttpLoggingInterceptor.Logger {

    @Override
    public void log(String message) {
        LogUtil.e(this.getClass(),message);
    }
}
