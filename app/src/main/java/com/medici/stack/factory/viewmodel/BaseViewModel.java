package com.medici.stack.factory.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;


/**
 * ***************************************
 *
 * @desc: 对ViewModel的简单封装
 * @author：李宗好
 * @time: 2017/12/7 0007 13:57
 * @email：lzh@cnbisoft.com
 * @version：
 * @history: T(View) 对象 不能够在官方组件VIEW MODEL 中使用,因为官方API 对ViewModel 做了缓存处理。
 *           当View被重构的时候,ViewModel 还是使用原来的对象,所有ViewModel的声明周期与View的生命
 *           周期是毫无关联的。所以我们要在ViewModel 中慎用 android.content.context下面的类。如果
 *           我们需要使用Application 可以继承AndroidViewModel 。如果需要清理资源,请重写ViewModel的
 *           onCleared 方法
 *
 * ***************************************
 */
public class BaseViewModel<T extends BaseContract.View> extends AndroidViewModel implements BaseContract.ViewModel<T> {

    /**
     * 一般情况下,ViewModel是不需要持View的对象。
     * 一切显示回调都可以通过数据绑定去解决。
     * 可以使用IViewModel去替代BaseContract
     */
    @Deprecated
    T mView;

    /**
     * 是否显示遮罩Loading 默认是false
     */
    public final SingleLiveEvent<Boolean> loadingEvent = new SingleLiveEvent();

    public final WarningMessage mWarningMessage = new WarningMessage();

    public BaseViewModel(@NonNull Application application, T view) {
        super(application);
        // 此处null判断,因为使用Google ViewModel组件会限制反射传参,没有传参。
        // 因此开发SetView方法,在外部实例化ViewModel时候,在外部调用setView方法。
        if(null != view){
            setView(view);
        }
    }

    /**
     * 设置一个View，子类可以复写
     */
    @Override
    public void setView(T view) {
        this.mView = view;
        this.mView.setViewModel(this);
    }

    /**
     * 给子类使用的获取View的操作
     * 不允许复写
     *
     * @return View
     */
    @Deprecated
    protected final T getView() {
        return mView;
    }

    /**
     * 暴露对Loading状态的LiveData
     * @return
     */
    @Override
    public SingleLiveEvent<Boolean> getLoadingEvent() {
        return loadingEvent;
    }

    @Override
    /**
     * 返回消息提醒数据LiveData对象  用于观察
     * @return
     */
    public WarningMessage getWarningMessage(){
        return mWarningMessage;
    }

    @Override
    /**
     * 启动业务加载
     */
    public void start() {
        loadingEvent.setValue(true);
    }

    @Override
    @CallSuper
    /**
     * 释放资源
     */
    public void destroy() {
        T view = mView;
        mView = null;
        if (view != null) {
            // 把ViewModel设置为NULL
            view.setViewModel(null);
        }
    }
}
