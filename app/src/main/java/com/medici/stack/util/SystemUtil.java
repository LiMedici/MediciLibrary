package com.medici.stack.util;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.medici.stack.util.blankj.StringUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @desc  系统工具类
 * @author 李宗好
 * @time:2016年12月15日 下午2:01:05
 *
 */
public final class SystemUtil {

	/** 获取android系统版本**/
	public static String getOSVersion() {
		String release = android.os.Build.VERSION.RELEASE;
		release = "android" + release;
		return release;
	}

	/** 获得android系统sdk版本*/
	public static String getOSVersionSDK() {
		return android.os.Build.VERSION.SDK;
	}

	/** 获得android系统sdk版本*/
	public static int getOSVersionSDKINT() {
		return android.os.Build.VERSION.SDK_INT;
	}
	
	/** 
	 * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android 
	 * @param context 
	 * @return 平板返回 True，手机返回 False 
	 */  
	public static boolean isPad(Context context) {  
	    return (context.getResources().getConfiguration().screenLayout  
	            & Configuration.SCREENLAYOUT_SIZE_MASK)  
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;  
	}

	/** 判断手机是否已插入SIM卡*/
	public static boolean isCheckSimCardAvailable() {
		Context context = UIUtil.getContext();
		if (null == context) {
			return false;
		}
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSimState() == TelephonyManager.SIM_STATE_READY;
	}

