package com.gmodelo.beans;

public class GroupToRouteBean {

	String routeId;
	String groupId;
	String countNum;
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getCountNum() {
		return countNum;
	}
	public void setCountNum(String countNum) {
		this.countNum = countNum;
	}
	public GroupToRouteBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public GroupToRouteBean(String routeId, String groupId, String countNum) {
		super();
		this.routeId = routeId;
		this.groupId = groupId;
		this.countNum = countNum;
	}
	@Override
	public String toString() {
		return "GroupToUser [routeId=" + routeId + ", groupId=" + groupId + ", countNum=" + countNum + "]";
	}
	
	
}
