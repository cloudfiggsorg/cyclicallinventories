package com.gmodelo.cyclicinventories.beans;

import java.util.ArrayList;
import java.util.List;

public class ReporteCalidadBean {
	
	private String docInvId;
	private String taskId;
	private String groupId;
	private String userId;
	private String dateDowload;
	private String dateUpload;
	private List<ReporteCalidadConteosBean> conteos;
	
	public String getDocInvId() {
		return docInvId;
	}
	public void setDocInvId(String docInvId) {
		this.docInvId = docInvId;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDateDowload() {
		return dateDowload;
	}
	public void setDateDowload(String dateDowload) {
		this.dateDowload = dateDowload;
	}
	public String getDateUpload() {
		return dateUpload;
	}
	public void setDateUpload(String dateUpload) {
		this.dateUpload = dateUpload;
	}
	
	public List<ReporteCalidadConteosBean> getConteos() {
		return conteos;
	}
	public void setConteos(List<ReporteCalidadConteosBean> conteos) {
		this.conteos = conteos;
	}
	public ReporteCalidadBean() {
		super();
		conteos = new ArrayList<>();
		// TODO Auto-generated constructor stub
	}
	public ReporteCalidadBean(String docInvId, String taskId, String groupId, String userId, String dateDowload,
			String dateUpload, List<ReporteCalidadConteosBean> conteos) {
		super();
		this.docInvId = docInvId;
		this.taskId = taskId;
		this.groupId = groupId;
		this.userId = userId;
		this.dateDowload = dateDowload;
		this.dateUpload = dateUpload;
		this.conteos = conteos;
	}
	@Override
	public String toString() {
		return "ReporteCalidadCabeceraBean [docInvId=" + docInvId + ", taskId=" + taskId + ", groupId=" + groupId
				+ ", userId=" + userId + ", dateDowload=" + dateDowload + ", dateUpload=" + dateUpload + ", conteos="
				+ conteos + "]";
	}

}
