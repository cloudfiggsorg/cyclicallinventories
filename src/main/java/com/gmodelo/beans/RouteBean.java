package com.gmodelo.beans;

public class RouteBean {
	
	String routeId;
	String routeDesc;
	String bukrs;
	String werks;
	String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getRouteDesc() {
		return routeDesc;
	}
	public void setRouteDesc(String routeDesc) {
		this.routeDesc = routeDesc;
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
	
	public RouteBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public RouteBean(String routeId, String routeDesc, String bukrs, String werks) {
		super();
		this.routeId = routeId;
		this.routeDesc = routeDesc;
		this.bukrs = bukrs;
		this.werks = werks;
	}
	
	@Override
	public String toString() {
		return "RouteBean [routeId=" + routeId + ", routeDesc=" + routeDesc + ", bukrs=" + bukrs + ", werks=" + werks
				+ "]";
	}
	
	

}
