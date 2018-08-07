package com.gmodelo.beans;

public class Lgnum {
	
	String lgnum; //Num almacen
	String lgort; //Almacen
	String lnumt; //denominacion num Almacen
	String werks; //centro
	
	public String getLgnum() {
		return lgnum;
	}
	public void setLgnum(String lgnum) {
		this.lgnum = lgnum;
	}
	public String getLgort() {
		return lgort;
	}
	public void setLgort(String lgort) {
		this.lgort = lgort;
	}
	public String getLnumt() {
		return lnumt;
	}
	public void setLnumt(String lnumt) {
		this.lnumt = lnumt;
	}
	public String getWerks() {
		return werks;
	}
	public void setWerks(String werks) {
		this.werks = werks;
	}
	
	public Lgnum() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Lgnum(String lgnum, String lgort, String lnumt, String werks) {
		super();
		this.lgnum = lgnum;
		this.lgort = lgort;
		this.lnumt = lnumt;
		this.werks = werks;
	}
	
	@Override
	public String toString() {
		return "Lgnum [lgnum=" + lgnum + ", lgort=" + lgort + ", lnumt=" + lnumt + ", werks=" + werks + "]";
	}
	
	
	

}
