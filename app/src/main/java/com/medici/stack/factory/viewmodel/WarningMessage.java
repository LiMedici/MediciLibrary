package com.medici.stack.factory.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * ***************************************
 *
 * @desc 提示消息的LiveData观察数据源
 *
 * ***************************************
 */
public class WarningMessage extends SingleLiveEvent<Integer> {

    public void observe(LifecycleOwner owner, final WarningObserver observer) {
        super.observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer t) {
                if (t == null) {
                    return;
                }
                observer.onNewMessage(t);
            }
        });
    }

    public interface WarningObserver {
        /**
         * Called when there is a new message to be shown.
         * @param messageResourceId The new message, non-null.
         */
        void onNewMessage(@StringRes int messageResourceId);
    }

}
