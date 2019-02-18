package com.gmodelo.cyclicinventories.beans;

import java.util.List;

public class DocInvBean {

	private Integer docInvId;
	private String route;
	private String bukrs;
	private String bukrsD;
	private String werks;
	private String werksD;
	private String type;
	private String status;
	private String createdBy;
	private String modifiedBy;
	private Integer docFatherInvId;
	private boolean sapRecount;
	private Long createdDate;
	private Long modifiedDate;
	private List<DocInvPositionBean> docInvPositions;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getDocFatherInvId() {
		return docFatherInvId;
	}

	public void setDocFatherInvId(Integer docFatherInvId) {
		this.docFatherInvId = docFatherInvId;
	}

	public List<DocInvPositionBean> getDocInvPositions() {
		return docInvPositions;
	}

	public void setDocInvPositions(List<DocInvPositionBean> docInvPositions) {
		this.docInvPositions = docInvPositions;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Long getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Long modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public boolean isSapRecount() {
		return sapRecount;
	}

	public void setSapRecount(boolean sapRecount) {
		this.sapRecount = sapRecount;
	}

	public DocInvBean(Integer docInvId, String route, String bukrs, String bukrsD, String werks, String werksD,
			String type, String status, String createdBy, String modifiedBy, Integer docFatherInvId, boolean sapRecount,
			Long createdDate, Long modifiedDate, List<DocInvPositionBean> docInvPositions) {
		super();
		this.docInvId = docInvId;
		this.route = route;
		this.bukrs = bukrs;
		this.bukrsD = bukrsD;
		this.werks = werks;
		this.werksD = werksD;
		this.type = type;
		this.status = status;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.docFatherInvId = docFatherInvId;
		this.sapRecount = sapRecount;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.docInvPositions = docInvPositions;
	}

	public DocInvBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "DocInvBean [docInvId=" + docInvId + ", route=" + route + ", bukrs=" + bukrs + ", bukrsD=" + bukrsD
				+ ", werks=" + werks + ", werksD=" + werksD + ", type=" + type + ", status=" + status + ", createdBy="
				+ createdBy + ", modifiedBy=" + modifiedBy + ", docFatherInvId=" + docFatherInvId + ", sapRecount="
				+ sapRecount + ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate + ", docInvPositions="
				+ docInvPositions + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bukrs == null) ? 0 : bukrs.hashCode());
		result = prime * result + ((bukrsD == null) ? 0 : bukrsD.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((docFatherInvId == null) ? 0 : docFatherInvId.hashCode());
		result = prime * result + ((docInvId == null) ? 0 : docInvId.hashCode());
		result = prime * result + ((docInvPositions == null) ? 0 : docInvPositions.hashCode());
		result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		result = prime * result + ((modifiedDate == null) ? 0 : modifiedDate.hashCode());
		result = prime * result + ((route == null) ? 0 : route.hashCode());
		result = prime * result + (sapRecount ? 1231 : 1237);
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		DocInvBean other = (DocInvBean) obj;
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
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (docFatherInvId == null) {
			if (other.docFatherInvId != null)
				return false;
		} else if (!docFatherInvId.equals(other.docFatherInvId))
			return false;
		if (docInvId == null) {
			if (other.docInvId != null)
				return false;
		} else if (!docInvId.equals(other.docInvId))
			return false;
		if (docInvPositions == null) {
			if (other.docInvPositions != null)
				return false;
		} else if (!docInvPositions.equals(other.docInvPositions))
			return false;
		if (modifiedBy == null) {
			if (other.modifiedBy != null)
				return false;
		} else if (!modifiedBy.equals(other.modifiedBy))
			return false;
		if (modifiedDate == null) {
			if (other.modifiedDate != null)
				return false;
		} else if (!modifiedDate.equals(other.modifiedDate))
			return false;
		if (route == null) {
			if (other.route != null)
				return false;
		} else if (!route.equals(other.route))
			return false;
		if (sapRecount != other.sapRecount)
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
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
