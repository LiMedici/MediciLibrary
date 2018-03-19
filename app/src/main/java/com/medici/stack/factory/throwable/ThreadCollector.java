package com.medici.stack.factory.throwable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * desc:线程的基本信息包括ID、名字、优先级和所在的线程组，可以根据实际情况手机
 *      某些线程的信息，收集发生Crash的线程信息即可。
 * author：李宗好
 * time: 2017/7/5 0005 15:17
 * ic_email_icon：lzh@cnbisoft.com
 */
public class ThreadCollector {

    @NonNull
    public static String collect(@Nullable Thread thread){
        StringBuilder result = new StringBuilder();

        if(thread != null){
            result.append("id=").append(thread.getId()).append("\n");
            result.append("name=").append(thread.getName()).append("\n");
            result.append("priority=").append(thread.getPriority()).append("\n");
            if(thread.getThreadGroup()!=null){
                result.append("groupName=").append(thread.getThreadGroup().getName()).append("\n");
            }
        }

        return result.toString();
    }

}
