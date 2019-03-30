package com.medici.stack.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;

import com.medici.stack.util.blankj.EncryptUtil;

/**
 * ***************************************
 *
 * @desc: 从Uri转化为绝对路径的工具类
 *
 * ***************************************
 */
public final class UriUtil {



    /**
     * 根据图片Uri获取在手机上的绝对路径
     */
    public static String getRealPathFromUri_Scheme(@NonNull Uri uri) {
        String scheme = uri.getScheme();
        String path = null;
        if (scheme == null)
            path = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            path = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = UIUtil.getContext().getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        path = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return path;
    }

    /**
     * 根据图片的Uri获取图片的绝对路径(适配多种API)
     *
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(@NonNull Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < Build.VERSION_CODES.HONEYCOMB) return getRealPathFromUri_BelowApi11(uri);
        if (sdkVersion < Build.VERSION_CODES.KITKAT) return getRealPathFromUri_Api11To18(uri);
        else return getRealPathFromUri_AboveApi19(uri);
    }

    /**
     * 适配api19以上,根据uri获取图片的绝对路径
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getRealPathFromUri_AboveApi19(Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};

        Cursor cursor = UIUtil.getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);
        if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    /**
     * 适配api11-api18,根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_Api11To18(Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(UIUtil.getContext(), uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_BelowApi11(Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = UIUtil.getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 用当前时间戳生成文件名
     * @param extension 扩展名
     * @return
     */
    public static String getPhotoFileName(@NonNull String extension) {
        String name = DateUtil.StringNowTime();
        String encryptName = EncryptUtil.encryptMD5ToString(name);
        if (TextUtils.isEmpty(encryptName)) {
            return name + extension;
        } else {
            return encryptName + extension;
        }
    }

}
