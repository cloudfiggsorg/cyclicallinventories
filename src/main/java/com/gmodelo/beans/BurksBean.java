package com.gmodelo.beans;

import java.util.List;

public class BurksBean {

	String burks;
	String burksDesc;
	List<WerksBean> werskList;

	public String getBurks() {
		return burks;
	}

	public void setBurks(String burks) {
		this.burks = burks;
	}

	public String getBurksDesc() {
		return burksDesc;
	}

	public void setBurksDesc(String burksDesc) {
		this.burksDesc = burksDesc;
	}

	public List<WerksBean> getWerskList() {
		return werskList;
	}

	public void setWerskList(List<WerksBean> werskList) {
		this.werskList = werskList;
	}

	public BurksBean(String burks, String burksDesc, List<WerksBean> werskList) {
		super();
		this.burks = burks;
		this.burksDesc = burksDesc;
		this.werskList = werskList;
	}

	public BurksBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "BurksBean [burks=" + burks + ", burksDesc=" + burksDesc + ", werskList=" + werskList + "]";
	}

}
