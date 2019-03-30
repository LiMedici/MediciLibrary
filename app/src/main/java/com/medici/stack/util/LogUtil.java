package com.medici.stack.util;

import android.util.Log;

import com.medici.stack.R;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @desc 日志工具类
 */
public class LogUtil {

	private static LogUtil INSTANCE = null;

	private LogUtil(){}

	/**
	 * 打开关闭log打印的闸
	 */
	private static boolean logBrake;
	
	/**
	 * 打开log日志打印闸
	 */
	public void openLogBrake() {
		logBrake = true;
	}
	
	/**
	 * 关闭log日志打印闸
	 */
	public void closeLogBrake(){
		logBrake = false;
	}

	private static String makeLogTag(Class cls) {
		return UIUtil.getString(R.string.app_name) + cls.getSimpleName();
	}

	public static LogUtil getInstance(){
		if(INSTANCE == null) {
			synchronized (LogUtil.class) {
				if (INSTANCE == null) {
					INSTANCE = new LogUtil();
				}
			}
		}
		return INSTANCE;
	}

	/**
	 * 打印消息日志
	 * @param dLog
	 */
	public static void d(Class cls,String dLog){
		if(logBrake)
			Log.d(makeLogTag(cls), dLog);
	}
	
	/**
	 * 打印消息日志
	 * @param iLog
	 */
	public static void i(Class cls,String iLog){
		if(logBrake)
		Log.i(makeLogTag(cls), iLog);
	}
	
	/**
	 * 打印警告日志
	 * @param wLog
	 */
	public static void w(Class cls,String wLog){
		if(logBrake)
		Log.w(makeLogTag(cls), wLog);
	}

	/**
	 * 打印错误日志
	 * @param eLog
	 */
	public static void e(Class cls,String eLog){
		if(logBrake)
			Log.e(makeLogTag(cls), eLog);
	}
	
}