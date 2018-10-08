package com.gmodelo.beans;

public class TareasTiemposZonasBean {

	String docInvId;
	String routeId;
	String Rdesc;
	String bukrs;
	String Bdesc;
	String werks;
	String Wdesc;
	String dateIni;
	String dateFin;
	String user;
	String taskId;
	String tiempo;
	String zoneId;
	String zoneD;
	String idGrupo;
	
	public String getTiempo() {
		return tiempo;
	}
	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}
	public String getRdesc() {
		return Rdesc;
	}
	public void setRdesc(String rdesc) {
		Rdesc = rdesc;
	}
	public String getDocInvId() {
		return docInvId;
	}
	public void setDocInvId(String docInvId) {
		this.docInvId = docInvId;
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
	public String getBdesc() {
		return Bdesc;
	}
	public void setBdesc(String bdesc) {
		Bdesc = bdesc;
	}
	public String getWerks() {
		return werks;
	}
	public void setWerks(String werks) {
		this.werks = werks;
	}
	public String getWdesc() {
		return Wdesc;
	}
	public void setWdesc(String wdesc) {
		Wdesc = wdesc;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getDateIni() {
		return dateIni;
	}
	public void setDateIni(String dateIni) {
		this.dateIni = dateIni;
	}
	public String getDateFin() {
		return dateFin;
	}
	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}
	public TareasTiemposZonasBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getIdGrupo() {
		return idGrupo;
	}
	public void setIdGrupo(String idGrupo) {
		this.idGrupo = idGrupo;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getZoneD() {
		return zoneD;
	}
	public void setZoneD(String zoneD) {
		this.zoneD = zoneD;
	}
	public TareasTiemposZonasBean(String docInvId, String routeId, String rdesc, String bukrs, String bdesc,
			String werks, String wdesc, String dateIni, String dateFin, String user, String taskId, String tiempo,
			String zoneId, String zoneD, String idGrupo) {
		super();
		this.docInvId = docInvId;
		this.routeId = routeId;
		Rdesc = rdesc;
		this.bukrs = bukrs;
		Bdesc = bdesc;
		this.werks = werks;
		Wdesc = wdesc;
		this.dateIni = dateIni;
		this.dateFin = dateFin;
		this.user = user;
		this.taskId = taskId;
		this.tiempo = tiempo;
		this.zoneId = zoneId;
		this.zoneD = zoneD;
		this.idGrupo = idGrupo;
	}
	@Override
	public String toString() {
		return "TareasTiemposZonasBean [docInvId=" + docInvId + ", routeId=" + routeId + ", Rdesc=" + Rdesc + ", bukrs="
				+ bukrs + ", Bdesc=" + Bdesc + ", werks=" + werks + ", Wdesc=" + Wdesc + ", dateIni=" + dateIni
				+ ", dateFin=" + dateFin + ", user=" + user + ", taskId=" + taskId + ", tiempo=" + tiempo + ", zoneId="
				+ zoneId + ", zoneD=" + zoneD + ", idGrupo=" + idGrupo + "]";
	}
	
	

}
