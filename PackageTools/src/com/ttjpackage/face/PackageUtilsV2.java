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
import com.ttjpackage.core.DicObject;
import com.ttjpackage.core.PckConfigInfo;
import com.ttjpackage.core.ProjEnums;
/***
 * ��̨���̴�������
 * @author caimaochang
 *
 */
public class PackageUtilsV2 {
	
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
		projLinkMap.put(ProjEnums.TTJ_DUBOO.getValue(), PckConfigInfo.ttjDubooBasePath);
		projLinkMap.put(ProjEnums.MALL_MANAGE.getValue(), PckConfigInfo.mallManageBasePath);
		projLinkMap.put(ProjEnums.MALL_SCHEDULE.getValue(),PckConfigInfo.mallScheduleBasePath);
		projLinkMap.put(ProjEnums.TTJ_SERVICE.getValue(), PckConfigInfo.ttjServiceBasePath);
		projLinkMap.put(ProjEnums.MALL_SERVICE.getValue(), PckConfigInfo.mallServiceBasePath);
		projLinkMap.put(ProjEnums.REPORT_SERVICE.getValue(), PckConfigInfo.ttjReportServiceBasePath);
		projLinkMap.put(ProjEnums.HRT_WX.getValue(), PckConfigInfo.hrtWxBasePath);
		projLinkMap.put(ProjEnums.HRT_MANATE.getValue(), PckConfigInfo.hrtManageBasePath);
		projLinkMap.put(ProjEnums.HRT_SERVICE.getValue(), PckConfigInfo.hrtServiceBasePath);
	}
	*//***
	 * @param args
	 *//*
	public static void main(String[] args) {
		try {
			System.out.println("start packing------------------------------------");			
			long startTime = System.currentTimeMillis();
			PackageUtilsV2 packageUtils = new PackageUtilsV2();
			String [] projs =  null;
			if(args != null && args.length == 1){
				String arg1 = args[0];
				projs = arg1.split("#");
				for(String proj:projs){
					if (ProjEnums.toArcBatchStatus(proj) == null) {
						System.out.println("��������ȷ:"+proj);
						return;
					}
				}
			}else{
				projs = packageUtils.getPckProjs();
			}
			//ɾ��Ŀǰ���ڴ��Ŀ¼���ļ�
			packageUtils.deleteCurDataDir();
			for(String proj:projs){
				System.out.println("start packing "+ProjEnums.getNameByCode(proj)+"-------------------");
				String  path = packageUtils.createPckDir(ProjEnums.toArcBatchStatus(proj)); // step1				
				packageUtils.doPck(path,projLinkMap.get(proj),ProjEnums.getNameByCode(proj));
				System.out.println("finish packing "+ProjEnums.getNameByCode(proj)+"-------------------");
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
	// step1 -�����ļ�
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
	*//****
	 * ���service���Ƿ���Ҫ���  true:��Ҫ��� false������Ҫ���
	 * @param keyWord
	 * @param filePath
	 * @return
	 *//*
	private boolean checkNeedPck(String keyWord,String filePath){
		if(filePath.indexOf(PckConfigInfo.mallServiceKeyWord) != -1){ //�̳�service
			if(keyWord.indexOf("mall") != -1){  //�̳ǵ���Ŀ
				return true; 
			}else{
				return false;
			}
		}		
		if(filePath.indexOf(PckConfigInfo.ttjServiceKeyWord) != -1){ //���Խ�service
			if(keyWord.indexOf("ttj") != -1 && !keyWord.equals(ProjEnums.TTJ_REPORT.getName())){
				return true;
			}else{
				return false;
			}
		}
		if(filePath.indexOf(PckConfigInfo.reportServiceKeyWord) != -1){ //���Խ𱨱�service
			if(keyWord.equals(ProjEnums.TTJ_REPORT.getName())){
				return true;
			}else{
				return false;
			}
		}
		
		if(filePath.indexOf(PckConfigInfo.hrtServiceBasePath) != -1){ //���׽�Service
			if(keyWord.indexOf("hrt") != -1){  //���׽蹤��
				return true; 
			}else{
				return false;
			}
		}
		return true;
	}
	
	*//***
	 * ������Ҫ����Щ��Ŀ�İ�
	 * @return
	 *//*
	private String[] getPckProjs() throws Exception{
		
		List<String> allProjs = new ArrayList<String>();
		List<DicObject> allList = ProjEnums.getLendingTypeList();
		
		for(DicObject obj : allList){ //������Ŀ�б�
			allProjs.add(obj.getName());
		}		
		List<String> projs = new ArrayList<String>();
		for(String proj:allProjs){
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(PckConfigInfo.packFilePath)), "UTF-8"));
			String lineTxt = null;
			while ((lineTxt = br.readLine()) != null) {
				String filePath = lineTxt;
				if(filePath.indexOf(proj) != -1){
					projs.add(ProjEnums.getCodeByName(proj));
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
	private void doPck(String rootPath,String basePath,String keyWord) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(PckConfigInfo.packFilePath)), "UTF-8"));
		String lineTxt = null;
		while ((lineTxt = br.readLine()) != null) {						
			String filePath = lineTxt;
			if(filePath.indexOf(keyWord) == -1){ //�ǵ�ǰ��Ŀ�ļ���ֱ������
				if(filePath.indexOf(PckConfigInfo.mallServiceKeyWord) != -1 || filePath.indexOf(PckConfigInfo.ttjServiceKeyWord) != -1 
						|| filePath.indexOf(PckConfigInfo.reportServiceKeyWord) != -1
						|| filePath.indexOf(PckConfigInfo.hrtServiceKeyWord) != -1){
					if(!checkNeedPck(keyWord, filePath)) continue; 
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
		if(filePath.indexOf(".java") != -1){ //�����ļ�
			List<String> sourcePathList = transToClassPath(filePath,basePath);
			if(sourcePathList != null && sourcePathList.size() > 0){
				for(String sourcePath:sourcePathList){
					int index1 = sourcePath.indexOf("\\web");
					if(index1 == -1){
						index1 = sourcePath.indexOf("/web");
					}
					String targetPath = rootPath + sourcePath.substring(index1);
					//����Ŀ¼
				    File dir = new File(targetPath.substring(0,targetPath.lastIndexOf("/")));
				    if(!dir.exists()){
				    	createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
				    }												
					copyFile(new File(sourcePath),new File(targetPath));
				}
			}				
			return;
		}
		if(filePath.indexOf(".jsp") != -1){ //ҳ���ļ�
			String sourcePath = transToJsp(filePath,basePath);
			int index1 = sourcePath.indexOf("/web");
			String targetPath = rootPath + sourcePath.substring(index1);
			
			createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
			copyFile(new File(sourcePath),new File(targetPath));
			return;
		}
		if(filePath.indexOf(".js") != -1 && filePath.indexOf(".jsp") == -1){ //js�ļ�
			String sourcePath = transToStatic(filePath,basePath); 
			int index1 = sourcePath.indexOf("/web");
			String targetPath = rootPath + sourcePath.substring(index1);
		
			createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
			copyFile(new File(sourcePath),new File(targetPath));
			return;
		}
		if(filePath.indexOf("static") != -1 || filePath.indexOf(".png") != -1 || filePath.indexOf(".css") != -1 || filePath.indexOf(".jpg") != -1){  //ͼƬ,css�Ⱦ�̬Ŀ¼���ļ�
			String sourcePath = transToStatic(filePath,basePath); 
			int index1 = sourcePath.indexOf("/web");
			String targetPath = rootPath + sourcePath.substring(index1);
		
			createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
			copyFile(new File(sourcePath),new File(targetPath));
			return;
		}
		if(filePath.indexOf("/src/main/resources") != -1){  //�����ļ�
			String sourcePath = transToResource(filePath,basePath); 
			int index1 = sourcePath.indexOf("/src/main/resources");
			String targetPath = rootPath + sourcePath.substring(index1);				
			targetPath = targetPath.replaceFirst("src/main/resources", "web/WEB-INF/classes");
			
			createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
			copyFile(new File(sourcePath),new File(targetPath));
			return;
		}
	}
	//ת�����ļ�
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

	//ת��jspҳ��
	private String transToJsp(String jspFilePath,String basePath){
		int statIndex = jspFilePath.indexOf("/web/");
		jspFilePath = basePath + jspFilePath.substring(statIndex);
		return jspFilePath;
	}
	
	//ת����̬�ļ���js��ͼƬ��css��
	private String transToStatic(String filePath,String basePath) {
		int statIndex = filePath.indexOf("/web/");
		filePath = basePath + filePath.substring(statIndex);
		return filePath;
	}
	
	// ת����Դ���ļ�
	private String transToResource(String filePath,String basePath) {
		int statIndex = filePath.indexOf("src/main/resources");
		filePath = basePath + filePath.substring(statIndex);
		return filePath;
	}
	
	
	public static void createDirectory(String path) throws Exception {
		try {
			// ����ļ�����
			File f = new File(path);
			if (!f.exists()) {
				// ���·��������,�򴴽�
				f.mkdirs();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static void copyFile(File sourcefile, File targetFile)
			throws IOException {		
		if(sourcefile.isDirectory()) return;
		
		// �½��ļ����������������л���
		FileInputStream input = new FileInputStream(sourcefile);
		BufferedInputStream inbuff = new BufferedInputStream(input);

		// �½��ļ���������������л���
		FileOutputStream out = new FileOutputStream(targetFile);
		BufferedOutputStream outbuff = new BufferedOutputStream(out);

		// ��������
		byte[] b = new byte[1024 * 5];
		int len = 0;
		while ((len = inbuff.read(b)) != -1) {
			outbuff.write(b, 0, len);
		}

		// ˢ�´˻���������
		outbuff.flush();

		// �ر���
		inbuff.close();
		outbuff.close();
		out.close();
		input.close();

	}

	public static void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {

		// �½�Ŀ��Ŀ¼

		(new File(targetDir)).mkdirs();

		// ��ȡԴ�ļ��е��µ��ļ���Ŀ¼
		File[] file = (new File(sourceDir)).listFiles();
		if (file != null && file.length > 0) {
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					if (file[i].getName().indexOf("svn") != -1) {
						continue;
					}
					// Դ�ļ�
					File sourceFile = file[i];
					// Ŀ���ļ�
					File targetFile = new File(
							new File(targetDir).getAbsolutePath()
									+ File.separator + file[i].getName());

					copyFile(sourceFile, targetFile);

				}
				if (file[i].isDirectory()) {
					if (file[i].getName().indexOf("svn") != -1) {
						continue;
					}
					// ׼�����Ƶ�Դ�ļ���
					String dir1 = sourceDir + File.separator
							+ file[i].getName();
					// ׼�����Ƶ�Ŀ���ļ���
					String dir2 = targetDir + File.separator
							+ file[i].getName();

					copyDirectiory(dir1, dir2);
				}
			}
		}

	}*/
}
