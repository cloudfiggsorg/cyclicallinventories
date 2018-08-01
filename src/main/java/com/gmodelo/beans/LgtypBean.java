package com.gmodelo.beans;

import java.util.List;

public class LgtypBean {

	String lgtyp;
	String lgtypDesc;
	List<LgberBean> lgberList;

	public String getLgtyp() {
		return lgtyp;
	}

	public void setLgtyp(String lgtyp) {
		this.lgtyp = lgtyp;
	}

	public String getLgtypDesc() {
		return lgtypDesc;
	}

	public void setLgtypDesc(String lgtypDesc) {
		this.lgtypDesc = lgtypDesc;
	}

	public List<LgberBean> getLgberList() {
		return lgberList;
	}

	public void setLgberList(List<LgberBean> lgberList) {
		this.lgberList = lgberList;
	}

	public LgtypBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LgtypBean(String lgtyp, String lgtypDesc, List<LgberBean> lgberList) {
		super();
		this.lgtyp = lgtyp;
		this.lgtypDesc = lgtypDesc;
		this.lgberList = lgberList;
	}

	@Override
	public String toString() {
		return "LgtypBean [lgtyp=" + lgtyp + ", lgtypDesc=" + lgtypDesc + ", lgberList=" + lgberList + "]";
	}

}
