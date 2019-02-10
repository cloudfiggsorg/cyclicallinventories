package com.gmodelo.cyclicinventories.beans;

public class ConciliationPositionBean {

	private String zoneId;
	private String zoneD;
	private String lgort;
	private String lgobe;
	private String lgpla;
	private String matnr;
	private String matnrD;
	private String measureUnit;
	private String count1A;
	private String count1B;
	private String count2;
	private String count3;
	private String countX;
	private String pkAsgId;
	private String note;
	private String prodDate;
	private String dateIni;
	private String dateEnd;
	private boolean flagColor;
	private String vhilm;
	private String vhilmCount;

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

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public String getCount1A() {
		return count1A;
	}

	public void setCount1A(String count1a) {
		count1A = count1a;
	}

	public String getCount1B() {
		return count1B;
	}

	public void setCount1B(String count1b) {
		count1B = count1b;
	}

	public String getCount2() {
		return count2;
	}

	public void setCount2(String count2) {
		this.count2 = count2;
	}

	public String getCount3() {
		return count3;
	}

	public void setCount3(String count3) {
		this.count3 = count3;
	}

	public String getCountX() {
		return countX;
	}

	public void setCountX(String countX) {
		this.countX = countX;
	}

	public String getLgort() {
		return lgort;
	}

	public void setLgort(String lgort) {
		this.lgort = lgort;
	}

	public String getLgobe() {
		return lgobe;
	}

	public void setLgobe(String lgobe) {
		this.lgobe = lgobe;
	}

	public String getPkAsgId() {
		return pkAsgId;
	}

	public void setPkAsgId(String pkAsgId) {
		this.pkAsgId = pkAsgId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public String getProdDate() {
		return prodDate;
	}

	public void setProdDate(String prodDate) {
		this.prodDate = prodDate;
	}

	public boolean isFlagColor() {
		return flagColor;
	}

	public void setFlagColor(boolean flagColor) {
		this.flagColor = flagColor;
	}

	public String getVhilm() {
		return vhilm;
	}

	public void setVhilm(String vhilm) {
		this.vhilm = vhilm;
	}

	public String getVhilmCount() {
		return vhilmCount;
	}

	public void setVhilmCount(String vhilmCount) {
		this.vhilmCount = vhilmCount;
	}

	public ConciliationPositionBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConciliationPositionBean(String zoneId, String zoneD, String lgort, String lgobe, String lgpla, String matnr,
			String matnrD, String measureUnit, String count1a, String count1b, String count2, String count3,
			String countX, String pkAsgId, String note, String prodDate, String dateIni, String dateEnd,
			boolean flagColor, String vhilm, String vhilmCount) {
		super();
		this.zoneId = zoneId;
		this.zoneD = zoneD;
		this.lgort = lgort;
		this.lgobe = lgobe;
		this.lgpla = lgpla;
		this.matnr = matnr;
		this.matnrD = matnrD;
		this.measureUnit = measureUnit;
		count1A = count1a;
		count1B = count1b;
		this.count2 = count2;
		this.count3 = count3;
		this.countX = countX;
		this.pkAsgId = pkAsgId;
		this.note = note;
		this.prodDate = prodDate;
		this.dateIni = dateIni;
		this.dateEnd = dateEnd;
		this.flagColor = flagColor;
		this.vhilm = vhilm;
		this.vhilmCount = vhilmCount;
	}

	@Override
	public String toString() {
		return "ConciliationPositionBean [zoneId=" + zoneId + ", zoneD=" + zoneD + ", lgort=" + lgort + ", lgobe="
				+ lgobe + ", lgpla=" + lgpla + ", matnr=" + matnr + ", matnrD=" + matnrD + ", measureUnit="
				+ measureUnit + ", count1A=" + count1A + ", count1B=" + count1B + ", count2=" + count2 + ", count3="
				+ count3 + ", countX=" + countX + ", pkAsgId=" + pkAsgId + ", note=" + note + ", prodDate=" + prodDate
				+ ", dateIni=" + dateIni + ", dateEnd=" + dateEnd + ", flagColor=" + flagColor + ", vhilm=" + vhilm
				+ ", vhilmCount=" + vhilmCount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((count1A == null) ? 0 : count1A.hashCode());
		result = prime * result + ((count1B == null) ? 0 : count1B.hashCode());
		result = prime * result + ((count2 == null) ? 0 : count2.hashCode());
		result = prime * result + ((count3 == null) ? 0 : count3.hashCode());
		result = prime * result + ((countX == null) ? 0 : countX.hashCode());
		result = prime * result + ((dateEnd == null) ? 0 : dateEnd.hashCode());
		result = prime * result + ((dateIni == null) ? 0 : dateIni.hashCode());
		result = prime * result + (flagColor ? 1231 : 1237);
		result = prime * result + ((lgobe == null) ? 0 : lgobe.hashCode());
		result = prime * result + ((lgort == null) ? 0 : lgort.hashCode());
		result = prime * result + ((lgpla == null) ? 0 : lgpla.hashCode());
		result = prime * result + ((matnr == null) ? 0 : matnr.hashCode());
		result = prime * result + ((matnrD == null) ? 0 : matnrD.hashCode());
		result = prime * result + ((measureUnit == null) ? 0 : measureUnit.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((pkAsgId == null) ? 0 : pkAsgId.hashCode());
		result = prime * result + ((prodDate == null) ? 0 : prodDate.hashCode());
		result = prime * result + ((vhilm == null) ? 0 : vhilm.hashCode());
		result = prime * result + ((vhilmCount == null) ? 0 : vhilmCount.hashCode());
		result = prime * result + ((zoneD == null) ? 0 : zoneD.hashCode());
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
		ConciliationPositionBean other = (ConciliationPositionBean) obj;
		if (count1A == null) {
			if (other.count1A != null)
				return false;
		} else if (!count1A.equals(other.count1A))
			return false;
		if (count1B == null) {
			if (other.count1B != null)
				return false;
		} else if (!count1B.equals(other.count1B))
			return false;
		if (count2 == null) {
			if (other.count2 != null)
				return false;
		} else if (!count2.equals(other.count2))
			return false;
		if (count3 == null) {
			if (other.count3 != null)
				return false;
		} else if (!count3.equals(other.count3))
			return false;
		if (countX == null) {
			if (other.countX != null)
				return false;
		} else if (!countX.equals(other.countX))
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
		if (flagColor != other.flagColor)
			return false;
		if (lgobe == null) {
			if (other.lgobe != null)
				return false;
		} else if (!lgobe.equals(other.lgobe))
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
		if (matnr == null) {
			if (other.matnr != null)
				return false;
		} else if (!matnr.equals(other.matnr))
			return false;
		if (matnrD == null) {
			if (other.matnrD != null)
				return false;
		} else if (!matnrD.equals(other.matnrD))
			return false;
		if (measureUnit == null) {
			if (other.measureUnit != null)
				return false;
		} else if (!measureUnit.equals(other.measureUnit))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (pkAsgId == null) {
			if (other.pkAsgId != null)
				return false;
		} else if (!pkAsgId.equals(other.pkAsgId))
			return false;
		if (prodDate == null) {
			if (other.prodDate != null)
				return false;
		} else if (!prodDate.equals(other.prodDate))
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
		if (zoneD == null) {
			if (other.zoneD != null)
				return false;
		} else if (!zoneD.equals(other.zoneD))
			return false;
		if (zoneId == null) {
			if (other.zoneId != null)
				return false;
		} else if (!zoneId.equals(other.zoneId))
			return false;
		return true;
	}

}
