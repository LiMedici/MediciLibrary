package com.medici.stack.factory.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.CheckResult;

/**
 * ***************************************
 * <p>
 * ViewModel工厂, 用来生产ViewModel
 * 因为组件化开发的原因, 不能够在create去使用上层业务的ViewModel
 * <p>
 * ***************************************
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    /**
     * ViewModel工厂实例
     */
    private static volatile ViewModelFactory INSTANCE;

    /**
     * 上下文对象
     */
    private Application mApplication;

    private ViewModelFactory(Application application) {
        mApplication = application;
    }

    /**
     * @param application
     * @return
     */
    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }

    @CheckResult
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return null;
    }


}
