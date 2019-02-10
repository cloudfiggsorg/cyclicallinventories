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
	private String vhilmCount;
	private String vhilm;

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

	public String getVhilmCount() {
		return vhilmCount;
	}

	public void setVhilmCount(String vhilmCount) {
		this.vhilmCount = vhilmCount;
	}

	public String getVhilm() {
		return vhilm;
	}

	public void setVhilm(String vhilm) {
		this.vhilm = vhilm;
	}

	public DocInvPositionBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "DocInvPositionBean [docInvId=" + docInvId + ", lgort=" + lgort + ", lgtyp=" + lgtyp + ", lgpla=" + lgpla
				+ ", matnr=" + matnr + ", theoric=" + theoric + ", counted=" + counted + ", diffCounted=" + diffCounted
				+ ", flag=" + flag + ", meins=" + meins + ", explosion=" + explosion + ", zoneId=" + zoneId
				+ ", dateIni=" + dateIni + ", dateEnd=" + dateEnd + ", vhilmCount=" + vhilmCount + ", vhilm=" + vhilm
				+ "]";
	}

	public DocInvPositionBean(int docInvId, String lgort, String lgtyp, String lgpla, String matnr, String theoric,
			String counted, String diffCounted, String flag, String meins, String explosion, Integer zoneId,
			String dateIni, String dateEnd, String vhilmCount, String vhilm) {
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
		this.vhilmCount = vhilmCount;
		this.vhilm = vhilm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((counted == null) ? 0 : counted.hashCode());
		result = prime * result + ((dateEnd == null) ? 0 : dateEnd.hashCode());
		result = prime * result + ((dateIni == null) ? 0 : dateIni.hashCode());
		result = prime * result + ((diffCounted == null) ? 0 : diffCounted.hashCode());
		result = prime * result + docInvId;
		result = prime * result + ((explosion == null) ? 0 : explosion.hashCode());
		result = prime * result + ((flag == null) ? 0 : flag.hashCode());
		result = prime * result + ((lgort == null) ? 0 : lgort.hashCode());
		result = prime * result + ((lgpla == null) ? 0 : lgpla.hashCode());
		result = prime * result + ((lgtyp == null) ? 0 : lgtyp.hashCode());
		result = prime * result + ((matnr == null) ? 0 : matnr.hashCode());
		result = prime * result + ((meins == null) ? 0 : meins.hashCode());
		result = prime * result + ((theoric == null) ? 0 : theoric.hashCode());
		result = prime * result + ((vhilm == null) ? 0 : vhilm.hashCode());
		result = prime * result + ((vhilmCount == null) ? 0 : vhilmCount.hashCode());
		result = prime * result + ((zoneId == null) ? 0 : zoneId.hashCode());
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
		DocInvPositionBean other = (DocInvPositionBean) obj;
		if (counted == null) {
			if (other.counted != null)
				return false;
		} else if (!counted.equals(other.counted))
			return false;
		if (dateEnd == null) {
			if (other.dateEnd != null)
				return false;
		} else if (!dateEnd.equals(other.dateEnd))
			return false;
		if (dateIni == null) {
			if (other.dateIni != null)
				return false;
		} else if (!dateIni.equals(other.dateIni))
			return false;
		if (diffCounted == null) {
			if (other.diffCounted != null)
				return false;
		} else if (!diffCounted.equals(other.diffCounted))
			return false;
		if (docInvId != other.docInvId)
			return false;
		if (explosion == null) {
			if (other.explosion != null)
				return false;
		} else if (!explosion.equals(other.explosion))
			return false;
		if (flag == null) {
			if (other.flag != null)
				return false;
		} else if (!flag.equals(other.flag))
			return false;
		if (lgort == null) {
			if (other.lgort != null)
				return false;
		} else if (!lgort.equals(other.lgort))
			return false;
		if (lgpla == null) {
			if (other.lgpla != null)
				return false;
		} else if (!lgpla.equals(other.lgpla))
			return false;
		if (lgtyp == null) {
			if (other.lgtyp != null)
				return false;
		} else if (!lgtyp.equals(other.lgtyp))
			return false;
		if (matnr == null) {
			if (other.matnr != null)
				return false;
		} else if (!matnr.equals(other.matnr))
			return false;
		if (meins == null) {
			if (other.meins != null)
				return false;
		} else if (!meins.equals(other.meins))
			return false;
		if (theoric == null) {
			if (other.theoric != null)
				return false;
		} else if (!theoric.equals(other.theoric))
			return false;
		if (vhilm == null) {
			if (other.vhilm != null)
				return false;
		} else if (!vhilm.equals(other.vhilm))
			return false;
		if (vhilmCount == null) {
			if (other.vhilmCount != null)
				return false;
		} else if (!vhilmCount.equals(other.vhilmCount))
			return false;
		if (zoneId == null) {
			if (other.zoneId != null)
				return false;
		} else if (!zoneId.equals(other.zoneId))
			return false;
		return true;
	}

}
