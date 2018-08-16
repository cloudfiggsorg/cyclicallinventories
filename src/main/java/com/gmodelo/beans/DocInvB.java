package com.gmodelo.beans;

public class DocInvB {
	String routeId;
	String bukrs;
	String type;
	String createdBy;
	String justification;
	String docInvId;
	public DocInvB() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DocInvB(String routeId, String bukrs, String type, String createdBy, String justification, String docInvId) {
		super();
		this.routeId = routeId;
		this.bukrs = bukrs;
		this.type = type;
		this.createdBy = createdBy;
		this.justification = justification;
		this.docInvId = docInvId;
	}
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getJustification() {
		return justification;
	}
	public void setJustification(String justification) {
		this.justification = justification;
	}
	public String getDocInvId() {
		return docInvId;
	}
	public void setDocInvId(String docInvId) {
		this.docInvId = docInvId;
	}
	@Override
	public String toString() {
		return "DocInvB [routeId=" + routeId + ", bukrs=" + bukrs + ", type=" + type + ", createdBy=" + createdBy
				+ ", justification=" + justification + ", docInvId=" + docInvId + "]";
	}
	

}
