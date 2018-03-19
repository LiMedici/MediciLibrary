package com.medici.stack.util;

/**
 * @desc: 防手贱Util
 * @author：李宗好 on 2017/5/9 0009 09:30
 * @email：lzh@cnbisoft.com
 */
public class CommonUtil {

    /**
     * 最后点击的时间
     */
    private static long lastClickTime;

    /**
     * 冷却时间
     */
    private static final int DELAY_TIME = 100;

    /**
     * 判断是否双击,没有到冷却时间
     * @return 是否是手贱行为
     */
    public static boolean isViewDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < DELAY_TIME) {
            LogUtil.i(CommonUtil.class,"防手贱,没处理事件");
            return true;
        }
        //发生了响应,就重新计时
        lastClickTime = time;
        return false;
    }
}
