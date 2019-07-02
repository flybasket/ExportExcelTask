package com.wisewater.vo;

import java.util.Arrays;

public enum ExportDataTypeEnum {
	ExportDataTypeCounter, ExportDataTypeMeterReading;

	public String excelName() {
		String excelName = "";
		switch (this.ordinal()) {
		case 0:
			excelName = "计量设备信息";
			break;
		case 1:
			excelName = "抄表信息";
			break;
		}

		return excelName;
	}

	public String tableName() {
		String tableName = "";
		switch (this.ordinal()) {
		case 0:
			tableName = "mp_counter";
			break;
		case 1:
			tableName = "mp_counter";
			break;
		}

		return tableName;
	}

	public String[] excelHeaders() {
		String[] headers = null;
		switch (this.ordinal()) {
		case 0:
			headers = (String[]) Arrays
					.asList("设备编号", "设备名", "户号", "地区", "通讯卡号", "安装时间", "抄表频率", "户名", "地址", "联系方式", "抄表行码").toArray();
			break;
		case 1:
			headers = (String[]) Arrays
					.asList("设备编号", "设备名", "户号", "地区", "通讯卡号", "安装时间", "抄表频率", "户名", "地址", "联系方式", "抄表行码").toArray();
			break;
		}
		return headers;
	}

}
