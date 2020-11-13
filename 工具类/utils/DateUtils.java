package com.springboot.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: DateUtils
 * @Description: 时间工具类
 * @author AnYanSen
 * @date 2018年8月31日 下午3:02:35
 *
 */
public class DateUtils {
	private static Logger logger = LoggerFactory.getLogger(DateUtils.class);

	public static final String DATE_TIME_PATTERN = "yyyyMMddHHmmss";
	public static final String MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
	//yyyy-MM-dd HH:mm:ss
	public static final String HOUR_PATTERN = "yyyy-MM-dd HH:mm:ss";
	//yyyy-MM-dd
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String MONTH_PATTERN = "yyyy-MM";
	public static final String YEAR_PATTERN = "yyyy";
	public static final String TODAY_PATTERN = "dd";
	public static final String MINUTE_ONLY_PATTERN = "mm";
	public static final String HOUR_ONLY_PATTERN = "HH";
	//HH:mm:ss
	public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
	//HH:mm
	public static final String DEFAULT_TIME_HOUR = "HH:mm";

	/**
	 * @Description:两个时间相减,获得相差天数，小时，分钟
	 * @author AnYanSen
	 * @param endTime   结束时间
	 * @param startTime 开始时间
	 * @return Map<String,Object>
	 * @date 2018年8月31日 下午2:55:37
	 */
	public static Map<String, Integer> getSubTwoTime(String endTime, String startTime) {
		Map<String, Integer> TimeMap = new HashMap<>();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = df.parse(startTime);
			Date d2 = df.parse(endTime);
			long diff = d2.getTime() - d1.getTime();// 这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);

			long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
			long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
			if (hours < 0) {
				hours = new BigDecimal(hours).abs().intValue();
			}
			if (minutes < 0) {
				minutes = new BigDecimal(minutes).abs().intValue();
			}
			TimeMap.put("days", Integer.parseInt(days + ""));
			TimeMap.put("hours", Integer.parseInt(hours + ""));
			TimeMap.put("minutes", Integer.parseInt(minutes + ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TimeMap;
	}

	/**
	 * 日期相加减天数
	 * 
	 * @param date        如果为Null，则为当前时间
	 * @param days        加减天数
	 * @param includeTime 是否包括时分秒,true表示包含
	 * @return
	 * @throws ParseException
	 */
	public static Date dateAdd(Date date, int days, boolean includeTime){

		try {
			if (date == null) {
				date = new Date();
			}
			if (!includeTime) {
				SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN);
				date = sdf.parse(sdf.format(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	/**
	 * 日期转时间字符串
	 * 
	 * @param date    Date
	 * @param pattern StrUtils.DATE_TIME_PATTERN || StrUtils.DATE_PATTERN，
	 *                如果为空，则为yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static String dateFormat(Date date, String pattern) {
		SimpleDateFormat sdf = null;
		try {
			if (pattern == null && pattern == "") {
				pattern = DateUtils.DATE_PATTERN;
			}
			sdf = new SimpleDateFormat(pattern);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sdf.format(date);
	}

	/**
	 * 字符串解析成时间对象
	 * 
	 * @param dateTimeString String
	 * @param pattern        StrUtils.DATE_TIME_PATTERN ||
	 *                       StrUtils.DATE_PATTERN，如果为空，则为yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static Date dateParse(String dateTimeString, String pattern) {
		SimpleDateFormat sdf = null;
		try {
			if (pattern == null && pattern == "") {
				pattern = DateUtils.DATE_PATTERN;
			}
			sdf = new SimpleDateFormat(pattern);
			return sdf.parse(dateTimeString);
		} catch (Exception e) {
			logger.debug("方法:DateUtils.dateParse()异常");
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * @Description:时间戳转字符串日期
	 * @author AnYanSen
	 * @param datems 时间戳
	 * @date 2018年9月6日 下午4:33:01
	 */
	public static String transForDate(Long datems, String pattern) {
		String time;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date date = new Date(datems * 1000);
		time = simpleDateFormat.format(date);
		return time;
	}
	
	
	/**
	* 取得当前时间戳
	* 
	* @return 当前时间戳
	*/
	public static String getCurrentTime() {
	return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	}
	/**
	 * 
	 * @Description: 生成流水线单号
	 * @author 郭兵
	 * @date 2019年4月17日 下午1:48:21
	 * @Param: @return
	 * @return: String
	 */
	public static String getSerialTime() {
		return new SimpleDateFormat("yyyyMMddHH").format(new Date());
		}
	/**
	 * @Description:时间戳转时间
	 * @author AnYanSen
	 * @param  date 时间戳
	 * @param  format 日期格式
	 * @return Date
	 * @date 2018年11月6日 下午6:39:59 
	 */
	public static Date timestampToDateAndDate(Long times,String format) {
		String res;
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
	        long lt = new Long(times);
	        Date date = new Date(lt);
		return date;
	}
	
	/**
	 * @Description:时间戳转时间
	 * @author AnYanSen
	 * @param  date 时间戳
	 * @param  format 日期格式
	 * @return String
	 * @date 2018年11月6日 下午6:39:59 
	 */
	public static String timestampToDateAndString(Long times,String format) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		long lt = new Long(times);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}


	/**
	* 取得当前日期
	* 
	* @return 当前日期
	*/
	public static String thisDate() {
	Calendar calendar = new GregorianCalendar();
	calendar.setTime(new Date());
	return new SimpleDateFormat(DATE_PATTERN).format(calendar.getTime());
	}


	/**
	* 取得当前时间
	* 
	* @return 当前时间
	*/
	public static String thisTime() {
	Calendar calendar = new GregorianCalendar();
	calendar.setTime(new Date());
	return new SimpleDateFormat(DEFAULT_TIME_FORMAT).format(calendar.getTime());
	}


	/**
	* 取得当前完整日期时间
	* 
	* @return 当前完整日期时间
	*/
	public static String getThisDateTime() {
	Calendar calendar = new GregorianCalendar();
	calendar.setTime(new Date());
	return new SimpleDateFormat(DATE_TIME_PATTERN).format(calendar.getTime());
	}
	
	/**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     * 
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 获取当前日期是星期几<br>
     * @param dt
     * @return  7   1    2
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"7","1", "2", "3", "4", "5", "6"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
    /**
     * @param
     * @author bing
     * @Description // 获取当前日期是星期几<br>
     * @date 14:20 2019/9/27
     * @return 星期日 星期一
     **/
    public static String getWeeksbyDate(Date dt) {
        String[] weekDays = {"星期日","星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
}
