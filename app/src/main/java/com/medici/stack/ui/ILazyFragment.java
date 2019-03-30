package com.medici.stack.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.View;

import com.orhanobut.logger.Logger;

/**
 * ***************************************
 * @desc:实现懒加载的Fragment
 * ***************************************
 */
public abstract class ILazyFragment extends IBaseFragment{

    /**
     * 是否已经初始化完成
     */
    protected boolean isPre = false;

    /**
     * 是否已经加载过数据
     */
    protected boolean mHasLoadedOnc = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        isPre = true;
        if(isCanLoad()){
            onVisible();
        }
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
        Logger.i("%s ======== onVisible",this.getClass().getCanonicalName());
        mHasLoadedOnc = true;
    }

    /**
     * 不可见时候调用
     */
    private void onInvisible() {
        Logger.i("%s ======== onInvisible",this.getClass().getCanonicalName());
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        super.onDestroyView();
        isPre = false;
        mHasLoadedOnc = false;
    }
}
