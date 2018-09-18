package com.gmodelo.beans;

public class TaskBean {
	
	int taskId;
	String groupId; 
	int docInvId;
	int count;
	
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
	public int getDocInvId() {
		return docInvId;
	}
	public void setDocInvId(int docInvId) {
		this.docInvId = docInvId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public TaskBean(int taskId, String groupId, int docInvId, int count) {
		super();
		this.taskId = taskId;
		this.groupId = groupId;
		this.docInvId = docInvId;
		this.count = count;
	}
	@Override
	public String toString() {
		return "TaskBean [taskId=" + taskId + ", groupId=" + groupId + ", docInvId=" + docInvId + ", count=" + count
				+ "]";
	}
	public TaskBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
