package com.medici.stack.ui;

import android.app.ProgressDialog;

import com.medici.stack.R;
import com.medici.stack.factory.presenter.BaseContract;
import com.medici.stack.factory.presenter.BasePresenter;
import com.medici.stack.util.UIUtil;

public abstract class PresenterActivity<Presenter extends BasePresenter>
        extends ToolbarActivity implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    protected ProgressDialog mLoadingDialog;

    @Override
    protected void initBefore() {
        super.initBefore();
        // 初始化Presenter
        initPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 界面关闭时进行销毁的操作
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    /**
     * 初始化Presenter
     *
     * @return Presenter
     */
    protected abstract Presenter initPresenter();

    @Override
    public void showError(int str) {
        // 不管你怎么样，我先隐藏我
        hideDialogLoading();

        // 显示错误, 优先使用占位布局
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerError(str);
        } else {
            UIUtil.showToastSafe(str);
        }
    }

    @Override
    public void showLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        } else {
            ProgressDialog dialog = mLoadingDialog;
            if (dialog == null) {
                dialog = new ProgressDialog(this, R.style.AppTheme_Dialog_Alert_Light);
                // 不可触摸取消
                dialog.setCanceledOnTouchOutside(false);
                // 强制取消关闭界面
                dialog.setCancelable(true);
                dialog.setOnCancelListener(dialogInterface->finish());
                mLoadingDialog = dialog;
            }

            dialog.setMessage(getText(R.string.prompt_loading));
            dialog.show();
        }
    }

    protected void hideDialogLoading() {
        ProgressDialog dialog = mLoadingDialog;
        if (dialog != null) {
            mLoadingDialog = null;
            dialog.dismiss();
        }
    }

    protected void hideLoading() {
        // 不管你怎么样，我先隐藏我
        hideDialogLoading();

        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerOk();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        // View中赋值Presenter
        mPresenter = presenter;
    }
}
