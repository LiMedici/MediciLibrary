package com.medici.stack.factory;

import android.support.v4.app.Fragment;

import com.google.common.collect.Maps;

import java.util.HashMap;

/**
 * @desc Fragment工厂类 享元设计模式
 * @author 李宗好
 * @time:2017年1月18日 下午3:04:48
 */
public class FragmentFactory {

	public static HashMap<String,Fragment> mFrags = Maps.newHashMap();
	
	public static Fragment createFragment(String key, int position){

		Fragment fragment = mFrags.get(key);
		
		if(null == fragment){
			switch (key) {
			default:
				break;
			}
			
			mFrags.put(key, fragment);
		}
		
		return fragment;
	}
	
	/**
	 * 清除缓存中的fragment
	 */
	public static void cleanAllFragment(){
		mFrags.clear();
	}

	public static Fragment cleanFragment(String key){
		return mFrags.remove(key);
	}
	
}
