package com.gmodelo.beans;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class E_Xtab6_SapEntity {

	String werks;
	String matnr;
	String menge;
	String meins;
	String dmbtr;
	String waers;
	String netwr;
	String bwaer;
	String ebeln;
	String ebelp;
	String sobkz;
	String pstyp;
	String bstmg;
	String bstme;
	String reswk;
	String bsakz;
	String lgort;
	String reslo;

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = matnr;
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

	public String getDmbtr() {
		return dmbtr;
	}

	public void setDmbtr(String dmbtr) {
		this.dmbtr = dmbtr;
	}

	public String getWaers() {
		return waers;
	}

	public void setWaers(String waers) {
		this.waers = waers;
	}

	public String getNetwr() {
		return netwr;
	}

	public void setNetwr(String netwr) {
		this.netwr = netwr;
	}

	public String getBwaer() {
		return bwaer;
	}

	public void setBwaer(String bwaer) {
		this.bwaer = bwaer;
	}

	public String getEbeln() {
		return ebeln;
	}

	public void setEbeln(String ebeln) {
		this.ebeln = ebeln;
	}

	public String getEbelp() {
		return ebelp;
	}

	public void setEbelp(String ebelp) {
		this.ebelp = ebelp;
	}

	public String getSobkz() {
		return sobkz;
	}

	public void setSobkz(String sobkz) {
		this.sobkz = sobkz;
	}

	public String getPstyp() {
		return pstyp;
	}

	public void setPstyp(String pstyp) {
		this.pstyp = pstyp;
	}

	public String getBstmg() {
		return bstmg;
	}

	public void setBstmg(String bstmg) {
		this.bstmg = bstmg;
	}

	public String getBstme() {
		return bstme;
	}

	public void setBstme(String bstme) {
		this.bstme = bstme;
	}

	public String getReswk() {
		return reswk;
	}

	public void setReswk(String reswk) {
		this.reswk = reswk;
	}

	public String getBsakz() {
		return bsakz;
	}

	public void setBsakz(String bsakz) {
		this.bsakz = bsakz;
	}

	public String getLgort() {
		return lgort;
	}

	public void setLgort(String lgort) {
		this.lgort = lgort;
	}

	public String getReslo() {
		return reslo;
	}

	public void setReslo(String reslo) {
		this.reslo = reslo;
	}

	public E_Xtab6_SapEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public E_Xtab6_SapEntity(JCoTable jcoTable) throws JCoException {
		super();
		this.werks = jcoTable.getString("WERKS");
		this.matnr = jcoTable.getString("MATNR");
		this.menge = jcoTable.getString("MENGE");
		this.meins = jcoTable.getString("MEINS");
		this.dmbtr = jcoTable.getString("DMBTR");
		this.waers = jcoTable.getString("WAERS");
		this.netwr = jcoTable.getString("NETWR");
		this.bwaer = jcoTable.getString("BWAER");
		this.ebeln = jcoTable.getString("EBELN");
		this.ebelp = jcoTable.getString("EBELP");
		this.sobkz = jcoTable.getString("SOBKZ");
		this.pstyp = jcoTable.getString("PSTYP");
		this.bstmg = jcoTable.getString("BSTMG");
		this.bstme = jcoTable.getString("BSTME");
		this.reswk = jcoTable.getString("RESWK");
		this.bsakz = jcoTable.getString("BSAKZ");
		this.lgort = jcoTable.getString("LGORT");
		this.reslo = jcoTable.getString("RESLO");
	}

	public E_Xtab6_SapEntity(String werks, String matnr, String menge, String meins, String dmbtr, String waers,
			String netwr, String bwaer, String ebeln, String ebelp, String sobkz, String pstyp, String bstmg,
			String bstme, String reswk, String bsakz, String lgort, String reslo) {
		super();
		this.werks = werks;
		this.matnr = matnr;
		this.menge = menge;
		this.meins = meins;
		this.dmbtr = dmbtr;
		this.waers = waers;
		this.netwr = netwr;
		this.bwaer = bwaer;
		this.ebeln = ebeln;
		this.ebelp = ebelp;
		this.sobkz = sobkz;
		this.pstyp = pstyp;
		this.bstmg = bstmg;
		this.bstme = bstme;
		this.reswk = reswk;
		this.bsakz = bsakz;
		this.lgort = lgort;
		this.reslo = reslo;
	}

	@Override
	public String toString() {
		return "E_Xtab6_SapEntity [werks=" + werks + ", matnr=" + matnr + ", menge=" + menge + ", meins=" + meins
				+ ", dmbtr=" + dmbtr + ", waers=" + waers + ", netwr=" + netwr + ", bwaer=" + bwaer + ", ebeln=" + ebeln
				+ ", ebelp=" + ebelp + ", sobkz=" + sobkz + ", pstyp=" + pstyp + ", bstmg=" + bstmg + ", bstme=" + bstme
				+ ", reswk=" + reswk + ", bsakz=" + bsakz + ", lgort=" + lgort + ", reslo=" + reslo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bsakz == null) ? 0 : bsakz.hashCode());
		result = prime * result + ((bstme == null) ? 0 : bstme.hashCode());
		result = prime * result + ((bstmg == null) ? 0 : bstmg.hashCode());
		result = prime * result + ((bwaer == null) ? 0 : bwaer.hashCode());
		result = prime * result + ((dmbtr == null) ? 0 : dmbtr.hashCode());
		result = prime * result + ((ebeln == null) ? 0 : ebeln.hashCode());
		result = prime * result + ((ebelp == null) ? 0 : ebelp.hashCode());
		result = prime * result + ((lgort == null) ? 0 : lgort.hashCode());
		result = prime * result + ((matnr == null) ? 0 : matnr.hashCode());
		result = prime * result + ((meins == null) ? 0 : meins.hashCode());
		result = prime * result + ((menge == null) ? 0 : menge.hashCode());
		result = prime * result + ((netwr == null) ? 0 : netwr.hashCode());
		result = prime * result + ((pstyp == null) ? 0 : pstyp.hashCode());
		result = prime * result + ((reslo == null) ? 0 : reslo.hashCode());
		result = prime * result + ((reswk == null) ? 0 : reswk.hashCode());
		result = prime * result + ((sobkz == null) ? 0 : sobkz.hashCode());
		result = prime * result + ((waers == null) ? 0 : waers.hashCode());
		result = prime * result + ((werks == null) ? 0 : werks.hashCode());
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
		E_Xtab6_SapEntity other = (E_Xtab6_SapEntity) obj;
		if (bsakz == null) {
			if (other.bsakz != null)
				return false;
		} else if (!bsakz.equals(other.bsakz))
			return false;
		if (bstme == null) {
			if (other.bstme != null)
				return false;
		} else if (!bstme.equals(other.bstme))
			return false;
		if (bstmg == null) {
			if (other.bstmg != null)
				return false;
		} else if (!bstmg.equals(other.bstmg))
			return false;
		if (bwaer == null) {
			if (other.bwaer != null)
				return false;
		} else if (!bwaer.equals(other.bwaer))
			return false;
		if (dmbtr == null) {
			if (other.dmbtr != null)
				return false;
		} else if (!dmbtr.equals(other.dmbtr))
			return false;
		if (ebeln == null) {
			if (other.ebeln != null)
				return false;
		} else if (!ebeln.equals(other.ebeln))
			return false;
		if (ebelp == null) {
			if (other.ebelp != null)
				return false;
		} else if (!ebelp.equals(other.ebelp))
			return false;
		if (lgort == null) {
			if (other.lgort != null)
				return false;
		} else if (!lgort.equals(other.lgort))
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
		if (menge == null) {
			if (other.menge != null)
				return false;
		} else if (!menge.equals(other.menge))
			return false;
		if (netwr == null) {
			if (other.netwr != null)
				return false;
		} else if (!netwr.equals(other.netwr))
			return false;
		if (pstyp == null) {
			if (other.pstyp != null)
				return false;
		} else if (!pstyp.equals(other.pstyp))
			return false;
		if (reslo == null) {
			if (other.reslo != null)
				return false;
		} else if (!reslo.equals(other.reslo))
			return false;
		if (reswk == null) {
			if (other.reswk != null)
				return false;
		} else if (!reswk.equals(other.reswk))
			return false;
		if (sobkz == null) {
			if (other.sobkz != null)
				return false;
		} else if (!sobkz.equals(other.sobkz))
			return false;
		if (waers == null) {
			if (other.waers != null)
				return false;
		} else if (!waers.equals(other.waers))
			return false;
		if (werks == null) {
			if (other.werks != null)
				return false;
		} else if (!werks.equals(other.werks))
			return false;
		return true;
	}

}
