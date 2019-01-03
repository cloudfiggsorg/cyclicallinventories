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
	private String dCreated; 
	private String modifiedBy;
	private String dModified;
	private List<RoutePositionBean> positions;
	private List<RouteGroupBean> groups;
	
	public RouteBean(){
		super();
		this.positions = new ArrayList<>();
		this.groups = new ArrayList<>();
	}

	public RouteBean(String routeId, String bukrs, String werks, String rdesc, String type, String bdesc, String wdesc,
			String createdBy, String dCreated, String modifiedBy, String dModified, List<RoutePositionBean> positions,
			List<RouteGroupBean> groups) {
		super();
		this.routeId = routeId;
		this.bukrs = bukrs;
		this.werks = werks;
		this.rdesc = rdesc;
		this.type = type;
		this.bdesc = bdesc;
		this.wdesc = wdesc;
		this.createdBy = createdBy;
		this.dCreated = dCreated;
		this.modifiedBy = modifiedBy;
		this.dModified = dModified;
		this.positions = positions;
		this.groups = groups;
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

	public String getdCreated() {
		return dCreated;
	}

	public void setdCreated(String dCreated) {
		this.dCreated = dCreated;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getdModified() {
		return dModified;
	}

	public void setdModified(String dModified) {
		this.dModified = dModified;
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

	@Override
	public String toString() {
		return "RouteBean [routeId=" + routeId + ", bukrs=" + bukrs + ", werks=" + werks + ", rdesc=" + rdesc
				+ ", type=" + type + ", bdesc=" + bdesc + ", wdesc=" + wdesc + ", createdBy=" + createdBy
				+ ", dCreated=" + dCreated + ", modifiedBy=" + modifiedBy + ", dModified=" + dModified + ", positions="
				+ positions + ", groups=" + groups + "]";
	}
		
}
