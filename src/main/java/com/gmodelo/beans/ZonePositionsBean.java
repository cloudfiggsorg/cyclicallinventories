package com.gmodelo.beans;

import java.util.List;

public class ZonePositionsBean {
	int pkAsgId;
	String zoneId;
	String lgtyp;
	String lgpla;
	String secuency;
	String imwm;
	
	List<ZonePositionMaterialsBean> positionMaterial;
	
	public ZonePositionsBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getPkAsgId() {
		return pkAsgId;
	}

	public void setPkAsgId(int pkAsgId) {
		this.pkAsgId = pkAsgId;
	}

	public List< ZonePositionMaterialsBean> getPositionMaterial() {
		return positionMaterial;
	}
	
	public void setPositionMaterial(List<ZonePositionMaterialsBean> positionMaterial) {
		this.positionMaterial = positionMaterial;
	}

	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getLgtyp() {
		return lgtyp;
	}
	public void setLgtyp(String lgtyp) {
		this.lgtyp = lgtyp;
	}
	public String getLgpla() {
		return lgpla;
	}
	public void setLgpla(String lgpla) {
		this.lgpla = lgpla;
	}
	public String getSecuency() {
		return secuency;
	}
	public void setSecuency(String secuency) {
		this.secuency = secuency;
	}
	public String getImwm() {
		return imwm;
	}
	public void setImwm(String imwm) {
		this.imwm = imwm;
	}

	public ZonePositionsBean(int pkAsgId, String zoneId, String lgtyp, String lgpla, String secuency,
			String imwm, List<ZonePositionMaterialsBean> positionMaterial) {
		super();
		this.pkAsgId = pkAsgId;
		this.zoneId = zoneId;
		this.lgtyp = lgtyp;
		this.lgpla = lgpla;
		this.secuency = secuency;
		this.imwm = imwm;
		this.positionMaterial = positionMaterial;
	}
	
	@Override
	public String toString() {
		return "ZonePositionsB [pkAsgId=" + pkAsgId + ", zoneId=" + zoneId + ", lgtyp="
				+ lgtyp + ", lgpla=" + lgpla + ", secuency=" + secuency + ", imwm=" + imwm + ", positionMaterial="
				+ positionMaterial + "]";
	}
	

}
