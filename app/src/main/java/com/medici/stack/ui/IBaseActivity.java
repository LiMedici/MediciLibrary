package com.medici.stack.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.medici.stack.model.data.BaseData;
import com.medici.stack.widget.convention.PlaceHolderView;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * ***************************************
 * @desc:技术堆栈Activity基类
 * @author：李宗好
 * @time: 2017/11/13 0013 10:45
 * @email：lzh@cnbisoft.com
 * @version：
 * @history:
 * ***************************************
 */
public abstract class IBaseActivity extends AppCompatActivity {

    private static final String TAG = IBaseActivity.class.getCanonicalName();

    protected BaseData m;

    protected PlaceHolderView mPlaceHolderView;

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m = new BaseData(this);
        Logger.d(this.getClass().getCanonicalName()+" ======== onCreate");

        Logger.i(this.getClass().getCanonicalName()+" ======== initWindow");
        initWindow();

        Logger.i(this.getClass().getCanonicalName()+" ======== initArgs");
        if(initArgs(getIntent().getExtras())){

            setContentView();

            Logger.i(this.getClass().getCanonicalName()+" ======== initView");
            initView();
            Logger.i(this.getClass().getCanonicalName()+" ======== initData");
            initData();
            Logger.i(this.getClass().getCanonicalName()+" ======== initListener");
            initListener();
        }else{
            finish();
        }


    }

    /**
     * 初始化有关Window的设置
     */
    protected void initWindow(){}

    /**
     * 初始化Bundle数据
     * @return
     */
    protected boolean initArgs(@NonNull Bundle bundle){
        return true;
    }

    /**
     * 设置ContentView布局
     */
    protected void setContentView(){
        int layoutId = getContentLayoutId();
        setContentView(layoutId);
    }

    @LayoutRes
    /**
     * 获取内容布局Id
     * @return
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    /**
     * 设置占位布局
     *
     * @param placeHolderView 继承了占位布局规范的View
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.mPlaceHolderView = placeHolderView;
    }

    @Override
    public void onBackPressed() {
        // 得到当前Activity下的所有Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        // 判断是否为空
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                // 判断是否为我们能够处理的Fragment类型
                if (fragment instanceof IBaseFragment) {
                    // 判断是否拦截了返回按钮
                    if (((IBaseFragment) fragment).onBackPressed()) {
                        // 如果有直接Return
                        return;
                    }
                }
            }
        }

        super.onBackPressed();
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d(TAG+" ======== onResume");

    }

    @Override
    protected void onPause() {
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
        Logger.d(TAG+" ======== onDestroy");
        m.release();
    }

}
