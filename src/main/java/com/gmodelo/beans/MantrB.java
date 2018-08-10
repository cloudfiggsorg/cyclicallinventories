package com.gmodelo.beans;

public class MantrB {
	String werks;
	String matnr;
	String maktx;
	public String getWerks() {
		return werks;
	}
	public void setWerks(String werks) {
		this.werks = werks;
	}
	public String getMatnr() {
		return matnr;
	}
	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}
	public String getMaktx() {
		return maktx;
	}
	public void setMaktx(String maktx) {
		this.maktx = maktx;
	}
	public MantrB() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MantrB(String werks, String matnr, String maktx) {
		super();
		this.werks = werks;
		this.matnr = matnr;
		this.maktx = maktx;
	}
	@Override
	public String toString() {
		return "MantrB [werks=" + werks + ", matnr=" + matnr + ", maktx=" + maktx + "]";
	}
	

}
