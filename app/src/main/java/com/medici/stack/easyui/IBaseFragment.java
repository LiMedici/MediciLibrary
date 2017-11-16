package com.medici.stack.easyui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Fragment基类
 * @time 2016/12/12 16:59
 * @author Administrator
 *
 */
public abstract class IBaseFragment extends RxFragment {

	private final String TAG = this.getClass().getCanonicalName();

	protected View mRootView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.d(TAG+" ======== onCreate");
	}

	@Override
	public void onStart() {
		super.onStart();
		Logger.d(TAG+" ======== onStart");
	}

	@Override
	public void onResume() {
		Logger.d(TAG+" ======== onResume");
		super.onResume();
	}

	@Override
	@CallSuper
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = createLoadedView(inflater);
		return mRootView;
	}

	@Override
	public void onPause() {
		super.onPause();
		Logger.d(TAG+" ======== onPause");
	}


	@Override
	public void onStop() {
		super.onStop();
		Logger.d(TAG+" ======== onStop");
	}

	protected abstract View createLoadedView(LayoutInflater inflater);
	
	@Override
	@CallSuper
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Logger.i(TAG+" ======== initView");
		initView();
		Logger.i(TAG+" ======== initData");
		initData();
		Logger.i(TAG+" ======== initListener");
		initListener();

	}
	
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logger.d(TAG+" ======== onDestroyView");
	}
	
	@Override
	@CallSuper
	public void onDestroy() {
		super.onDestroy();
		Logger.d(TAG+" ======== onDestroy");
		mRootView = null;
	}
	
	protected abstract void initView();
	protected abstract void initData();
	protected abstract void initListener();

}
