package com.example.mapengineering.model;

import java.io.Serializable;

public class DataDetailModel implements Serializable{

	private String ID;
	private String zhuanghao;
	private String qianshi;
	private String zhongshi;
	private String houshi;
	
	

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
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
