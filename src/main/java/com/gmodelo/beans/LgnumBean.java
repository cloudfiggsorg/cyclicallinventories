package com.gmodelo.beans;

import java.util.List;

public class LgnumBean {

	String lgnum;
	String lgnumDesc;
	List<LgtypBean> lgtypList;

	public String getLgnum() {
		return lgnum;
	}

	public void setLgnum(String lgnum) {
		this.lgnum = lgnum;
	}

	public String getLgnumDesc() {
		return lgnumDesc;
	}

	public void setLgnumDesc(String lgnumDesc) {
		this.lgnumDesc = lgnumDesc;
	}

	public List<LgtypBean> getLgtypList() {
		return lgtypList;
	}

	public void setLgtypList(List<LgtypBean> lgtypList) {
		this.lgtypList = lgtypList;
	}

	public LgnumBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LgnumBean(String lgnum, String lgnumDesc, List<LgtypBean> lgtypList) {
		super();
		this.lgnum = lgnum;
		this.lgnumDesc = lgnumDesc;
		this.lgtypList = lgtypList;
	}

	@Override
	public String toString() {
		return "LgnumBean [lgnum=" + lgnum + ", lgnumDesc=" + lgnumDesc + ", lgtypList=" + lgtypList + "]";
	}

}
