package com.gmodelo.beans;

import java.util.ArrayList;
import java.util.List;

public class ConciliacionBean {
	String route;
	String type;
	String bukrs;
	String bukrsD;
	String werks;
	String werksD;
	String justification;
	int docInvId;
	List<ConciliationPositionBean> positions;
	
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public List<ConciliationPositionBean> getPositions() {
		return positions;
	}
	public void setPositions(List<ConciliationPositionBean> positions) {
		this.positions = positions;
	}
	
	public ConciliacionBean(String route, String type, String bukrs, String bukrsD, String werks, String werksD,
			String justification, int docInvId, List<ConciliationPositionBean> positions) {
		super();
		this.route = route;
		this.type = type;
		this.bukrs = bukrs;
		this.bukrsD = bukrsD;
		this.werks = werks;
		this.werksD = werksD;
		this.justification = justification;
		this.docInvId = docInvId;
		this.positions = positions;
	}
	public ConciliacionBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ConciliacionBean [route=" + route + ", type=" + type + ", bukrs=" + bukrs + ", bukrsD=" + bukrsD
				+ ", werks=" + werks + ", werksD=" + werksD + ", justification=" + justification + ", docInvId="
				+ docInvId + ", positions=" + positions + "]";
	}
	
	
	
	
}
