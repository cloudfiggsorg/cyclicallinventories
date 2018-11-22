package com.gmodelo.beans;

import com.sap.conn.jco.JCoTable;

public class E_Error_SapEntity {

	String type;
	String languaje;
	String message;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLanguaje() {
		return languaje;
	}

	public void setLanguaje(String languaje) {
		this.languaje = languaje;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public E_Error_SapEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public E_Error_SapEntity(JCoTable errorTable) {
		super();
		this.type = errorTable.getString("TYPE");
		this.languaje = errorTable.getString("LANGUAJE");
		this.message = errorTable.getString("MESSAGE");
	}

	public E_Error_SapEntity(String type, String languaje, String message) {
		super();
		this.type = type;
		this.languaje = languaje;
		this.message = message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((languaje == null) ? 0 : languaje.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		E_Error_SapEntity other = (E_Error_SapEntity) obj;
		if (languaje == null) {
			if (other.languaje != null)
				return false;
		} else if (!languaje.equals(other.languaje))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "E_Error_SapEntity [type=" + type + ", languaje=" + languaje + ", message=" + message + "]";
	}

}
