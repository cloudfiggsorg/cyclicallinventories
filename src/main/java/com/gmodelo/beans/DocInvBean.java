package com.gmodelo.beans;

public class DocInvBean {

	Integer docInvId;
	String route;
	String bukrs;
	String bukrsD;
	String werks;
	String werksD;
	String type;
	String status;
	String createdBy;
	Integer docFatherInvId;

	public DocInvBean() {
		super();
	}

	public Integer getDocFatherInvId() {
		return docFatherInvId;
	}

	public void setDocFatherInvId(Integer docFatherInvId) {
		this.docFatherInvId = docFatherInvId;
	}

	public Integer getDocInvId() {
		return docInvId;
	}

	public void setDocInvId(Integer docInvId) {
		this.docInvId = docInvId;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getBukrs() {
		return bukrs;
	}

	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}

	public String getBukrsD() {
		return bukrsD;
	}

	public void setBukrsD(String bukrsD) {
		this.bukrsD = bukrsD;
	}

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public String getWerksD() {
		return werksD;
	}

	public void setWerksD(String werksD) {
		this.werksD = werksD;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public DocInvBean(Integer docInvId, String route, String bukrs, String bukrsD, String werks, String werksD,
			String type, String status, String createdBy, Integer docFatherInvId) {
		super();
		this.docInvId = docInvId;
		this.route = route;
		this.bukrs = bukrs;
		this.bukrsD = bukrsD;
		this.werks = werks;
		this.werksD = werksD;
		this.type = type;
		this.status = status;
		this.createdBy = createdBy;
		this.docFatherInvId = docFatherInvId;
	}

	@Override
	public String toString() {
		return "DocInvBean [docInvId=" + docInvId + ", route=" + route + ", bukrs=" + bukrs + ", bukrsD=" + bukrsD
				+ ", werks=" + werks + ", werksD=" + werksD + ", type=" + type + ", status=" + status + ", createdBy="
				+ createdBy + ", docFatherInvId=" + docFatherInvId + "]";
	}

}
