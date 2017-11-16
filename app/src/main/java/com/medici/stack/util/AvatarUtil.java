package com.medici.stack.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.cnbi.ic9.R;
import com.cnbi.ic9.util.constant.FileConstant;
import com.cnbi.ic9.util.tool.blankj.FileUtil;
import com.cnbi.ic9.util.tool.blankj.IOUtil;
import com.cnbi.ic9.util.tool.blankj.IntentUtil;
import com.cnbi.ic9.view.pop.AvatarPopWin;
import com.lzy.imagepicker.util.ProviderUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 头像上传更改的工具类
 * @Desc TODO
 * @author 李宗好
 * @time:2017年3月1日下午5:32:41
 */
public final class AvatarUtil {

	//单例对象
	private static AvatarUtil INSTANCE = null;

	//上下文对象
	private Activity mActivity;

	//弹窗显示的PopWin
	private AvatarPopWin mPopwin;

	//权限Util
	private PermissionUtil mPermissionUtil;

	//当前拍照保存的File
	public static File currentFile;
	//裁剪的文件
	public static File cropFile;
	
	public static final int REQUEST_LOCAL_SELECT = 0;// 从本地选择
	public static final int REQUEST_CAMERA_UPLOAD = 1;// 拍照上传
	public static final int REQUEST_PHOTO_CLIPPER = 2;// 图片裁剪
	
	private AvatarUtil(@NonNull Activity activity){
		mActivity = activity;
		mPermissionUtil = PermissionUtil.getInstance(mActivity);
	}

	/**
	 * 不能返回单例,因为弹窗所依赖的Activity必须一直是挂载的状态
	 * @param activity
	 * @return
     */
	public static AvatarUtil newInstance(Activity activity){
		INSTANCE = new AvatarUtil(activity);
		return INSTANCE;
	}
	
	/**
	 * open pop
	 * @param view
	 */
	public void showPopFormBottom(View view) {
		mPopwin = new AvatarPopWin(mActivity, onClickListener);
		mPopwin.showAtLocation(view, Gravity.CENTER, 0, 0);
	}

	/**
	 * close pop
	 */
	public void closePopWin() {
		if (mPopwin.isShowing()) {
			mPopwin.dismiss();
		}
	}
	
	/**
	 * 底部弹窗按钮点击事件的监听
	 */
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_take_photo:
				if (mPermissionUtil.isHavePermission(Manifest.permission.CAMERA)) {
					takePhoto();
				}
				break;
			case R.id.btn_pick_photo:
				if (mPermissionUtil.isHavePermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
					selectPhoto();
				}
				break;
			}
		}

	};
	

	
	/**
	 * 拍照
	 */
	private void takePhoto() {
		//关闭pop
		closePopWin();

		// 创建照片的存储目录
		FileUtil.createOrExistsDir(FileConstant.TAKE_PHOTO);
		currentFile = new File(FileConstant.TAKE_PHOTO, FileUtil.createEncryptPath(".jpg"));

		Intent intent = null;

		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
			Uri uri = Uri.fromFile(currentFile);
			intent = IntentUtil.getCaptureIntent(uri);
		} else {
			//7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
			//并且这样可以解决MIUI系统上拍照返回size为0的情况
			Uri uri = FileProvider.getUriForFile(mActivity, ProviderUtil.getFileProviderName(mActivity), currentFile);
			intent = IntentUtil.getCaptureIntent(uri);
		}
		mActivity.startActivityForResult(intent, REQUEST_CAMERA_UPLOAD);
		mActivity.overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}
	
	/**
	 * 从本地选择照片
	 */
	private void selectPhoto() {
		closePopWin();
		Intent intent = IntentUtil.getPickIntentWithGallery();
		mActivity.startActivityForResult(intent, REQUEST_LOCAL_SELECT);
		mActivity.overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}

	/**
	 * android 7.0 后需要用共享文件的方式
	 * @param file
	 * @return
     */
	public Uri uriFromFileShare7(File file){
		Uri uri = FileProvider.getUriForFile(mActivity, ProviderUtil.getFileProviderName(mActivity), file);
		return uri;
	}
	
	/**
	 * 剪辑图片
	 * @param uriFrom
	 */
	public void doCropPhoto(Uri uriFrom) {

		// 创建照片的存储目录
		FileUtil.createOrExistsDir(FileConstant.TAKE_PHOTO);
		currentFile = new File(FileConstant.TAKE_PHOTO, FileUtil.createEncryptPath(".jpg"));

		Uri toUri = null;

		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
			toUri = Uri.fromFile(currentFile);
		} else {
			//7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
			//并且这样可以解决MIUI系统上拍照返回size为0的情况
			toUri = FileProvider.getUriForFile(mActivity, ProviderUtil.getFileProviderName(mActivity), currentFile);
		}

		Intent intent = IntentUtil.buildImageCropIntent(uriFrom,toUri,9998,9999,80,80,true);
		mActivity.startActivityForResult(intent, REQUEST_PHOTO_CLIPPER);
		mActivity.overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}
	
	/**
	 * 保存裁剪后的图片
	 * @param bmp
	 */
	public File saveCropedBmp(@NonNull Bitmap bmp) {
		String path = UIUtil.getContext().getFilesDir() + FileUtil.createEncryptPath(".jpg");
		cropFile = new File(path);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(cropFile);
			if(null != bmp && !bmp.isRecycled()){
                bmp.compress(Bitmap.CompressFormat.PNG,100,fos);
                fos.flush();
            }
		} catch (IOException e) {
			e.printStackTrace();
			IOUtil.closeIO(fos);
		}
		return cropFile;
	}
	
	/**
	 * 裁剪后的头像显示到控件上
	 * @param proImg
	 */
	public void bmpShowProImg(File cropFile, ImageView proImg){
		GlideUtil.loadImageViewTransformation(UIUtil.getContext(),cropFile,proImg,new CropCircleTransformation(UIUtil.getContext()));
	}
	
	/**
	 * 釋放
	 */
	public void releaseObject(){
		mActivity = null;
		INSTANCE = null;
	}

}
