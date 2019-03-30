package com.medici.stack.factory.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.medici.stack.model.data.BaseDataSource;
import com.medici.stack.model.data.DataSource;

import java.util.List;

/**
 * @desc 基础的仓库源的ViewModel定义
 */
public abstract class BaseSourceViewModel<Data, ViewModel,
        Source extends BaseDataSource<Data>,
        View extends BaseContract.RecyclerView>
        extends BaseRecyclerViewModel<ViewModel, View>
        implements DataSource.SucceedCallback<List<Data>> {

    protected Source mSource;

    protected MutableLiveData<List<Data>> mDataLiveData;

    /**
     * 返回LiveData对象
     * @return MutableLiveData
     */
    public MutableLiveData<List<Data>> getLoadedLiveData(){
        return mDataLiveData;
    }

    public BaseSourceViewModel(@NonNull Application application, View view, Source mSource) {
        super(application, view);
        this.mSource = mSource;
    }

    @Override
    public void start() {
        super.start();
        if (mSource != null){
            mDataLiveData = mSource.load();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        mSource.dispose();
        mSource = null;
    }
}
