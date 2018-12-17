package com.gmodelo.cyclicinventories.beans;

import java.util.List;

public class WerksBean {

	private String werks;
	private String werksDesc;
	private List<Lgnum> lgnumList;

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

	public List<Lgnum> getLgnumList() {
		return lgnumList;
	}

	public void setLgnumList(List<Lgnum> lgnumList) {
		this.lgnumList = lgnumList;
	}

	@Override
	public String toString() {
		return "WerksBean [werks=" + werks + ", werksDesc=" + werksDesc + ", lgnumList=" + lgnumList + "]";
	}

	public WerksBean(String werks, String werksDesc, List<Lgnum> lgnumList) {
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
