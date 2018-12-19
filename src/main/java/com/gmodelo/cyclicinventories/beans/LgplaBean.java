package com.gmodelo.cyclicinventories.beans;

import java.util.Date;
import java.util.HashMap;

public class LgplaBean {

	private String lgpla;
	private String lgplaDesc;
	private Date beginDate;
	private Date endDate;
	// This map is String - KeyObject / CarrilValues Object
	private HashMap<String, LgplaValuesBean> lgplaValues;

	public String getLgpla() {
		return lgpla;
	}

	public void setLgpla(String lgpla) {
		this.lgpla = lgpla;
	}

	public String getLgplaDesc() {
		return lgplaDesc;
	}

	public void setLgplaDesc(String lgplaDesc) {
		this.lgplaDesc = lgplaDesc;
	}

	public HashMap<String, LgplaValuesBean> getLgplaValues() {
		return lgplaValues;
	}

	public void setLgplaValues(HashMap<String, LgplaValuesBean> lgplaValues) {
		this.lgplaValues = lgplaValues;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public LgplaBean(String lgpla, String lgplaDesc, Date beginDate, Date endDate,
			HashMap<String, LgplaValuesBean> lgplaValues) {
		super();
		this.lgpla = lgpla;
		this.lgplaDesc = lgplaDesc;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.lgplaValues = lgplaValues;
	}

	public LgplaBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "LgplaBean [lgpla=" + lgpla + ", lgplaDesc=" + lgplaDesc + ", beginDate=" + beginDate + ", endDate="
				+ endDate + ", lgplaValues=" + lgplaValues + "]";
	}

}
