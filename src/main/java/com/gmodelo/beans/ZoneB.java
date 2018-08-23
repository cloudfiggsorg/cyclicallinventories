package com.gmodelo.beans;

import java.util.HashMap;
import java.util.List;

public class ZoneB {
	String zoneId;
	String zdesc;
	String bukrs;
	String werks;
	String lgort;
	
	List<ZonePositionsB> positionsB;
	
	public List<ZonePositionsB> getPositionsB() {
		return positionsB;
	}
	public void setPositionsB(List<ZonePositionsB> positionsB) {
		this.positionsB = positionsB;
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

	public ZoneB() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ZoneB(String zoneId, String zdesc, String bukrs, String werks, String lgort,
			List<ZonePositionsB> positionsB) {
		super();
		this.zoneId = zoneId;
		this.zdesc = zdesc;
		this.bukrs = bukrs;
		this.werks = werks;
		this.lgort = lgort;
		this.positionsB = positionsB;
	}
	@Override
	public String toString() {
		return "ZoneB [zoneId=" + zoneId + ", zdesc=" + zdesc + ", bukrs=" + bukrs + ", werks=" + werks + ", lgort="
				+ lgort + ", positionsB=" + positionsB + "]";
	}

	
}
