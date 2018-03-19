package com.medici.stack.util;

import android.util.Log;

import com.medici.stack.R;

/**
 * @desc 日志工具类
 * @author 龚佳新
 * @time:2016年12月12日 下午12:31:32
 */
public class LogUtil {

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

	/**
	 * 获取log打印tag
	 * @param cls
	 * @return
	 */
	public static String makeLogTag(Class cls) {
		return UIUtil.getString(R.string.app_name) + cls.getSimpleName();
	}

	public static LogUtil getInstance(){
		return new LogUtil();
	}
	
	/**
	 * 打印错误日志
	 * @param eLog
	 */
	public static void e(Class cls,String eLog){
		if(logBrake)
		Log.e(makeLogTag(cls), eLog);
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
	
}