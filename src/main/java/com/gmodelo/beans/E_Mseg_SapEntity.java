package com.gmodelo.beans;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class E_Mseg_SapEntity {

	String mblnr;
	String zeile;
	String bwart;
	String matnr;
	String werks;
	String lgort;
	String insmk;
	String shkzg;
	String menge;
	String meins;
	String bukrs;
	String lgnum;
	String lgtyp;
	String lgpla;
	String budat_mkpf;
	String cputm_mkpf;

	public String getMblnr() {
		return mblnr;
	}

	public void setMblnr(String mblnr) {
		this.mblnr = mblnr;
	}

	public String getZeile() {
		return zeile;
	}

	public void setZeile(String zeile) {
		this.zeile = zeile;
	}

	public String getBwart() {
		return bwart;
	}

	public void setBwart(String bwart) {
		this.bwart = bwart;
	}

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public String getLgort() {
		return lgort;
	}

	public void setLgort(String lgort) {
		this.lgort = lgort;
	}

	public String getInsmk() {
		return insmk;
	}

	public void setInsmk(String insmk) {
		this.insmk = insmk;
	}

	public String getShkzg() {
		return shkzg;
	}

	public void setShkzg(String shkzg) {
		this.shkzg = shkzg;
	}

	public String getMenge() {
		return menge;
	}

	public void setMenge(String menge) {
		this.menge = menge;
	}

	public String getMeins() {
		return meins;
	}

	public void setMeins(String meins) {
		this.meins = meins;
	}

	public String getBukrs() {
		return bukrs;
	}

	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}

	public String getLgnum() {
		return lgnum;
	}

	public void setLgnum(String lgnum) {
		this.lgnum = lgnum;
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

	public String getBudat_mkpf() {
		return budat_mkpf;
	}

	public void setBudat_mkpf(String budat_mkpf) {
		this.budat_mkpf = budat_mkpf;
	}

	public String getCputm_mkpf() {
		return cputm_mkpf;
	}

	public void setCputm_mkpf(String cputm_mkpf) {
		this.cputm_mkpf = cputm_mkpf;
	}

	public E_Mseg_SapEntity(String mblnr, String zeile, String bwart, String matnr, String werks, String lgort,
			String insmk, String shkzg, String menge, String meins, String bukrs, String lgnum, String lgtyp,
			String lgpla, String budat_mkpf, String cputm_mkpf) {
		super();
		this.mblnr = mblnr;
		this.zeile = zeile;
		this.bwart = bwart;
		this.matnr = matnr;
		this.werks = werks;
		this.lgort = lgort;
		this.insmk = insmk;
		this.shkzg = shkzg;
		this.menge = menge;
		this.meins = meins;
		this.bukrs = bukrs;
		this.lgnum = lgnum;
		this.lgtyp = lgtyp;
		this.lgpla = lgpla;
		this.budat_mkpf = budat_mkpf;
		this.cputm_mkpf = cputm_mkpf;
	}

	public E_Mseg_SapEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public E_Mseg_SapEntity(JCoTable jcoTable) throws JCoException {
		super();
		this.mblnr = jcoTable.getString("MBLNR");
		this.zeile = jcoTable.getString("ZEILE");
		this.bwart = jcoTable.getString("BWART");
		this.matnr = jcoTable.getString("MATNR");
		this.werks = jcoTable.getString("WERKS");
		this.lgort = jcoTable.getString("LGORT");
		this.insmk = jcoTable.getString("INSMK");
		this.shkzg = jcoTable.getString("SHKZG");
		this.menge = jcoTable.getString("MENGE");
		this.meins = jcoTable.getString("MEINS");
		this.bukrs = jcoTable.getString("BUKRS");
		this.lgnum = jcoTable.getString("LGNUM");
		this.lgtyp = jcoTable.getString("LGTYP");
		this.lgpla = jcoTable.getString("LGPLA");
		this.budat_mkpf = jcoTable.getString("BUDAT_MKPF");
		this.cputm_mkpf = jcoTable.getString("CPUTM_MKPF");
	}

	@Override
	public String toString() {
		return "E_Mseg_SapEntity [mblnr=" + mblnr + ", zeile=" + zeile + ", bwart=" + bwart + ", matnr=" + matnr
				+ ", werks=" + werks + ", lgort=" + lgort + ", insmk=" + insmk + ", shkzg=" + shkzg + ", menge=" + menge
				+ ", meins=" + meins + ", bukrs=" + bukrs + ", lgnum=" + lgnum + ", lgtyp=" + lgtyp + ", lgpla=" + lgpla
				+ ", budat_mkpf=" + budat_mkpf + ", cputm_mkpf=" + cputm_mkpf + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((budat_mkpf == null) ? 0 : budat_mkpf.hashCode());
		result = prime * result + ((bukrs == null) ? 0 : bukrs.hashCode());
		result = prime * result + ((bwart == null) ? 0 : bwart.hashCode());
		result = prime * result + ((cputm_mkpf == null) ? 0 : cputm_mkpf.hashCode());
		result = prime * result + ((insmk == null) ? 0 : insmk.hashCode());
		result = prime * result + ((lgnum == null) ? 0 : lgnum.hashCode());
		result = prime * result + ((lgort == null) ? 0 : lgort.hashCode());
		result = prime * result + ((lgpla == null) ? 0 : lgpla.hashCode());
		result = prime * result + ((lgtyp == null) ? 0 : lgtyp.hashCode());
		result = prime * result + ((matnr == null) ? 0 : matnr.hashCode());
		result = prime * result + ((mblnr == null) ? 0 : mblnr.hashCode());
		result = prime * result + ((meins == null) ? 0 : meins.hashCode());
		result = prime * result + ((menge == null) ? 0 : menge.hashCode());
		result = prime * result + ((shkzg == null) ? 0 : shkzg.hashCode());
		result = prime * result + ((werks == null) ? 0 : werks.hashCode());
		result = prime * result + ((zeile == null) ? 0 : zeile.hashCode());
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
		E_Mseg_SapEntity other = (E_Mseg_SapEntity) obj;
		if (budat_mkpf == null) {
			if (other.budat_mkpf != null)
				return false;
		} else if (!budat_mkpf.equals(other.budat_mkpf))
			return false;
		if (bukrs == null) {
			if (other.bukrs != null)
				return false;
		} else if (!bukrs.equals(other.bukrs))
			return false;
		if (bwart == null) {
			if (other.bwart != null)
				return false;
		} else if (!bwart.equals(other.bwart))
			return false;
		if (cputm_mkpf == null) {
			if (other.cputm_mkpf != null)
				return false;
		} else if (!cputm_mkpf.equals(other.cputm_mkpf))
			return false;
		if (insmk == null) {
			if (other.insmk != null)
				return false;
		} else if (!insmk.equals(other.insmk))
			return false;
		if (lgnum == null) {
			if (other.lgnum != null)
				return false;
		} else if (!lgnum.equals(other.lgnum))
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
		if (mblnr == null) {
			if (other.mblnr != null)
				return false;
		} else if (!mblnr.equals(other.mblnr))
			return false;
		if (meins == null) {
			if (other.meins != null)
				return false;
		} else if (!meins.equals(other.meins))
			return false;
		if (menge == null) {
			if (other.menge != null)
				return false;
		} else if (!menge.equals(other.menge))
			return false;
		if (shkzg == null) {
			if (other.shkzg != null)
				return false;
		} else if (!shkzg.equals(other.shkzg))
			return false;
		if (werks == null) {
			if (other.werks != null)
				return false;
		} else if (!werks.equals(other.werks))
			return false;
		if (zeile == null) {
			if (other.zeile != null)
				return false;
		} else if (!zeile.equals(other.zeile))
			return false;
		return true;
	}

}
