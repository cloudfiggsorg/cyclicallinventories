package com.gmodelo.beans;

public class MaterialToZoneBean {

	String zoneId;
	String position;
	String matnr;
	
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String routeId) {
		this.zoneId = routeId;
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
	
	public MaterialToZoneBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public MaterialToZoneBean(String zoneId, String position, String matnr) {
		super();
		this.zoneId = zoneId;
		this.position = position;
		this.matnr = matnr;
	}
	
	@Override
	public String toString() {
		return "MaterialToZoneBean [zoneId=" + zoneId + ", position=" + position + ", matnr=" + matnr + "]";
	}
	
	
	
}
