package com.gmodelo.beans;

public class RouteB {
	String routeId;
	String bukrs;
	String werks;
	String rdesc;
	String status;
	String modifiedBy;
	String modifiedDate;
	String createdBy;
	String createdDate;
	String positionId;
	String lgort;
	String lgtyp;
	String zoneId;
	String secuency;
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
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
	public String getRdesc() {
		return rdesc;
	}
	public void setRdesc(String rdesc) {
		this.rdesc = rdesc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getLgort() {
		return lgort;
	}
	public void setLgort(String lgort) {
		this.lgort = lgort;
	}
	public String getLgtyp() {
		return lgtyp;
	}
	public void setLgtyp(String lgtyp) {
		this.lgtyp = lgtyp;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getSecuency() {
		return secuency;
	}
	public void setSecuency(String secuency) {
		this.secuency = secuency;
	}
	public RouteB() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RouteB(String routeId, String bukrs, String werks, String rdesc, String status, String modifiedBy,
			String modifiedDate, String createdBy, String createdDate, String positionId, String lgort, String lgtyp,
			String zoneId, String secuency) {
		super();
		this.routeId = routeId;
		this.bukrs = bukrs;
		this.werks = werks;
		this.rdesc = rdesc;
		this.status = status;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.positionId = positionId;
		this.lgort = lgort;
		this.lgtyp = lgtyp;
		this.zoneId = zoneId;
		this.secuency = secuency;
	}
	@Override
	public String toString() {
		return "RouteB [routeId=" + routeId + ", bukrs=" + bukrs + ", werks=" + werks + ", rdesc=" + rdesc + ", status="
				+ status + ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", positionId=" + positionId + ", lgort=" + lgort + ", lgtyp="
				+ lgtyp + ", zoneId=" + zoneId + ", secuency=" + secuency + "]";
	}
	
	
}
