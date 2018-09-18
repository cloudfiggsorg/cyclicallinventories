package com.gmodelo.beans;

public class DocInvPositionBean {
	int positionId;
	int docInvId;
	int lgort;
	String lgtyp;
	String lgpla;
	String matnr;
	String theoric;
	String counted;
	String diffCounted;
	String flag;
	String meins;
	String explosion;
	String gdes;
	String lgtypDes;
	String matnrDes;
	
	@Override
	public String toString() {
		return "DocInvPositionBean [positionId=" + positionId + ", docInvId=" + docInvId + ", lgort=" + lgort
				+ ", lgtyp=" + lgtyp + ", lgpla=" + lgpla + ", matnr=" + matnr + ", theoric=" + theoric + ", counted="
				+ counted + ", diffCounted=" + diffCounted + ", flag=" + flag + ", meins=" + meins + ", explosion="
				+ explosion + ", gdes=" + gdes + ", lgtypDes=" + lgtypDes + ", matnrDes=" + matnrDes + "]";
	}

	public DocInvPositionBean(int positionId, int docInvId, int lgort, String lgtyp, String lgpla, String matnr,
			String theoric, String counted, String diffCounted, String flag, String meins, String explosion,
			String gdes, String lgtypDes, String matnrDes) {
		super();
		this.positionId = positionId;
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
		this.gdes = gdes;
		this.lgtypDes = lgtypDes;
		this.matnrDes = matnrDes;
	}

	public String getMatnrDes() {
		return matnrDes;
	}

	public void setMatnrDes(String matnrDes) {
		this.matnrDes = matnrDes;
	}

	public DocInvPositionBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getGdes() {
		return gdes;
	}
	public void setGdes(String gdes) {
		this.gdes = gdes;
	}
	public String getLgtypDes() {
		return lgtypDes;
	}
	public void setLgtypDes(String lgtypDes) {
		this.lgtypDes = lgtypDes;
	}
	public int getPositionId() {
		return positionId;
	}
	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}
	public int getDocInvId() {
		return docInvId;
	}
	public void setDocInvId(int docInvId) {
		this.docInvId = docInvId;
	}
	public int getLgort() {
		return lgort;
	}
	public void setLgort(int lgort) {
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
	
}