	/** sim卡是否可使用*/
	public static boolean isCanUseSim() {
		Context context = UIUtil.getContext();
		if (null == context) {
			return false;
		}
		try {
			TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			return TelephonyManager.SIM_STATE_READY == mgr.getSimState();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 取得当前sim手机卡的IMSI
	 * @return
	 */
	@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
	public static String getIMSI() {
		Context context = UIUtil.getContext();
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String imsi = tm.getSubscriberId();
			return imsi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 返回本地手机号码，这个号码不能获取到 */
	public static String getNativePhoneNumber() {
		Context context = UIUtil.getContext();
		TelephonyManager telephonyManager;
		telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String NativePhoneNumber = telephonyManager.getLine1Number();
		return NativePhoneNumber;
	}

	/** 返回手机服务商名称*/
	public static String getProvidersName() {
		String ProvidersName = null;
		// 返回唯一的用户ID;就是这张卡的编号
		String IMSI = getIMSI();
		// IMSI号前46000是国家，紧接46002是中国移动，01是中国联通，03是中国电信
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
			ProvidersName = "中国移动";
		} else if (IMSI.startsWith("46001")) {
			ProvidersName = "中国联通";
		} else if (IMSI.startsWith("46003")) {
			ProvidersName = "中国电信";
		} else {
			ProvidersName = "其他服务" + IMSI;
		}
		return ProvidersName;
	}

	/** 获取当前设备的SN */
	public static String getSimSN() {
		Context context = UIUtil.getContext();
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String simSN = tm.getSimSerialNumber();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 获得设备的横向dpi */
	public static float getWidthDpi() {
		Context context = UIUtil.getContext();
		DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
		return dm.densityDpi;
	}

	/** 获得设备的纵向dpi */
	public static float getHeightDpi() {
		Context context = UIUtil.getContext();
		DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
		return dm.ydpi;
	}

	/** 获取设备信息 */
	public static String[] getDivceInfo() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = {"", ""};
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cpuInfo;
	}

	/** 判断手机CPU是否支持NEON指令*/
	public static boolean isNEON() {
		boolean isNEON = false;
		String cupinfo = getCPUInfos();
		if (cupinfo != null) {
			cupinfo = cupinfo.toLowerCase();
			isNEON = cupinfo != null && cupinfo.contains("neon");
		}
		return isNEON;
	}

	/** 读取CPU信息文件，获取CPU信息 */
	private static String getCPUInfos() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		StringBuilder resusl = new StringBuilder();
		String resualStr = null;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((str2 = localBufferedReader.readLine()) != null) {
				resusl.append(str2);
				// String cup = str2;
			}
			if (resusl != null) {
				resualStr = resusl.toString();
				return resualStr;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resualStr;
	}

	/** 获取当前设备cpu的型号*/
	public static int getCPUModel() {
		return matchABI(getSystemProperty("ro.product.cpu.abi")) | matchABI(getSystemProperty("ro.product.cpu.abi2"));
	}

	/** 匹配当前设备的cpu型号 */
	private static int matchABI(String abiString) {
		if (TextUtils.isEmpty(abiString)) {
			return 0;
		}
		if ("armeabi".equals(abiString)) {
			return 1;
		} else if ("armeabi-v7a".equals(abiString)) {
			return 2;
		} else if ("x86".equals(abiString)) {
			return 4;
		} else if ("mips".equals(abiString)) {
			return 8;
		}
		return 0;
	}

	/** 获取CPU核心数*/
	public static int getCpuCount() {
		return Runtime.getRuntime().availableProcessors();
	}

	/** 获取Rom版本 */
	public static String getRomversion() {
		String rom = "";
		try {
			String modversion = getSystemProperty("ro.modversion");
			String displayId = getSystemProperty("ro.build.display.id");
			if (modversion != null && !modversion.equals("")) {
				rom = modversion;
			}
			if (displayId != null && !displayId.equals("")) {
				rom = displayId;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rom;
	}

	/** 获取系统配置参数 */
	public static String getSystemProperty(String key) {
		String pValue = null;
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method m = c.getMethod("get", String.class);
			pValue = m.invoke(null, key).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pValue;
	}

	/** 获取系统中的Library*/
	public static List<String> getSystemLibs() {
		Context context = UIUtil.getContext();
		if (null == context) {
			return null;
		}
		PackageManager pm = context.getPackageManager();
		String[] libNames = pm.getSystemSharedLibraryNames();
		List<String> listLibNames = Arrays.asList(libNames);
		return listLibNames;
	}

	/** 获取手机外部可用空间大小，单位为byte */
	public static long getExternalTotalSpace() {
		long totalSpace = -1L;
		if (FileUtil.isMediaMounted()) {
			try {
				// 获取外部存储目录
				String path = Environment.getExternalStorageDirectory().getPath();
				StatFs stat = new StatFs(path);
				long blockSize = stat.getBlockSize();
				long totalBlocks = stat.getBlockCount();
				totalSpace = totalBlocks * blockSize;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return totalSpace;
	}

	/** 获取外部存储可用空间，单位为byte */
	public static long getExternalSpace() {
		long availableSpace = -1L;
		if (FileUtil.isMediaMounted()) {
			try {
				String path = Environment.getExternalStorageDirectory().getPath();
				StatFs stat = new StatFs(path);
				availableSpace = stat.getAvailableBlocks() * (long) stat.getBlockSize();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return availableSpace;
	}

	/** 获取手机内部空间大小，单位为byte */
	public static long getTotalInternalSpace() {
		long totalSpace = -1L;
		try {
			// 获取该区域可用的文件系统
			String path = Environment.getDataDirectory().getPath();
			StatFs stat = new StatFs(path);
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			totalSpace = totalBlocks * blockSize;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalSpace;
	}

	/** 获取手机内部可用空间大小，单位为byte */
	public static long getAvailableInternalMemorySize() {
		long availableSpace = -1l;
		try {
			// 获取 Android 数据目录
			String path = Environment.getDataDirectory().getPath();
			// 模拟linux的df命令的一个类,获得SD卡和手机内存的使用情况
			StatFs stat = new StatFs(path);
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			// 返回 Int ，获取当前可用的存储空间
			availableSpace = availableBlocks * blockSize;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return availableSpace;
	}

	/** 获取单个应用分配内存，单位为byte */
	public static long getOneAppMaxMemory() {
		Context context = UIUtil.getContext();
		if (context == null) {
			return -1;
		}
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return activityManager.getMemoryClass() * 1024 * 1024;
	}

	/** 获取指定本应用占用的内存，单位为byte */
	public static long getUsedMemory() {
		return getUsedMemory(null);
	}

	/** 获取指定包名应用占用的内存，单位为byte */
	public static long getUsedMemory(String packageName) {
		Context context = UIUtil.getContext();
		if (context == null) {
			return -1;
		}
		if (StringUtil.isEmpty(packageName)) {
			packageName = context.getPackageName();
		}
		long size = 0;
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> runapps = activityManager.getRunningAppProcesses();
		// 遍历运行中的程序
		for (ActivityManager.RunningAppProcessInfo runapp : runapps) {
			// 得到程序进程名，进程名一般就是包名，但有些程序的进程名并不对应一个包名
			if (packageName.equals(runapp.processName)) {
				// 返回指定PID程序的内存信息，可以传多个PID，返回的也是数组型的信息
				Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{runapp.pid});
				// 得到内存信息中已使用的内存，单位是K
				size = processMemoryInfo[0].getTotalPrivateDirty() * 1024;
			}
		}
		return size;
	}

	/** 获取手机剩余内存，单位为byte */
	public static long getAvailableMemory() {
		Context context = UIUtil.getContext();
		if (context == null) {
			return -1;
		}
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		return info.availMem;
	}

	/** 获取手机总内存，单位为byte */
	public static long getTotalMemory() {
		long size = 0;
		// 系统内存信息文件
		String path = "/proc/meminfo";
		try {
			// 读出来是带单位kb的，并且单位前有空格，所以去掉最后三位
			String totalMemory = FileUtil.readProperties(path, "MemTotal", null);
			if (!StringUtil.isEmpty(totalMemory) && totalMemory.length() > 3) {
				size = Long.valueOf(totalMemory.substring(0, totalMemory.length() - 3)) * 1024;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/** 手机低内存运行阀值，单位为byte */
	public static long getThresholdMemory() {
		Context context = UIUtil.getContext();
		if (context == null) {
			return -1;
		}
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		return info.threshold;
	}

	/** 手机是否处于低内存运行*/
	public static boolean isLowMemory() {
		Context context = UIUtil.getContext();
		if (context == null) {
			return false;
		}
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		return info.lowMemory;
	}
}
