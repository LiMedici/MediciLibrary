package com.medici.stack.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medici.stack.model.data.BaseData;
import com.medici.stack.widget.convention.PlaceHolderView;
import com.orhanobut.logger.Logger;

/**
 * Fragment基类
 */
public abstract class IBaseFragment extends Fragment {

	protected View mRootView = null;

	protected PlaceHolderView mPlaceHolderView;

	protected BaseData m;

	// 标示是否第一次初始化数据
	protected boolean mIsFirstInitData = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.d("%s ======== onCreate",this.getClass().getCanonicalName());
		m = new BaseData(this);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		Logger.d("%s ======== onAttach",this.getClass().getCanonicalName());
		Logger.i("%s ======== initArgs",this.getClass().getCanonicalName());
		// 初始化参数
		initArgs(getArguments());

	}

	@Override
	public void onStart() {
		super.onStart();
		Logger.d("%s ======== onStart",this.getClass().getCanonicalName());
	}

	@Override
	public void onResume() {
		Logger.d("%s ======== onResume",this.getClass().getCanonicalName());
		super.onResume();
	}

	@Override
	@CallSuper
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mRootView == null) {
			int layId = getContentLayoutId();
			// 初始化当前的跟布局，但是不在创建时就添加到container里边
			View view = inflater.inflate(layId, container, false);
			mRootView = view;
			initWidget();
		} else {
			if (mRootView.getParent() != null) {
				// 把当前Root从其父控件中移除
				((ViewGroup) mRootView.getParent()).removeView(mRootView);
			}
		}

		return mRootView;
	}

	@Override
	public void onPause() {
		super.onPause();
		Logger.d("%s ======== onPause",this.getClass().getCanonicalName());
	}


	@Override
	public void onStop() {
		super.onStop();
		Logger.d("%s ======== onStop",this.getClass().getCanonicalName());
	}

	@LayoutRes
	/**
	 * 获取布局Id
	 * @return
	 */
	protected abstract int getContentLayoutId();

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (mIsFirstInitData) {
			// 触发一次以后就不会触发
			mIsFirstInitData = false;
			// 触发
			onFirstInit();
		}
		// 当View创建完成后初始化数据
		initData();
	}

	/**
	 * 初始化Args数据
	 * @param bundle
	 * @return 初始化成功或者失败
	 */
	protected boolean initArgs(Bundle bundle){
		return true;
	}

	/**
	 * 初始化View
	 */
	protected void initWidget(){
		Logger.i("%s ======== initWidget",this.getClass().getCanonicalName());
	}

	/**
	 * 初始化数据
	 */
	protected void initData(){
		Logger.i("%s ======== initData",this.getClass().getCanonicalName());
	}

	/**
	 * 当首次初始化数据的时候会调用的方法
	 */
	protected void onFirstInit() {
		Logger.i("%s ======== onFirstInit",this.getClass().getCanonicalName());
	}

	/**
	 * 设置占位布局
	 *
	 * @param placeHolderView 继承了占位布局规范的View
	 */
	public void setPlaceHolderView(PlaceHolderView placeHolderView) {
		this.mPlaceHolderView = placeHolderView;
	}

	/**
	 * 返回按键触发时调用
	 *
	 * @return 返回True代表我已处理返回逻辑，Activity不用自己finish。
	 * 返回False代表我没有处理逻辑，Activity自己走自己的逻辑
	 */
	public boolean onBackPressed() {
		return false;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logger.d("%s ======== onDestroyView",this.getClass().getCanonicalName());
	}

	@Override
	@CallSuper
	public void onDestroy() {
		super.onDestroy();
		Logger.d("%s ======== onDestroy",this.getClass().getCanonicalName());
		m.release();
		mRootView = null;
	}

}
