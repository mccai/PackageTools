package com.ttjpackage.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateTimeUtil {
	private static SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	public static final String yyyyMMddHHmmss="yyyy-MM-dd HH:mm:ss";
	public static final String yyyyMMdd="yyyyMMdd";
	  private int weeks = 0;
	/**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public static int daysBetween(Date smdate,Date bdate) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
        return Integer.parseInt(String.valueOf(between_days));           
    }    
	
	public static Date dayNow(int days){
		   Calendar   cal   =   Calendar.getInstance();
		   cal.add(Calendar.DATE,   days);
		   System.out.println(format.format(cal.getTime()));
		   return cal.getTime();
		 
	}
	public static Date getEveryDay6ClockAM(){
		SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd 06:00:00");
		String temp=format1.format(new Date());
		return parse(temp, yyyyMMddHHmmss);
	}

	public static Date parse(String date,String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		try {
			return sdf.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String formatDate(Date da,String format){
		if(null == da){
			return "";
		}
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		return sdf.format(da);
	}

	public static String formatDate(Date da){

		return format.format(da);
	}
	/** 获取当月的最后一天
	 * @return
	 */
	public static String getLastMonth(){
		Calendar cale = Calendar.getInstance(); 
		cale.add(Calendar.MONTH, 1);
		cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天 
		String lastDay = format.format(cale.getTime());
		return lastDay;
	}
	/** 获取当月的第一天
	 * @return
	 */
	public static String getFirstMonth(){
		Calendar   cal_1=Calendar.getInstance();//获取当前日期 

		cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		String firstDay = format.format(cal_1.getTime());
		System.out.println("-----1------firstDay:"+firstDay);
		return firstDay;
	}

	public static List<String> getBetweenTwoDateList(String startDateStr,String endDateStr,int type) throws Exception{  
		List<String> list = new ArrayList<String>();    
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");   
		Date startDate = simpleDateFormat.parse(startDateStr); // 开始日期  
		Date endDate   = simpleDateFormat.parse(endDateStr); //结束日期  
		Calendar startCalendar = Calendar.getInstance();  
		Calendar endCalendar = Calendar.getInstance();  
		startCalendar.setTime(startDate);  
		endCalendar.setTime(endDate);  
		if(type==1)
			endCalendar.add(Calendar.MONTH, 1);
		else if(type==2)
			endCalendar.add(Calendar.DAY_OF_MONTH, 1);
		else
			endCalendar.add(Calendar.HOUR, 1); 
		String result = null;  
		while (startCalendar.compareTo(endCalendar) < 0) {  
			startDate = startCalendar.getTime();  
			switch(type){  
			case 1:  
				result =  new SimpleDateFormat("yyyy-MM-dd").format(startDate);  

				list.add(result);  
				// 开始日期加一个月直到等于结束日期为止  
				startCalendar.add(Calendar.MONTH, 1);  
				break;  
			case 2:  
				result =  new SimpleDateFormat("yyyy-MM-dd").format(startDate);  
				list.add(result);  


				// 开始日期加一个天直到等于结束日期为止  
				startCalendar.add(Calendar.DAY_OF_MONTH, 1);  
				break;  
			default:  
				result =  new SimpleDateFormat("yyyy-MM-dd HH").format(startDate);  
				result = result.substring(0, result.length());  

				if(list.size()>0){
					String stt=list.get(list.size()-1).split(",")[2];
					String str=stt.substring(0,13)+"时,"+stt+","+result+":00:00";
					list.add(str);  
				}
				else
					list.add(result+"时,"+result+":00:00"+","+result+":00:00");
				// 开始日期加一个月直到等于结束日期为止  
				startCalendar.add(Calendar.HOUR, 1);  
				break;  
			}  
		}  
		return list;  
	} 
	
	 //----------------------------
	 //方法名称：isSameDate(String date1,String date2)
	 //功能描述：判断date1和date2是否在同一天或者同-周，或者同一个月
	 //输入参数：date1,date2
	 //输出参数：
	 //返 回 值：false 或 true
	 //其它说明：主要用到Calendar类中的一些方法
	 //-----------------------------
	 public static boolean isCommonWeek(Date d1,Date d2)
	 {
		 
		 
	  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	  Calendar cal1 = Calendar.getInstance();
	  Calendar cal2 = Calendar.getInstance();
	  cal1.setTime(d1);
	  cal2.setTime(d2);
	  int subYear = cal1.get(Calendar.YEAR)-cal2.get(Calendar.YEAR);
	  //subYear==0,说明是同一年
	  if(subYear == 0)
	  {
	   if(cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
	    return true;
	  }
	  
	  
	  //例子:cal1是"2005-1-1"，cal2是"2004-12-25"
	  //java对"2004-12-25"处理成第52周
	  // "2004-12-26"它处理成了第1周，和"2005-1-1"相同了
	  //大家可以查一下自己的日历
	  //处理的比较好
	  //说明:java的一月用"0"标识，那么12月用"11"
	  else if(subYear==1 && cal2.get(Calendar.MONTH)==11)
	  {
	   if(cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
	    return true;
	  }
	  //例子:cal1是"2004-12-31"，cal2是"2005-1-1"
	  else if(subYear==-1 && cal1.get(Calendar.MONTH)==11)
	  {
	   if(cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
	    return true;
	    
	  }
	  return false;
	 }

	 
	 /** 判读是否相同日期
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isCommonDate(Date date1,Date date2){
		 SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
		  String str1=formate.format(date1);
		  String str2=formate.format(date2);
		  return str1.equals(str2);
	 }
	 /** 判读是否相同日期
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isCommonMonth(Date date1,Date date2){
		 SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM");
		  String str1=formate.format(date1);
		  String str2=formate.format(date2);
		  return str1.equals(str2);
	 }
	 /** 判读是否相同日期
		 * @param date1
		 * @param date2
		 * @return
		 */
		public static boolean isCommonYear(Date date1,Date date2){
			 SimpleDateFormat formate=new SimpleDateFormat("yyyy");
			  String str1=formate.format(date1);
			  String str2=formate.format(date2);
			  return str1.equals(str2);
		 }
		
		public static boolean isCommonTime(Date date1,Date date2,Integer type){
			if(type==1)
				return isCommonDate(date1, date2);
			else if(type==2)
				return isCommonWeek(date1, date2);
			else if(type==3)
				return isCommonMonth(date1, date2);
			else if(type==4)
				return isCommonYear(date1, date2);
			else
				return false;
		}
		
		
		/** 获取当天时间最早和最晚的时间
		 * @param date
		 * @return
		 */
		public  static  String[] getStartAndEndDate(Date date){
			  String[] strs=new String[2];
			  String startTime=null;
			  String endTime=null;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			       String now = formatter.format(date);  
			       String hour = now.split(" ")[1].toString().split(":")[0];  
			          Calendar todayEnd = Calendar.getInstance(); 
			          todayEnd.setTime(date);
			          todayEnd.set(Calendar.HOUR_OF_DAY, 23);  
			          todayEnd.set(Calendar.MINUTE, 59);  
			           todayEnd.set(Calendar.SECOND, 59);  
			           endTime = formatter.format(todayEnd.getTime());
			           strs[1]=endTime;
			           Calendar todayStart = Calendar.getInstance(); 
			           todayStart.setTime(date);
			           
			           todayStart.set(Calendar.HOUR_OF_DAY, 0);  
			           todayStart.set(Calendar.MINUTE, 0);  
			           todayStart.set(Calendar.SECOND, 0);  
				       startTime = formatter.format(todayStart.getTime()); 
			           strs[0]=startTime;
		               return strs;
		}
		/** 获取当天时间最早和最晚的时间
		 * @param date
		 * @return
		 */
		public  static  String[] getStartAndEndWeek(Date mdate){
			  String[] strs=new String[2];
			   SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
			     int b=mdate.getDay();
			     Date fdate ;
			     List <Date> list = new ArrayList();
			     Long fTime=mdate.getTime()-b*24*3600000;
			     for(int a=0;a<7;a++)
			     {  
			      fdate= new Date();
			      fdate.setTime(fTime+(a*24*3600000));
			      if(a==0)
			      strs[0]=formate.format(fdate)+" 00:00:00";
			      else if(a==6)
			       strs[1]=formate.format(fdate)+" 23:59:59";
			      list.add(a, fdate);
			     }
			     System.out.println(strs[0]);
			     System.out.println(strs[1]);
			    return strs;
		      
		}
		/** 获取月时间最早和最晚的时间
		 * @param date
		 * @return
		 */
		public  static  String[] getStartAndEndMonth(Date mdate){
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			     String[] strs=new String[2];
			     Calendar c = Calendar.getInstance();    
			     c.setTime(mdate);
		         c.add(Calendar.MONTH, 0);
		         c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		         String first = format.format(c.getTime())+" 00:00:00";
		         System.out.println("===============first:"+first);
		         strs[0]=first;
		         //获取当前月最后一天
		        Calendar ca = Calendar.getInstance();  
		        ca.setTime(mdate);
		         ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
		         String last = format.format(ca.getTime())+" 23:59:59";
		         System.out.println(last);
		         strs[1]=last;
			    return strs; 
		}
		/** 获取月时间最早和最晚的时间
		 * @param date
		 * @return
		 */
		public  static  String[] getStartAndEndYear(Date mdate){
			  String[] strs=new String[2];
			  SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			  Integer year=Integer.valueOf(format.format(mdate).substring(0,4));
			  Calendar calendar = Calendar.getInstance();  
			   calendar.setTime(mdate);
			   calendar.clear();  
		   calendar.set(Calendar.YEAR, year);  
		   Date currYearFirst = calendar.getTime();  
           strs[0]=format.format(currYearFirst)+" 00:00:00";
		   Calendar calendarEnd = Calendar.getInstance();
		   
		   
		   calendarEnd.clear();  
		   calendarEnd.set(Calendar.YEAR, year);  
		   calendarEnd.roll(Calendar.DAY_OF_YEAR, -1);  
		   Date currYearLast = calendarEnd.getTime(); 
		   strs[1]=format.format(currYearLast)+" 23:59:59";
		   System.out.println(strs[0]);
		   System.out.println(strs[1]);
		   return strs; 
		}
		
		public static String[] getStartAndEndTime(Date date,Integer type){
			if(type==1)
				return getStartAndEndDate(date);
			else if(type==2)
				return getStartAndEndWeek(date);
			else if(type==3)
				return getStartAndEndMonth(date);
			else if(type==4)
				return getStartAndEndYear(date);
			return null;
			
		}
		
		/** 判读当前时间，是否在 16:20 和 8:10 之间
		 * 
		 */
		public static boolean isBetweenTime(){
			Integer num1=162000;
			Integer num2=81000;
			SimpleDateFormat formate=new SimpleDateFormat("HHmmss");
			Integer nowTime=Integer.valueOf(formate.format(new Date()));
			if(nowTime>num2&&nowTime<num1)
				return true;
			else
				return false;
		}
		
		/**
		 * 获取最晚时间 yyyy/MM/dd 23:59:59
		 * @param date
		 * @return
		 */
		public static Date getDateLast(Date date){
			if(date==null)
				return null;
			Calendar cDay1 = Calendar.getInstance();  
			cDay1.setTime(date);  
			cDay1.set( cDay1.get(Calendar.YEAR), cDay1.get(Calendar.MONTH), cDay1.get(Calendar.DATE), 23, 59, 59);
			Date lastDate = cDay1.getTime();  
			return lastDate;
		}
		
		/**
		 * 获取最早时间 yyyy/MM/dd 00:00:00
		 * @param date
		 * @return
		 */
		public static Date getDateEarly(Date date){
			if(date==null)
				return null;
			Calendar cDay1 = Calendar.getInstance();  
			cDay1.setTime(date);  
			cDay1.set( cDay1.get(Calendar.YEAR), cDay1.get(Calendar.MONTH), cDay1.get(Calendar.DATE), 00, 00, 00);
			Date lastDate = cDay1.getTime();  
			return lastDate;
		}
		
		/**
		 * 获取当月最晚时间 yyyy/MM/dd 23:59:59
		 * @param OldDate
		 * @return
		 */
		public static Date getLastDayOfMontch(Date OldDate){
			Calendar cDay1 = Calendar.getInstance();  
			cDay1.setTime(OldDate);  
			int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);  
			cDay1.set( cDay1.get(Calendar.YEAR), cDay1.get(Calendar.MONTH), lastDay, 23, 59, 59);
			Date lastDate = cDay1.getTime();  
			return lastDate;
		}
	
		
		
		
		   public String getPreviousMonthEnd()
		   {
		     String str = "";
		     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		     Calendar lastDate = Calendar.getInstance();
		     lastDate.add(2, -1);
		     lastDate.set(5, 1);
		     lastDate.roll(5, -1);
		     str = sdf.format(lastDate.getTime());
		     return str;
		   }
		   public static boolean isSunDay() {
			   Date bdate =new Date(); 
		       Calendar cal = Calendar.getInstance();
		       cal.setTime(bdate);
			    if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
			    return true;
			    else 
			   return false;
		   }
	
	public static int getTimeDelta(Date date1,Date date2){  
        long timeDelta = (date1.getTime()-date2.getTime()) /1000;//单位是秒  
        int secondsDelta = timeDelta>0?(int)timeDelta:(int)Math.abs(timeDelta);  
        return secondsDelta;  
    } 
	
	public static Date addMonth(int add){
		Calendar cal = Calendar.getInstance(); 
        cal.setTime(new Date()); 
        cal.add(cal.MONTH, add);
		return cal.getTime();
	}
	
	
}
