package com.gmodelo.beans;

import java.util.ArrayList;
import java.util.List;

public class DocInvBean {
	RouteBean route;
	String bukrs;
	String bukrsD;
	String werks;
	String werksD;
	String type;
	String createdBy;
	String justification;
	int docInvId;
	List<DocInvPositionBean> positions;
	
	public DocInvBean() {
		super();
		positions = new ArrayList<DocInvPositionBean>();
		route = null;
		// TODO Auto-generated constructor stub
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

	public RouteBean getRoute() {
		return route;
	}
	public void setRouteId(RouteBean route) {
		this.route = route;
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
	public int getDocInvId() {
		return docInvId;
	}
	public void setDocInvId(int docInvId) {
		this.docInvId = docInvId;
	}
	
	public List<DocInvPositionBean> getPositions() {
		return positions;
	}
	public void setPositions(List<DocInvPositionBean> positions) {
		this.positions = positions;
	}
	public void setRoute(RouteBean route) {
		this.route = route;
	}

	@Override
	public String toString() {
		return "DocInvBean [route=" + route + ", bukrs=" + bukrs + ", werks=" + werks + ", type=" + type
				+ ", createdBy=" + createdBy + ", justification=" + justification + ", docInvId=" + docInvId
				+ ", positions=" + positions + "]";
	}
	
}
