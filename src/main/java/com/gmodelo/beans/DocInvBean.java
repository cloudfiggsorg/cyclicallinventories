package com.gmodelo.beans;

import java.util.List;

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
	String modifiedBy;
	Integer docFatherInvId;

	List<DocInvPositionBean> docInvPositions;

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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getDocFatherInvId() {
		return docFatherInvId;
	}

	public void setDocFatherInvId(Integer docFatherInvId) {
		this.docFatherInvId = docFatherInvId;
	}

	public List<DocInvPositionBean> getDocInvPositions() {
		return docInvPositions;
	}

	public void setDocInvPositions(List<DocInvPositionBean> docInvPositions) {
		this.docInvPositions = docInvPositions;
	}

	public DocInvBean(Integer docInvId, String route, String bukrs, String bukrsD, String werks, String werksD,
			String type, String status, String createdBy, String modifiedBy, Integer docFatherInvId,
			List<DocInvPositionBean> docInvPositions) {
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
		this.modifiedBy = modifiedBy;
		this.docFatherInvId = docFatherInvId;
		this.docInvPositions = docInvPositions;
	}

	public DocInvBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "DocInvBean [docInvId=" + docInvId + ", route=" + route + ", bukrs=" + bukrs + ", bukrsD=" + bukrsD
				+ ", werks=" + werks + ", werksD=" + werksD + ", type=" + type + ", status=" + status + ", createdBy="
				+ createdBy + ", modifiedBy=" + modifiedBy + ", docFatherInvId=" + docFatherInvId + ", docInvPositions="
				+ docInvPositions + "]";
	}

	

}
