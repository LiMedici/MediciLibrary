package com.medici.stack.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;

import com.medici.stack.util.blankj.ActivityUtil;
import com.medici.stack.util.blankj.IntentUtil;
import com.medici.stack.util.blankj.SpanUtil;

/**
 * @desc 申请权限的Util 基于RxPermission
 */
public final class RxPermissionUtil {

	/**
	 * 私有化构造函数
	 */
	private RxPermissionUtil() {

	}

	/**
	 * 跳转到应用设置详情页面
	 * @param packageName
	 */
	public static void toAppSettingActivity(@NonNull final String packageName) {
		Intent mIntent = IntentUtil.getAppDetailsSettingsIntent(packageName);
		ActivityUtil.startActivity(mIntent);
	}

	/**
	 * 显示Dialog
	 * @param content
	 */
	public static void showMessageOKCancel(@NonNull final Activity mActivity, @NonNull final String content) {
		SpanUtil util = new SpanUtil();
		SpannableStringBuilder message = util.append(content).setForegroundColor(Color.RED).create();
		String packageName = mActivity.getPackageName();
		new AlertDialog.Builder(mActivity)
				.setTitle("提示")
				.setMessage(message)
				.setPositiveButton("前去设置", ((dialogInterface, i) -> toAppSettingActivity(packageName)))
				.setNegativeButton("禁止", null)
				.create()
				.show();
	}

}
