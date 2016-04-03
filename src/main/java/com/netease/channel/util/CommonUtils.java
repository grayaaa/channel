package com.netease.channel.util;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;


/**
 * 工具方法类
 */
public class CommonUtils {

    private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);
    
	public static final String EMAIL_PATTERN = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$"; 
    public static final long DAY_MILLIS = 86400000l;
	private static final String CDN_PULL_HOST = "http://wspull.bn.netease.com";
	private static final String MOBILE_CDN_PULL_HOST = "http://hls.wspull.bn.netease.com";
	
  /**
   * 获取指定时间所在天的起止时间（精确到秒）
   * Date[0]:开始时间
   * Date[1]:结束时间（包含）
   * @param date
   * @return
   */
  public static Date[] getFirstAndLastTimeOfDay(Date date){
    Date[] dates = new Date[2];
    
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    dates[0] = c.getTime();
    
    c.add(Calendar.DAY_OF_MONTH, 1);
    c.add(Calendar.SECOND, -1);
    dates[1] = c.getTime();
    
    return dates;
  }
  
	/**
	 * 获取指定时间所在周的起止时间（精确到秒）
	 * Date[0]:开始时间
	 * Date[1]:结束时间（不包含）
	 * @param date
	 * @return
	 */
	public static Date[] getFirstAndLastDayOfWeek(Date date){
	  Date[] dates = new Date[2];
	  
	  Calendar c = Calendar.getInstance();
	  c.setFirstDayOfWeek(Calendar.MONDAY);
	  c.setTime(date);
	  c.set(Calendar.DAY_OF_WEEK,c.getFirstDayOfWeek());
	  c.set(Calendar.HOUR_OF_DAY, 0);
	  c.set(Calendar.MINUTE, 0);
	  c.set(Calendar.SECOND, 0);
	  c.set(Calendar.MILLISECOND, 0);
	  dates[0] = c.getTime();
	  
	  c.setTime(date);
	  c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
	  c.set(Calendar.HOUR_OF_DAY, 0);
	  c.set(Calendar.MINUTE, 0);
	  c.set(Calendar.SECOND, 0);
	  c.set(Calendar.MILLISECOND, 0);
	  c.add(Calendar.DAY_OF_MONTH, 1);
	  dates[1] = c.getTime();
	  
	  return dates;
	}
	
	/**
   * 获取指定时间所在月的起止时间（精确到秒）
   * Date[0]:开始时间
   * Date[1]:结束时间（不包含）
   * @param date
   * @return
   */
  public static Date[] getFirstAndLastDayOfMonth(Date date){
    Date[] dates = new Date[2];
    
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH), 1, 0, 0, 0);
    c.set(Calendar.MILLISECOND, 0);
    dates[0] = c.getTime();
    
    c.roll(Calendar.DATE, -1);
    dates[1] = c.getTime();
    
    return dates;
  }
	
	/**
	 * 获取某年的第一天
	 * @param year
	 * @return
	 */
	public static Date getCurrYearFirstDay(int year){
	    Calendar calendar = Calendar.getInstance();
	    calendar.clear();
	    calendar.set(Calendar.YEAR, year);
	    Date currYearFirst = calendar.getTime(); 
	    return currYearFirst;
	}  
	
	/**
	 * 获取当年年份
	 * @return
	 */
	public static int getCurrYear(){  
        Calendar currCal=Calendar.getInstance();    
        int currentYear = currCal.get(Calendar.YEAR);  
        return currentYear;  
  }  
	
	public static int getChatLevel(int chatLevel,int duration){
		int result = 0;
		switch (chatLevel){
		  case 0:
			switch (duration){
			  case 0:
				  result = 1;
			  break;
			  case 5:
				  result = 2;
			  break;
			  case 10:
				  result = 3;
			  break;
			  default:
				  result = -1;
			}
			break;
		  case 1:
				switch (duration){
				  case -1:
					  result = 4;
				  break;
				  case 5:
					  result = 5;
				  break;
				  case 10:
					  result = 6;
				  break;
				  default:
					  result = 4;
				}
				break;
		  case 3:
				switch (duration){
				  case -1:
					  result = 7;
				  break;
				  case 5:
					  result = 8;
				  break;
				  case 10:
					  result = 9;
				  break;
				  default:
					  result = 7;
				}
				break;
		  case -1:
			  	result = 10;
				break;
		  case -2:
			  	result = 11;
				break;
		  case -3:
			  	result = 12;
				break;
		  case -4:
			  	result = 13;
				break;
		  default:
			  result = -1;
			
		}
		return result;
	}
	
	public static int[] getChatLAndDurByLev(int chatLevel){
		int [] result = new int[2];
		switch (chatLevel){
		  case 1:
			  	result[0] = 0; result[1] = 0; 
				break;
		  case 2:
			  	result[0] = 0; result[1] = 5; 
				break;
		  case 3:
			  	result[0] = 0; result[1] = 10; 
				break;
		  case 4:
			  	result[0] = 1; result[1] = -1;
				break;
		  case 5:
			  	result[0] = 1; result[1] = 5;
				break;
		  case 6:
			  	result[0] = 1; result[1] = 10;
				break;
		  case 7:
			  	result[0] = 3; result[1] = -1;
				break;
		  case 8:
			  	result[0] = 3; result[1] = 5;
				break;
		  case 9:
			  	result[0] = 3; result[1] = 10;
				break;
		  case 10:
			  	result[0] = -1; result[1] = -1;
				break;
		  case 11:
			  	result[0] = -2; result[1] = -1;
				break;
		  case 12:
			  	result[0] = -3; result[1] = -1;
				break;
		  case 13:
			  	result[0] = -4; result[1] = -1;
				break;
		  default:
			  result[0] = 0; result[1] = 0;
			
		}
		return result;
	}
	
	/**
	 * 判断两个日期时间相差的月份
	 * @param startDate 开始日期	
	 * @param endDate  结束日期
	 * @return 月份差
	 */
	public static int getMonth(Date startDate,Date endDate) {
		Calendar starCal = Calendar.getInstance();
        starCal.setTime(startDate);

        int sYear = starCal.get(Calendar.YEAR);
        int sMonth = starCal.get(Calendar.MONTH);
//        int sDay = starCal.get(Calendar.DATE);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int eYear = endCal.get(Calendar.YEAR);
        int eMonth = endCal.get(Calendar.MONTH);
//        int eDay = endCal.get(Calendar.DATE);

        int monthday = ((eYear - sYear) * 12 + (eMonth - sMonth));
//
//        if (sDay < eDay) {
//            monthday = monthday + 1;
//        }
        return monthday;
	}
	
	/**
	 * 获取前n个月的前n天的日期
	 * @param nMonth
	 * @param nDay
	 * @return
	 */
	public static Date getBeforeDateMonthAndDay(int nMonth,int nDay){
		Calendar calendar = Calendar.getInstance(); 
		calendar.add(Calendar.DATE, -nDay);    //得到前一天 
		calendar.add(Calendar.MONTH, -nMonth);    //得到前一个月 
		return calendar.getTime();
	}
	
	/**
	 * 把日期类型转化为yyyy-MM-dd 00:00:00
	 * @param date
	 * @return
	 */
	public static Date formatDate(Date date){
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 0);  
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);  
		return calendar.getTime();
	}
	
	/**
	 * 
	 * @param srcToConvert
	 * @param encoding
	 * @return
	 */
	private static String getMd5Sign(String srcToConvert, String encoding) {
		String cryptograph = null;
		try {
			byte passToConvertByte[] = srcToConvert.getBytes(encoding);
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			byte gottenPassByte[] = messagedigest.digest(passToConvertByte);
			cryptograph = "";
			for (int i = 0; i < gottenPassByte.length; i++) {
				String temp = Integer
						.toHexString(gottenPassByte[i] & 0x000000ff);
				if (temp.length() < 2)
					temp = "0" + temp;
				cryptograph += temp;
			}
		} catch (Exception e) {
			cryptograph = null;
		}
		return cryptograph;
	}
	
	/**
	 * 获取当前周的周一时间
	 * @param date
	 * @return
	 */
	public static Date getNowWeekMonday() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return DateUtils.truncate(cal.getTime(), Calendar.DATE);
	}
	
	public static Date getNowadaysTime() {
		return DateUtils.truncate(new Date(), Calendar.DATE);
	}
}
