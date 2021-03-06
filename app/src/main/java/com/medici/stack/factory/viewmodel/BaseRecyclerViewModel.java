package com.medici.stack.factory.viewmodel;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.medici.stack.util.UIUtil;
import com.medici.stack.widget.recycler.RecyclerBindingAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 *
 * @desc 对RecyclerView进行的一个简单的ViewModel封装
 *
 */
public class BaseRecyclerViewModel<ViewModel, View extends BaseContract.RecyclerView>
        extends BaseViewModel<View> {

    public BaseRecyclerViewModel(@NonNull Application application, View view) {
        super(application, view);
    }

    /**
     * 刷新一堆新数据到界面中
     *
     * @param dataList 新数据
     */
    protected void refreshData(final List<ViewModel> dataList) {
        UIUtil.getHandler().post(()->{
            View view = getView();
            if (view == null) return;
            // 基本的更新数据并刷新界面
            RecyclerBindingAdapter<ViewModel> adapter = view.getRecyclerAdapter();
            adapter.replace(dataList);
            // 回调数据已经加载完成
            view.onAdapterDataChanged();
        });
    }

    /**
     * 刷新界面操作，该操作可以保证执行方法在主线程进行
     *
     * @param diffResult 一个差异的结果集
     * @param dataList   具体的新数据
     */
    protected void refreshData(final DiffUtil.DiffResult diffResult, final List<ViewModel> dataList) {
        UIUtil.getHandler().post(()->{
            refreshDataOnUiThread(diffResult, dataList);
        });
    }


    private void refreshDataOnUiThread(final DiffUtil.DiffResult diffResult, final List<ViewModel> dataList) {
        View view = getView();
        if (view == null) return;
        // 基本的更新数据并刷新界面
        RecyclerBindingAdapter<ViewModel> adapter = view.getRecyclerAdapter();

        // 改变数据集合并不通知界面刷新
        adapter.getItems().clear();
        adapter.getItems().addAll(dataList);
        // 通知界面刷新占位布局
        view.onAdapterDataChanged();

        // 进行增量更新
        diffResult.dispatchUpdatesTo(adapter);
    }

}
