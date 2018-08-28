package com.gmodelo.beans;

public class GroupBean {
	String groupId;
	String groupDesc;
	
	public GroupBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GroupBean(String groupId, String groupDesc) {
		super();
		this.groupId = groupId;
		this.groupDesc = groupDesc;
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
		return "GroupBean [groupId=" + groupId + ", groupDesc=" + groupDesc + "]";
	}
	
}
