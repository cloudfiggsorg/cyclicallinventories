package com.gmodelo.beans;

public class ZonePositionMaterialsBean {
	
	int zoneId;
	int posMat;
	String matnr;
	String typMat;
	String descTM;
	
	public int getZoneId() {
		return zoneId;
	}
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}
	public int getPosMat() {
		return posMat;
	}
	public void setPosMat(int posMat) {
		this.posMat = posMat;
	}
	public int getPkPosMat() {
		return posMat;
	}
	public void setPkPosMat(int pkPosMat) {
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
	public ZonePositionMaterialsBean(int zoneId, int posMat, String matnr, String typMat, String descTM) {
		super();
		this.zoneId = zoneId;
		this.posMat = posMat;
		this.matnr = matnr;
		this.typMat = typMat;
		this.descTM = descTM;
	}
	
	@Override
	public String toString() {
		return "ZonePositionMaterialsBean [zoneId=" + zoneId + ", posMat=" + posMat + ", matnr=" + matnr + ", typMat="
				+ typMat + ", descTM=" + descTM + "]";
	}
	
}
