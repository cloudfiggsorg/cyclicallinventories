package com.gmodelo.beans;

public class TmatnrBean {
	
	String matnr;
	String maktx;
	public String getMaktx() {
		return maktx;
	}
	public void setMaktx(String maktx) {
		this.maktx = maktx;
	}
	String typ_mat;
	String den_typ_mat;
	
	public String getMatnr() {
		return matnr;
	}
	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}
	public String getTyp_mat() {
		return typ_mat;
	}
	public void setTyp_mat(String typ_mat) {
		this.typ_mat = typ_mat;
	}
	public String getDen_typ_mat() {
		return den_typ_mat;
	}
	public void setDen_typ_mat(String den_typ_mat) {
		this.den_typ_mat = den_typ_mat;
	}
	public TmatnrBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TmatnrBean(String matnr, String maktx, String typ_mat, String den_typ_mat) {
		super();
		this.matnr = matnr;
		this.maktx = maktx;
		this.typ_mat = typ_mat;
		this.den_typ_mat = den_typ_mat;
	}
	@Override
	public String toString() {
		return "TmatnrBean [matnr=" + matnr + ", maktx=" + maktx + ", typ_mat=" + typ_mat + ", den_typ_mat="
				+ den_typ_mat + "]";
	}
	
	
}
