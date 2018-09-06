package com.gmodelo.beans;

public class GroupToUserBean {
	
	String groupId;
	String userId;
	
	public GroupToUserBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getGroupId() {
		return groupId;
	}
//	public void setGroupId(String groupId) {
//		this.groupId = groupId;
//	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public GroupToUserBean(String groupId, String userId) {
		super();
		this.groupId = groupId;
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		return "GroupToUser [groupId=" + groupId + ", userId=" + userId + "]";
	}

}
