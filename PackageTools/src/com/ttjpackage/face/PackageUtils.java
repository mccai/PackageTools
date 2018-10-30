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
 * ��̨���̴�������(ֻ֧�ֵ������̴�������)
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
				System.out.println("����������");
				return;
			}
			String arg1 = args[0];
			if (ProjEnums.toArcBatchStatus(arg1) == null) {
				System.out.println(" ��������ȷ");
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

	private void doPck(String rootPath,String basePath) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(PckConfigInfo.packFilePath)), "UTF-8"));
		String lineTxt = null;
		while ((lineTxt = br.readLine()) != null) {			
			System.out.println(lineTxt);
			String filePath = lineTxt;
			File file = new File(filePath);
			if(file.isDirectory()) continue;
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
				continue;
			}
			if(filePath.indexOf(".jsp") != -1){ //ҳ���ļ�
				String sourcePath = transToJsp(filePath,basePath);
				int index1 = sourcePath.indexOf("/web");
				String targetPath = rootPath + sourcePath.substring(index1);
				
				createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
				copyFile(new File(sourcePath),new File(targetPath));
				continue;
			}
			if(filePath.indexOf(".js") != -1 && filePath.indexOf(".jsp") == -1){ //js�ļ�
				String sourcePath = transToStatic(filePath,basePath); 
				int index1 = sourcePath.indexOf("/web");
				String targetPath = rootPath + sourcePath.substring(index1);
			
				createDirectory(targetPath.substring(0,targetPath.lastIndexOf("/")));
				copyFile(new File(sourcePath),new File(targetPath));
				continue;
			}
			if(filePath.indexOf("static") != -1 || filePath.indexOf(".png") != -1 || filePath.indexOf(".css") != -1){  //ͼƬ,css�Ⱦ�̬Ŀ¼���ļ�
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
		int statIndex = filePath.indexOf("/web/static");
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
