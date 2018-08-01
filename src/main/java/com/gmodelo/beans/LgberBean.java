package com.gmodelo.beans;

import java.util.List;

public class LgberBean {

	String lgber;
	String lgberDesc;
	List<LgplaBean> lgplaList;

	public String getLgber() {
		return lgber;
	}

	public void setLgber(String lgber) {
		this.lgber = lgber;
	}

	public String getLgberDesc() {
		return lgberDesc;
	}

	public void setLgberDesc(String lgberDesc) {
		this.lgberDesc = lgberDesc;
	}

	public List<LgplaBean> getLgplaList() {
		return lgplaList;
	}

	public void setLgplaList(List<LgplaBean> lgplaList) {
		this.lgplaList = lgplaList;
	}

	public LgberBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LgberBean(String lgber, String lgberDesc, List<LgplaBean> lgplaList) {
		super();
		this.lgber = lgber;
		this.lgberDesc = lgberDesc;
		this.lgplaList = lgplaList;
	}

	@Override
	public String toString() {
		return "LgberBean [lgber=" + lgber + ", lgberDesc=" + lgberDesc + ", lgplaList=" + lgplaList + "]";
	}

	//

}
