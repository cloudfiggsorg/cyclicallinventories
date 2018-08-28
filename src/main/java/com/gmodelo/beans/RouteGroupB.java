package com.gmodelo.beans;

public class RouteGroupB {
	
	String routeGroup;
	String groupId;
	String gdesc;
	String countNum;
	
	public RouteGroupB() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RouteGroupB(String pkRouteGroup, String groupId, String gdesc, String countNum) {
		super();
		this.routeGroup = pkRouteGroup;
		this.groupId = groupId;
		this.gdesc = gdesc;
		this.countNum = countNum;
	}
	
	public String getPkRouteGroup() {
		return routeGroup;
	}
	public void setPkRouteGroup(String pkRouteGroup) {
		this.routeGroup = pkRouteGroup;
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
		return "RouteGroupB [pkRouteGroup=" + routeGroup + ", groupId=" + groupId + ", gdesc=" + gdesc + ", countNum="
				+ countNum + "]";
	}
	
}
