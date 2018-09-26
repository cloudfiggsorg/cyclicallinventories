package com.gmodelo.beans;

public class TaskBean {
	
	String taskId;
	String groupId; 
	DocInvBean docInvId;
	String taskJSON;
	long dCreated;
	long dDownlad;
	long dUpload;
	boolean status;
	String taskIdFather;	
	
	public TaskBean(){
		super();
	}
	
	public TaskBean(String taskId, String groupId, DocInvBean docInvId, String taskJSON, long dCreated, long dDownlad,
			long dUpload, boolean status, String taskIdFather) {
		super();
		this.taskId = taskId;
		this.groupId = groupId;
		this.docInvId = docInvId;
		this.taskJSON = taskJSON;
		this.dCreated = dCreated;
		this.dDownlad = dDownlad;
		this.dUpload = dUpload;
		this.status = status;
		this.taskIdFather = taskIdFather;
	}

	@Override
	public String toString() {
		return "TaskBean [taskId=" + taskId + ", groupId=" + groupId + ", docInvId=" + docInvId + ", taskJSON="
				+ taskJSON + ", dCreated=" + dCreated + ", dDownlad=" + dDownlad + ", dUpload=" + dUpload + ", status="
				+ status + ", taskIdFather=" + taskIdFather + "]";
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

	public String getTaskJSON() {
		return taskJSON;
	}

	public void setTaskJSON(String taskJSON) {
		this.taskJSON = taskJSON;
	}

	public long getdCreated() {
		return dCreated;
	}

	public void setdCreated(long dCreated) {
		this.dCreated = dCreated;
	}

	public long getdDownlad() {
		return dDownlad;
	}

	public void setdDownlad(long dDownlad) {
		this.dDownlad = dDownlad;
	}

	public long getdUpload() {
		return dUpload;
	}

	public void setdUpload(long dUpload) {
		this.dUpload = dUpload;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getTaskIdFather() {
		return taskIdFather;
	}

	public void setTaskIdFather(String taskIdFather) {
		this.taskIdFather = taskIdFather;
	}
	
}
