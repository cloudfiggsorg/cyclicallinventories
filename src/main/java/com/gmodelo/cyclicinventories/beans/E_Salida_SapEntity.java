package com.gmodelo.cyclicinventories.beans;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class E_Salida_SapEntity {

	private String nlpla;
	private String matnr;
	private String nistm;
	private String vistm;
	private String meins;
	private String qdatu;
	private String qzeit;

	public String getNlpla() {
		return nlpla;
	}

	public void setNlpla(String nlpla) {
		this.nlpla = nlpla;
	}

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}

	public String getNistm() {
		return nistm;
	}

	public void setNistm(String nistm) {
		this.nistm = nistm;
	}

	public String getVistm() {
		return vistm;
	}

	public void setVistm(String vistm) {
		this.vistm = vistm;
	}

	public String getMeins() {
		return meins;
	}

	public void setMeins(String meins) {
		this.meins = meins;
	}

	public String getQdatu() {
		return qdatu;
	}

	public void setQdatu(String qdatu) {
		this.qdatu = qdatu;
	}

	public String getQzeit() {
		return qzeit;
	}

	public void setQzeit(String qzeit) {
		this.qzeit = qzeit;
	}

	public E_Salida_SapEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public E_Salida_SapEntity(JCoTable jcoTable) throws JCoException {
		super();
		this.nlpla = jcoTable.getString("NLPLA");
		this.matnr = jcoTable.getString("MATNR");
		this.nistm = jcoTable.getString("NISTM");
		this.vistm = jcoTable.getString("VISTM");
		this.meins = jcoTable.getString("MEINS");
		this.qdatu = jcoTable.getString("QDATU");
		this.qzeit = jcoTable.getString("QZEIT");
	}

	public E_Salida_SapEntity(String nlpla, String matnr, String nistm, String vistm, String meins, String qdatu,
			String qzeit) {
		super();
		this.nlpla = nlpla;
		this.matnr = matnr;
		this.nistm = nistm;
		this.vistm = vistm;
		this.meins = meins;
		this.qdatu = qdatu;
		this.qzeit = qzeit;
	}

	@Override
	public String toString() {
		return "E_Salida_SapEntity [nlpla=" + nlpla + ", matnr=" + matnr + ", nistm=" + nistm + ", vistm=" + vistm
				+ ", meins=" + meins + ", qdatu=" + qdatu + ", qzeit=" + qzeit + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matnr == null) ? 0 : matnr.hashCode());
		result = prime * result + ((meins == null) ? 0 : meins.hashCode());
		result = prime * result + ((nistm == null) ? 0 : nistm.hashCode());
		result = prime * result + ((nlpla == null) ? 0 : nlpla.hashCode());
		result = prime * result + ((qdatu == null) ? 0 : qdatu.hashCode());
		result = prime * result + ((qzeit == null) ? 0 : qzeit.hashCode());
		result = prime * result + ((vistm == null) ? 0 : vistm.hashCode());
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
		E_Salida_SapEntity other = (E_Salida_SapEntity) obj;
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
		if (nistm == null) {
			if (other.nistm != null)
				return false;
		} else if (!nistm.equals(other.nistm))
			return false;
		if (nlpla == null) {
			if (other.nlpla != null)
				return false;
		} else if (!nlpla.equals(other.nlpla))
			return false;
		if (qdatu == null) {
			if (other.qdatu != null)
				return false;
		} else if (!qdatu.equals(other.qdatu))
			return false;
		if (qzeit == null) {
			if (other.qzeit != null)
				return false;
		} else if (!qzeit.equals(other.qzeit))
			return false;
		if (vistm == null) {
			if (other.vistm != null)
				return false;
		} else if (!vistm.equals(other.vistm))
			return false;
		return true;
	}

}
