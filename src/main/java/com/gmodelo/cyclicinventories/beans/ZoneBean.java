package com.gmodelo.cyclicinventories.beans;

import java.util.ArrayList;
import java.util.List;

public class ZoneBean {
	
	private String zoneId;
	private String zdesc;
	private String bukrs;
	private String werks;
	private String lgort;
	private String bDesc;
	private String wDesc;
	private String gDesc;
	private String createdBy;
	private String modifiedBy;	
	private List<ZonePositionsBean> positions;
	
	public ZoneBean(){
		super();
		this.positions = new ArrayList<>();
	}
	
	public ZoneBean(String zoneId, String zdesc, String bukrs, String werks, String lgort, String bDesc, String wDesc,
			String gDesc, String createdBy, String modifiedBy, List<ZonePositionsBean> positions) {
		super();
		this.zoneId = zoneId;
		this.zdesc = zdesc;
		this.bukrs = bukrs;
		this.werks = werks;
		this.lgort = lgort;
		this.bDesc = bDesc;
		this.wDesc = wDesc;
		this.gDesc = gDesc;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.positions = positions;
	}

	@Override
	public String toString() {
		return "ZoneBean [zoneId=" + zoneId + ", zdesc=" + zdesc + ", bukrs=" + bukrs + ", werks=" + werks + ", lgort="
				+ lgort + ", bDesc=" + bDesc + ", wDesc=" + wDesc + ", gDesc=" + gDesc + ", createdBy=" + createdBy
				+ ", modifiedBy=" + modifiedBy + ", positions=" + positions + "]";
	}
	
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
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
		
}
