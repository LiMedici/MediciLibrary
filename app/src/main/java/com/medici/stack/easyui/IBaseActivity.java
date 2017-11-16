package com.medici.stack.easyui;

import android.os.Bundle;
import android.support.annotation.CallSuper;

import com.cnbi.ic9.model.bean.BaseData;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * ***************************************
 * desc:技术堆栈Activity基类
 * author：李宗好
 * time: 2017/11/13 0013 10:45
 * email：lzh@cnbisoft.com
 * version：
 * history:
 * ***************************************
 */
public abstract class IBaseActivity extends RxAppCompatActivity{

    private final String TAG = this.getClass().getCanonicalName();

    protected BaseData m;

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m = new BaseData(this);
        Logger.d(this.getClass().getCanonicalName()+" ======== onCreate");
        Logger.i(this.getClass().getCanonicalName()+" ======== initView");
        initView(savedInstanceState);
        Logger.i(this.getClass().getCanonicalName()+" ======== initData");
        initData(savedInstanceState);
        Logger.i(this.getClass().getCanonicalName()+" ======== initListener");
        initListener();
    }

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initListener();

    @Override
    protected void onStart() {
        super.onStart();
        Logger.d(TAG+" ======== onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.d(TAG+" ======== onRestart");
    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Logger.d(TAG+" ======== onResume");

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Logger.d(TAG+" ======== onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d(TAG+" ======== onStop");
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(TAG+" ======== onDestory");
        m.release();
    }
}
