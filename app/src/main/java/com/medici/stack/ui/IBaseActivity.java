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
 *
 * @desc:技术堆栈Activity基类
 *
 * ***************************************
 */
public abstract class IBaseActivity extends AppCompatActivity{

    protected BaseData m;

    protected PlaceHolderView mPlaceHolderView;

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m = new BaseData(this);
        Logger.d("%s ======== onCreate",this.getClass().getCanonicalName());

        Logger.i("%s ======== initWindow",this.getClass().getCanonicalName());
        initWindow();

        Logger.i("%s ======== initArgs",this.getClass().getCanonicalName());
        if(initArgs(getIntent().getExtras())){

            setContentView();

            Logger.i("%s ======== initBefore",this.getClass().getCanonicalName());
            initBefore();
            Logger.i("%s ======== initWidget",this.getClass().getCanonicalName());
            initWidget();
            Logger.i("%s ======== initData",this.getClass().getCanonicalName());
            initData();
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
     * 初始化监听
     */
    protected  void initBefore(){

    }

    /**
     * 初始化View
     */
    protected void initWidget(){

    }

    /**
     * 初始化数据
     */
    protected void initData(){

    }

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
        Logger.d("%s ======== onStart",this.getClass().getCanonicalName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.d("%s ======== onRestart",this.getClass().getCanonicalName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d("%s ======== onResume",this.getClass().getCanonicalName());

    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d("%s ======== onPause",this.getClass().getCanonicalName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d("%s ======== onStop",this.getClass().getCanonicalName());
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        Logger.d("%s ======== onDestroy",this.getClass().getCanonicalName());
        m.release();
    }

}
