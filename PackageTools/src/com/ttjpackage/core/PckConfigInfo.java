package com.ttjpackage.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class PckConfigInfo {
	
	public static String packageBasePath = ""; //打包路径
	public static String packFilePath = ""; //打包文件路径
	
	public static Map<String, String> wsBasePath = new HashMap<String, String>(); // 项目工作空间
	public static Map<String,List<String>> projRelatedMp = new HashMap<String,List<String>>();  //关联项目定义
	public static Map<String,String> projWsMp = new HashMap<String,String>();//项目名-工作空间对照表
	
	public static void doInit(){
		Properties pro = new Properties();
		FileInputStream in= null;
		
		FileInputStream in2= null;
		Properties pro2 = new Properties();
		
		FileInputStream in3= null;
		Properties pro3 = new Properties();
		
		try {
			in = new FileInputStream("pckConfig.properties");
			pro.load(in);
			packageBasePath = pro.getProperty("packageBasePath");
			packFilePath = pro.getProperty("packFilePath");
			
			if(pro.entrySet() != null && pro.entrySet().size() > 0){
				for(Entry<Object,Object> item : pro.entrySet()){
					wsBasePath.put(item.getKey().toString(), item.getValue().toString());
				}
			}
			
			in2 = new FileInputStream("projRelatedConfig.properties");
			pro2.load(in2);
			if(pro2.entrySet() != null && pro2.entrySet().size() > 0){
				for(Entry<Object,Object> item : pro2.entrySet()){
					String key = item.getKey().toString();
					String val = item.getValue().toString();
					
					String[]projs = val.split("#");
					projRelatedMp.put(key, Arrays.asList(projs));
				}
			}
			
			in3 = new FileInputStream("projWsConfig.properties");
			pro3.load(in3);
			if(pro3.entrySet() != null && pro3.entrySet().size() > 0){
				for(Entry<Object,Object> item : pro3.entrySet()){
					projWsMp.put(item.getKey().toString(), item.getValue().toString());
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}finally{
			try {
				in.close();
				in2.close();
				in3.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
	}
}
