package com.medici.stack.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.medici.stack.R;

public abstract class DataBindingToolbarActivity<ViewBinding extends ViewDataBinding> extends IBaseActivity {

    protected ViewBinding mViewBinding;
    protected Toolbar mToolbar;

    @Override
    protected void setContentView() {
        int layoutId = getContentLayoutId();
        mViewBinding = DataBindingUtil.setContentView(this,layoutId);
    }

    @CallSuper
    @Override
    protected void initWidget() {
        super.initWidget();
        initToolbar((Toolbar) findViewById(R.id.toolbar));
    }

    /**
     * 初始化toolbar
     *
     * @param toolbar Toolbar
     */
    public void initToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        initTitleNeedBack();
    }

    protected void initTitleNeedBack() {
        // 设置左上角的返回按钮为实际的返回效果
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            // 不显示Toolbar自带的标题
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // 当点击界面导航返回时，Finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }
}
