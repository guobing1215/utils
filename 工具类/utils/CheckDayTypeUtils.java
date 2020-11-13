package com.springboot.modules.marketcenter.service;

import com.springboot.core.utils.DateUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: bing
 * @Date: 2019/11/11 15:16
 * @Description:
 */
public class CheckDayTypeUtils {

    private static String START_TIME = "00:00";
    private static String END_TIME = "23:59";

    public static Boolean checkWeeks(String cycleWeek, Date thisTime, Date quantumStart, Date quantumEnd) {
        Boolean flag = false;
        if (cycleWeek.length() == 1) {
            if (cycleWeek.indexOf(DateUtils.getWeekOfDate(new Date())) != -1) {
                if (DateUtils.isEffectiveDate(thisTime, quantumStart, quantumEnd)) {
                    flag = true;
                }
            }
        }
        if (cycleWeek.length() > 1) {
            if (cycleWeek.indexOf(DateUtils.getWeekOfDate(new Date())) != -1) {
                String[] split = cycleWeek.split(",");
                if (split[0].equals(DateUtils.getWeekOfDate(new Date()))) {
                    //第一个
                    quantumEnd = DateUtils.dateParse(END_TIME, DateUtils.DEFAULT_TIME_HOUR);
                } else if (split[split.length - 1].equals(DateUtils.getWeekOfDate(new Date()))) {
                    //最后一个
                    quantumStart = DateUtils.dateParse(START_TIME, DateUtils.DEFAULT_TIME_HOUR);
                } else {
                    quantumStart = DateUtils.dateParse(START_TIME, DateUtils.DEFAULT_TIME_HOUR);
                    quantumEnd = DateUtils.dateParse(END_TIME, DateUtils.DEFAULT_TIME_HOUR);
                }
                if (DateUtils.isEffectiveDate(thisTime, quantumStart, quantumEnd)) {
                    flag = true;
                }
            }
        }
        return flag;
    }
    /**
     * @Description // 校验周几时间
     * @author bing
     * @date 13:33 2019/11/28
     * @return  Boolean
     **/
    public static Boolean checkDays(String checkDays,int day,Date thisTime, Date quantumStart, Date quantumEnd) {
        Boolean flag = false;
        List<Integer> list= Arrays.asList(checkDays .split(",")).stream().map(s -> Integer.parseInt((s.trim()))).sorted().collect(Collectors.toList());
        if (!list.contains(day)) {
            return flag;
        }
        if (checkDays.length() == 1) {
            if (DateUtils.isEffectiveDate(thisTime, quantumStart, quantumEnd)) {
                flag = true;
            }
        }
        if (checkDays.length() > 1) {
            Integer weekOfDateBefore ;
            Integer weekOfDateAfter ;
            //判断上一个
            if (day == 1) {
                weekOfDateBefore = 7;
            } else {
                weekOfDateBefore = day - 1;
            }
            if (list.contains(weekOfDateBefore)) {
                //判断后一个有没有值
                if (day == 7) {
                    weekOfDateAfter = 1;
                } else {
                    weekOfDateAfter = day + 1;
                }
                if (list.contains(weekOfDateAfter)) {
                    quantumStart = DateUtils.dateParse(START_TIME, DateUtils.DEFAULT_TIME_HOUR);
                    quantumEnd = DateUtils.dateParse(END_TIME, DateUtils.DEFAULT_TIME_HOUR);
                } else {
                    quantumStart = DateUtils.dateParse(START_TIME, DateUtils.DEFAULT_TIME_HOUR);
                }
                //上一个没有值,说明时间从今天开始
            } else {
                quantumEnd = DateUtils.dateParse(END_TIME, DateUtils.DEFAULT_TIME_HOUR);
            }
            if (DateUtils.isEffectiveDate(thisTime, quantumStart, quantumEnd)) {
                flag = true;
            }
        }
        return flag;
    }



    }
