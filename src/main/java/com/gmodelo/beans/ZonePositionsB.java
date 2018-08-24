package com.gmodelo.beans;

import java.util.HashMap;
import java.util.List;

public class ZonePositionsB {
	String zoneId;
	String positionId;
	String lgtyp;
	String lgpla;
	String secuency;
	String imwm;
	List<ZonePositionMaterialsB> positionMaterial;
	
	public ZonePositionsB() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public List< ZonePositionMaterialsB> getPositionMaterial() {
		return positionMaterial;
	}

	public void setPositionMaterial(List<ZonePositionMaterialsB> positionMaterial) {
		this.positionMaterial = positionMaterial;
	}

	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
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

	public ZonePositionsB(String zoneId, String positionId, String lgtyp, String lgpla, String secuency, String imwm,
			List< ZonePositionMaterialsB> positionMaterial) {
		super();
		this.zoneId = zoneId;
		this.positionId = positionId;
		this.lgtyp = lgtyp;
		this.lgpla = lgpla;
		this.secuency = secuency;
		this.imwm = imwm;
		this.positionMaterial = positionMaterial;
	}

	@Override
	public String toString() {
		return "ZonePositionsB [zoneId=" + zoneId + ", positionId=" + positionId + ", lgtyp=" + lgtyp + ", lgpla="
				+ lgpla + ", secuency=" + secuency + ", imwm=" + imwm + ", positionMaterial=" + positionMaterial + "]";
	}
	public String toKey(String zoneId) {
		return zoneId + this.positionId;
	}

}