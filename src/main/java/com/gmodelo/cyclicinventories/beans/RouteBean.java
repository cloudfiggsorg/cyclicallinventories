package com.gmodelo.cyclicinventories.beans;

import java.util.ArrayList;
import java.util.List;

public class RouteBean {
	
	private String routeId;
	private String bukrs;
	private String werks;
	private String rdesc;
	private String type;
	private String bdesc;
	private String wdesc;	
	private String createdBy;
	private String modifiedBy;
	private List<RoutePositionBean> positions;
	private List<RouteGroupBean> groups;
	
	public RouteBean(){
		super();
		this.positions = new ArrayList<RoutePositionBean>();
		this.groups = new ArrayList<RouteGroupBean>();
	}
	
	public RouteBean(String routeId, String bukrs, String werks, String rdesc, String type, String bdesc, String wdesc,
			String createdBy, String modifiedBy, List<RoutePositionBean> positions, List<RouteGroupBean> groups) {
		super();
		this.routeId = routeId;
		this.bukrs = bukrs;
		this.werks = werks;
		this.rdesc = rdesc;
		this.type = type;
		this.bdesc = bdesc;
		this.wdesc = wdesc;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.positions = positions;
		this.groups = groups;
	}

	@Override
	public String toString() {
		return "RouteBean [routeId=" + routeId + ", bukrs=" + bukrs + ", werks=" + werks + ", rdesc=" + rdesc
				+ ", type=" + type + ", bdesc=" + bdesc + ", wdesc=" + wdesc + ", createdBy=" + createdBy
				+ ", modifiedBy=" + modifiedBy + ", positions=" + positions + ", groups=" + groups + "]";
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBdesc() {
		return bdesc;
	}

	public void setBdesc(String bdesc) {
		this.bdesc = bdesc;
	}

	public String getWdesc() {
		return wdesc;
	}

	public void setWdesc(String wdesc) {
		this.wdesc = wdesc;
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

	public List<RoutePositionBean> getPositions() {
		return positions;
	}

	public void setPositions(List<RoutePositionBean> positions) {
		this.positions = positions;
	}

	public List<RouteGroupBean> getGroups() {
		return groups;
	}

	public void setGroups(List<RouteGroupBean> groups) {
		this.groups = groups;
	}
	
}
