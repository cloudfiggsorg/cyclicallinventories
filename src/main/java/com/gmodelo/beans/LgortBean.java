package com.gmodelo.beans;

public class LgortBean {
	
	String werks; //centro
	String lgort; //almacen
	String lgobe; // denominacion almacen
	
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
	public String getLgobe() {
		return lgobe;
	}
	public void setLgobe(String lgobe) {
		this.lgobe = lgobe;
	}
	
	public LgortBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LgortBean(String werks, String lgort, String lgobe) {
		super();
		this.werks = werks;
		this.lgort = lgort;
		this.lgobe = lgobe;
	}
	@Override
	public String toString() {
		return "LgortBean [werks=" + werks + ", lgort=" + lgort + ", lgobe=" + lgobe + "]";
	}
	
	

}
