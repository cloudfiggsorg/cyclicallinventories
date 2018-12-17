package com.gmodelo.cyclicinventories.beans;

public class ZonePositionMaterialsBean {
	
	private int posMat;
	private String matnr;
	private String descM;
	
	public int getPosMat() {
		return posMat;
	}
	public void setPosMat(int posMat) {
		this.posMat = posMat;
	}
	public String getMatnr() {
		return matnr;
	}
	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}
	public String getDescM() {
		return descM;
	}
	public void setDescM(String descM) {
		this.descM = descM;
	}
	public ZonePositionMaterialsBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ZonePositionMaterialsBean(int posMat, String matnr, String descTM) {
		super();
		this.posMat = posMat;
		this.matnr = matnr;
		this.descM = descTM;
	}
	
	@Override
	public String toString() {
		return "ZonePositionMaterialsBean [posMat=" + posMat + ", matnr=" + matnr + ", descTM=" + descM + "]";
	}
	
}
