package com.medici.stack.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志打印至文件工具类
 * 
 * @author 李宗好
 * @time 2016/12/13 08:55
 */
public class LogWriter {
	
	private static LogWriter mLogWriter;

	private static String mPath;

	private static Writer mWriter;

	private static SimpleDateFormat df;

	private LogWriter(String path) {
		this.mPath = path;
		this.mWriter = null;
	}

	/**
	 * 打开log打印
	 * 
	 * @param path
	 *            打印文件的存储路径
	 * @return 打印log对象
	 * @throws IOException
	 */
	public static LogWriter open(String path) throws IOException {
		if (mLogWriter == null) {
			mLogWriter = new LogWriter(path);
		}
		File mFile = new File(mPath);
		if(!mFile.getParentFile().exists()){
			mFile.getParentFile().mkdirs();
			mFile.createNewFile();
		}
		mWriter = new BufferedWriter(new FileWriter(mPath), 2048);

		df = new SimpleDateFormat("[yy-MM-dd hh:mm:ss]: ");

		return mLogWriter;
	}

	/**
	 * 关闭log打印
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		mWriter.close();
	}

	/**
	 * 打印log到文件中
	 * 
	 * @param log
	 * @throws IOException
	 */
	public void print(String log) throws IOException {
		mWriter.write(df.format(new Date()));
		mWriter.write(log);
		mWriter.write("\n");
		mWriter.flush();
	}

	/**
	 * 打印log到文件中 能看到是哪个类打印的日志
	 * @param cls
	 * @param log
	 * @throws IOException
	 */
	public void print(Class cls, String log) throws IOException {
		mWriter.write(df.format(new Date()));
		mWriter.write(cls.getSimpleName() + " ");
		mWriter.write(log);
		mWriter.write("\n");
		mWriter.flush();
	}

}
