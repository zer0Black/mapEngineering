package com.example.mapengineering.model;

public class DataModel {

	private String Date;
	private String startTime;
	private String endTime;
	private String manCodeOne;
	private String manCodeTwo;
	private String manCodeThree;
	private int measureStyle;
	private Boolean isAgainMeasure;
	
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
	public int getMeasureStyle() {
		return measureStyle;
	}
	public void setMeasureStyle(int measureStyle) {
		this.measureStyle = measureStyle;
	}
	public Boolean getIsAgainMeasure() {
		return isAgainMeasure;
	}
	public void setIsAgainMeasure(Boolean isAgainMeasure) {
		this.isAgainMeasure = isAgainMeasure;
	}
	
}
