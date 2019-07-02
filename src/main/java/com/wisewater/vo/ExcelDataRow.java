package com.wisewater.vo;

public class ExcelDataRow {
	private String code; // 设备编号
	private String name;
	private String hno;
	private String area;
	private String simCard;
	private String installTime;
	private String meterReadingRate;
	private String hname;
	private String address;
	private String mobile;
	private byte[] image;

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public ExcelDataRow() {
		super();
	}

	public ExcelDataRow(String code, String name, String hno, String area, String simCard, String installTime,
			String meterReadingRate, String hname, String address, String mobile, byte[] image) {
		super();
		this.code = code;
		this.name = name;
		this.hno = hno;
		this.area = area;
		this.simCard = simCard;
		this.installTime = installTime;
		this.meterReadingRate = meterReadingRate;
		this.hname = hname;
		this.address = address;
		this.mobile = mobile;
		this.image = image;
	}

	public ExcelDataRow(String code, String name, String hno, String area, String simCard, String installTime,
			String meterReadingRate, String hname, String address, String mobile) {
		super();
		this.code = code;
		this.name = name;
		this.hno = hno;
		this.area = area;
		this.simCard = simCard;
		this.installTime = installTime;
		this.meterReadingRate = meterReadingRate;
		this.hname = hname;
		this.address = address;
		this.mobile = mobile;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHno() {
		return hno;
	}

	public void setHno(String hno) {
		this.hno = hno;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getSimCard() {
		return simCard;
	}

	public void setSimCard(String simCard) {
		this.simCard = simCard;
	}

	public String getInstallTime() {
		return installTime;
	}

	public void setInstallTime(String installTime) {
		this.installTime = installTime;
	}

	public String getMeterReadingRate() {
		return meterReadingRate;
	}

	public void setMeterReadingRate(String meterReadingRate) {
		this.meterReadingRate = meterReadingRate;
	}

	public String getHname() {
		return hname;
	}

	public void setHname(String hname) {
		this.hname = hname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
