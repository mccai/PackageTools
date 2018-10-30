package com.ttjpackage.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ProjEnums {
	TTJ_PC("01", "ttj-web"),
	TTJ_WX("02", "ttj-wx"),
	TTJ_APP("03", "ttj-app"),
	TTJ_MANAGE("04", "ttj-manage"),	
	TTJ_REPORT("05", "ttj-report"),
	MALL_WX("06", "mall-wx"),
	MALL_PC("07", "mall-web"),
	MALL_APP("08", "mall-app"),
	TTJ_DUBOO("09", "ttj-duboo"),	
	MALL_MANAGE("10", "mall-manage"),
	MALL_SCHEDULE("11","mall-schedule"),
	TTJ_SERVICE("12","srtech-service"),
	MALL_SERVICE("13","mall-service"),
	REPORT_SERVICE("14","report-service"),
	HRT_WX("15","hrt-wx"),
	HRT_MANATE("16","hrt-manage"),
	HRT_SERVICE("17","hrt-service");
	
	
	public static ProjEnums toArcBatchStatus(String value) {
		return codeMap.get(value);
	}

	public static String getNameByCode(String value) { 
		ProjEnums status = codeMap.get(value);
		if (status != null) {
			return status.getName();
		}
		return "δ֪״̬";
	}

	public static String getCodeByName(String name) {
		ProjEnums status = nameMap.get(name);
		if (status != null) {
			return status.getValue();
		}
		return "";
	}

	private static Map<String, ProjEnums> codeMap;

	private static Map<String, ProjEnums> nameMap;
	static {
		codeMap = new HashMap<String, ProjEnums>();
		nameMap = new HashMap<String, ProjEnums>();
		for (ProjEnums status : ProjEnums.values()) {
			codeMap.put(status.getValue(), status);
			nameMap.put(status.getName(), status);
		}
	}

	public static List<DicObject> getLendingTypeList(){
		List<DicObject> lst = new ArrayList<DicObject>();
		for (ProjEnums status : ProjEnums.values()) {
			DicObject d = new DicObject();
			d.setValue(status.getValue());
			d.setName(status.getName());
			lst.add(d);
		}
		return lst;
	}
	
	private String value;

	private String name;

	private ProjEnums(String value, String name) {
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
