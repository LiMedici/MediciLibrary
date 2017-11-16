package com.medici.stack.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.cnbi.ic9.R;
import com.orhanobut.logger.Logger;

/**
 * @Desc DialogUtil
 * @author 李宗好
 * @time:2017年2月22日下午2:23:41
 */
public class DialogUtil {

	private static AlertDialog dialog;

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
			dialog = builder.create();
			dialog.setView(view);
			//不可取消
			dialog.setCancelable(false);
			dialog.show();
		}

	}
	
	public static boolean isShowing(){
		if(null == dialog) return false;
		return dialog.isShowing();
	}

	public static void closeDialog() {
		if(null != dialog){
			if (dialog.isShowing()) {
				Logger.w("是谁调用的呢 "+dialog.getContext().toString());
				dialog.dismiss();
			}
			dialog = null;
		}
	}
}
