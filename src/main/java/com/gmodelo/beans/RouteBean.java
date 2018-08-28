package com.gmodelo.beans;

import java.util.List;

public class RouteBean {
	String routeId;
	String bukrs;
	String werks;
	String rdesc;
	String type;
	List<RoutePositionB> positions;
	List<RouteGroupB> groups;
	
	public List<RouteGroupB> getGroups() {
		return groups;
	}
	public void setGroups(List<RouteGroupB> groups) {
		this.groups = groups;
	}
	public List<RoutePositionB> getPositions() {
		return positions;
	}
	public void setPositions(List<RoutePositionB> positions) {
		this.positions = positions;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	
	public RouteBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RouteBean(String routeId, String bukrs, String werks, String rdesc, String type, List<RoutePositionB> positions,
			List<RouteGroupB> groups) {
		super();
		this.routeId = routeId;
		this.bukrs = bukrs;
		this.werks = werks;
		this.rdesc = rdesc;
		this.type = type;
		this.positions = positions;
		this.groups = groups;
	}
	@Override
	public String toString() {
		return "RouteB [routeId=" + routeId + ", bukrs=" + bukrs + ", werks=" + werks + ", rdesc=" + rdesc + ", type="
				+ type + ", positions=" + positions + ", groups=" + groups + "]";
	}
	
	
	
		
}
