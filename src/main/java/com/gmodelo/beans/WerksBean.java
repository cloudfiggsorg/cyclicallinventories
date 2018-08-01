package com.gmodelo.beans;

import java.util.List;

public class WerksBean {

	String werks;
	String werksDesc;
	List<LgnumBean> lgnumList;

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public String getWerksDesc() {
		return werksDesc;
	}

	public void setWerksDesc(String werksDesc) {
		this.werksDesc = werksDesc;
	}

	public List<LgnumBean> getLgnumList() {
		return lgnumList;
	}

	public void setLgnumList(List<LgnumBean> lgnumList) {
		this.lgnumList = lgnumList;
	}

	@Override
	public String toString() {
		return "WerksBean [werks=" + werks + ", werksDesc=" + werksDesc + ", lgnumList=" + lgnumList + "]";
	}

	public WerksBean(String werks, String werksDesc, List<LgnumBean> lgnumList) {
		super();
		this.werks = werks;
		this.werksDesc = werksDesc;
		this.lgnumList = lgnumList;
	}

	public WerksBean() {
		super();
		// TODO Auto-generated constructor stub
	}

}
