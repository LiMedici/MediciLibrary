package com.medici.stack.ui;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.medici.stack.factory.viewmodel.BaseContract;
import com.medici.stack.factory.viewmodel.BaseViewModel;
import com.medici.stack.factory.viewmodel.ViewModelFactory;
import com.medici.stack.util.DialogUtil;
import com.medici.stack.util.UIUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @desc MVVM+DataBinding架构 基类Activity
 */
public abstract class DataBindingViewModelActivity<ViewBinding extends ViewDataBinding,ViewModel extends BaseViewModel>
        extends DataBindingToolbarActivity<ViewBinding> implements BaseContract.View<ViewModel> {

    protected ViewModel mViewModel;

    @CallSuper
    @Override
    protected void initWidget() {
        super.initWidget();
        // 初始化ViewModel
        initViewModel();
        // 观察ViewModel Loading状态的变化并与生命周期绑定
        mViewModel.getLoadingEvent().observe(this,new LoadingObserver());
        mViewModel.getWarningMessage().observe(this,new WarningObserver());
    }

    @Override
    /**
     * View中赋值ViewModel
     */
    public void setViewModel(ViewModel viewModel) {
        mViewModel = viewModel;
    }

    /**
     * 初始化 ViewModel
     *
     * @return ViewModel
     */
    protected ViewModel initViewModel() {
        return obtainViewModel(this);
    }

    /**
     * 通过ViewModelFactory工厂 制造ViewModel
     * @param activity 上传的上下文对象
     * @return ViewModel对象
     */
    @Override
    public ViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        Class<ViewModel> clx = (Class<ViewModel>) getInstancedGenericKClass(this.getClass());
        ViewModel viewModel = ViewModelProviders.of(activity, factory).get(clx);
        viewModel.setView(this);
        return viewModel;
    }

    /**
     * get generic parameter K
     * @param z 当前对象的class对象
     * @return 返回ViewModel 范型的class对象
     */
    private Class<ViewModel> getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class<ViewModel> tempClass = (Class<ViewModel>) temp;
                    if (AndroidViewModel.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void showError(int str) {
        // 不管你怎么样，我先隐藏我
        DialogUtil.closeDialog();

        // 显示错误, 优先使用占位布局
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerError(str);
        } else {
            UIUtil.showToastSafe(str);
        }
    }

    @Override
    /**
     * 显示Loading状态
     */
    public void showLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        } else {
            DialogUtil.showDialog(this);
        }
    }

    @Override
    /**
     * 隐藏Loading状态 并刷新UI
     */
    public void hideLoading() {
        // 不管你怎么样，我先隐藏我
        DialogUtil.closeDialog();

        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerOk();
        }
    }

    /**
     * Loading状态改变的观察回调
     */
    private class LoadingObserver implements Observer<Boolean>{

        @Override
        public void onChanged(@Nullable Boolean bool) {
            if(bool){
                showLoading();
            }else{
                hideLoading();
            }
        }
    }

    /**
     * 消息提醒发生改变的观察回调
     */
    private class WarningObserver implements Observer<Integer>{
        @Override
        public void onChanged(@Nullable Integer str) {
            showError(str);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 界面关闭时进行销毁的操作
        if (mViewModel != null) {
            mViewModel.destroy();
        }
    }
}
