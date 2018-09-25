package com.gmodelo.beans;

public class TaskBean {
	
	int taskId;
	String groupId; 
	DocInvBean docInvId;
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public DocInvBean getDocInvId() {
		return docInvId;
	}
	public void setDocInvId(DocInvBean docInvId) {
		this.docInvId = docInvId;
	}
	
	public TaskBean(int taskId, String groupId, DocInvBean docInvId) {
		super();
		this.taskId = taskId;
		this.groupId = groupId;
		this.docInvId = docInvId;
	}
	@Override
	public String toString() {
		return "TaskBean [taskId=" + taskId + ", groupId=" + groupId + ", docInvId=" + docInvId + "]";
	}
	public TaskBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
