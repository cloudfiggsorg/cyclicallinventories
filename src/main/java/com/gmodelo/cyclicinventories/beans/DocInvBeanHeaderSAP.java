package com.gmodelo.cyclicinventories.beans;

import java.util.List;

public class DocInvBeanHeaderSAP {

	private Integer docInvId;
	private String route;
	private String bukrs;
	private String bukrsD;
	private String werks;
	private String werksD;
	private String type;
	private String creationDate;
	private String conciliationDate;
	private List<PosDocInvBean> docInvPosition;
	private String createdBy;
	private String concSAPDate;
	private boolean sapRecount;
	private boolean cost;

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

	public List<PosDocInvBean> getDocInvPosition() {
		return docInvPosition;
	}

	public void setDocInvPosition(List<PosDocInvBean> docInvPosition) {
		this.docInvPosition = docInvPosition;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getConcSAPDate() {
		return concSAPDate;
	}

	public void setConcSAPDate(String concSAPDate) {
		this.concSAPDate = concSAPDate;
	}

	public boolean isSapRecount() {
		return sapRecount;
	}

	public void setSapRecount(boolean sapRecount) {
		this.sapRecount = sapRecount;
	}

	public DocInvBeanHeaderSAP() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean isCost() {
		return cost;
	}

	public void setCost(boolean cost) {
		this.cost = cost;
	}

	public DocInvBeanHeaderSAP(Integer docInvId, String route, String bukrs, String bukrsD, String werks, String werksD,
			String type, String creationDate, String conciliationDate, List<PosDocInvBean> docInvPosition,
			String createdBy, String concSAPDate, boolean sapRecount, boolean cost) {
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
		this.createdBy = createdBy;
		this.concSAPDate = concSAPDate;
		this.sapRecount = sapRecount;
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "DocInvBeanHeaderSAP [docInvId=" + docInvId + ", route=" + route + ", bukrs=" + bukrs + ", bukrsD="
				+ bukrsD + ", werks=" + werks + ", werksD=" + werksD + ", type=" + type + ", creationDate="
				+ creationDate + ", conciliationDate=" + conciliationDate + ", docInvPosition=" + docInvPosition
				+ ", createdBy=" + createdBy + ", concSAPDate=" + concSAPDate + ", sapRecount=" + sapRecount + ", cost="
				+ cost + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bukrs == null) ? 0 : bukrs.hashCode());
		result = prime * result + ((bukrsD == null) ? 0 : bukrsD.hashCode());
		result = prime * result + ((concSAPDate == null) ? 0 : concSAPDate.hashCode());
		result = prime * result + ((conciliationDate == null) ? 0 : conciliationDate.hashCode());
		result = prime * result + (cost ? 1231 : 1237);
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((docInvId == null) ? 0 : docInvId.hashCode());
		result = prime * result + ((docInvPosition == null) ? 0 : docInvPosition.hashCode());
		result = prime * result + ((route == null) ? 0 : route.hashCode());
		result = prime * result + (sapRecount ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((werks == null) ? 0 : werks.hashCode());
		result = prime * result + ((werksD == null) ? 0 : werksD.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocInvBeanHeaderSAP other = (DocInvBeanHeaderSAP) obj;
		if (bukrs == null) {
			if (other.bukrs != null)
				return false;
		} else if (!bukrs.equals(other.bukrs))
			return false;
		if (bukrsD == null) {
			if (other.bukrsD != null)
				return false;
		} else if (!bukrsD.equals(other.bukrsD))
			return false;
		if (concSAPDate == null) {
			if (other.concSAPDate != null)
				return false;
		} else if (!concSAPDate.equals(other.concSAPDate))
			return false;
		if (conciliationDate == null) {
			if (other.conciliationDate != null)
				return false;
		} else if (!conciliationDate.equals(other.conciliationDate))
			return false;
		if (cost != other.cost)
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (docInvId == null) {
			if (other.docInvId != null)
				return false;
		} else if (!docInvId.equals(other.docInvId))
			return false;
		if (docInvPosition == null) {
			if (other.docInvPosition != null)
				return false;
		} else if (!docInvPosition.equals(other.docInvPosition))
			return false;
		if (route == null) {
			if (other.route != null)
				return false;
		} else if (!route.equals(other.route))
			return false;
		if (sapRecount != other.sapRecount)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (werks == null) {
			if (other.werks != null)
				return false;
		} else if (!werks.equals(other.werks))
			return false;
		if (werksD == null) {
			if (other.werksD != null)
				return false;
		} else if (!werksD.equals(other.werksD))
			return false;
		return true;
	}

	
}
