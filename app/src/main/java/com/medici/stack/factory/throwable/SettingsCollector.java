package com.medici.stack.factory.throwable;

import android.content.ContentProvider;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 在Android中,许多的系统属性都是在系统设置中进行设置的,比如蓝牙,Wi-Fi的状态
 * 当前的首选语言,屏幕亮度等。这些信息存放在数据库中.对这些数据库的读写操作对应
 * 着Android SDK中的Settings类,我们对系统设置的读写本质上就是对这些数据库表的操作
 */

public class SettingsCollector {

    private static Context mContext;

    public SettingsCollector(@NonNull Context mContext) {
        this.mContext = mContext;
    }

    /**
     * URI content://settings/system
     * @return 系统Setting log
     */
    @NonNull
    public String collectSystemSettings(){
        final StringBuilder result = new StringBuilder();

        final Field[] keys = Settings.System.class.getFields();

        for (final Field key:keys) {
            if(!key.isAnnotationPresent(Deprecated.class) && key.getType() == String.class){
                try {
                    final Object value = Settings.System.getString(mContext.getContentResolver(),(String)key.get(null));
                    if(value != null){
                        result.append(key.getName()).append("=").append(value).append("\n");
                    }
                } catch (@NonNull IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return result.toString();
    }


    /**
     * URI content://settings/secure
     * 以键值对的形式存放系统的安全设置,这个是只读的,获取这种类型设置的代码如下
     * @return
     */
    public String collectSecureSettings() {
        final StringBuilder result = new StringBuilder();

        final Field[] keys = Settings.Secure.class.getFields();

        for (final Field key : keys) {
            if (!key.isAnnotationPresent(Deprecated.class) && key.getType() == String.class
                    && isAuthorized(key)) {
                try {
                    final Object value = Settings.System.getString(mContext.getContentResolver(), (String) key.get(null));
                    if (value != null) {
                        result.append(key.getName()).append("=").append(value).append("\n");
                    }
                } catch (@NonNull IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return result.toString();

    }

    /**
     * URI content://settings/global
     * 以键值对的形式存放系统中对所有用户公用的偏好设置,它是只读的
     * @return
     */
    @NonNull
    public String collectGlobalSettings(){
        if(Build.VERSION.SDK_INT <Build.VERSION_CODES.JELLY_BEAN_MR1){
            return "";
        }

        final StringBuilder result = new StringBuilder();

        try {
            final Class<?> globalClass = Class.forName("android.provider.Setting$Global");

            final Field[] keys = globalClass.getFields();

            final Method getString = globalClass.getMethod("getString", ContentProvider.class,String.class);

            for (final Field key:keys) {
                if(!key.isAnnotationPresent(Deprecated.class) && key.getType() == String.class
                        && isAuthorized(key)){
                    final Object value = getString.invoke(null,mContext.getContentResolver(),key.get(null));

                    if (value != null) {
                        result.append(key.getName()).append("=").append(value).append("\n");
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    /**
     * 是不是授权认可的
     * @param key
     * @return
     */
    @NonNull
    @CheckResult
    private boolean isAuthorized(@Nullable Field key){
        if(null == key || key.getName().startsWith("WIFI_AP")){
            return false;
        }
        return true;
    }
}
