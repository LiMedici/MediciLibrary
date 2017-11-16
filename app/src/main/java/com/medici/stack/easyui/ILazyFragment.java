package com.medici.stack.easyui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

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
public abstract class ILazyFragment extends RxFragment{

    protected View rootView;

    //是否已经初始化完成
    protected boolean isPre = false;

    //是否已经加载过数据
    protected boolean mHasLoadedOnc = false;

    @Nullable
    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = createLoadedView(inflater);
        initView();
        isPre = true;
        if(isCanLoad()){
            onVisible();
        }
        return rootView;
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

    //可见
    protected void onVisible() {
        lazyLoad();
        initListener();
        mHasLoadedOnc = true;
    }
    //不可见
    protected void onInvisible() {}
    //加载View
    protected abstract View createLoadedView(LayoutInflater inflater);
    //初始化View
    protected abstract void initView();
    //延迟加载
    protected abstract void lazyLoad();
    protected abstract void initListener();

    @Override
    @CallSuper
    public void onDestroyView() {
        super.onDestroyView();
        isPre = false;
        mHasLoadedOnc = false;
    }
}
