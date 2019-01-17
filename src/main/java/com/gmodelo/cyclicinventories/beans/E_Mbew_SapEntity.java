package com.gmodelo.cyclicinventories.beans;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class E_Mbew_SapEntity {
	private String matnr;
	private String bwkey;
	private String zprecio;

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}

	public String getBwkey() {
		return bwkey;
	}

	public void setBwkey(String bwkey) {
		this.bwkey = bwkey;
	}

	public String getZprecio() {
		return zprecio;
	}

	public void setZprecio(String zprecio) {
		this.zprecio = zprecio;
	}

	public E_Mbew_SapEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public E_Mbew_SapEntity(String matnr, String bwkey, String zprecio) {
		super();
		this.matnr = matnr;
		this.bwkey = bwkey;
		this.zprecio = zprecio;
	}
	
	public E_Mbew_SapEntity(JCoTable jcoTable) throws JCoException {
		super();
		this.matnr = jcoTable.getString("MATNR");
		this.bwkey = jcoTable.getString("BWKEY");
		this.zprecio = jcoTable.getString("ZPRECIO");
	}


	@Override
	public String toString() {
		return "E_Mbew_SapEntity [matnr=" + matnr + ", bwkey=" + bwkey + ", zprecio=" + zprecio + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bwkey == null) ? 0 : bwkey.hashCode());
		result = prime * result + ((matnr == null) ? 0 : matnr.hashCode());
		result = prime * result + ((zprecio == null) ? 0 : zprecio.hashCode());
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
		E_Mbew_SapEntity other = (E_Mbew_SapEntity) obj;
		if (bwkey == null) {
			if (other.bwkey != null)
				return false;
		} else if (!bwkey.equals(other.bwkey))
			return false;
		if (matnr == null) {
			if (other.matnr != null)
				return false;
		} else if (!matnr.equals(other.matnr))
			return false;
		if (zprecio == null) {
			if (other.zprecio != null)
				return false;
		} else if (!zprecio.equals(other.zprecio))
			return false;
		return true;
	}

}
