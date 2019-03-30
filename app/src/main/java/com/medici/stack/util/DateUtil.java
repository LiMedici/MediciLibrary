package com.medici.stack.util;

import com.medici.stack.util.blankj.TimeUtil;
import com.medici.stack.util.blankj.constant.TimeConstant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 
 * @desc 日期工具类
 */
public class DateUtil {

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 将时间戳转位日期类型
     * @param timeStamp 时间戳
     * @return 日期时间
     */
    public static Date toDate(long timeStamp) {
        return new Date(timeStamp);
    }


    /**
     * 获取日期是否是昨天
     * @param date 待判断的日期
     * @return true 昨天
     */
    public static boolean IsYesterday(Date date){
        long millis = date.getTime();
        Calendar cal = Calendar.getInstance();
        // 获取今天是该月的多少天
        int day = cal.get(Calendar.DAY_OF_MONTH);

        // 获取昨天凌晨的时间戳
        cal.set(Calendar.DAY_OF_MONTH,day - 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long wee =  cal.getTimeInMillis();
        // 比较返回结果
        return millis >= wee && millis < wee + TimeConstant.DAY * 2;
    }

    /**
     * 获取显示文本
     * @param oldDate 日期时间
     *
     * @return "18:00","昨天 15:00","星期三 12:58","01-04 10:27","2017/12/27 17:58"
     */
    public static String getShowTime(Date oldDate){
        // 获取此时此刻的时间
        Date nowDate = new Date();

        String timeStr = TimeUtil.date2String(oldDate,new SimpleDateFormat("HH:mm"));

        if(TimeUtil.isToday(oldDate)){
            // 判断是否是今天 "18:00"
            timeStr = timeStr;
        }else if(IsYesterday(oldDate)){
            // 判断是否是昨天 "18:00"
            timeStr = "昨天 "+timeStr;
        }else if(TimeUtil.getTimeSpan(oldDate,nowDate, TimeConstant.MSEC) < 7 * TimeConstant.DAY){
            // 如果传入日期与当前日期在一星期以内,显示当前星期
            timeStr = TimeUtil.getChineseWeek(oldDate) + " "+timeStr;
        }else if(oldDate.getYear() == nowDate.getYear()){
            // 如果传入日期与当前时间是同一年
            timeStr = TimeUtil.date2String(oldDate,new SimpleDateFormat("MM-dd HH:mm"));
        }else{
            // 显示具体日期
            timeStr = TimeUtil.date2String(oldDate,new SimpleDateFormat("yyyy/MM/dd HH:mm"));
        }

        return timeStr;
    }

    /**
     * 获取显示文本
     * @param timeStamp 时间戳
     *
     * @return "18:00","昨天 15:00","星期三 12:58","01-04 10:27","2017/12/27 17:58"
     */
    public static String getShowTime(long timeStamp){
        Date oldDate = new Date(timeStamp);
        return getShowTime(oldDate);
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String StringNowTime() {
        return dateFormatter.get().format(new Date());
    }

    /**
     * 返回时间戳
     * @return
     */
    public static long currentTimeMillis(){
        return System.currentTimeMillis();
    }

    /**
     * 返回某年某月有多少天
     * @param year 某年
     * @param month 某月
     * @return
     */
    public static int returnDayOfYearMonth(int year,int month){
        Calendar calendar = Calendar.getInstance();
        //先指定年份
        calendar.set(Calendar.YEAR, year);
        //再指定月份 Java月份从0开始算
        calendar.set(Calendar.MONTH, month - 1);
        //获取指定年份中指定月份有几天
        int daysCountOfMonth = calendar.getActualMaximum(Calendar.DATE);
        return daysCountOfMonth;
    }
}
