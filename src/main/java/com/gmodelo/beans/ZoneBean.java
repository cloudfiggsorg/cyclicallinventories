package com.gmodelo.beans;

import java.util.List;

public class ZoneBean {
	String zoneId;
	String zdesc;
	String bukrs;
	String werks;
	String lgort;
	String bDesc;
	String wDesc;
	String gDesc;
	
	List<ZonePositionsBean> positions;
	
	public String getbDesc() {
		return bDesc;
	}
	public void setbDesc(String bDesc) {
		this.bDesc = bDesc;
	}
	public String getwDesc() {
		return wDesc;
	}
	public void setwDesc(String wDesc) {
		this.wDesc = wDesc;
	}
	public String getgDesc() {
		return gDesc;
	}
	public void setgDesc(String gDesc) {
		this.gDesc = gDesc;
	}
	public List<ZonePositionsBean> getPositions() {
		return positions;
	}
	public void setPositions(List<ZonePositionsBean> positionsB) {
		this.positions = positionsB;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getZdesc() {
		return zdesc;
	}
	public void setZdesc(String zdesc) {
		this.zdesc = zdesc;
	}
	public String getBukrs() {
		return bukrs;
	}
	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}
	public String getWerks() {
		return werks;
	}
	public void setWerks(String werks) {
		this.werks = werks;
	}
	public String getLgort() {
		return lgort;
	}
	public void setLgort(String lgort) {
		this.lgort = lgort;
	}

	public ZoneBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ZoneBean( String zdesc, String bukrs, String werks, String lgort,
			List<ZonePositionsBean> positionsB) {
		super();
		this.zdesc = zdesc;
		this.bukrs = bukrs;
		this.werks = werks;
		this.lgort = lgort;
		this.positions = positionsB;
	}
	
	public ZoneBean(String zoneId, String zdesc, String bukrs, String werks, String lgort, String bDesc, String wDesc,
			String gDesc, List<ZonePositionsBean> positionsB) {
		super();
		this.zoneId = zoneId;
		this.zdesc = zdesc;
		this.bukrs = bukrs;
		this.werks = werks;
		this.lgort = lgort;
		this.bDesc = bDesc;
		this.wDesc = wDesc;
		this.gDesc = gDesc;
		this.positions = positionsB;
	}
	
	@Override
	public String toString() {
		return "ZoneBean [zoneId=" + zoneId + ", zdesc=" + zdesc + ", bukrs=" + bukrs + ", werks=" + werks + ", lgort="
				+ lgort + ", bDesc=" + bDesc + ", wDesc=" + wDesc + ", gDesc=" + gDesc + ", positionsB=" + positions
				+ "]";
	}
	
	

	
}
