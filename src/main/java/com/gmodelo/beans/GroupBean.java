package com.gmodelo.beans;
import java.util.ArrayList;
import java.util.List;

import com.bmore.ume001.beans.User;

public class GroupBean {
	
	String groupId;
	String gdesc;
	List<User> users;
	
	public GroupBean() {
		super();
		this.users= new ArrayList<User>();
		// TODO Auto-generated constructor stub
	}
	
	public GroupBean(String groupId, String groupDesc, List<User> users) {
		super();
		this.groupId = groupId;
		this.gdesc = groupDesc;
		this.users = users;
	}
	
	@Override
	public String toString() {
		return "GroupBean [groupId=" + groupId + ", groupDesc=" + gdesc + ", users=" + users + "]";
	}
	
	public List<User> getUsers() {
		return users;
	}


	public void setUsers(List<User> users) {
		this.users = users;
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
	
	public void setGdesc(String groupDesc) {
		this.gdesc = groupDesc;
	}

	
}
