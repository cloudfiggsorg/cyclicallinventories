package com.gmodelo.beans;

public class ReporteDocInvBean {
	int docInvId;
	int routeId;
	String lgtyp;
	String ltypt;
	String lgpla;
	String matnr;
	String matnrD;
	String userID;
	String routeD;
	String bukrs;
	String bukrsD;
	String werks;
	String werksD;
	String typeD;
	String date;
	String lgort;
	String lgortD;
	String theoric;
	String counted;
	String diff;
	String flag;
	public int getDocInvId() {
		return docInvId;
	}
	public void setDocInvId(int docInvId) {
		this.docInvId = docInvId;
	}
	public int getRouteId() {
		return routeId;
	}
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	public String getLgtyp() {
		return lgtyp;
	}
	public void setLgtyp(String lgtyp) {
		this.lgtyp = lgtyp;
	}
	public String getLtypt() {
		return ltypt;
	}
	public void setLtypt(String ltypt) {
		this.ltypt = ltypt;
	}
	public String getLgpla() {
		return lgpla;
	}
	public void setLgpla(String lgpla) {
		this.lgpla = lgpla;
	}
	public String getMatnr() {
		return matnr;
	}
	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}
	public String getMatnrD() {
		return matnrD;
	}
	public void setMatnrD(String matnrD) {
		this.matnrD = matnrD;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getRouteD() {
		return routeD;
	}
	public void setRouteD(String routeD) {
		this.routeD = routeD;
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
	public String getTypeD() {
		return typeD;
	}
	public void setTypeD(String typeD) {
		this.typeD = typeD;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLgort() {
		return lgort;
	}
	public void setLgort(String lgort) {
		this.lgort = lgort;
	}
	public String getLgortD() {
		return lgortD;
	}
	public void setLgortD(String lgortD) {
		this.lgortD = lgortD;
	}
	public String getTheoric() {
		return theoric;
	}
	public void setTheoric(String theoric) {
		this.theoric = theoric;
	}
	public String getCounted() {
		return counted;
	}
	public void setCounted(String counted) {
		this.counted = counted;
	}
	public String getDiff() {
		return diff;
	}
	public void setDiff(String diff) {
		this.diff = diff;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public ReporteDocInvBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ReporteDocInvBean(int docInvId, int routeId, String lgtyp, String ltypt, String lgpla, String matnr,
			String matnrD, String userID, String routeD, String bukrs, String bukrsD, String werks, String werksD,
			String typeD, String date, String lgort, String lgortD, String theoric, String counted, String diff,
			String flag) {
		super();
		this.docInvId = docInvId;
		this.routeId = routeId;
		this.lgtyp = lgtyp;
		this.ltypt = ltypt;
		this.lgpla = lgpla;
		this.matnr = matnr;
		this.matnrD = matnrD;
		this.userID = userID;
		this.routeD = routeD;
		this.bukrs = bukrs;
		this.bukrsD = bukrsD;
		this.werks = werks;
		this.werksD = werksD;
		this.typeD = typeD;
		this.date = date;
		this.lgort = lgort;
		this.lgortD = lgortD;
		this.theoric = theoric;
		this.counted = counted;
		this.diff = diff;
		this.flag = flag;
	}
	@Override
	public String toString() {
		return "ReporteDocInvId [docInvId=" + docInvId + ", routeId=" + routeId + ", lgtyp=" + lgtyp + ", ltypt="
				+ ltypt + ", lgpla=" + lgpla + ", matnr=" + matnr + ", matnrD=" + matnrD + ", userID=" + userID
				+ ", routeD=" + routeD + ", bukrs=" + bukrs + ", bukrsD=" + bukrsD + ", werks=" + werks + ", werksD="
				+ werksD + ", typeD=" + typeD + ", date=" + date + ", lgort=" + lgort + ", lgortD=" + lgortD
				+ ", theoric=" + theoric + ", counted=" + counted + ", diff=" + diff + ", flag=" + flag + "]";
	}
	
	
}
