package com.gmodelo.beans;

public class RoutePositionB {
	String routeId;
	String positionId;
	String lgort;
	String lgtyp;
	String zoneId;
	String secuency;
	String zdesc;
	
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getZdesc() {
		return zdesc;
	}
	public void setZdesc(String zdesc) {
		this.zdesc = zdesc;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getLgort() {
		return lgort;
	}
	public void setLgort(String lgort) {
		this.lgort = lgort;
	}
	public String getLgtyp() {
		return lgtyp;
	}
	public void setLgtyp(String lgtyp) {
		this.lgtyp = lgtyp;
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
	public RoutePositionB() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RoutePositionB(String routeId, String positionId, String lgort, String lgtyp,
			String zoneId, String secuency, String zdesc) {
		super();
		
		this.routeId = routeId;
		this.positionId = positionId;
		this.lgort = lgort;
		this.lgtyp = lgtyp;
		this.zoneId = zoneId;
		this.secuency = secuency;
		this.zdesc = zdesc;
	}
	
	public RoutePositionB(String routeId, String lgort, String lgtyp,
			String zoneId, String secuency) {
		super();
		this.routeId = routeId;
		this.lgort = lgort;
		this.lgtyp = lgtyp;
		this.zoneId = zoneId;
		this.secuency = secuency;
	}
	
	public RoutePositionB(String lgort, String lgtyp,
			String zoneId, String secuency) {
		super();
		this.lgort = lgort;
		this.lgtyp = lgtyp;
		this.zoneId = zoneId;
		this.secuency = secuency;
	}
	
	@Override
	public String toString() {
		return "RoutePositionB [routeId=" + routeId + ", positionId=" + positionId
				+ ", lgort=" + lgort + ", lgtyp=" + lgtyp + ", zoneId=" + zoneId + ", secuency=" + secuency + ", zdesc="
				+ zdesc + "]";
	}

}
