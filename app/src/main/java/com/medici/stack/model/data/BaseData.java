package com.medici.stack.model.data;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.medici.stack.util.UIUtil;

/**
 * Activity基类数据
 */
public class BaseData {

    private static final String TAG = "BASE_DATA";

    public Context mContext;
    private DialogFragment mLoadingDialog;
    private FragmentManager mFragmentManager;
    private Handler mHandler;

    public BaseData(@NonNull AppCompatActivity activity) {
        this.mContext = activity;
        this.mFragmentManager = activity.getSupportFragmentManager();
        this.mHandler = UIUtil.getHandler();
        initHttp();
    }

    public BaseData(@NonNull Fragment fragment) {
        this.mContext = fragment.getActivity();
        this.mFragmentManager = fragment.getChildFragmentManager();
        this.mHandler = UIUtil.getHandler();
        initHttp();
    }

    public BaseData(@NonNull Service service) {
        this.mContext = service;
        this.mHandler = UIUtil.getHandler();
        initHttp();
    }

    private void initHttp() {
    }

    /**
     * 设置加载的dialog
     * @param loadingDialog
     */
    public void setLoadingDialog(@NonNull DialogFragment loadingDialog) {
        this.mLoadingDialog = loadingDialog;
    }

    /**
     * 打开或者关闭dialog
     * @param show 开关
     */
    public void showLoading(final boolean show) {
        if(null == mLoadingDialog) return;
        mHandler.post(()->{
            if (show) {
                mLoadingDialog.show(mFragmentManager, "loading_dialog");
            } else {
                mLoadingDialog.dismiss();
            }
        });
    }

    /**
     * 启动Activity
     * @param clazz
     */
    public void startActivity(Class clazz) {
        Intent intent = new Intent(mContext, clazz);
        mContext.startActivity(intent);
    }

    /**
     * 启动Activity 并传输bundle数据
     * @param clazz
     * @param bundle
     */
    public void startActivity(Class clazz, Bundle bundle) {
        Intent intent = new Intent(mContext, clazz);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 释放自己
     */
    public void release(){
        mContext = null;
        mLoadingDialog = null;
        mFragmentManager = null;
        mHandler = null;
    }
}
