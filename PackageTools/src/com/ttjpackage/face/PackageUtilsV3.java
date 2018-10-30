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
import java.util.Map.Entry;
import java.util.Set;

import com.ttjpackage.core.DateTimeUtil;
import com.ttjpackage.core.DicObject;
import com.ttjpackage.core.PckConfigInfo;
import com.ttjpackage.core.ProjEnums;
/***
 * 后台工程打包入口类
 * @author caimaochang
 *
 */
public class PackageUtilsV3 {
	
	public static Map<String,String> projLinkMap = new HashMap<String, String>();
	
	static{
		PckConfigInfo.doInit();
		if(PckConfigInfo.projWsMp != null && PckConfigInfo.projWsMp.size() > 0){
			Set<Entry<String,String>> projSet = PckConfigInfo.projWsMp.entrySet(); 
			for(Entry<String,String> proj : projSet){
				projLinkMap.put(proj.getKey(), PckConfigInfo.wsBasePath.get(proj.getValue()));
			}
		}
	}
	/***
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println("start packing------------------------------------");			
			long startTime = System.currentTimeMillis();
			PackageUtilsV3 packageUtils = new PackageUtilsV3();
			String [] projs =  null;
			if(args != null && args.length == 1){
				String arg1 = args[0];
				projs = arg1.split("#");
				for(String proj:projs){
					if (ProjEnums.toArcBatchStatus(proj) == null) {
						System.out.println("参数不正确:"+proj);
						return;
					}
				}
			}else{
				projs = packageUtils.getPckProjs();
			}
			//删除目前日期打包目录下文件
			packageUtils.deleteCurDataDir();
			for(String proj:projs){
				try {
					System.out.println("start packing "+proj+"-------------------");
					String  path = packageUtils.createPckDir(proj); // step1
					if(PckConfigInfo.projRelatedMp.containsKey(proj)){ //如果是service工程
						String proj_ = PckConfigInfo.projRelatedMp.get(proj).get(0);
						packageUtils.doPck(path,projLinkMap.get(proj_),proj);
					}else{
						packageUtils.doPck(path,projLinkMap.get(proj),proj);
					}
					
					System.out.println("finish packing "+proj+"-------------------");
				} catch (Exception e) {
					continue;
				}
			}
			
			long diff = System.currentTimeMillis()-startTime;
			System.out.println("finish all packing-----------------------------------");
			System.out.println("packing using "+diff+" millisecond");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void deleteAllFilesOfDir(File path) {  
	    if (!path.exists())  
	        return;  
	    if (path.isFile()) {  
	        path.delete();  
	        return;  
	    }  
	    File[] files = path.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        deleteAllFilesOfDir(files[i]);  
	    }  
	    path.delete();  
	}  
	
	private void deleteCurDataDir(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String datePath = DateTimeUtil.formatDate(new Date(),
				DateTimeUtil.yyyyMMdd);
		String basePath = PckConfigInfo.packageBasePath + File.separator + datePath+ File.separator;
		File file = new File(basePath);
		if(file.exists() && file.isDirectory()){
			deleteAllFilesOfDir(file);
		}
	}
	// step1 -创建文件
	private String createPckDir(String projPath) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String datePath = DateTimeUtil.formatDate(new Date(),
				DateTimeUtil.yyyyMMdd);
	
		String fullPckPath = PckConfigInfo.packageBasePath + File.separator + datePath
				+ File.separator + projPath;
		File dir = new File(fullPckPath);
		if(!dir.exists()){
			createDirectory(fullPckPath);
		}		
		return fullPckPath;
	}
	/****
	 * 检查service类是否需要打包  true:需要打包 false：不需要打包
	 * @param keyWord
	 * @param filePath
	 * @return
	 */
	private boolean checkNeedPck(String keyWord,String filePath,String serviceProjName){
		List<String> relateProj = PckConfigInfo.projRelatedMp.get(serviceProjName);
		return relateProj.contains(keyWord);
	}
	
