package com.gmodelo.cyclicinventories.beans;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class E_Mard_SapEntity {
	
	private String matnr;
	private String werks;
	private String lgort;
	private String labst;
	private String umlme;
	private String insme;
	private String einme;
	private String speme;
	private String retme;

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

	public String getLabst() {
		return labst;
	}

	public void setLabst(String labst) {
		this.labst = labst;
	}

	public String getUmlme() {
		return umlme;
	}

	public void setUmlme(String umlme) {
		this.umlme = umlme;
	}

	public String getInsme() {
		return insme;
	}

	public void setInsme(String insme) {
		this.insme = insme;
	}

	public String getEinme() {
		return einme;
	}

	public void setEinme(String einme) {
		this.einme = einme;
	}

	public String getSpeme() {
		return speme;
	}

	public void setSpeme(String speme) {
		this.speme = speme;
	}

	public String getRetme() {
		return retme;
	}

	public void setRetme(String retme) {
		this.retme = retme;
	}

	public E_Mard_SapEntity(String matnr, String werks, String lgort, String labst, String umlme, String insme,
			String einme, String speme, String retme) {
		super();
		this.matnr = matnr;
		this.werks = werks;
		this.lgort = lgort;
		this.labst = labst;
		this.umlme = umlme;
		this.insme = insme;
		this.einme = einme;
		this.speme = speme;
		this.retme = retme;
	}

	public E_Mard_SapEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public E_Mard_SapEntity(JCoTable jcoTable) throws JCoException {
		super();
		this.matnr = jcoTable.getString("MATNR");
		this.werks = jcoTable.getString("WERKS");
		this.lgort = jcoTable.getString("LGORT");
		this.labst = jcoTable.getString("LABST");
		this.umlme = jcoTable.getString("UMLME");
		this.insme = jcoTable.getString("INSME");
		this.einme = jcoTable.getString("EINME");
		this.speme = jcoTable.getString("SPEME");
		this.retme = jcoTable.getString("RETME");
	}

	@Override
	public String toString() {
		return "E_MARD [matnr=" + matnr + ", werks=" + werks + ", lgort=" + lgort + ", labst=" + labst + ", umlme="
				+ umlme + ", insme=" + insme + ", einme=" + einme + ", speme=" + speme + ", retme=" + retme + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((einme == null) ? 0 : einme.hashCode());
		result = prime * result + ((insme == null) ? 0 : insme.hashCode());
		result = prime * result + ((labst == null) ? 0 : labst.hashCode());
		result = prime * result + ((lgort == null) ? 0 : lgort.hashCode());
		result = prime * result + ((matnr == null) ? 0 : matnr.hashCode());
		result = prime * result + ((retme == null) ? 0 : retme.hashCode());
		result = prime * result + ((speme == null) ? 0 : speme.hashCode());
		result = prime * result + ((umlme == null) ? 0 : umlme.hashCode());
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
		E_Mard_SapEntity other = (E_Mard_SapEntity) obj;
		if (einme == null) {
			if (other.einme != null)
				return false;
		} else if (!einme.equals(other.einme))
			return false;
		if (insme == null) {
			if (other.insme != null)
				return false;
		} else if (!insme.equals(other.insme))
			return false;
		if (labst == null) {
			if (other.labst != null)
				return false;
		} else if (!labst.equals(other.labst))
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
		if (retme == null) {
			if (other.retme != null)
				return false;
		} else if (!retme.equals(other.retme))
			return false;
		if (speme == null) {
			if (other.speme != null)
				return false;
		} else if (!speme.equals(other.speme))
			return false;
		if (umlme == null) {
			if (other.umlme != null)
				return false;
		} else if (!umlme.equals(other.umlme))
			return false;
		if (werks == null) {
			if (other.werks != null)
				return false;
		} else if (!werks.equals(other.werks))
			return false;
		return true;
	}

}
