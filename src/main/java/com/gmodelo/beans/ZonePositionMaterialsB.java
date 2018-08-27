package com.gmodelo.beans;

public class ZonePositionMaterialsB {
	String pkPosMat;
	String matnr;
	String typMat;
	String descTM;
	
	public String getPkPosMat() {
		return pkPosMat;
	}
	public void setPkPosMat(String pkPosMat) {
		this.pkPosMat = pkPosMat;
	}
	public String getMatnr() {
		return matnr;
	}
	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}
	public String getTypMat() {
		return typMat;
	}
	public void setTypMat(String typMat) {
		this.typMat = typMat;
	}
	public String getDescTM() {
		return descTM;
	}
	public void setDescTM(String descTM) {
		this.descTM = descTM;
	}
	public ZonePositionMaterialsB() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ZonePositionMaterialsB(String pkPosMat, String matnr, String typMat, String descTM) {
		super();
		this.pkPosMat = pkPosMat;
		this.matnr = matnr;
		this.typMat = typMat;
		this.descTM = descTM;
	}
	@Override
	public String toString() {
		return "ZonePositionMaterialsB [pkPosMat=" + pkPosMat + ", matnr=" + matnr + ", typMat=" + typMat + ", descTM="
				+ descTM + "]";
	}
	
}
