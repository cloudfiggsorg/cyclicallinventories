package com.gmodelo.beans;

public class ZonePositionMaterialsBean {
	
	int zoneId;
	int posMat;
	String matnr;
	String descM;
	
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
	public ZonePositionMaterialsBean(int zoneId, int posMat, String matnr, String descTM) {
		super();
		this.zoneId = zoneId;
		this.posMat = posMat;
		this.matnr = matnr;
		this.descM = descTM;
	}
	
	@Override
	public String toString() {
		return "ZonePositionMaterialsBean [zoneId=" + zoneId + ", posMat=" + posMat + ", matnr=" + matnr + ", descTM=" + descM + "]";
	}
	
}
