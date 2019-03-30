package com.medici.stack.factory.throwable;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.Map;
import java.util.TreeMap;

/**
 * 某些类型的Crash依赖于应用的SharedPreferences中的某些信息项
 */
public class SharedPreferencesCollector {

    private static Context mContext;

    private static String[] mSharedPrefIds;

    public SharedPreferencesCollector(Context mContext, String[] mSharedPrefIds) {
        this.mContext = mContext;
        this.mSharedPrefIds = mSharedPrefIds;
    }

    @NonNull
    public static String collect(){
        final StringBuilder result = new StringBuilder();

        //收集默认的 SharedPreferences 信息
        final Map<String,SharedPreferences> sharedPrefs = new TreeMap<>();

        sharedPrefs.put("default", PreferenceManager.getDefaultSharedPreferences(mContext));

        //收集应用自定义的 SharedPreferences 信息
        if(mSharedPrefIds != null){
            for(final String sharedPrefId:mSharedPrefIds){
                sharedPrefs.put(sharedPrefId,mContext.getSharedPreferences(sharedPrefId,Context.MODE_PRIVATE));
            }
        }

        //遍历所有的SharedPreferences文件
        for(Map.Entry<String,SharedPreferences> entry:sharedPrefs.entrySet()){
            final String sharedPrefId = entry.getKey();
            final SharedPreferences pref = entry.getValue();


            final Map<String,?> prefEntries = pref.getAll();

            //如果sharedPreferences文件内容为空
            if(prefEntries.isEmpty()){
                result.append(sharedPrefId).append('=').append("empty\n");
                continue;
            }

            //遍历添加某个SharedPreferences 文件中的内容
            for(final Map.Entry<String,?> prefEntry:prefEntries.entrySet()){
                final Object prefValue = prefEntry.getValue();

                result.append(sharedPrefId).append('.').append(prefEntry.getKey()).append('=');
                result.append(prefValue == null?"null":prefValue.toString());
                result.append("\n");
            }

            result.append("\n");
        }

        return result.toString();
    }
}
