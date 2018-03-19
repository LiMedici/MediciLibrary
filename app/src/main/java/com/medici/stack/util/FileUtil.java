package com.medici.stack.util;

import android.content.Context;
import android.os.Environment;
import android.os.SystemClock;
import android.os.storage.StorageManager;
import android.webkit.MimeTypeMap;

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
 * @author cnbilzh
 * @time:2016年12月15日 下午1:41:47
 */

public class FileUtil {
	/**
	 * 应用根目录
	 */
	public static final String ROOT_DIR = "CloudBrainPower";

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

	/** 获取file目录 **/
	public static String getFileDir() {
		return getDir(getExternalStoragePath(),FILE_DIR);
	}

	/**
	 * 获取头像的临时存储文件地址
	 *
	 * @return 临时文件
	 */
	public static File getPortraitTmpFile() {
		// 得到头像目录的缓存地址
		File dir = new File(getCacheDir(), PORTRAIT_DIR);
		// 创建所有的对应的文件夹
		//noinspection ResultOfMethodCallIgnored
		dir.mkdirs();

		// 删除旧的一些缓存为文件
		File[] files = dir.listFiles();
		if (files != null && files.length > 0) {
			for (File file : files) {
				//noinspection ResultOfMethodCallIgnored
				file.delete();
			}
		}

		// 返回一个当前时间戳的目录文件地址
		File path = new File(dir, SystemClock.uptimeMillis() + ".jpg");
		return path.getAbsoluteFile();
	}

	/**
	 * 获取声音文件的本地地址
	 *
	 * @param isTmp 是否是缓存文件， True，每次返回的文件地址是一样的
	 * @return 录音文件的地址
	 */
	public static File getAudioTmpFile(boolean isTmp) {
		File dir = new File(getCacheDir(), AUDIO_DIR);
		//noinspection ResultOfMethodCallIgnored
		dir.mkdirs();

		File[] files = dir.listFiles();
		if (files != null && files.length > 0) {
			for (File file : files) {
				//noinspection ResultOfMethodCallIgnored
//				file.delete();
			}
		}

		// aar
		File path = new File(dir, isTmp ? "tmp.mp3" : SystemClock.uptimeMillis() + ".mp3");
		return path.getAbsoluteFile();
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
	 * 根据键读取值
	 * @param filePath Properties文件地址
	 * @param key 键
	 * @param defaultValue 默认返回值
	 * @return 值
	 */
	public static String readProperties(String filePath, String key, String defaultValue) {
		if (StringUtil.isEmpty(key) || StringUtil.isEmpty(filePath)) {
			return null;
		}
		String value = null;
		FileInputStream fis = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			fis = new FileInputStream(f);
			Properties p = new Properties();
			p.load(fis);
			value = p.getProperty(key, defaultValue);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeIO(fis);
		}
		return value;
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
		final String extension = com.medici.stack.util.blankj.FileUtil.getFileExtension(file);
		return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
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

	/**
	 * 解压文件
	 * @param is 压缩包文件流
	 * @param dir 解压目录
	 * @throws IOException
	 */
	public static void unzip(InputStream is, String dir) throws IOException {
		File dest = new File(dir);
		if (!dest.exists()) {
			dest.mkdirs();
		}

		if (!dest.isDirectory())
			throw new IOException("Invalid Unzip destination " + dest);
		if (null == is) {
			throw new IOException("InputStream is null");
		}

		ZipInputStream zip = new ZipInputStream(is);

		ZipEntry ze;
		while ((ze = zip.getNextEntry()) != null) {
			final String path = dest.getAbsolutePath()
					+ File.separator + ze.getName();

			String zeName = ze.getName();
			char cTail = zeName.charAt(zeName.length() - 1);
			if (cTail == File.separatorChar) {
				File file = new File(path);
				if (!file.exists()) {
					if (!file.mkdirs()) {
						throw new IOException("Unable to create folder " + file);
					}
				}
				continue;
			}

			FileOutputStream fos = new FileOutputStream(path);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = zip.read(bytes)) != -1) {
				fos.write(bytes, 0, c);
			}
			zip.closeEntry();
			fos.close();
		}
	}

}
