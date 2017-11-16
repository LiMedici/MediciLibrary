package com.medici.stack.model.event;

import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 配合EventBus使用，可向指定class传值，可向同一Tag传值
 *
 * @author Like
 * @version 1.0.0
 */
public class EventWrapper {

    public Object data; // 可以绑定的额外数据
    private String targetClassName; // 要传递的类名
    private String fromClassName;
    private String toClassName;
    private Integer requestCode; // 请求码，功能等同与onActivityResult中的请求码
    private String tag;

    public EventWrapper(Object data) {
        this.data = data;
    }

    public EventWrapper(Object data, Class<?> targetClass, Integer requestCode) {
        this(data);
        this.targetClassName = targetClass.getName();
        this.requestCode = requestCode;
    }

    public EventWrapper(Object data, String tag) {
        this(data);
        this.tag = tag;
    }

    public EventWrapper(Object data, String targetClassName, String tag) {
        this(data);
        this.targetClassName = targetClassName;
        this.tag = tag;
    }

    public Object getData() {
        return data;
    }

    public static boolean isMatch(EventWrapper event, Class<?> clazz,
                                  int requestCode, boolean block) {
        String thisClassName = clazz.getName();
        boolean isMatch = TextUtils
                .equals(event.targetClassName, thisClassName)
                && event.requestCode == requestCode;
        if (isMatch && block) {
            EventBus.getDefault().removeStickyEvent(event);
        }
        return isMatch;
    }

    public static boolean isMatch(EventWrapper event, Class<?> clazz,
                                  int requestCode) {
        String thisClassName = clazz.getName();
        boolean isMatch = TextUtils
                .equals(event.targetClassName, thisClassName)
                && event.requestCode == requestCode;
        if (isMatch) {
            EventBus.getDefault().removeStickyEvent(event);
        }
        return isMatch;
    }

    public static boolean isMatch(EventWrapper event, String tag) {
        boolean isMatch = TextUtils.equals(event.tag, tag);
        if (isMatch) {
            EventBus.getDefault().removeStickyEvent(event);
        }
        return isMatch;
    }

    public static boolean isMatch(EventWrapper event, Class<?> clazz, String tag) {
        boolean isMatch = (TextUtils.equals(event.tag, tag) && event.targetClassName.equals(clazz.getName()));
        if (isMatch) {
            EventBus.getDefault().removeStickyEvent(event);
        }
        return isMatch;
    }

    public static boolean isMatch(EventWrapper event, String tag, boolean block) {
        boolean isMatch = TextUtils.equals(event.tag, tag);
        if (isMatch && block) {
            EventBus.getDefault().removeStickyEvent(event);
        }
        return isMatch;
    }

}
