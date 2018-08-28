package com.gmodelo.beans;

public class ZonePositionMaterialsBean {
	
	String posMat;
	String matnr;
	String typMat;
	String descTM;
	
	public String getPkPosMat() {
		return posMat;
	}
	public void setPkPosMat(String pkPosMat) {
		this.posMat = pkPosMat;
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
	public ZonePositionMaterialsBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ZonePositionMaterialsBean(String pkPosMat, String matnr, String typMat, String descTM) {
		super();
		this.posMat = pkPosMat;
		this.matnr = matnr;
		this.typMat = typMat;
		this.descTM = descTM;
	}
	@Override
	public String toString() {
		return "ZonePositionMaterialsB [pkPosMat=" + posMat + ", matnr=" + matnr + ", typMat=" + typMat + ", descTM="
				+ descTM + "]";
	}
	
}
