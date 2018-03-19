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
 * @author cnbilzh
 * @time 2017/12/05 16:08
 * @desc 对RecyclerView进行的一个简单的ViewModel封装
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
        Logger.e("users next new list size:"+dataList.size());
        adapter.getItems().addAll(dataList);

        // TODO 先刷新解决问题 更新RecyclerView数据时，需要保证外部数据集和内部数据集实时保持一致
        // 经过测试,是重复刷新的问题,虽然暂时解决,但是LiveData 还有可能去多次回调的场景
        // 例如数据多次更改,多次PostValue
        // 多次切换UI 也会发生这个异常
        adapter.notifyDataSetChanged();

        // 通知界面刷新占位布局
        view.onAdapterDataChanged();

        // 进行增量更新
        diffResult.dispatchUpdatesTo(adapter);
    }

}
