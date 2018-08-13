package com.gmodelo.beans;

public class ZoneB {
	String zoneId;
	String zdesc;
	String bukrs;
	String werks;
	String lgort;
	String created_by;
	String created_date;
	String positionId;
	String lgtyp;
	String lgpla;
	String secuency;
	
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getZdesc() {
		return zdesc;
	}
	public void setZdesc(String zdesc) {
		this.zdesc = zdesc;
	}
	public String getBukrs() {
		return bukrs;
	}
	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}
	public String getWerks() {
		return werks;
	}
	public void setWerks(String werks) {
		this.werks = werks;
	}
	public String getLgort() {
		return lgort;
	}
	public void setLgort(String lgort) {
		this.lgort = lgort;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
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
	public ZoneB() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ZoneB(String zoneId, String zdesc, String bukrs, String werks, String lgort, String created_by,
			String created_date, String positionId, String lgtyp, String lgpla, String secuency) {
		super();
		this.zoneId = zoneId;
		this.zdesc = zdesc;
		this.bukrs = bukrs;
		this.werks = werks;
		this.lgort = lgort;
		this.created_by = created_by;
		this.created_date = created_date;
		this.positionId = positionId;
		this.lgtyp = lgtyp;
		this.lgpla = lgpla;
		this.secuency = secuency;
	}
	@Override
	public String toString() {
		return "ZoneB [zoneId=" + zoneId + ", zdesc=" + zdesc + ", bukrs=" + bukrs + ", werks=" + werks + ", lgort="
				+ lgort + ", created_by=" + created_by + ", created_date=" + created_date + ", positionId=" + positionId
				+ ", lgtyp=" + lgtyp + ", lgpla=" + lgpla + ", secuency=" + secuency + "]";
	}
	
}
