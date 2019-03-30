package com.medici.stack.model.data;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

/**
 * ***************************************
 *
 * @desc: 基础的单一数据来源接口定义
 *
 * ***************************************
 */
public interface BaseDataSource<Data> extends DataSource{
    /**
     * 有一个基本的数据源加载方法
     * @return 返回LiveData数据
     */
    MutableLiveData<List<Data>> load();

    /**
     * 数据保存加载方法
     * @param list
     */
    void onDataSave(Data... list);

    /**
     * 数据删除的方法
     * @param list
     */
    void onDataDelete(Data... list);

    /**
     * 清除表所有记录
     */
    void clear();
}
