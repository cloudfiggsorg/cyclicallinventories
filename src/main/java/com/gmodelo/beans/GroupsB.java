package com.gmodelo.beans;

public class GroupsB {
	String groupId;
	String gdes;
	String gtype;
	String createBy;
	String createdDate;
	
	public GroupsB() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GroupsB(String groupId, String gdes, String gtype, String createBy, String createdDate) {
		super();
		this.groupId = groupId;
		this.gdes = gdes;
		this.gtype = gtype;
		this.createBy = createBy;
		this.createdDate = createdDate;
	}
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGdes() {
		return gdes;
	}
	public void setGdes(String gdes) {
		this.gdes = gdes;
	}
	public String getGtype() {
		return gtype;
	}
	public void setGtype(String gtype) {
		this.gtype = gtype;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	@Override
	public String toString() {
		return "GroupsB [groupId=" + groupId + ", gdes=" + gdes + ", gtype=" + gtype + ", createBy=" + createBy
				+ ", createdDate=" + createdDate + "]";
	}
	
}
