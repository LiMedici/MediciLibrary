package com.medici.stack.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.medici.stack.R;

/**
 * @Desc DialogUtil 弹窗遮罩的作用
 * @author cnbilzh
 * @time:2017年2月22日下午2:23:41
 */
public class DialogUtil {

	private static AlertDialog mLoadingDialog;

	private static com.medici.stack.ui.ProgressDialog mCustomDialog;

	/**
	 * 创建dialog 显示dialog
	 * @param mActivity 上下文,dialog依附的窗体
	 * @param text 		dialog文本
	 */
	public static void showDialog(Activity mActivity, String text) {
		if(!isShowing()){
			View view = View.inflate(UIUtil.getContext(), R.layout.dialog_loading_layout, null);
			TextView loadingText = (TextView) view.findViewById(R.id.loading_text);
			loadingText.setText(text);
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			mLoadingDialog = builder.create();
			mLoadingDialog.setView(view);
			//不可取消
			mLoadingDialog.setCancelable(false);
			mLoadingDialog.show();
		}
	}

	/**
	 * 创建ProcessDialog 显示创建ProcessDialog
	 * @param mActivity 上下文,dialog依附的窗体
	 * @param theme 		dialog主题
	 */
	public static void showDialog(Activity mActivity, @StyleRes int theme) {
		if(!isShowing()){
			ProgressDialog dialog = new ProgressDialog(mActivity,theme);
			if (dialog == null) {
				dialog = new ProgressDialog(mActivity, R.style.AppTheme_Dialog_Alert_Light);
				// 不可触摸取消
				dialog.setCanceledOnTouchOutside(false);
				// 强制取消关闭界面
				dialog.setCancelable(true);
				dialog.setOnCancelListener(dialogInterface->mActivity.finish());
				mLoadingDialog = dialog;
			}

			dialog.setMessage(UIUtil.getString(R.string.prompt_loading));
			dialog.show();
		}
	}


	/**
	 * 创建自定义的Dialog 显示创建自定义Dialog
	 * @param mActivity 上下文,dialog依附的窗体
	 * @param stringRes String资源
	 */
	public static void showDialog(FragmentActivity mActivity, @StringRes int stringRes) {
		// showDialog 关闭之前的Dialog
		DialogUtil.closeDialog();
		String desc = UIUtil.getString(stringRes);
		Bundle mBundle = new Bundle();
		mBundle.putString(com.medici.stack.ui.ProgressDialog.LOADING_DESC,desc);
		if(null == mCustomDialog){
			mCustomDialog = com.medici.stack.ui.ProgressDialog.show(mActivity.getSupportFragmentManager(),mBundle);
		}
	}

	/**
	 * 创建自定义的Dialog 显示创建自定义Dialog
	 * @param mActivity 上下文,dialog依附的窗体
	 */
	public static void showDialog(FragmentActivity mActivity) {
		if(null == mCustomDialog){
			// 设置一个干净的Bundle
			Bundle mBundle = new Bundle();
			mCustomDialog = com.medici.stack.ui.ProgressDialog.show(mActivity.getSupportFragmentManager(),mBundle);
		}
	}

	/**
	 * ProgressDialog 与 custom view Dialog
	 * @return
	 */
	public static boolean isShowing(){
		if(null == mLoadingDialog) return false;
		return mLoadingDialog.isShowing();
	}


	/**
	 * 关闭Dialog
	 */
	public static void closeDialog() {
		if(null != mLoadingDialog){
			if (mLoadingDialog.isShowing()) {
				mLoadingDialog.dismiss();
			}
			mLoadingDialog = null;
		}else if(null != mCustomDialog){
			// 关闭自定义的Dialog
			mCustomDialog.dismiss();
			mCustomDialog = null;
		}
	}
}
