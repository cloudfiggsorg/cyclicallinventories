package com.gmodelo.beans;

public class RoutePositionBean {

	String routeId;			//Id de ruta
	String positionRouteId;	//Id de ruta de posicion
	String lgort;			//Almacen
	String lgtyp;			//Tipo de almacen
	Integer zoneId;			//Id de zona
	String secuency;		//secuencia
	
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getPositionRouteId() {
		return positionRouteId;
	}
	public void setPositionRouteId(String positionRouteId) {
		this.positionRouteId = positionRouteId;
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
	public Integer getZoneId() {
		return zoneId;
	}
	public void setZoneId(Integer zoneId) {
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
	
	public RoutePositionBean(String routeId, String positionRouteId, String lgort, String lgtyp, Integer zoneId,
			String secuency) {
		super();
		this.routeId = routeId;
		this.positionRouteId = positionRouteId;
		this.lgort = lgort;
		this.lgtyp = lgtyp;
		this.zoneId = zoneId;
		this.secuency = secuency;
	}
	
	@Override
	public String toString() {
		return "RoutePositionBean [routeId=" + routeId + ", positionRouteId=" + positionRouteId + ", lgort=" + lgort
				+ ", lgtyp=" + lgtyp + ", zoneId=" + zoneId + ", secuency=" + secuency + "]";
	}
	
	
	
}
