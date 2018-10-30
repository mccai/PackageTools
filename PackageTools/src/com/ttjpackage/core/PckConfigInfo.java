package com.ttjpackage.core;

import java.io.FileInputStream;
import java.util.Properties;

public class PckConfigInfo {
	public static String packageBasePath = ""; //���·��
	public static String packFilePath = ""; //����ļ�·��
	public static String ttjWebBasePath = ""; //���Խ�web��Ŀ����Ŀ¼
	public static String ttjWxBasePath = ""; //���Խ�wx��Ŀ����Ŀ¼
	public static String ttjAppBasePath = ""; //���Խ�APP��Ŀ����Ŀ¼
	public static String ttjManageBasePath = ""; //���Խ��̨������Ŀ����Ŀ¼
	public static String ttjReportBasePath = ""; //���Խ𱨱���Ŀ����Ŀ¼
	public static String ttjServiceBasePath = ""; //���Խ�service����Ŀ¼
	public static String ttjReportServiceBasePath = ""; //����service����Ŀ¼
	
	public static String mallWxBasePath = ""; //�̳�΢����Ŀ����Ŀ¼
	public static String mallWebBasePath = ""; //�̳�web��Ŀ����Ŀ¼
	public static String mallAppBasePath = ""; //�̳�app��Ŀ����Ŀ¼
	public static String ttjDubooBasePath = "";  //ttj-duboo��Ŀ����Ŀ¼(ʵ�����̳�ʹ��)
	public static String mallManageBasePath = ""; //�̳Ǻ�̨������Ŀ����Ŀ¼
	public static String mallScheduleBasePath = ""; //�̳Ƕ�ʱ���񹤳�Ŀ¼
	public static String mallServiceBasePath = ""; //�̳�service����Ŀ¼
	
	public static String hrtWxBasePath = "";//���׽�WXĿ¼
	public static String hrtManageBasePath = ""; //���׽�manageĿ¼
	public static String hrtServiceBasePath = ""; // ���׽�service����Ŀ¼
	
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
