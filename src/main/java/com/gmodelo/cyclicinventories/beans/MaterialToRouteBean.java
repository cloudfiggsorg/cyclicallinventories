package com.gmodelo.cyclicinventories.beans;

public class MaterialToRouteBean {

	private String routeId;
	private String position;
	private String matnr;
	
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getMatnr() {
		return matnr;
	}
	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}
	
	public MaterialToRouteBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public MaterialToRouteBean(String routeId, String position, String matnr) {
		super();
		this.routeId = routeId;
		this.position = position;
		this.matnr = matnr;
	}
	
	@Override
	public String toString() {
		return "MaterialToRouteBean [routeId=" + routeId + ", position=" + position + ", matnr=" + matnr + "]";
	}
	
}
