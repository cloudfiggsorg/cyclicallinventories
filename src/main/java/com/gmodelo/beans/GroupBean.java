package com.gmodelo.beans;
import java.util.ArrayList;
import java.util.List;

import com.bmore.ume001.beans.User;

public class GroupBean {
	
	String groupId;
	String gdesc;
	String bukrs;
	String bDesc;
	String werks;
	String wDesc;
	List<User> users;
	
	public GroupBean() {
		super();
		this.users= new ArrayList<User>();
		// TODO Auto-generated constructor stub
	}
	
	

	public GroupBean(String groupId, String gdesc, String bukrs, String bDesc, String werks, String wDesc,
			List<User> users) {
		super();
		this.groupId = groupId;
		this.gdesc = gdesc;
		this.bukrs = bukrs;
		this.bDesc = bDesc;
		this.werks = werks;
		this.wDesc = wDesc;
		this.users = users;
	}


	@Override
	public String toString() {
		return "GroupBean [groupId=" + groupId + ", gdesc=" + gdesc + ", bukrs=" + bukrs + ", bDesc=" + bDesc
				+ ", werks=" + werks + ", wDesc=" + wDesc + ", users=" + users + "]";
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

	public String getBukrs() {
		return bukrs;
	}

	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}

	public String getbDesc() {
		return bDesc;
	}

	public void setbDesc(String bDesc) {
		this.bDesc = bDesc;
	}

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public String getwDesc() {
		return wDesc;
	}

	public void setwDesc(String wDesc) {
		this.wDesc = wDesc;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
		
}
