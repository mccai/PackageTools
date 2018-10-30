package com.ttjpackage.core;

import java.io.FileInputStream;
import java.util.Properties;

public class PckConfigInfo {
	public static String packageBasePath = ""; //打包路径
	public static String packFilePath = ""; //打包文件路径
	public static String ttjWebBasePath = ""; //淘淘金web项目工程目录
	public static String ttjWxBasePath = ""; //淘淘金wx项目工程目录
	public static String ttjAppBasePath = ""; //淘淘金APP项目工程目录
	public static String ttjManageBasePath = ""; //淘淘金后台管理项目工程目录
	public static String ttjReportBasePath = ""; //淘淘金报表项目工程目录
	public static String ttjServiceBasePath = ""; //淘淘金service工程目录
	public static String ttjReportServiceBasePath = ""; //报表service工程目录
	
	public static String mallWxBasePath = ""; //商城微信项目工程目录
	public static String mallWebBasePath = ""; //商城web项目工程目录
	public static String mallAppBasePath = ""; //商城app项目工程目录
	public static String ttjDubooBasePath = "";  //ttj-duboo项目工程目录(实际是商城使用)
	public static String mallManageBasePath = ""; //商城后台管理项目工程目录
	public static String mallScheduleBasePath = ""; //商城定时任务工程目录
	public static String mallServiceBasePath = ""; //商城service工程目录
	
	public static String hrtWxBasePath = "";//融易借WX目录
	public static String hrtManageBasePath = ""; //融易借manage目录
	public static String hrtServiceBasePath = ""; // 融易借service工程目录
	
	public static String ttjServiceKeyWord = "srtech-service";
	public static String mallServiceKeyWord = "mall-service";
	public static String reportServiceKeyWord = "report-service"; 
	public static String hrtServiceKeyWord = "hrt-service";
	
	public static void doInit(){
		Properties pro = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream("pckConfig.properties");
			pro.load(in);
			packageBasePath = pro.getProperty("packageBasePath");
			packFilePath = pro.getProperty("packFilePath");
			ttjWebBasePath = pro.getProperty("ttjWebBasePath");
			ttjWxBasePath = pro.getProperty("ttjWxBasePath");
			ttjAppBasePath = pro.getProperty("ttjAppBasePath");
			ttjManageBasePath = pro.getProperty("ttjManageBasePath");
			ttjReportBasePath = pro.getProperty("ttjReportBasePath");
			mallWxBasePath = pro.getProperty("mallWxBasePath");
			mallWebBasePath = pro.getProperty("mallWebBasePath");
			mallAppBasePath = pro.getProperty("mallAppBasePath");
			ttjDubooBasePath = pro.getProperty("ttjDubooBasePath");
			mallManageBasePath = pro.getProperty("mallManageBasePath");
			mallScheduleBasePath = pro.getProperty("mallScheduleBasePath");
			ttjServiceBasePath = pro.getProperty("ttjServiceBasePath");
			ttjReportServiceBasePath = pro.getProperty("ttjReportServiceBasePath");
			mallServiceBasePath = pro.getProperty("mallServiceBasePath");
			hrtWxBasePath =pro.getProperty("hrtWxBasePath");
			hrtManageBasePath =pro.getProperty("hrtManageBasePath");
			hrtServiceBasePath =pro.getProperty("hrtServiceBasePath");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		
	}
}
