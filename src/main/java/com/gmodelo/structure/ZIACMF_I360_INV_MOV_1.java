package com.gmodelo.structure;

import java.util.List;

import com.gmodelo.beans.E_Error_SapEntity;
import com.gmodelo.beans.E_Mseg_SapEntity;

public class ZIACMF_I360_INV_MOV_1 {

	List<E_Mseg_SapEntity> eMseg_SapEntities;
	E_Error_SapEntity eError_SapEntities;

	public List<E_Mseg_SapEntity> geteMseg_SapEntities() {
		return eMseg_SapEntities;
	}

	public void seteMseg_SapEntities(List<E_Mseg_SapEntity> eMseg_SapEntities) {
		this.eMseg_SapEntities = eMseg_SapEntities;
	}

	public E_Error_SapEntity geteError_SapEntities() {
		return eError_SapEntities;
	}

	public void seteError_SapEntities(E_Error_SapEntity eError_SapEntities) {
		this.eError_SapEntities = eError_SapEntities;
	}

	public ZIACMF_I360_INV_MOV_1() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ZIACMF_I360_INV_MOV_1(List<E_Mseg_SapEntity> eMseg_SapEntities, E_Error_SapEntity eError_SapEntities) {
		super();
		this.eMseg_SapEntities = eMseg_SapEntities;
		this.eError_SapEntities = eError_SapEntities;
	}

	@Override
	public String toString() {
		return "ZIACMF_I360_INV_MOV_1 [eMseg_SapEntities=" + eMseg_SapEntities + ", eError_SapEntities="
				+ eError_SapEntities + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eError_SapEntities == null) ? 0 : eError_SapEntities.hashCode());
		result = prime * result + ((eMseg_SapEntities == null) ? 0 : eMseg_SapEntities.hashCode());
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
		ZIACMF_I360_INV_MOV_1 other = (ZIACMF_I360_INV_MOV_1) obj;
		if (eError_SapEntities == null) {
			if (other.eError_SapEntities != null)
				return false;
		} else if (!eError_SapEntities.equals(other.eError_SapEntities))
			return false;
		if (eMseg_SapEntities == null) {
			if (other.eMseg_SapEntities != null)
				return false;
		} else if (!eMseg_SapEntities.equals(other.eMseg_SapEntities))
			return false;
		return true;
	}

}
