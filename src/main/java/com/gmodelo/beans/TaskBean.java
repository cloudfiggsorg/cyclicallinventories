package com.gmodelo.beans;

public class TaskBean {
	
	String taskId;
	String groupId; 
	DocInvBean docInvId;
	String taskIdFather;
	
	public TaskBean(){
		super();
	}
	
	public TaskBean(String taskId, String groupId, DocInvBean docInvId, String taskIdFather) {
		super();
		this.taskId = taskId;
		this.groupId = groupId;
		this.docInvId = docInvId;
		this.taskIdFather = taskIdFather;
	}

	@Override
	public String toString() {
		return "TaskBean [taskId=" + taskId + ", groupId=" + groupId + ", docInvId=" + docInvId + ", taskIdFather="
				+ taskIdFather + "]";
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
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

	public String getTaskIdFather() {
		return taskIdFather;
	}

	public void setTaskIdFather(String taskIdFather) {
		this.taskIdFather = taskIdFather;
	}
	
}
