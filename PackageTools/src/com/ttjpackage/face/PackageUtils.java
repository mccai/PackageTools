package com.ttjpackage.face;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttjpackage.core.DateTimeUtil;
import com.ttjpackage.core.PckConfigInfo;
import com.ttjpackage.core.ProjEnums;
/***
 * 后台工程打包入口类(只支持单个工程打增量包)
 * @author caimaochang
 *
 */
public class PackageUtils {
	
	/*public static Map<String,String> projLinkMap = new HashMap<String, String>();
	
	static{
		PckConfigInfo.doInit();
		projLinkMap.put(ProjEnums.TTJ_PC.getValue(), PckConfigInfo.ttjWebBasePath);
		projLinkMap.put(ProjEnums.TTJ_WX.getValue(), PckConfigInfo.ttjWxBasePath);
		projLinkMap.put(ProjEnums.TTJ_APP.getValue(), PckConfigInfo.ttjAppBasePath);
		projLinkMap.put(ProjEnums.TTJ_MANAGE.getValue(), PckConfigInfo.ttjManageBasePath);
		projLinkMap.put(ProjEnums.TTJ_REPORT.getValue(), PckConfigInfo.ttjReportBasePath);
		projLinkMap.put(ProjEnums.MALL_WX.getValue(), PckConfigInfo.mallWxBasePath);
		projLinkMap.put(ProjEnums.MALL_PC.getValue(), PckConfigInfo.mallWebBasePath);
		projLinkMap.put(ProjEnums.MALL_APP.getValue(), PckConfigInfo.mallAppBasePath);
		//projLinkMap.put(ProjEnums.TTJ_DUBOO.getValue(), PckConfigInfo.mallDubooBasePath);
		projLinkMap.put(ProjEnums.MALL_MANAGE.getValue(), PckConfigInfo.mallManageBasePath);				
	}
	*//***
	 * @param args
	 *//*
	public static void main(String[] args) {
		try {						
			if (args == null || args.length != 1) {
				System.out.println("参数不完整");
				return;
			}
			String arg1 = args[0];
			if (ProjEnums.toArcBatchStatus(arg1) == null) {
				System.out.println(" 参数不正确");
				return;
			}						
			
			System.out.println("start packing------------------------------------");
			long startTime = System.currentTimeMillis();
			PackageUtils packageUtils = new PackageUtils();
			
			String  path = packageUtils.createPckDir(ProjEnums.toArcBatchStatus(arg1)); // step1
						
			
			packageUtils.doPck(path,projLinkMap.get(arg1));
			long diff = System.currentTimeMillis()-startTime;
			System.out.println("finish packing-----------------------------------");
			System.out.println("packing using "+diff+" millisecond");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}

	// step1 -创建文件
	private String createPckDir(ProjEnums proj) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String datePath = DateTimeUtil.formatDate(new Date(),
				DateTimeUtil.yyyyMMdd);
		String projPath = proj.getName();

		String fullPckPath = PckConfigInfo.packageBasePath + File.separator + datePath
				+ File.separator + projPath;
		File dir = new File(fullPckPath);
		if(!dir.exists()){
			createDirectory(fullPckPath);
		}		
		return fullPckPath;
	}

	private void doPck(String rootPath,String basePath) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(PckConfigInfo.packFilePath)), "UTF-8"));
		String lineTxt = null;
		while ((lineTxt = br.readLine()) != null) {			
			System.out.println(lineTxt);
			String filePath = lineTxt;
			File file = new File(filePath);
			if(file.isDirectory()) continue;
			if(filePath.indexOf(".java") != -1){ //是类文件
				List<String> sourcePathList = transToClassPath(filePath,basePath);
				if(sourcePathList != null && sourcePathList.size() > 0){
					for(String sourcePath:sourcePathList){
						int index1 = sourcePath.indexOf("\\web");
						if(index1 == -1){
							index1 = sourcePath.indexOf("/web");
						}
						String targetPath = rootPath + sourcePath.substring(index1);
						//创建目录
					    File dir = new File(targetPath.substring(0,targetPath.lastIndexOf("/")));
					    if(!dir.exists()){
					    	createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
					    }												
						copyFile(new File(sourcePath),new File(targetPath));
					}
				}				
				continue;
			}
			if(filePath.indexOf(".jsp") != -1){ //页面文件
				String sourcePath = transToJsp(filePath,basePath);
				int index1 = sourcePath.indexOf("/web");
				String targetPath = rootPath + sourcePath.substring(index1);
				
				createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
				copyFile(new File(sourcePath),new File(targetPath));
				continue;
			}
			if(filePath.indexOf(".js") != -1 && filePath.indexOf(".jsp") == -1){ //js文件
				String sourcePath = transToStatic(filePath,basePath); 
				int index1 = sourcePath.indexOf("/web");
				String targetPath = rootPath + sourcePath.substring(index1);
			
				createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
				copyFile(new File(sourcePath),new File(targetPath));
				continue;
			}
			if(filePath.indexOf("static") != -1 || filePath.indexOf(".png") != -1 || filePath.indexOf(".css") != -1){  //图片,css等静态目录的文件
				String sourcePath = transToStatic(filePath,basePath); 
				int index1 = sourcePath.indexOf("/web");
				String targetPath = rootPath + sourcePath.substring(index1);
			
				createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
				copyFile(new File(sourcePath),new File(targetPath));
				continue;
			}			
		}
		br.close();
	}
	
	//转换类文件
	private List<String> transToClassPath(String classFilePath,String basePath){
		List<String> files = new ArrayList<String>();
		
		int comIndex = classFilePath.indexOf("/com");
		String subPath = classFilePath.substring(comIndex);
		String prefix = "/web/WEB-INF/classes";
	    subPath = prefix +subPath;
	    subPath = subPath.replaceAll(".java", ".class");
	    subPath = basePath+subPath;
	    String fileName = subPath.substring(subPath.lastIndexOf("/")+1,subPath.lastIndexOf("."))+"$";
	    String dirPath = subPath.substring(0,subPath.lastIndexOf("/"));
	    File dir = new File(dirPath);
	    if(dir.isDirectory()){
	    	File[] fileList = dir.listFiles();
	    	if(fileList != null && fileList.length > 0){
	    		for(File item : fileList){
	    			if(item.getAbsolutePath().indexOf(fileName) != -1){
	    				files.add(item.getAbsolutePath().replaceAll("\\\\", "/"));
	    			}	    			
	    		}
	    	}
	    }	    
	    files.add(subPath);
		return files;
	}

	//转换jsp页面
	private String transToJsp(String jspFilePath,String basePath){
		int statIndex = jspFilePath.indexOf("/web/");
		jspFilePath = basePath + jspFilePath.substring(statIndex);
		return jspFilePath;
	}
	
	//转换静态文件（js，图片，css）
	private String transToStatic(String filePath,String basePath) {
		int statIndex = filePath.indexOf("/web/static");
		filePath = basePath + filePath.substring(statIndex);
		return filePath;
	}
		
	
	public static void createDirectory(String path) throws Exception {
		try {
			// 获得文件对象
			File f = new File(path);
			if (!f.exists()) {
				// 如果路径不存在,则创建
				f.mkdirs();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static void copyFile(File sourcefile, File targetFile)
			throws IOException {		
		if(sourcefile.isDirectory()) return;
		
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourcefile);
		BufferedInputStream inbuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream out = new FileOutputStream(targetFile);
		BufferedOutputStream outbuff = new BufferedOutputStream(out);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len = 0;
		while ((len = inbuff.read(b)) != -1) {
			outbuff.write(b, 0, len);
		}

		// 刷新此缓冲的输出流
		outbuff.flush();

		// 关闭流
		inbuff.close();
		outbuff.close();
		out.close();
		input.close();

	}

	public static void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {

		// 新建目标目录

		(new File(targetDir)).mkdirs();

		// 获取源文件夹当下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		if (file != null && file.length > 0) {
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					if (file[i].getName().indexOf("svn") != -1) {
						continue;
					}
					// 源文件
					File sourceFile = file[i];
					// 目标文件
					File targetFile = new File(
							new File(targetDir).getAbsolutePath()
									+ File.separator + file[i].getName());

					copyFile(sourceFile, targetFile);

				}
				if (file[i].isDirectory()) {
					if (file[i].getName().indexOf("svn") != -1) {
						continue;
					}
					// 准备复制的源文件夹
					String dir1 = sourceDir + File.separator
							+ file[i].getName();
					// 准备复制的目标文件夹
					String dir2 = targetDir + File.separator
							+ file[i].getName();

					copyDirectiory(dir1, dir2);
				}
			}
		}

	}*/
}