	/***
	 * 返回需要打哪些项目的包
	 * @return
	 */
	private String[] getPckProjs() throws Exception{
		
		List<String> allProjs = new ArrayList<String>();		
		allProjs.addAll(PckConfigInfo.projWsMp.keySet());
				
		List<String> projs = new ArrayList<String>();
		for(String proj:allProjs){
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(PckConfigInfo.packFilePath)), "UTF-8"));
			String lineTxt = null;
			while ((lineTxt = br.readLine()) != null) {
				String filePath = lineTxt;
				if(filePath.indexOf(proj) != -1){
					projs.add(proj);
					br.close();
					break;
				}
			}
			br.close();
		}
		if(projs != null && projs.size() > 0){
			String[] strArr = new String[projs.size()];
			projs.toArray(strArr);
			
			return strArr;
		}else{
			return null;
		}		
	}

	private String checkConfigedService(String filePath){
		Set<String> serviceProjSet = PckConfigInfo.projRelatedMp.keySet();
		for(String item : serviceProjSet){
			if(filePath.indexOf(item) != -1){
				return item;
			}
		}
		return null;
	}
	private void doPck(String rootPath,String basePath,String keyWord) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(PckConfigInfo.packFilePath)), "UTF-8"));
		String lineTxt = null;
		while ((lineTxt = br.readLine()) != null) {						
			String filePath = lineTxt;
			if(filePath.indexOf(keyWord) == -1){ //非当前项目文件，直接跳过
				String serviceProj = checkConfigedService(filePath);
				if(serviceProj != null){
					if(!checkNeedPck(keyWord, filePath,serviceProj)) continue; 
				}else{
					continue;
				}
			}
			if(filePath.indexOf("*") > -1){
				String dirPath = filePath.substring(0, filePath.indexOf("*"));
				System.out.println("dirPath:"+dirPath);
				String[] fullDirPath = makeDirPath(dirPath, basePath);
			    dealDir(fullDirPath[0], basePath, rootPath,fullDirPath[1]);
			}else{
				System.out.println(filePath);
				dealSingleFile(filePath, basePath, rootPath);
			}
			
		}
		br.close();
	}
	
	public void dealDir(String fullDirPath,String basePath,String rootPath,String preFix) throws Exception{
		File dir = new File(fullDirPath);
	    if(dir.isDirectory()){
	    	File[] fileList = dir.listFiles();
	    	if(fileList != null && fileList.length > 0){
	    		for(File item : fileList){
	    			if(item.getAbsolutePath().indexOf(".svn") > -1){
	    				continue;
	    			}
	    			if(item.isDirectory()){
	    				dealDir(item.getAbsolutePath(), basePath, rootPath,preFix);
	    			}else{
	    				String fileP = "";
	    				String fileFullPath = item.getAbsolutePath().replaceAll("\\\\","\\/");
	    				if(fileFullPath.indexOf("/src/") > -1){
	    					fileP = preFix + fileFullPath.substring(fileFullPath.indexOf("/src/"));
	    				}
	    				
	    				if(fileFullPath.indexOf("/web/") > -1){
	    					fileP = preFix + fileFullPath.substring(fileFullPath.indexOf("/web/"));
	    				}
	    				
	    				dealSingleFile(fileP, basePath, rootPath);
	    			}
	    		}
	    	}
	    }else{
	    	dealSingleFile(fullDirPath, basePath, rootPath);
	    }
	}
	
	public String[] makeDirPath(String dirPath,String basePath){
		String[] res = new String[2];
		if(dirPath.indexOf("/src/") > -1){
			res[0] = basePath + dirPath.substring(dirPath.indexOf("/src/"));
			res[1] = dirPath.substring(0, dirPath.indexOf("/src/"));
			return res;
		}
		if(dirPath.indexOf("/web/") > -1){
			res[0] =basePath + dirPath.substring(dirPath.indexOf("/web/"));
			res[1] =dirPath.substring(0, dirPath.indexOf("/web/"));
			return res;
		}
		return null;
	}
	public void dealSingleFile(String filePath,String basePath,String rootPath) throws Exception{
		File file = new File(filePath);
		if(file.isDirectory()) return;
		System.out.println(filePath);
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
			return;
		}
		if(filePath.indexOf(".jsp") != -1){ //页面文件
			String sourcePath = transToJsp(filePath,basePath);
			int index1 = sourcePath.indexOf("/web");
			String targetPath = rootPath + sourcePath.substring(index1);
			
			createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
			copyFile(new File(sourcePath),new File(targetPath));
			return;
		}
		if(filePath.indexOf(".js") != -1 && filePath.indexOf(".jsp") == -1){ //js文件
			String sourcePath = transToStatic(filePath,basePath); 
			int index1 = sourcePath.indexOf("/web");
			String targetPath = rootPath + sourcePath.substring(index1);
		
			createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
			copyFile(new File(sourcePath),new File(targetPath));
			return;
		}
		if(filePath.indexOf("static") != -1 || filePath.indexOf(".png") != -1 || filePath.indexOf(".css") != -1 || filePath.indexOf(".jpg") != -1){  //图片,css等静态目录的文件
			String sourcePath = transToStatic(filePath,basePath); 
			int index1 = sourcePath.indexOf("/web");
			String targetPath = rootPath + sourcePath.substring(index1);
		
			createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
			copyFile(new File(sourcePath),new File(targetPath));
			return;
		}
		if(filePath.indexOf("/src/main/resources") != -1){  //配置文件
			String sourcePath = transToResource(filePath,basePath); 
			int index1 = sourcePath.indexOf("/web");
			String targetPath = rootPath + sourcePath.substring(index1);				
			targetPath = targetPath.replaceFirst("src/main/resources", "web/WEB-INF/classes");
			
			createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
			copyFile(new File(sourcePath),new File(targetPath));
			return;
		}
		
		if(filePath.indexOf("/template") != -1){  //配置文件
			String sourcePath = transToStatic(filePath,basePath); 
			int index1 = sourcePath.indexOf("/web");
			String targetPath = rootPath + sourcePath.substring(index1);		
			createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
			copyFile(new File(sourcePath),new File(targetPath));
			return;
		}
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
		int statIndex = filePath.indexOf("/web/");
		filePath = basePath + filePath.substring(statIndex);
		return filePath;
	}
	
	// 转换资源类文件
	private String transToResource(String filePath,String basePath) {
		int comIndex = filePath.indexOf("/resources");
		String subPath = filePath.substring(comIndex+10);
		String prefix = "/web/WEB-INF/classes";
	    subPath = prefix +subPath;
	    subPath = basePath+subPath;
	    return subPath;
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

	}
}
