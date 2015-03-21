package com.example.mapengineering.model;

public class DataModel {

	private String ID;
	private String Date;
	private String startTime;
	private String endTime;
	private String startPoint;//起始水准点
	private String endPoint;//终止水准点
	private String manCodeOne;
	private String manCodeTwo;
	private String manCodeThree;
	private int measureType;//测量类型（中平？）
	private Boolean isAgainMeasure;//是不是复测
	private Boolean flag;//测量完为true，未测量完为false
	
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(String startPoint) {
		this.startPoint = startPoint;
	}
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	public String getManCodeOne() {
		return manCodeOne;
	}
	public void setManCodeOne(String manCodeOne) {
		this.manCodeOne = manCodeOne;
	}
	public String getManCodeTwo() {
		return manCodeTwo;
	}
	public void setManCodeTwo(String manCodeTwo) {
		this.manCodeTwo = manCodeTwo;
	}
	public String getManCodeThree() {
		return manCodeThree;
	}
	public void setManCodeThree(String manCodeThree) {
		this.manCodeThree = manCodeThree;
	}
	public int getMeasureType() {
		return measureType;
	}
	public void setMeasureType(int measureType) {
		this.measureType = measureType;
	}
	public Boolean getIsAgainMeasure() {
		return isAgainMeasure;
	}
	public void setIsAgainMeasure(Boolean isAgainMeasure) {
		this.isAgainMeasure = isAgainMeasure;
	}
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	
}
