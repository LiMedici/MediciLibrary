package com.medici.stack.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.View;

import com.medici.stack.model.data.BaseData;
import com.orhanobut.logger.Logger;

/**
 * ***************************************
 * desc:实现懒加载的Fragment
 * author：李宗好
 * time: 2017/11/13 0013 11:43
 * email：lzh@cnbisoft.com
 * version：
 * history:
 * ***************************************
 */
public abstract class ILazyFragment extends IBaseFragment{

    private static final String TAG = ILazyFragment.class.getCanonicalName();

    protected BaseData m;

    /**
     * 是否已经初始化完成
     */
    protected boolean isPre = false;

    /**
     * 是否已经加载过数据
     */
    protected boolean mHasLoadedOnc = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d(TAG+" ======== onCreate");
        m = new BaseData(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Logger.d(TAG+" ======== onAttach");

        Logger.d(TAG+" ======== initArgs");
        // 初始化参数
        initArgs(getArguments());

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // 取消调用IBaseFragment onViewCreated方法  懒加载方式
        Logger.d(TAG+" ======== onViewCreated");
        initView();
        isPre = true;
        if(isCanLoad()){
            onVisible();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.d(TAG+" ======== onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d(TAG+" ======== onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d(TAG+" ======== onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.d(TAG+" ======== onStop");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isCanLoad()){
            if(getUserVisibleHint()) {
                onVisible();
            } else {
                onInvisible();
            }
        }
    }

    /**
     * 判断能不能加载数据
     * @return
     */
    protected boolean isCanLoad(){
        if (!isPre || mHasLoadedOnc) {
            return false;
        }
        return true;
    }

    /**
     * 可见的时候调用
     */
    private void onVisible() {
        initData();
        initListener();
        mHasLoadedOnc = true;
    }

    /**
     * 不可见时候调用
     */
    private void onInvisible() {}

    @Override
    @CallSuper
    public void onDestroyView() {
        super.onDestroyView();
        isPre = false;
        mHasLoadedOnc = false;
    }
}
