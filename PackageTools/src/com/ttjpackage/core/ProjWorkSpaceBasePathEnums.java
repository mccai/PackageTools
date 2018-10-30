package com.ttjpackage.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ProjWorkSpaceBasePathEnums {
	TTJ_PC("01", "C:/ttj_20160713/ttj2.0/ttj-web/"),
	TTJ_WX("02", "C:/ttj_20160713/ttj2.0/ttj-wx/"),
	TTJ_APP("03", "C:/ttj_20160713/ttj2.0/ttj-app/"),	
	MALL_WX("04", "C:/ttj_20160713/mall/mall-wx/"),
	MALL_PC("05", "C:/ttj_20160713/mall/mall-web/"),
	MALL_APP("06", "C:/ttj_20160713/mall/mall-app/"),
	MALL_DUBOO("07", "C:/ttj_20160713/mall/mall-duboo/"),
	TTJ_MANAGE("08", "C:/ttj_20160713/ttj2.0/ttj-manage/"),
	MALL_MANAGE("09", "C:/ttj_20160713/mall/mall-manage/"),
	TTJ_REPORT("10", "C:/ttj_20160713/ttj2.0/ttj-report/");
	
	public static ProjWorkSpaceBasePathEnums toArcBatchStatus(String value) {
		return codeMap.get(value);
	}

	public static String getNameByCode(String value) { 
		ProjWorkSpaceBasePathEnums status = codeMap.get(value);
		if (status != null) {
			return status.getName();
		}
		return "δ֪״̬";
	}

	public static String getCodeByName(String name) {
		ProjWorkSpaceBasePathEnums status = nameMap.get(name);
		if (status != null) {
			return status.getValue();
		}
		return "";
	}

	private static Map<String, ProjWorkSpaceBasePathEnums> codeMap;

	private static Map<String, ProjWorkSpaceBasePathEnums> nameMap;
	static {
		codeMap = new HashMap<String, ProjWorkSpaceBasePathEnums>();
		nameMap = new HashMap<String, ProjWorkSpaceBasePathEnums>();
		for (ProjWorkSpaceBasePathEnums status : ProjWorkSpaceBasePathEnums.values()) {
			codeMap.put(status.getValue(), status);
			nameMap.put(status.getName(), status);
		}
	}

	public static List<DicObject> getLendingTypeList(){
		List<DicObject> lst = new ArrayList<DicObject>();
		for (ProjWorkSpaceBasePathEnums status : ProjWorkSpaceBasePathEnums.values()) {
			DicObject d = new DicObject();
			d.setValue(status.getValue());
			d.setName(status.getName());
			lst.add(d);
		}
		return lst;
	}
	
	private String value;

	private String name;

	private ProjWorkSpaceBasePathEnums(String value, String name) {
		this.value = value;
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return this.getValue() + "-" + this.getName();
	}
}
