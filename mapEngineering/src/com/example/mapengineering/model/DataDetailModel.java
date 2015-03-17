package com.example.mapengineering.model;

import java.io.Serializable;

public class DataDetailModel implements Serializable{

	private String startPoint;
	private String endPoint;
	private String zhuanghao;
	private String qianshi;
	private String zhongshi;
	private String houshi;
	
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
	public String getZhuanghao() {
		return zhuanghao;
	}
	public void setZhuanghao(String zhuanghao) {
		this.zhuanghao = zhuanghao;
	}
	public String getQianshi() {
		return qianshi;
	}
	public void setQianshi(String qianshi) {
		this.qianshi = qianshi;
	}
	public String getZhongshi() {
		return zhongshi;
	}
	public void setZhongshi(String zhongshi) {
		this.zhongshi = zhongshi;
	}
	public String getHoushi() {
		return houshi;
	}
	public void setHoushi(String houshi) {
		this.houshi = houshi;
	}
}
