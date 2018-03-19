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
 * @time 2016/12/12 16:59
 * @author Administrator
 *
 */
public abstract class IBaseFragment extends Fragment {

	private static final String TAG = IBaseFragment.class.getCanonicalName();

	protected View mRootView = null;

	protected BaseData m;

	protected PlaceHolderView mPlaceHolderView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m = new BaseData(this);
		Logger.d(TAG+" ======== onCreate");
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		Logger.d(TAG+" ======== onAttach");

		Logger.d(TAG+" ======== initArgs");
		// 初始化参数
		initArgs(getArguments());

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
		if (mRootView == null) {
			int layId = getContentLayoutId();
			// 初始化当前的跟布局，但是不在创建时就添加到container里边
			View view = inflater.inflate(layId, container, false);
			mRootView = view;
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
		Logger.d(TAG+" ======== onPause");
	}


	@Override
	public void onStop() {
		super.onStop();
		Logger.d(TAG+" ======== onStop");
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
		Logger.d(TAG+" ======== initView");
		initView();
		Logger.d(TAG+" ======== initData");
		initData();
		Logger.d(TAG+" ======== initListener");
		initListener();
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
	protected abstract void initView();

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 初始化View各种监听
	 */
	protected abstract void initListener();

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
		Logger.d(TAG+" ======== onDestroyView");
	}

	@Override
	@CallSuper
	public void onDestroy() {
		super.onDestroy();
		Logger.d(TAG+" ======== onDestroy");
		m.release();
		mRootView = null;
	}

}
