package com.medici.stack.widget.recycler;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * ***************************************
 *
 * @desc: 用来UI已经显示的数据和持久化数据的比较,并回调
 * @author：李宗好
 * @time: 2017/12/25 0025 14:48
 * @email：lzh@cnbisoft.com
 * @version：
 * @history:
 *
 * ***************************************
 */
public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback{

    private List<T> mOldList,mNewList;

    public DiffUiDataCallback(List<T> mOldList, List<T> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    /**
     * 两个类是否就是同一个东西，比如Id相等的User
     */
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T oldBean = mOldList.get(oldItemPosition);
        T newBean = mNewList.get(newItemPosition);
        return newBean.isSame(oldBean);
    }

    @Override
    /**
     * 在经过相等判断后，进一步判断是否有数据更改比如
     * 同一个用户的两个不同实例，其中的name字段不同
     */
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T oldBean = mOldList.get(oldItemPosition);
        T newBean = mNewList.get(newItemPosition);
        return newBean.isUiContentSame(oldBean);
    }

    /**
     * 进行比较的数据类型
     * 范型的目的，就是你是和一个你这个类型的数据进行比较
     * @param <T>
     */
    public interface UiDataDiffer<T> {
        /**
         * 传递一个旧的数据给你，问你是否和你标示的是同一个数据
         * @param old
         * @return
         */
        boolean isSame(T old);

        /**
         * 你和旧的数据对比，内容是否相同
         * @param old
         * @return
         */
        boolean isUiContentSame(T old);
    }
}
