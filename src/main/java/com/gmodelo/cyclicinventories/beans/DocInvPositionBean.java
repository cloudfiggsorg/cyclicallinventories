package com.gmodelo.cyclicinventories.beans;

public class DocInvPositionBean {
	
	private int docInvId;
	private String lgort;
	private String lgtyp;
	private String lgpla;
	private String matnr;
	private String theoric;
	private String counted;
	private String diffCounted;
	private String flag;
	private String meins;
	private String explosion;
	private Integer zoneId;
	private String dateIni;
	private String dateEnd;

	public int getDocInvId() {
		return docInvId;
	}

	public void setDocInvId(int docInvId) {
		this.docInvId = docInvId;
	}

	public String getLgort() {
		return lgort;
	}

	public void setLgort(String lgort) {
		this.lgort = lgort;
	}

	public String getLgtyp() {
		return lgtyp;
	}

	public void setLgtyp(String lgtyp) {
		this.lgtyp = lgtyp;
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

	public String getDiffCounted() {
		return diffCounted;
	}

	public void setDiffCounted(String diffCounted) {
		this.diffCounted = diffCounted;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMeins() {
		return meins;
	}

	public void setMeins(String meins) {
		this.meins = meins;
	}

	public String getExplosion() {
		return explosion;
	}

	public void setExplosion(String explosion) {
		this.explosion = explosion;
	}

	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public String getDateIni() {
		return dateIni;
	}

	public void setDateIni(String dateIni) {
		this.dateIni = dateIni;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	@Override
	public String toString() {
		return "DocInvPositionBean [docInvId=" + docInvId + ", lgort=" + lgort + ", lgtyp=" + lgtyp + ", lgpla=" + lgpla
				+ ", matnr=" + matnr + ", theoric=" + theoric + ", counted=" + counted + ", diffCounted=" + diffCounted
				+ ", flag=" + flag + ", meins=" + meins + ", explosion=" + explosion + ", zoneId=" + zoneId
				+ ", dateIni=" + dateIni + ", dateEnd=" + dateEnd + "]";
	}

	public DocInvPositionBean(int docInvId, String lgort, String lgtyp, String lgpla, String matnr, String theoric,
			String counted, String diffCounted, String flag, String meins, String explosion, Integer zoneId,
			String dateIni, String dateEnd) {
		super();
		this.docInvId = docInvId;
		this.lgort = lgort;
		this.lgtyp = lgtyp;
		this.lgpla = lgpla;
		this.matnr = matnr;
		this.theoric = theoric;
		this.counted = counted;
		this.diffCounted = diffCounted;
		this.flag = flag;
		this.meins = meins;
		this.explosion = explosion;
		this.zoneId = zoneId;
		this.dateIni = dateIni;
		this.dateEnd = dateEnd;
	}

	public DocInvPositionBean() {
		super();
		// TODO Auto-generated constructor stub
	}

}
