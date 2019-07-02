package com.wisewater.vo;

public class ExcelQueryParam {
	private int page = 0;
	private int size = 0;

	private ExportDataTypeEnum dataTypeEnum;

	public ExcelQueryParam(int page, int size, ExportDataTypeEnum dataTypeEnum) {
		super();
		this.page = page;
		this.size = size;
		this.dataTypeEnum = dataTypeEnum;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public ExportDataTypeEnum getDataTypeEnum() {
		return dataTypeEnum;
	}

	public void setDataTypeEnum(ExportDataTypeEnum dataTypeEnum) {
		this.dataTypeEnum = dataTypeEnum;
	}

}
