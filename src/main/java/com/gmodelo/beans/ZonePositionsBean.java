package com.gmodelo.beans;

import java.util.ArrayList;
import java.util.List;

public class ZonePositionsBean {
	int pkAsgId;
	String zoneId;
	String lgtyp;
	String lgpla;
	String secuency;
	String imwm;
	String lgtypDesc;
	String lgnum;
	List<ZonePositionMaterialsBean> materials;
	
	public ZonePositionsBean() {		
		super();
		this.materials = new ArrayList<ZonePositionMaterialsBean>();		
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

	public List< ZonePositionMaterialsBean> getMaterials() {
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
	@Override
	public String toString() {
		return "ZonePositionsBean [pkAsgId=" + pkAsgId + ", zoneId=" + zoneId + ", lgtyp=" + lgtyp + ", lgpla=" + lgpla
				+ ", secuency=" + secuency + ", imwm=" + imwm + ", lgtypDesc=" + lgtypDesc + ", lgnum=" + lgnum
				+ ", materials=" + materials + "]";
	}
	public ZonePositionsBean(int pkAsgId, String zoneId, String lgtyp, String lgpla, String secuency, String imwm,
			String lgtypDesc, String lgnum, List<ZonePositionMaterialsBean> materials) {
		super();
		this.pkAsgId = pkAsgId;
		this.zoneId = zoneId;
		this.lgtyp = lgtyp;
		this.lgpla = lgpla;
		this.secuency = secuency;
		this.imwm = imwm;
		this.lgtypDesc = lgtypDesc;
		this.lgnum = lgnum;
		this.materials = materials;
	}
}
