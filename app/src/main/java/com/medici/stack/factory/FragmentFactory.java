package com.medici.stack.factory;

import android.support.v4.app.Fragment;

import com.google.common.collect.Maps;
import com.medici.stack.util.blankj.EmptyUtil;

import java.util.HashMap;

/**
 * @desc Fragment工厂类 享元设计模式
 */
public class FragmentFactory {

	public static HashMap<String,Fragment> mFrags = Maps.newHashMap();
	
	public static Fragment createFragment(String key, int position){

		final Fragment fragment = mFrags.get(key);
		
		if(EmptyUtil.isEmpty(fragment)){
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
