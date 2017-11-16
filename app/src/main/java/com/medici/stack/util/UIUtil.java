package com.medici.stack.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.cnbi.ic9.Ic9ClientApp;
import com.squareup.leakcanary.RefWatcher;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

public final class UIUtil {
	@SuppressLint("StaticFieldLeak")
	private static Application application;

	private static WeakReference<Activity> topActivityWeakRef;
	private static List<Activity> listActivity = new LinkedList<>();

	private UIUtil() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	/**
	 * 初始化工具类
	 * @param app 应用
	 */
	public static void init(@NonNull final Application app) {
		UIUtil.application = app;
		app.registerActivityLifecycleCallbacks(mCallback);
	}

	/**
	 * 获取Application
	 *
	 * @return Application
	 */
	public static Application getApp() {
		if (application != null) return application;
		throw new NullPointerException("u should init first");
	}

	public static List<Activity> getListActivity() {
		return listActivity;
	}

	private static void setTopActivityWeakRef(Activity activity) {
		if (topActivityWeakRef == null || !activity.equals(topActivityWeakRef.get())) {
			topActivityWeakRef = new WeakReference<>(activity);
		}
	}

	@CheckResult
	public static Activity getTopActivity() {
		if(null == topActivityWeakRef || null == topActivityWeakRef.get()){
			return null;
		}
		return topActivityWeakRef.get();
	}

	public static Context getContext() {
		return Ic9ClientApp.getApplication();
	}

	public static Thread getMainThread() {
		return Ic9ClientApp.getMainThread();
	}

	public static long getMainThreadId() {
		return Ic9ClientApp.getMainThreadId();
	}

	private static Application.ActivityLifecycleCallbacks mCallback = new Application.ActivityLifecycleCallbacks() {
		@Override
		public void onActivityCreated(Activity activity, Bundle bundle) {
			listActivity.add(activity);
			setTopActivityWeakRef(activity);
		}

		@Override
		public void onActivityStarted(Activity activity) {
			setTopActivityWeakRef(activity);
		}

		@Override
		public void onActivityResumed(Activity activity) {
			setTopActivityWeakRef(activity);
		}

		@Override
		public void onActivityPaused(Activity activity) {

		}

		@Override
		public void onActivityStopped(Activity activity) {

		}

		@Override
		public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

		}

		@Override
		public void onActivityDestroyed(Activity activity) {
			listActivity.remove(activity);
		}
	};



	/*获取RefWathcer检查内存是否泄露*/
	public static RefWatcher getRefWathcher(){
		return Ic9ClientApp.getRefWatcher();
	}

	/** 获取主线程的handler */
	public static Handler getHandler() {
		return Ic9ClientApp.getMainThreadHandler();
	}

	/** 延时在主线程执行runnable */
	public static boolean postDelayed(Runnable runnable, long delayMillis) {
		return getHandler().postDelayed(runnable, delayMillis);
	}

	/** 在主线程执行runnable */
	public static boolean post(Runnable runnable) {
		return getHandler().post(runnable);
	}

	/** 从主线程looper里面移除runnable */
	public static void removeCallbacks(Runnable runnable) {
		getHandler().removeCallbacks(runnable);
	}

	public static View inflate(int resId){
		return LayoutInflater.from(getContext()).inflate(resId,null);
	}

	/** 获取资源 */
	public static Resources getResources() {
		return getContext().getResources();
	}

	/** 获取文字 */
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/** 获取文字数组 */
	public static String[] getStringArray(int resId) {
		return getResources().getStringArray(resId);
	}

	/** 获取dimen */
	public static int getDimens(int resId) {
		return getResources().getDimensionPixelSize(resId);
	}

	/** 获取drawable */
	public static Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}

	/** 获取颜色 */
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/** 获取颜色选择器*/
	public static ColorStateList getColorStateList(int resId) {
		return getResources().getColorStateList(resId);
	}
	//判断当前的线程是不是在主线程
	public static boolean isRunInMainThread() {
		return android.os.Process.myTid() == getMainThreadId();
	}

	public static void runInMainThread(Runnable runnable) {
		if (isRunInMainThread()) {
			runnable.run();
		} else {
			post(runnable);
		}
	}

	public static void startActivity(Intent intent){
		Activity activity = (Activity) getTopActivity();
		if(activity != null){
			activity.startActivity(intent);
		}else{
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getContext().startActivity(intent);
		}
	}

	/** 对toast的简易封装线程安全，可以在非UI线程调用*/
	public static void showToastSafe(final int resId) {
		showToastSafe(getString(resId));
	}

	/** 对toast的简易封装线程安全，可以在非UI线程调用*/
	public static void showToastSafe(final String str) {
		if (isRunInMainThread()) {
			showToast(str);
		} else {
			post(new Runnable() {
				@Override
				public void run() {
					showToast(str);
				}
			});
		}
	}

	private static void showToast(String str) {
		Activity frontActivity = (Activity) getTopActivity();
		if (frontActivity != null) {
			Toast.makeText(frontActivity, str, Toast.LENGTH_LONG).show();
		}
	}
}


