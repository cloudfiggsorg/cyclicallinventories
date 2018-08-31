package com.gmodelo.beans;

import java.util.List;

public class ZoneUserBean {
	String zoneId;
	String zdesc;
	
	List<ZoneUserPositionsBean> positionsB;

	public List<ZoneUserPositionsBean> getPositionsB() {
		return positionsB;
	}
	public void setPositionsB(List<ZoneUserPositionsBean> positionsB) {
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

	public ZoneUserBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ZoneUserBean(String zoneId, String zdesc, List<ZoneUserPositionsBean> positionsB) {
		super();
		this.zoneId = zoneId;
		this.zdesc = zdesc;
		this.positionsB = positionsB;
	}
	@Override
	public String toString() {
		return "ZoneUserBean [zoneId=" + zoneId + ", zdesc=" + zdesc + ", positionsB=" + positionsB + "]";
	}

}
