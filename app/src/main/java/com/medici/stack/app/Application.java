package com.medici.stack.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.medici.stack.factory.throwable.AppUncaughtExceptionHandler;
import com.medici.stack.util.LogUtil;
import com.medici.stack.util.ResolutionUtil;
import com.medici.stack.util.UIUtil;
import com.medici.stack.util.blankj.EmptyUtil;
import com.medici.stack.util.blankj.ProcessUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * @author cnbilzh
 * @time 2017/12/5 11:08
 *
 * ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////                                                                                                               ////
 * ////                                                                                                               ////
 * ////                                                                                                               ////
 * ////                                                                                                               ////
 * ////                                                   _ooOoo_                                                     ////
 * ////                                                  o8888888o
 * ////                                                  88" . "88
 * ////                                                  (| -_- |)
 * ////                                                  O\  =  /O
 * ////                                               ____/`---'\____
 * ////                                             .'  \\|     |//  `.
 * ////                                            /  \\|||  :  |||//  \
 * ////                                           /  _||||| -:- |||||-  \
 * ////                                           |   | \\\  -  /// |   |
 * ////                                           | \_|  ''\---/''  |   |
 * ////                                           \  .-\__  `-`  ___/-. /
 * ////                                         ___`. .'  /--.--\  `. . __
 * ////                                      ."" '<  `.___\_<|>_/___.'  >'"".
 * ////                                     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * ////                                     \  \ `-.   \_ __\ /__ _/   .-` /  /
 * ////                                ======`-.____`-.___\_____/___.-`____.-'======
 * ////                                                   `=---='
 * ////                                ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * ////                                           佛祖保佑       永无BUG
 * ////
 * ////                                                                                                               ////
 * ////                                                                                                               ////
 * ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
public class Application extends android.app.Application {

	private static Context mContext = null;
	/**
	 * 获得主线程
	 */
	private static Thread mMainThread = null;
	/**
	 * 获得主线程的id
	 */
	private static int mMainThreadId;
	/**
	 * 获得主线程的handler
	 */
	private static Handler mMainHandler = null;
	/**
	 * 获得主线程的Looper
	 */
	private static Looper mMainLooper = null;
	/**
	 * 内存泄漏检测的RefWatcher
	 */
	 private static RefWatcher mRefWatcher = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		mMainThread = Thread.currentThread();
		mMainThreadId = android.os.Process.myTid();
		mMainHandler = new Handler();
		mMainLooper = getMainLooper();

		Thread.setDefaultUncaughtExceptionHandler(new AppUncaughtExceptionHandler());

		// 初始化工具类UIUtil
		UIUtil.init(this);
		//获取进程名称
		String processName = ProcessUtil.getForegroundProcessName();
		//避免运行在其它进程的组件实例化的时候，多次执行初始化的操作
		if(!EmptyUtil.isEmpty(processName) && processName.equals(this.getPackageName())){

			ResolutionUtil.getInstance().init(this);
			//打开日志阀
			LogUtil.getInstance().openLogBrake();

			/**
			 * desc 初始化Logger
			 * <p>showThreadInfo (Optional) Whether to show thread info or not. Default true</p>
			 * <p>methodCount (Optional) How many method line to show. Default 2</p>
			 * <p>methodOffset (Optional) Hides internal method calls up to offset. Default 5</p>
			 * <p>logStrategy (Optional) Changes the log strategy to print out. Default LogCat</p>
			 * <p>tag (Optional) print out tag</p>
			 */
			FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
					.showThreadInfo(true)
					.methodCount(2)
					.methodOffset(5)
					.tag("CloudBrainPower")
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
}
