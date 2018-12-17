package com.gmodelo.cyclicinventories.beans;

public class TareasTiemposLgplaBean {
	
	private String docInvId;
	private String routeId;
	private String Rdesc;
	private String bukrs;
	private String Bdesc;
	private String werks;
	private String Wdesc;
	private String dateIni;
	private String dateFin;
	private String user;
	private String taskId;
	private String tiempo;
	private String lgpla;
	private String idGrupo;
	
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
	public TareasTiemposLgplaBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getLgpla() {
		return lgpla;
	}
	public void setLgpla(String lgpla) {
		this.lgpla = lgpla;
	}
	public String getIdGrupo() {
		return idGrupo;
	}
	public void setIdGrupo(String idGrupo) {
		this.idGrupo = idGrupo;
	}
	public TareasTiemposLgplaBean(String docInvId, String routeId, String rdesc, String bukrs, String bdesc, String werks,
			String wdesc, String dateIni, String dateFin, String user, String taskId, String tiempo, String lgpla,
			String idGrupo) {
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
		this.lgpla = lgpla;
		this.idGrupo = idGrupo;
	}
	@Override
	public String toString() {
		return "TareasTiemposBean [docInvId=" + docInvId + ", routeId=" + routeId + ", Rdesc=" + Rdesc + ", bukrs="
				+ bukrs + ", Bdesc=" + Bdesc + ", werks=" + werks + ", Wdesc=" + Wdesc + ", dateIni=" + dateIni
				+ ", dateFin=" + dateFin + ", user=" + user + ", taskId=" + taskId + ", tiempo=" + tiempo + ", lgpla="
				+ lgpla + ", idGrupo=" + idGrupo + "]";
	}

	
	

}
