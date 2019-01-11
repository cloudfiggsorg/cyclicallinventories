package com.gmodelo.cyclicinventories.beans;

public class ContingencyTaskBean {

	String taskId;
	String bukrs;
	String werks;
	String routeId;
	String lgort;
	int positionId;
	String lgtyp;
	int pkAsgId;
	String zoneId;
	String zoneSecuency;
	String lgpla;
	String lgplaSecuency;
	String matnr;
	String vhilm;
	String vhilmQuan;
	String totalConverted;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
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
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getLgort() {
		return lgort;
	}
	public void setLgort(String lgort) {
		this.lgort = lgort;
	}
	public int getPositionId() {
		return positionId;
	}
	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}
	public String getLgtyp() {
		return lgtyp;
	}
	public void setLgtyp(String lgtyp) {
		this.lgtyp = lgtyp;
	}
	public int getPkAsgId() {
		return pkAsgId;
	}
	public void setPkAsgId(int pkAsgId) {
		this.pkAsgId = pkAsgId;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getZoneSecuency() {
		return zoneSecuency;
	}
	public void setZoneSecuency(String zoneSecuency) {
		this.zoneSecuency = zoneSecuency;
	}
	public String getLgpla() {
		return lgpla;
	}
	public void setLgpla(String lgpla) {
		this.lgpla = lgpla;
	}
	public String getLgplaSecuency() {
		return lgplaSecuency;
	}
	public void setLgplaSecuency(String lgplaSecuency) {
		this.lgplaSecuency = lgplaSecuency;
	}
	public String getMatnr() {
		return matnr;
	}
	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}
	public String getVhilm() {
		return vhilm;
	}
	public void setVhilm(String vhilm) {
		this.vhilm = vhilm;
	}
	public String getVhilmQuan() {
		return vhilmQuan;
	}
	public void setVhilmQuan(String vhilmQuan) {
		this.vhilmQuan = vhilmQuan;
	}
	public String getTotalConverted() {
		return totalConverted;
	}
	public void setTotalConverted(String totalConverted) {
		this.totalConverted = totalConverted;
	}
	
	public ContingencyTaskBean(String taskId, String bukrs, String werks, String routeId, String lgort, int positionId,
			String lgtyp, int pkAsgId, String zoneId, String zoneSecuency, String lgpla, String lgplaSecuency,
			String matnr, String vhilm, String vhilmQuan, String totalConverted) {
		super();
		this.taskId = taskId;
		this.bukrs = bukrs;
		this.werks = werks;
		this.routeId = routeId;
		this.lgort = lgort;
		this.positionId = positionId;
		this.lgtyp = lgtyp;
		this.pkAsgId = pkAsgId;
		this.zoneId = zoneId;
		this.zoneSecuency = zoneSecuency;
		this.lgpla = lgpla;
		this.lgplaSecuency = lgplaSecuency;
		this.matnr = matnr;
		this.vhilm = vhilm;
		this.vhilmQuan = vhilmQuan;
		this.totalConverted = totalConverted;
	}
	
	
	public ContingencyTaskBean() {
		super();
	}
	@Override
	public String toString() {
		return "ContingencyTaskBean [taskId=" + taskId + ", bukrs=" + bukrs + ", werks=" + werks + ", routeId="
				+ routeId + ", lgort=" + lgort + ", positionId=" + positionId + ", lgtyp=" + lgtyp + ", pkAsgId="
				+ pkAsgId + ", zoneId=" + zoneId + ", zoneSecuency=" + zoneSecuency + ", lgpla=" + lgpla
				+ ", lgplaSecuency=" + lgplaSecuency + ", matnr=" + matnr + ", vhilm=" + vhilm + ", vhilmQuan="
				+ vhilmQuan + ", totalConverted=" + totalConverted + "]";
	}
	
	
	
}
