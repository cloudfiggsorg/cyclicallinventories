package com.gmodelo.cyclicinventories.beans;

public class LgortBeanView {
	
	private String werks; //centro
	private String lgort; //almacen
	private String lgobe; // denominacion almacen
	private String lnumt; // Descripción del no. De almacén
	private String imwm; // Tipo de almacén
	private String lgNum; 
	
	public LgortBeanView(){
		super();
	}

	public LgortBeanView(String werks, String lgort, String lgobe, String lnumt, String imwm, String lgNum) {
		super();
		this.werks = werks;
		this.lgort = lgort;
		this.lgobe = lgobe;
		this.lnumt = lnumt;
		this.imwm = imwm;
		this.lgNum = lgNum;
	}

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

	public String getLnumt() {
		return lnumt;
	}

	public void setLnumt(String lnumt) {
		this.lnumt = lnumt;
	}

	public String getImwm() {
		return imwm;
	}

	public void setImwm(String imwm) {
		this.imwm = imwm;
	}

	public String getLgNum() {
		return lgNum;
	}

	public void setLgNum(String lgNum) {
		this.lgNum = lgNum;
	}
	
}
