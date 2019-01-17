package com.gmodelo.cyclicinventories.structure;

import java.util.List;

import com.gmodelo.cyclicinventories.beans.E_Error_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mbew_SapEntity;

public class ZIACMF_MBEW {

	List<E_Mbew_SapEntity> eMbewSapEntities;

	private E_Error_SapEntity eError_SapEntities;

	public List<E_Mbew_SapEntity> geteMbewSapEntities() {
		return eMbewSapEntities;
	}

	public void seteMbewSapEntities(List<E_Mbew_SapEntity> eMbewSapEntities) {
		this.eMbewSapEntities = eMbewSapEntities;
	}

	public E_Error_SapEntity geteError_SapEntities() {
		return eError_SapEntities;
	}

	public void seteError_SapEntities(E_Error_SapEntity eError_SapEntities) {
		this.eError_SapEntities = eError_SapEntities;
	}

	public ZIACMF_MBEW() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ZIACMF_MBEW(List<E_Mbew_SapEntity> eMbewSapEntities, E_Error_SapEntity eError_SapEntities) {
		super();
		this.eMbewSapEntities = eMbewSapEntities;
		this.eError_SapEntities = eError_SapEntities;
	}

	@Override
	public String toString() {
		return "ZIACMF_MBEW [eMbewSapEntities=" + eMbewSapEntities + ", eError_SapEntities=" + eError_SapEntities + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eError_SapEntities == null) ? 0 : eError_SapEntities.hashCode());
		result = prime * result + ((eMbewSapEntities == null) ? 0 : eMbewSapEntities.hashCode());
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
		ZIACMF_MBEW other = (ZIACMF_MBEW) obj;
		if (eError_SapEntities == null) {
			if (other.eError_SapEntities != null)
				return false;
		} else if (!eError_SapEntities.equals(other.eError_SapEntities))
			return false;
		if (eMbewSapEntities == null) {
			if (other.eMbewSapEntities != null)
				return false;
		} else if (!eMbewSapEntities.equals(other.eMbewSapEntities))
			return false;
		return true;
	}

}
