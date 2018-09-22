package com.gmodelo.beans;

import java.util.List;

public class RouteUserBean {
	String routeId;
	String bukrs;
	String werks;
	String taskId;
	String rdesc;
	String type;
	String bdesc;
	String wdesc;
	long dateIni;
	long dateEnd;
	List<RouteUserPositionBean> positions;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public long getDateIni() {
		return dateIni;
	}

	public void setDateIni(long dateIni) {
		this.dateIni = dateIni;
	}

	public long getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(long dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getBdesc() {
		return bdesc;
	}

	public void setBdesc(String bdesc) {
		this.bdesc = bdesc;
	}

	public String getWdesc() {
		return wdesc;
	}

	public void setWdesc(String wdesc) {
		this.wdesc = wdesc;
	}

	public List<RouteUserPositionBean> getPositions() {
		return positions;
	}

	public void setPositions(List<RouteUserPositionBean> positions) {
		this.positions = positions;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getBukrs() {
		return bukrs;
	}

	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public String getRdesc() {
		return rdesc;
	}

	public void setRdesc(String rdesc) {
		this.rdesc = rdesc;
	}

	public RouteUserBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RouteUserBean(String routeId, String bukrs, String werks, String taskId, String rdesc, String type,
			String bdesc, String wdesc, long dateIni, long dateEnd, List<RouteUserPositionBean> positions) {
		super();
		this.routeId = routeId;
		this.bukrs = bukrs;
		this.werks = werks;
		this.taskId = taskId;
		this.rdesc = rdesc;
		this.type = type;
		this.bdesc = bdesc;
		this.wdesc = wdesc;
		this.dateIni = dateIni;
		this.dateEnd = dateEnd;
		this.positions = positions;
	}

	@Override
	public String toString() {
		return "RouteUserBean [routeId=" + routeId + ", bukrs=" + bukrs + ", werks=" + werks + ", taskId=" + taskId
				+ ", rdesc=" + rdesc + ", type=" + type + ", bdesc=" + bdesc + ", wdesc=" + wdesc + ", dateIni="
				+ dateIni + ", dateEnd=" + dateEnd + ", positions=" + positions + "]";
	}

}
