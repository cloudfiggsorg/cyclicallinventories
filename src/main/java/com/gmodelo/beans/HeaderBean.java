package com.gmodelo.beans;

import java.util.Date;
import java.util.UUID;

public class HeaderBean {

	String invDocId;
	String routeId;
	String type;
	Date beginCount;
	Date endCount;
	BurksBean burks;

	public String getInvDocId() {
		return invDocId;
	}

	public void setInvDocId(String invDocId) {
		this.invDocId = invDocId;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getBeginCount() {
		return beginCount;
	}

	public void setBeginCount(Date beginCount) {
		this.beginCount = beginCount;
	}

	public Date getEndCount() {
		return endCount;
	}

	public void setEndCount(Date endCount) {
		this.endCount = endCount;
	}

	public BurksBean getBurks() {
		return burks;
	}

	public void setBurks(BurksBean burks) {
		this.burks = burks;
	}

	@Override
	public String toString() {
		return "HeaderBean [invDocId=" + invDocId + ", routeId=" + routeId + ", type=" + type + ", beginCount="
				+ beginCount + ", endCount=" + endCount + ", burks=" + burks + "]";
	}

	public HeaderBean(String invDocId, String routeId, String type, Date beginCount, Date endCount, BurksBean burks) {
		super();
		this.invDocId = invDocId;
		this.routeId = routeId;
		this.type = type;
		this.beginCount = beginCount;
		this.endCount = endCount;
		this.burks = burks;
	}

	public HeaderBean() {
		super();
		this.invDocId = UUID.randomUUID().toString();
		this.routeId = UUID.randomUUID().toString();
		this.type  = "Diario";
	}

}
