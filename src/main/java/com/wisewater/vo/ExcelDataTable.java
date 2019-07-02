package com.wisewater.vo;

import java.util.List;

public class ExcelDataTable<T> {

	private String realPath;
	private String fileTitle;

	private List<String> titles;

	private List<T> dataRows;

	public String getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}

	public List<String> getTitles() {
		return titles;
	}

	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

	public List<T> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<T> dataRows) {
		this.dataRows = dataRows;
	}

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

}
