package com.gmodelo.cyclicinventories.beans;

import java.util.HashMap;

public class ZoneUserPositionsBean {
	
	private int pkAsgId;
	private String zoneId;
	private String lgtyp;
	private String lgpla;
	private String secuency;
	private String imwm;	
	private HashMap<String, LgplaValuesBean> lgplaValues;
	
	public ZoneUserPositionsBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getPkAsgId() {
		return pkAsgId;
	}

	public void setPkAsgId(int pkAsgId) {
		this.pkAsgId = pkAsgId;
	}
	
	public HashMap<String, LgplaValuesBean> getLgplaValues() {
		return lgplaValues;
	}

	public void setLgplaValues(HashMap<String, LgplaValuesBean> lgplaValues) {
		this.lgplaValues = lgplaValues;
	}

	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getLgtyp() {
		return lgtyp;
	}
	public void setLgtyp(String lgtyp) {
		this.lgtyp = lgtyp;
	}
	public String getLgpla() {
		return lgpla;
	}
	public void setLgpla(String lgpla) {
		this.lgpla = lgpla;
	}
	public String getSecuency() {
		return secuency;
	}
	public void setSecuency(String secuency) {
		this.secuency = secuency;
	}
	public String getImwm() {
		return imwm;
	}
	public void setImwm(String imwm) {
		this.imwm = imwm;
	}

	public ZoneUserPositionsBean(int pkAsgId, String zoneId, String lgtyp, String lgpla, String secuency, String imwm,
			HashMap<String, LgplaValuesBean> lgplaValues) {
		super();
		this.pkAsgId = pkAsgId;
		this.zoneId = zoneId;
		this.lgtyp = lgtyp;
		this.lgpla = lgpla;
		this.secuency = secuency;
		this.imwm = imwm;
		this.lgplaValues = lgplaValues;
	}

	@Override
	public String toString() {
		return "ZoneUserPositionsBean [pkAsgId=" + pkAsgId + ", zoneId=" + zoneId + ", lgtyp=" + lgtyp + ", lgpla="
				+ lgpla + ", secuency=" + secuency + ", imwm=" + imwm + ", lgplaValues=" + lgplaValues + "]";
	}
	
	
}
