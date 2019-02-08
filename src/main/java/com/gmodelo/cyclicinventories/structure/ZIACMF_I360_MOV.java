package com.gmodelo.cyclicinventories.structure;

import java.util.List;

import com.gmodelo.cyclicinventories.beans.E_Error_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Salida_SapEntity;

public class ZIACMF_I360_MOV {

	private E_Error_SapEntity eError_SapEntities;
	private List<E_Salida_SapEntity> eSalida_SapEntities;

	public E_Error_SapEntity geteError_SapEntities() {
		return eError_SapEntities;
	}

	public void seteError_SapEntities(E_Error_SapEntity eError_SapEntities) {
		this.eError_SapEntities = eError_SapEntities;
	}

	public List<E_Salida_SapEntity> geteSalida_SapEntities() {
		return eSalida_SapEntities;
	}

	public void seteSalida_SapEntities(List<E_Salida_SapEntity> eSalida_SapEntities) {
		this.eSalida_SapEntities = eSalida_SapEntities;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eError_SapEntities == null) ? 0 : eError_SapEntities.hashCode());
		result = prime * result + ((eSalida_SapEntities == null) ? 0 : eSalida_SapEntities.hashCode());
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
		ZIACMF_I360_MOV other = (ZIACMF_I360_MOV) obj;
		if (eError_SapEntities == null) {
			if (other.eError_SapEntities != null)
				return false;
		} else if (!eError_SapEntities.equals(other.eError_SapEntities))
			return false;
		if (eSalida_SapEntities == null) {
			if (other.eSalida_SapEntities != null)
				return false;
		} else if (!eSalida_SapEntities.equals(other.eSalida_SapEntities))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZIACMF_I360_MOV [eError_SapEntities=" + eError_SapEntities + ", eSalida_SapEntities="
				+ eSalida_SapEntities + "]";
	}

	public ZIACMF_I360_MOV() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ZIACMF_I360_MOV(E_Error_SapEntity eError_SapEntities, List<E_Salida_SapEntity> eSalida_SapEntities) {
		super();
		this.eError_SapEntities = eError_SapEntities;
		this.eSalida_SapEntities = eSalida_SapEntities;
	}

}
