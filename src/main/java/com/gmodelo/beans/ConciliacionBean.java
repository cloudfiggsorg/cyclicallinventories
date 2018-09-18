package com.gmodelo.beans;

import java.util.ArrayList;
import java.util.List;

public class ConciliacionBean {
	String route;
	String bukrs;
	String bukrsD;
	String werks;
	String werksD;
	String justification;
	int docInvId;
	List<DocInvPositionBean> positions;
	
	public ConciliacionBean() {
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

	public String getRoute() {
		return route;
	}
	public void setRouteId(String route) {
		this.route = route;
	}
	public String getBukrs() {
		return bukrs;
	}
	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
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
	public void setRoute(String route) {
		this.route = route;
	}

	@Override
	public String toString() {
		return "ConciliacionBean [route=" + route + ", bukrs=" + bukrs + ", bukrsD=" + bukrsD + ", werks=" + werks
				+ ", werksD=" + werksD + ", justification=" + justification + ", docInvId=" + docInvId + ", positions="
				+ positions + "]";
	}

	
	
}
