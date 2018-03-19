package com.medici.stack.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.medici.stack.util.blankj.ActivityUtil;
import com.medici.stack.util.blankj.IntentUtil;

/**
 * 
 * @desc 申请权限的Util 充满限制 只能对许多Permisson的拒绝返回一种Content描述
 * @author 李宗好
 * @time:2016年12月28日 下午2:30:00
 *
 */
public final class PermissionUtil {
	/**
	 * util对象
	 */
	private static PermissionUtil INSTANCE = null;

	/**
	 * 请求权限的Activity
	 */
	private Activity mActivity;

	/**
	 * 显示被狠心拒绝后显示的文本
	 */
	private String content;
	
	private IRequestPermissionListener mListener;

	/**
	 * 私有化构造函数
	 * @param mActivity
	 */
	private PermissionUtil(Activity mActivity) {
		this.mActivity = mActivity;
	}

	public static PermissionUtil getInstance(Activity mActivity) {
		INSTANCE = new PermissionUtil(mActivity);
		return INSTANCE;
	}

	/**
	 * 跳转到应用设置详情页面
	 */
	public void toAppDetailSetting() {
		String packageName = mActivity.getPackageName();
		Intent intent = IntentUtil.getAppDetailsSettingsIntent(packageName);
		ActivityUtil.startActivity(intent);
	}

	/**
	 * 显示Dialog
	 * @param mActivity
	 * @param message
	 */
	public void showMessageOKCancel(@NonNull final Activity mActivity, @NonNull final CharSequence message) {
		new AlertDialog.Builder(mActivity)
				.setTitle("提示")
				.setMessage(message)
				.setPositiveButton("前去设置", (dialog,witch)->toAppDetailSetting())
				.setNegativeButton("禁止", null).create().show();
	}

	/**
	 * 请求权限
	 * @param permissions 权限组
	 * @param content 被用户禁止显示提醒的content
	 * @param requestCode 请求Code
	 * @param listener 请求回调
     */
	public void requestPermission(@NonNull final String[] permissions, @NonNull final String content,
                                  final int requestCode, @NonNull IRequestPermissionListener listener) {

		this.content = content;
		setIRequestPermissionListener(listener);

		//系统版本判断
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// 请求权限
			ActivityCompat.requestPermissions(mActivity, permissions, requestCode);
		}
	}

	/**
	 * 处理请求权限的结果
	 * @param permissions
	 * @param grantResults
	 */
	public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		for (int i = 0; i < grantResults.length ; i++) {
			if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
				// Permission Granted 请求权限通过
				if(mListener!=null){
					mListener.result(permissions[i], content, true, true);
				}
			} else {
				// Permission Denied 请求权限失败
				boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
						permissions[i]);

				// 表明用户彻底禁止弹窗请求。
				if (!isTip) {
					// 提示用户进入用户权限设置
					if(mListener!=null){
						mListener.result(permissions[i], content, false, isTip);
					}					
				}
			}
		}
	}
	
	/**
	 * 是否已经取得权限

	 * @return true false
	 */
	public boolean isHavePermission(String permission) {
		int hasReadExternalStoragePermission = ContextCompat.checkSelfPermission(mActivity, permission);
		// 已经取得该权限
		if (hasReadExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
			return true;
		}
		return false;
	}
	
	/**
	 * 设置回调的接口
	 * @param listener
	 */
	private void setIRequestPermissionListener(IRequestPermissionListener listener) {
		this.mListener = listener;
	}
	
	public interface IRequestPermissionListener{

		/**
		 * 回调请求结果
		 * @param permission 请求的权限
		 * @param content    请求被狠心拒绝后显示的文本
		 * @param isSuccess  是否获取权限
		 * @param isTip  是否被狠心拒绝
		 */
		void result(String permission, String content, boolean isSuccess, boolean isTip);
	}
}
