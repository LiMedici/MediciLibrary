package com.medici.stack.factory.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ***************************************
 *
 * @desc: 对LiveData的一层简单封装 空数据的call
 *
 * ***************************************
 */
public class SingleLiveEvent<T> extends MutableLiveData<T> {

    private static final String TAG = SingleLiveEvent.class.getCanonicalName();

    private final AtomicBoolean mPending = new AtomicBoolean(false);

    @MainThread
    @Override
    public void observe(LifecycleOwner owner, final Observer<T> observer) {

        if (hasActiveObservers()) {
            Logger.w("Multiple observers registered but only one will be notified of changes.");
        }

        // Observe the internal MutableLiveData
        super.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                // 1. 比较AtomicBoolean和expect的值,如果一致，执行方法内的语句。其实就是一个if语句。
                // 2. 把AtomicBoolean的值设成update,比较最要的是这两件事是一气呵成的,这连个动作之间不会被打断，
                //    任何内部或者外部的语句都不可能在两个动作之间运行。为多线程的控制提供了解决的方案。
                if (mPending.compareAndSet(true, false)) {
                    observer.onChanged(t);
                }
            }
        });
    }

    @MainThread
    @Override
    public void setValue(@Nullable T t) {
        mPending.set(true);
        super.setValue(t);
    }

    @MainThread
    public void call() {
        setValue(null);
    }
}
