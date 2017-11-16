package com.medici.stack.util;

import android.hardware.Camera.Size;

import com.orhanobut.logger.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * desc:对Camera开发设置PreviewSize和PictureSize的工具类
 *      防止设置不正常的分辨率出现setParamter的异常错误
 * author：李宗好
 * time: 2017/8/10 0010 09:41
 * email：lzh@cnbisoft.com
 */
public class CameraUtil {

    private CameraSizeComparator sizeComparator = new CameraSizeComparator();

    private static CameraUtil mCameraUtil = null;

    private CameraUtil(){}

    public static CameraUtil getInstance(){
        if(mCameraUtil == null){
            mCameraUtil = CameraUtilHolder.mCameraUtil;
        }
        return mCameraUtil;
    }

    /**
     * 返回单例的实例
     */
    private static class CameraUtilHolder{
        public static final CameraUtil mCameraUtil = new CameraUtil();
    }

    /**
     * 返回合适尺寸的预览大小
     * @param list
     * @param th
     * @return
     */
    public Size getPreviewSize(List<Size> list, int th){
        Collections.sort(list, sizeComparator);

        int i = 0;
        for(Size size:list){
            if((size.width >= th) && equalRate(size, 1.55f)){
                Logger.w("最终设置预览尺寸:w = " + size.width + "h = " + size.height);
                break;
            }
            i++;
        }

        return list.get(i);
    }

    /**
     * 返回合适尺寸的图片拍照大小
     * @param list
     * @param th
     * @return
     */
    public Size getPictureSize(List<Size> list, int th){
        Collections.sort(list, sizeComparator);

        int i = 0;
        for(Size size:list){
            if((size.width >= th) && equalRate(size, 1.55f)){
                Logger.w("最终设置图片尺寸:w = " + size.width + "h = " + size.height);
                break;
            }
            i++;
        }
        return list.get(i);
    }

    /**
     * 长宽是否符合一定的比例
     * @param s
     * @param rate
     * @return
     */
    public boolean equalRate(Size s, float rate){
        float r = (float)(s.width)/(float)(s.height);
        if(Math.abs(r - rate) <= 0.3) {
            return true;
        }else{
            return false;
        }
    }


    /**
     * 比较规则
     */
    public static class CameraSizeComparator implements Comparator<Size> {
        //按升序排列
        public int compare(Size lhs, Size rhs) {
            if(lhs.width == rhs.width){
                return 0;
            }
            else if(lhs.width > rhs.width){
                return 1;
            }
            else{
                return -1;
            }
        }

    }
}
