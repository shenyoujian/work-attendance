package com.ljs.common.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author ljs
 * @Description TODO
 * @Date 2018/8/18 21:48
 **/
public class DateUtil {


    private static Calendar calendar = Calendar.getInstance();

    /**
     * Author ljs
     * Description 获取今天是星期几
     * Date 2018/8/18 21:49
     **/
    public static int getTodayWeek() {
        //把今天的日期设进去
        calendar.setTime(new Date());
        //获取今天是星期几的信息，减1是因为星期天是1
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        //当=0即星期天的时候改为7
        if (week <= 0) week = 7;
        return week;
    }


    /**
     * Author ljs
     * Description 获取时间差单位为分钟
     * Date 2018/8/18 22:23
     **/
    public static int getMinutes(Date startDate, Date endDate) {
        long start = startDate.getTime();
        long end = endDate.getTime();
        //毫秒换算为分钟
        int minutes = (int) ((start - end)/(1000*60));
        return minutes;
    }


    /**
     * Author ljs
     * Description 根据传入的时间和分钟 获取当天的某个时间
     * Date 2018/8/18 22:36
     **/
    public static Date getDate(int hour, int minute){
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    public static void main(String[] args) {

        System.out.println(1^2);
    }
}
