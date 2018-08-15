package com.gmodelo.beans;

public class NgortB {
	String werks;
	String lgort;
	String lgnum;
	String lnumt;
	String imwm;
	
	public NgortB() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public NgortB(String werks, String lgort, String lgnum, String lnumt, String imwm) {
		super();
		this.werks = werks;
		this.lgort = lgort;
		this.lgnum = lgnum;
		this.lnumt = lnumt;
		this.imwm = imwm;
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
	public String getLgnum() {
		return lgnum;
	}
	public void setLgnum(String lgnum) {
		this.lgnum = lgnum;
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
	@Override
	public String toString() {
		return "Ngort [werks=" + werks + ", lgort=" + lgort + ", lgnum=" + lgnum + ", lnumt=" + lnumt + ", imwm=" + imwm
				+ "]";
	}
	

}
