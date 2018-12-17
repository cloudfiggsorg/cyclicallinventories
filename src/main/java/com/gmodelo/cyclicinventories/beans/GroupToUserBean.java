package com.gmodelo.cyclicinventories.beans;

public class GroupToUserBean {
	
	private String groupId;
	private String userId;
	
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
