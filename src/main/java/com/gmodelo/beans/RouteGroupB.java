package com.gmodelo.beans;

public class RouteGroupB {
	String groupId;
	String gdesc;
	String countNum;
	public RouteGroupB() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RouteGroupB(String groupId, String gdesc, String countNum) {
		super();
		this.groupId = groupId;
		this.gdesc = gdesc;
		this.countNum = countNum;
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
		return "RouteGroupB [groupId=" + groupId + ", gdesc=" + gdesc + ", countNum=" + countNum + "]";
	}
		      

}
