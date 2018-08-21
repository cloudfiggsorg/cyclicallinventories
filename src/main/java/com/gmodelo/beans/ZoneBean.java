package com.gmodelo.beans;

public class ZoneBean {
	
	String lgort; // almacen de la zona
	String lgobe; // descripcion de almacen
	Integer idZone; //id de la zona
	String zoneDesc; //descripcion de la zona
	String bukrs; //sociedad de la zona
	String werks; //centro de la zona
	
	public String getLgort() {
		return lgort;
	}
	public void setLgort(String lgort) {
		this.lgort = lgort;
	}
	public String getLgobe() {
		return lgobe;
	}
	public void setLgobe(String lgobe) {
		this.lgobe = lgobe;
	}
	public Integer getIdZone() {
		return idZone;
	}
	public void setIdZone(Integer idZone) {
		this.idZone = idZone;
	}
	public String getZoneDesc() {
		return zoneDesc;
	}
	public void setZoneDesc(String zoneDesc) {
		this.zoneDesc = zoneDesc;
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
	
	public ZoneBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ZoneBean(String lgort, String lgobe, Integer idZone, String zoneDesc, String bukrs, String werks) {
		super();
		this.lgort = lgort;
		this.lgobe = lgobe;
		this.idZone = idZone;
		this.zoneDesc = zoneDesc;
		this.bukrs = bukrs;
		this.werks = werks;
	}
	@Override
	public String toString() {
		return "ZoneBean [lgort=" + lgort + ", lgobe=" + lgobe + ", idZone=" + idZone + ", zoneDesc=" + zoneDesc
				+ ", bukrs=" + bukrs + ", werks=" + werks + "]";
	}
	

}
