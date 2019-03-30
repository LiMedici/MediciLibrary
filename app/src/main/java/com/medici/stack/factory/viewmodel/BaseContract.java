package com.medici.stack.factory.viewmodel;

import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;

import com.medici.stack.widget.recycler.RecyclerBindingAdapter;

/**
 * ***************************************
 *
 * @desc: MVVM 架构的公共基本契约,尽量的取消接口回调,实现双向binding
 *
 * ***************************************
 */
public interface BaseContract {

    /**
     * 基本的界面职责
     * @param <T>
     */
    interface View<T extends ViewModel> {

        /**
         * 生产ViewModel
         * @param activity
         * @return
         */
        T obtainViewModel(FragmentActivity activity);

        /**
         * 支持设置一个ViewModel
         * @param viewModel
         */
        void setViewModel(T viewModel);

        /**
         * 显示进度条
         */
        void showLoading();

        /**
         * 隐藏进度条
         */
        void hideLoading();

        /**
         * 显示一个字符串错误
         * @param str 字符串资源
         */
        void showError(@StringRes int str);
    }

    /**
     * 基本的ViewModel职责
     */
    interface ViewModel<T extends View> {
        /**
         * 共用的开始触发
         */
        void start();

        /**
         * 将View 设置到ViewModel中
         * @param view
         */
        void setView(T view);

        /**
         * 获取加载状态的LiveData
         * @return SingleLiveEvent<Boolean>
         */
        SingleLiveEvent<Boolean> getLoadingEvent();

        /**
         * 获取消息提醒的LiveData
         * @return
         */
        WarningMessage getWarningMessage();

        /**
         * 共用的销毁触发
         */
        void destroy();
    }

    /**
     * 基本的一个列表的View的职责
     * @param <T>
     * @param <ViewMode>
     */
    interface RecyclerView<T extends ViewModel, ViewMode> extends BaseContract.View<T> {

        /**
         * 拿到一个适配器，然后自己自主的进行刷新
         * @return
         */
        RecyclerBindingAdapter<ViewMode> getRecyclerAdapter();

        /**
         * 当适配器数据更改了的时候触发
         */
        void onAdapterDataChanged();
    }

}
