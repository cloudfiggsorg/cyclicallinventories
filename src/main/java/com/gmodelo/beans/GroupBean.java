package com.gmodelo.beans;

public class GroupBean {
	String groupId;
	String groupDesc;
	String groupType;
	
	public GroupBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GroupBean(String groupId, String groupDesc, String groupType) {
		super();
		this.groupId = groupId;
		this.groupDesc = groupDesc;
		this.groupType = groupType;
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
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	@Override
	public String toString() {
		return "GroupBean [groupId=" + groupId + ", groupDesc=" + groupDesc + ", groupType=" + groupType + "]";
	}
	
}
