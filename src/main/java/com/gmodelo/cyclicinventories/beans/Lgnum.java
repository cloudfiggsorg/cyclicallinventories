package com.gmodelo.cyclicinventories.beans;

public class Lgnum {
	
	private String lgnum; //Num almacen
	private String lgort; //Almacen
	private String lnumt; //denominacion num Almacen
	
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
	
	public Lgnum() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Lgnum(String lgnum, String lgort, String lnumt, String werks) {
		super();
		this.lgnum = lgnum;
		this.lgort = lgort;
		this.lnumt = lnumt;
	}
	
	@Override
	public String toString() {
		return "Lgnum [lgnum=" + lgnum + ", lgort=" + lgort + ", lnumt=" + lnumt + "]";
	}
	
}
