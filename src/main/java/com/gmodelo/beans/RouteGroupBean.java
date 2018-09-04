package com.gmodelo.beans;

public class RouteGroupBean {
	
	int routeGroup;
	String groupId;
	String gdesc;
	String countNum;
	
	public RouteGroupBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public RouteGroupBean(int routeGroup, String groupId, String gdesc, String countNum) {
		super();
		this.routeGroup = routeGroup;
		this.groupId = groupId;
		this.gdesc = gdesc;
		this.countNum = countNum;
	}
	
	public RouteGroupBean( String groupId, String gdesc, String countNum) {
		super();
		this.groupId = groupId;
		this.gdesc = gdesc;
		this.countNum = countNum;
	}

	public int getRouteGroup() {
		return routeGroup;
	}
	public void setRouteGroup(int routeGroup) {
		this.routeGroup = routeGroup;
	}	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGdesc() {
		return gdesc;
	}
	public void setGdesc(String gdesc) {
		this.gdesc = gdesc;
	}
	public String getCountNum() {
		return countNum;
	}
	public void setCountNum(String countNum) {
		this.countNum = countNum;
	}

	@Override
	public String toString() {
		return "RouteGroupBean [routeGroup=" + routeGroup + ", groupId=" + groupId + ", gdesc="
				+ gdesc + ", countNum=" + countNum + "]";
	}
	
	
}
