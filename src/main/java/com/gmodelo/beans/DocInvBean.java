package com.gmodelo.beans;

public class DocInvBean {
	String route;
	String bukrs;
	String bukrsD;
	String werks;
	String werksD;
	String createdBy;
	int docInvId;

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public DocInvBean() {
		super();
	}
	
	public String getBukrsD() {
		return bukrsD;
	}

	public void setBukrsD(String bukrsD) {
		this.bukrsD = bukrsD;
	}

	public String getWerksD() {
		return werksD;
	}

	public void setWerksD(String werksD) {
		this.werksD = werksD;
	}

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}
	public String getBukrs() {
		return bukrs;
	}
	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public int getDocInvId() {
		return docInvId;
	}
	public void setDocInvId(int docInvId) {
		this.docInvId = docInvId;
	}
}
