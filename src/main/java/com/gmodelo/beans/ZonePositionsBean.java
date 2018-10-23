package com.gmodelo.beans;

import java.util.List;

public class ZonePositionsBean {
	int pkAsgId;
	String zoneId;
	String zoneD;
	String lgtyp;
	String lgpla;
	Integer secuency;
	String imwm;
	String lgtypDesc;
	String lgnum;
	List<ZonePositionMaterialsBean> materials;

	public ZonePositionsBean() {
		super();
	}

	public String getZoneD() {
		return zoneD;
	}

	public void setZoneD(String zoneD) {
		this.zoneD = zoneD;
	}

	public String getLgnum() {
		return lgnum;
	}

	public void setLgnum(String lgnum) {
		this.lgnum = lgnum;
	}

	public String getLgtypDesc() {
		return lgtypDesc;
	}

	public void setLgtypDesc(String lgtypDesc) {
		this.lgtypDesc = lgtypDesc;
	}

	public int getPkAsgId() {
		return pkAsgId;
	}

	public void setPkAsgId(int pkAsgId) {
		this.pkAsgId = pkAsgId;
	}

	public List<ZonePositionMaterialsBean> getMaterials() {
		return materials;
	}

	public void setMaterials(List<ZonePositionMaterialsBean> positionMaterial) {
		this.materials = positionMaterial;
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

	public String getImwm() {
		return imwm;
	}

	public void setImwm(String imwm) {
		this.imwm = imwm;
	}

	public Integer getSecuency() {
		return secuency;
	}

	public void setSecuency(Integer secuency) {
		this.secuency = secuency;
	}

	@Override
	public String toString() {
		return "ZonePositionsBean [pkAsgId=" + pkAsgId + ", zoneId=" + zoneId + ", zoneD=" + zoneD + ", lgtyp=" + lgtyp
				+ ", lgpla=" + lgpla + ", secuency=" + secuency + ", imwm=" + imwm + ", lgtypDesc=" + lgtypDesc
				+ ", lgnum=" + lgnum + ", materials=" + materials + "]";
	}

	public ZonePositionsBean(int pkAsgId, String zoneId, String zoneD, String lgtyp, String lgpla, Integer secuency,
			String imwm, String lgtypDesc, String lgnum, List<ZonePositionMaterialsBean> materials) {
		super();
		this.pkAsgId = pkAsgId;
		this.zoneId = zoneId;
		this.zoneD = zoneD;
		this.lgtyp = lgtyp;
		this.lgpla = lgpla;
		this.secuency = secuency;
		this.imwm = imwm;
		this.lgtypDesc = lgtypDesc;
		this.lgnum = lgnum;
		this.materials = materials;
	}

	//
}
