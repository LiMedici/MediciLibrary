package com.medici.stack.ui;

import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.medici.stack.R;
import com.medici.stack.util.UIUtil;

/**
 * ***************************************
 *
 * @desc: 自定义活动页面的进度条
 * @author：李宗好
 * @time: 2017/12/22 0022 16:37
 * @email：lzh@cnbisoft.com
 * @version：
 * @history:
 *
 * ***************************************
 */
public class ProgressDialog extends AppCompatDialogFragment {

    public static final String LOADING_DESC = "LOADING_DESC";

    private AnimationDrawable mAnimDrawable;

    /**
     * 显示的Loading Desc
     */
    private String mDesc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(new MyObserver());
        mDesc = getLoadingDesc();
        // 如果在DialogFragment 设置不可取消,同样也需要设置到Fragment中
        this.setCancelable(false);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new AppCompatDialog(getContext(), R.style.AppTheme_Dialog);
        // 不允许取消
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        // DialogFragment Dialog 默认宽度等是充满窗体,设置包裹
        Window window = getDialog().getWindow();
        //注意此处 不然设置宽高没有效果
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowParams = window.getAttributes();
        // 设置固定宽
        windowParams.width = UIUtil.getDimens(R.dimen.x300);
        window.setAttributes(windowParams);
        return inflater.inflate(R.layout.dialog_custom_loading_layout,container,false);
    }

    /**
     * 获取Loading描述
     * @return
     */
    @CheckResult
    public String getLoadingDesc() {
        Bundle mBundle = getArguments();
        if(null == mBundle) return null;
        String desc = mBundle.getString(LOADING_DESC);
        return desc;
    }

    /**
     * 私有的show方法
     * @param manager
     */
    public static ProgressDialog show(FragmentManager manager, Bundle mBundle) {
        // 调用AppCompatDialog以及准备好的显示方法
        ProgressDialog dialog = new ProgressDialog();
        dialog.setArguments(mBundle);
        dialog.show(manager, ProgressDialog.class.getName());
        return dialog;
    }

    public class MyObserver implements LifecycleObserver{
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void startAnim(){

            if(mAnimDrawable == null){
                View view = getView();

                //　找到 Drawable
                ImageView mIvAnim = view.findViewById(R.id.iv_anim);
                TextView mTvDesc = view.findViewById(R.id.tv_desc);
                Drawable ivDrawable = mIvAnim.getBackground();
                if(ivDrawable instanceof AnimationDrawable){
                    mAnimDrawable = (AnimationDrawable) ivDrawable;
                }

                if(TextUtils.isEmpty(mDesc)){
                    // 隐藏View
                    mTvDesc.setVisibility(View.GONE);
                }else{
                    // 显示View
                    mTvDesc.setVisibility(View.VISIBLE);
                    mTvDesc.setText(mDesc);
                }
            }

            if(mAnimDrawable != null){
                mAnimDrawable.start();
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void stop(){
            if(mAnimDrawable != null){
                mAnimDrawable.stop();
            }
        }
    }
}
