package com.gmodelo.beans;

public class ApegosBean {
	String docInvId;
	String routeId;
	String Rdesc;
	String bukrs;
	String Bdesc;
	String werks;
	String Wdesc;
	String dType;
	String userDocInv;
	String userCount;
	String dateIni;
	String dateFin;
	String taskId;
	String grupo;
	String creacion;
	String ejecucion;
	String tiempo;
	String apegos;
	String lgort;
	String lgDesc;
	
	public String getLgDesc() {
		return lgDesc;
	}
	public void setLgDesc(String lgDesc) {
		this.lgDesc = lgDesc;
	}
	public String getLgort() {
		return lgort;
	}
	public void setLgort(String lgort) {
		this.lgort = lgort;
	}
	public String getApegos() {
		return apegos;
	}
	public void setApegos(String apegos) {
		this.apegos = apegos;
	}
	public String getTiempo() {
		return tiempo;
	}
	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}
	public String getCreacion() {
		return creacion;
	}
	public void setCreacion(String creacion) {
		this.creacion = creacion;
	}
	public String getEjecucion() {
		return ejecucion;
	}
	public void setEjecucion(String ejecucion) {
		this.ejecucion = ejecucion;
	}
	public String getRdesc() {
		return Rdesc;
	}
	public void setRdesc(String rdesc) {
		Rdesc = rdesc;
	}
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
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
	public String getdType() {
		return dType;
	}
	public void setdType(String dType) {
		this.dType = dType;
	}
	public String getUserDocInv() {
		return userDocInv;
	}
	public void setUserDocInv(String userDocInv) {
		this.userDocInv = userDocInv;
	}
	public String getUserCount() {
		return userCount;
	}
	public void setUserCount(String userCount) {
		this.userCount = userCount;
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
	public ApegosBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ApegosBean(String docInvId, String routeId, String rdesc, String bukrs, String bdesc, String werks,
			String wdesc, String dType, String userDocInv, String userCount, String dateIni, String dateFin,
			String taskId, String grupo, String creacion, String ejecucion, String tiempo, String apegos, String lgort,
			String lgDesc) {
		super();
		this.docInvId = docInvId;
		this.routeId = routeId;
		Rdesc = rdesc;
		this.bukrs = bukrs;
		Bdesc = bdesc;
		this.werks = werks;
		Wdesc = wdesc;
		this.dType = dType;
		this.userDocInv = userDocInv;
		this.userCount = userCount;
		this.dateIni = dateIni;
		this.dateFin = dateFin;
		this.taskId = taskId;
		this.grupo = grupo;
		this.creacion = creacion;
		this.ejecucion = ejecucion;
		this.tiempo = tiempo;
		this.apegos = apegos;
		this.lgort = lgort;
		this.lgDesc = lgDesc;
	}
	@Override
	public String toString() {
		return "ApegosBean [docInvId=" + docInvId + ", routeId=" + routeId + ", Rdesc=" + Rdesc + ", bukrs=" + bukrs
				+ ", Bdesc=" + Bdesc + ", werks=" + werks + ", Wdesc=" + Wdesc + ", dType=" + dType + ", userDocInv="
				+ userDocInv + ", userCount=" + userCount + ", dateIni=" + dateIni + ", dateFin=" + dateFin
				+ ", taskId=" + taskId + ", grupo=" + grupo + ", creacion=" + creacion + ", ejecucion=" + ejecucion
				+ ", tiempo=" + tiempo + ", apegos=" + apegos + ", lgort=" + lgort + ", lgDesc=" + lgDesc + "]";
	}
	
	
	

	

}
