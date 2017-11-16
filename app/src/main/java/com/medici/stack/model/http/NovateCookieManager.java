package com.medici.stack.model.http;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * desc: cookie 的管理
 * author：李宗好
 * time: 2017/6/2 10:30
 * ic_email_icon：lzh@cnbisoft.com
 */
public class NovateCookieManager implements CookieJar {

    private static final String TAG = "NovateCookieManger";
    private static PersistentCookieStore cookieStore;

    /**
     * Mandatory constructor for the NovateCookieManger
     */
    public NovateCookieManager() {
        if (cookieStore == null) {
            cookieStore = new PersistentCookieStore();
        }
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }

}
