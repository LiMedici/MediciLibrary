package com.medici.stack.util;

import java.util.Properties;

/**
 * @desc 读取raw下的配置文件
 */
public class PropertyUtil {
	
	public static Properties loadProperties(String name,String defType) {
        Properties props = new Properties();
        int id = UIUtil.getResources().getIdentifier(name, defType, UIUtil.getContext().getPackageName());
        try {
			props.load(UIUtil.getResources().openRawResource(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
        return props;
	}
}
