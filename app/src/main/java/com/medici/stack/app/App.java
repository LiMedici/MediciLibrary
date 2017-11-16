package com.medici.stack.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;

import com.medici.stack.model.throwable.AppUncaughtExceptionHandler;
import com.medici.stack.util.LogUtil;
import com.medici.stack.util.ResolutionUtil;
import com.medici.stack.util.UIUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.Map;

public class App extends Application {

	private static Context mContext = null;
	// 获得主线程
	private static Thread mMainThread = null;
	// 获得主线程的id
	private static int mMainThreadId;
	// 获得主线程的handler
	private static Handler mMainHandler = null;
	// 获得主线程的Looper
	private static Looper mMainLooper = null;

	private static RefWatcher mRefWatcher = null;

	
	private static Map<String,Object> mSettingMap = null;
	
	@Override
	public void onCreate() {
		mContext = this;
		mMainThread = Thread.currentThread();
		mMainThreadId = android.os.Process.myTid();
		mMainHandler = new Handler();
		mMainLooper = getMainLooper();

		UIUtil.init(this);

		ResolutionUtil.getInstance().init(this);
		//打开日志阀
		LogUtil.getInstance().openLogBrake();

		Thread.setDefaultUncaughtExceptionHandler(new AppUncaughtExceptionHandler());

		//初始化Logger
		FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
				.showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
				.methodCount(7)         // (Optional) How many method line to show. Default 2
//				.methodOffset(5)        // (Optional) Hides internal method calls up to offset. Default 5
//				.logStrategy()  // (Optional) Changes the log strategy to print out. Default LogCat
				.tag("IC9-Output")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
				.build();

		Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

		//初始化检查内存泄露的检查器
		if (!LeakCanary.isInAnalyzerProcess(this)) {
			// This process is dedicated to LeakCanary for heap analysis.
			// You should not init your app in this process.
			mRefWatcher = LeakCanary.install(this);
		}
		// Normal app init code...
	}

	/**
	 * 方法数量超过65535的解决方式
	 */
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	public static Context getApplication() {
		return mContext;
	}

	public static Thread getMainThread() {
		return mMainThread;
	}

	public static int getMainThreadId() {
		return mMainThreadId;
	}

	public static Handler getMainThreadHandler() {
		return mMainHandler;
	}

	public static RefWatcher getRefWatcher(){
		return mRefWatcher;
	}

	public static Map<String,Object> getSettingMap(){
		return mSettingMap;
	}

	public static void setSettingMap(Map<String,Object> settingMap){
		mSettingMap = settingMap;
	}
}
