package com.gmodelo.cyclicinventories.beans;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MaterialExplosionBean {

	String matnr;
	String bmein;
	String bmeng;
	String menge;
	String bmcal;
	String idnrk;
	String meins;
	String lgort;

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}

	public String getBmein() {
		return bmein;
	}

	public void setBmein(String bmein) {
		this.bmein = bmein;
	}

	public String getBmeng() {
		return bmeng;
	}

	public void setBmeng(String bmeng) {
		this.bmeng = bmeng;
	}

	public String getMenge() {
		return menge;
	}

	public void setMenge(String menge) {
		this.menge = menge;
	}

	public String getBmcal() {
		return bmcal;
	}

	public void setBmcal(String bmcal) {
		this.bmcal = bmcal;
	}

	public String getIdnrk() {
		return idnrk;
	}

	public void setIdnrk(String idnrk) {
		this.idnrk = idnrk;
	}

	public String getMeins() {
		return meins;
	}

	public void setMeins(String meins) {
		this.meins = meins;
	}

	public String getLgort() {
		return lgort;
	}

	public void setLgort(String lgort) {
		this.lgort = lgort;
	}

	public MaterialExplosionBean(String matnr, String bmein, String bmeng, String menge, String bmcal, String idnrk,
			String meins, String lgort) {
		super();
		this.matnr = matnr;
		this.bmein = bmein;
		this.bmeng = bmeng;
		this.menge = menge;
		this.bmcal = bmcal;
		this.idnrk = idnrk;
		this.meins = meins;
		this.lgort = lgort;
	}

	public MaterialExplosionBean(ResultSet rs) throws SQLException {
		super();
		this.matnr = rs.getString("DIP_MATNR");
		this.bmein = rs.getString("BMEIN");
		this.bmeng = rs.getString("BMENG");
		this.menge = rs.getString("MENGE");
		this.bmcal = rs.getString("BMCAL");
		this.idnrk = rs.getString("IDNRK");
		this.meins = rs.getString("MEINS");
		this.lgort = rs.getString("EX_LGORT");

	}

	public MaterialExplosionBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "MaterialExplosionBean [matnr=" + matnr + ", bmein=" + bmein + ", bmeng=" + bmeng + ", menge=" + menge
				+ ", bmcal=" + bmcal + ", idnrk=" + idnrk + ", meins=" + meins + ", lgort=" + lgort + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bmcal == null) ? 0 : bmcal.hashCode());
		result = prime * result + ((bmein == null) ? 0 : bmein.hashCode());
		result = prime * result + ((bmeng == null) ? 0 : bmeng.hashCode());
		result = prime * result + ((idnrk == null) ? 0 : idnrk.hashCode());
		result = prime * result + ((lgort == null) ? 0 : lgort.hashCode());
		result = prime * result + ((matnr == null) ? 0 : matnr.hashCode());
		result = prime * result + ((meins == null) ? 0 : meins.hashCode());
		result = prime * result + ((menge == null) ? 0 : menge.hashCode());
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
		MaterialExplosionBean other = (MaterialExplosionBean) obj;
		if (bmcal == null) {
			if (other.bmcal != null)
				return false;
		} else if (!bmcal.equals(other.bmcal))
			return false;
		if (bmein == null) {
			if (other.bmein != null)
				return false;
		} else if (!bmein.equals(other.bmein))
			return false;
		if (bmeng == null) {
			if (other.bmeng != null)
				return false;
		} else if (!bmeng.equals(other.bmeng))
			return false;
		if (idnrk == null) {
			if (other.idnrk != null)
				return false;
		} else if (!idnrk.equals(other.idnrk))
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
		return true;
	}

}
