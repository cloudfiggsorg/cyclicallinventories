package com.gmodelo.cyclicinventories.beans;

import java.util.List;

public class ReporteDocInvBeanHeader {

	private Integer docInvId;
	private String route;
	private String bukrs;
	private String bukrsD;
	private String werks;
	private String werksD;
	private String type;
	private String creationDate;
	private String conciliationDate;
	private List<ReporteDocInvBean> docInvPosition;

	public Integer getDocInvId() {
		return docInvId;
	}

	public void setDocInvId(Integer docInvId) {
		this.docInvId = docInvId;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getBukrs() {
		return bukrs;
	}

	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}

	public String getBukrsD() {
		return bukrsD;
	}

	public void setBukrsD(String bukrsD) {
		this.bukrsD = bukrsD;
	}

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public String getWerksD() {
		return werksD;
	}

	public void setWerksD(String werksD) {
		this.werksD = werksD;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getConciliationDate() {
		return conciliationDate;
	}

	public void setConciliationDate(String conciliationDate) {
		this.conciliationDate = conciliationDate;
	}

	public List<ReporteDocInvBean> getDocInvPosition() {
		return docInvPosition;
	}

	public void setDocInvPosition(List<ReporteDocInvBean> docInvPosition) {
		this.docInvPosition = docInvPosition;
	}

	public ReporteDocInvBeanHeader(Integer docInvId, String route, String bukrs, String bukrsD, String werks,
			String werksD, String type, String creationDate, String conciliationDate,
			List<ReporteDocInvBean> docInvPosition) {
		super();
		this.docInvId = docInvId;
		this.route = route;
		this.bukrs = bukrs;
		this.bukrsD = bukrsD;
		this.werks = werks;
		this.werksD = werksD;
		this.type = type;
		this.creationDate = creationDate;
		this.conciliationDate = conciliationDate;
		this.docInvPosition = docInvPosition;
	}

	public ReporteDocInvBeanHeader() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ReporteDocInvBeanHeader [docInvId=" + docInvId + ", route=" + route + ", bukrs=" + bukrs + ", bukrsD="
				+ bukrsD + ", werks=" + werks + ", werksD=" + werksD + ", type=" + type + ", creationDate="
				+ creationDate + ", conciliationDate=" + conciliationDate + ", docInvPosition=" + docInvPosition + "]";
	}

}
