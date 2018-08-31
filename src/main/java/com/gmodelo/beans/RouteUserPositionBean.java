package com.gmodelo.beans;

public class RouteUserPositionBean {
	
	String routeId;
	int positionId;
	String lgort;
	String gdesc;
	ZoneUserBean zone;
	String secuency;

	
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
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
	public ZoneUserBean getZoneId() {
		return zone;
	}
	public void setZoneId(ZoneUserBean zone) {
		this.zone = zone;
	}
	public String getSecuency() {
		return secuency;
	}
	public void setSecuency(String secuency) {
		this.secuency = secuency;
	}
	public RouteUserPositionBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public RouteUserPositionBean(String routeId, int positionId, String lgort, String gdesc, ZoneUserBean zone,
			String secuency) {
		super();
		this.routeId = routeId;
		this.positionId = positionId;
		this.lgort = lgort;
		this.gdesc = gdesc;
		this.zone = zone;
		this.secuency = secuency;
	}
	
	@Override
	public String toString() {
		return "RouteUserPositionBean [routeId=" + routeId + ", positionId=" + positionId + ", lgort=" + lgort
				+ ", gdesc=" + gdesc + ", zoneId=" + zone + ", secuency=" + secuency + "]";
	}
	

}
