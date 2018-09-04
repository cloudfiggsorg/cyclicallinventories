package com.gmodelo.beans;

public class RoutePositionBean {
	
	int positionId;
	String lgort;
	String gdesc;
	String zoneId;
	String secuency;
	String zdesc;
	
	public String getZdesc() {
		return zdesc;
	}
	public void setZdesc(String zdesc) {
		this.zdesc = zdesc;
	}
	public int getPositionId() {
		return positionId;
	}
	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}
	public String getLgort() {
		return lgort;
	}
	public void setLgort(String lgort) {
		this.lgort = lgort;
	}
	public String getGdesc() {
		return gdesc;
	}
	public void setGdesc(String gdesc) {
		this.gdesc = gdesc;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getSecuency() {
		return secuency;
	}
	public void setSecuency(String secuency) {
		this.secuency = secuency;
	}
	public RoutePositionBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RoutePositionBean(int positionId, String lgort, String gdesc,
			String zoneId, String secuency, String zdesc) {
		super();
		
		this.positionId = positionId;
		this.lgort = lgort;
		this.gdesc = gdesc;
		this.zoneId = zoneId;
		this.secuency = secuency;
		this.zdesc = zdesc;
	}
	
	public RoutePositionBean(String routeId, String lgort, String gdesc,
			String zoneId, String secuency) {
		super();
		this.lgort = lgort;
		this.gdesc = gdesc;
		this.zoneId = zoneId;
		this.secuency = secuency;
	}
	
	public RoutePositionBean(String lgort, String gdesc,
			String zoneId, String secuency) {
		super();
		this.lgort = lgort;
		this.gdesc = gdesc;
		this.zoneId = zoneId;
		this.secuency = secuency;
	}
	
	public RoutePositionBean(
			String zoneId, String secuency) {
		super();
		this.zoneId = zoneId;
		this.secuency = secuency;
	}
	
	@Override
	public String toString() {
		return "RoutePositionB [positionId=" + positionId
				+ ", lgort=" + lgort + ", gdesc=" + gdesc + ", zoneId=" + zoneId + ", secuency=" + secuency + ", zdesc="
				+ zdesc + "]";
	}

}
