package com.gmodelo.beans;

import java.util.ArrayList;
import java.util.List;

import com.bmore.ume001.beans.User;

public class RouteGroupBean {
	
	int routeGroup;
	String groupId;
	String gdesc;
	String countNum;
	List<User> users;
	
	public RouteGroupBean() {
		super();
		this.users = new ArrayList<User>();
	}
	
	public RouteGroupBean(int routeGroup, String groupId, String gdesc, String countNum, List<User> users) {
		super();
		this.routeGroup = routeGroup;
		this.groupId = groupId;
		this.gdesc = gdesc;
		this.countNum = countNum;
		this.users = users;
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

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	@Override
	public String toString() {
		return "RouteGroupBean [routeGroup=" + routeGroup + ", groupId=" + groupId + ", gdesc=" + gdesc + ", countNum="
				+ countNum + ", users=" + users + "]";
	}
}
