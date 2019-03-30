package com.medici.stack.util;

import android.content.Context;
import android.os.Environment;
import android.os.SystemClock;
import android.os.storage.StorageManager;
import android.webkit.MimeTypeMap;

import com.medici.stack.R;
import com.medici.stack.util.blankj.IOUtil;
import com.medici.stack.util.blankj.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @desc File操作类
 */

public final class FileUtil {

	private FileUtil(){}

	public static final String ROOT_DIR = UIUtil.getString(R.string.app_name);

	/**
	 * 应用下载目录
	 */
	public static final String DOWNLOAD_DIR = "download";

	/**
	 * 应用缓存目录
	 */
	public static final String CACHE_DIR = "cache";
	/**
	 * 应用文件目录
	 */
	public static final String FILE_DIR = "file";

	/**
	 * 头像缓存目录
	 */
	public static final String PORTRAIT_DIR = "portrait";

	/**
	 * 音频录制缓存目录
	 */
	public static final String AUDIO_DIR = "audio";


	/** 获取下载目录 **/
	public static String getDownloadDir() {
		return getDir(getExternalStoragePath(),DOWNLOAD_DIR);
	}

	/** 获取缓存目录 **/
	public static String getCacheDir() {
		return getDir(getExternalStoragePath(),CACHE_DIR);
	}

	/** 获取头像目录 **/
	public static String getPortraitDir() {
		return getDir(getExternalStoragePath(),PORTRAIT_DIR);
	}

	/** 获取音频目录 **/
	public static String getAudioDir() {
		return getDir(getExternalStoragePath(),AUDIO_DIR);
	}

	/** 获取SD下的应用目录 */
	public static String getExternalStoragePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		sb.append(File.separator);
		sb.append(ROOT_DIR);
		sb.append(File.separator);
		return sb.toString();
	}

	/** 获取应用的file目录 */
	public static String getFilePath() {
		File f = UIUtil.getContext().getFilesDir();
		if (null == f) {
			return null;
		} else {
			return f.getAbsolutePath() + "/";
		}
	}

	/** 获取应用的cache目录 */
	public static String getCachePath() {
		File f = UIUtil.getContext().getCacheDir();
		if (null == f) {
			return null;
		} else {
			return f.getAbsolutePath() + "/";
		}
	}

	/** 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录 */
	public static String getDir(String rootDir,String name) {
		StringBuilder sb = new StringBuilder();
		// 拼接目录 rootDir 后缀已经有了 separator
		sb.append(rootDir);
		sb.append(name);
		sb.append(File.separator);
		String path = sb.toString();
		File file = new File(path);
		file.mkdirs();
		return path;
	}

	/**
	 * 转换文件大小 Formater.formatFileSize();
	 * 
	 * @param fileSize
	 * @return 转换后字符串
	 */
	public static String formatFileSize(long fileSize) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileSize < 1024) {
			fileSizeString = df.format((double) fileSize) + "B";
		} else if (fileSize < 1048576) {
			fileSizeString = df.format((double) fileSize / 1024) + "K";
		} else if (fileSize < 1073741824) {
			fileSizeString = df.format((double) fileSize / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileSize / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/** 
	 * 根据文件后缀名获得对应的MIME类型。 
	 * @param file 文件
	 */  
	public static String getMIMEType(final File file) {
		if (file == null) return null;
		String filePath = file.getPath();
		if (isSpace(filePath)) return filePath;
		int lastPoi = filePath.lastIndexOf('.');
		int lastSep = filePath.lastIndexOf(File.separator);
		if (lastPoi == -1 || lastSep >= lastPoi) return "";
		final String extension = filePath.substring(lastPoi + 1);
		return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
	}

	private static boolean isSpace(final String s) {
		if (s == null) return true;
		for (int i = 0, len = s.length(); i < len; ++i) {
			if (!Character.isWhitespace(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	/**
     * 获取外置SD卡路径
     * 不管这个SD卡路径上的存储卡是否挂载 都会返回
     */
    public static String[] getStoragePath() {
        StorageManager storageManager = (StorageManager) UIUtil.getContext().getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
            getVolumePathsMethod.setAccessible(true);
            Object[] params = {};
            Object invoke = getVolumePathsMethod.invoke(storageManager, params);
            return (String[])invoke;
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 获取存储卡的挂载状态.
     * @param path 判断存储的路径
     * @return
     */
    public static String getStorageState(String path) {  
        try {  
            StorageManager sm = (StorageManager) UIUtil.getContext().getSystemService(Context.STORAGE_SERVICE);
            Method getVolumeStateMethod = StorageManager.class.getMethod("getVolumeState", new Class[] {String.class});  
            String state = (String) getVolumeStateMethod.invoke(sm, path);  
            return state;  
        } catch (Exception e) {  
            e.printStackTrace();
        }  
        return null;  
    }

	/** 判断SD卡是否挂载 */
	public static boolean isMediaMounted() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return true;
		} else {
			return false;
		}
	}

}
