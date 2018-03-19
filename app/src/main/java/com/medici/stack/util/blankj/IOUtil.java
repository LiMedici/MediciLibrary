package com.medici.stack.util.blankj;


import java.io.Closeable;
import java.io.IOException;

/**
 * @desc 关闭流工具类
 * @author 李宗好
 * @time:2017年1月5日 上午8:58:58
 */
public final class IOUtil {

	/**
	 * 关闭IO
	 *
	 * @param closeables closeables
	 */
	public static void closeIO(final Closeable... closeables) {

		if (closeables == null) return;
		for (Closeable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 安静关闭IO
	 *
	 * @param closeables closeables
	 */
	public static void closeIOQuietly(final Closeable... closeables) {
		if (closeables == null) return;
		for (Closeable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (IOException ignored) {
				}
			}
		}
	}
}
