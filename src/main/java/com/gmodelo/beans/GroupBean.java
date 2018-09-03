package com.gmodelo.beans;
import java.util.ArrayList;
import java.util.List;

import com.bmore.ume001.beans.User;

public class GroupBean {
	String groupId;
	String groupDesc;
	List<User> users;
	
	public GroupBean() {
		super();
		this.users= new ArrayList<User>();
		// TODO Auto-generated constructor stub
	}
	

	public GroupBean(String groupId, String groupDesc, List<User> users) {
		super();
		this.groupId = groupId;
		this.groupDesc = groupDesc;
		this.users = users;
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
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}


	@Override
	public String toString() {
		return "GroupBean [groupId=" + groupId + ", groupDesc=" + groupDesc + ", users=" + users + "]";
	}

	
}
