package com.gmodelo.cyclicinventories.structure;

import java.util.List;

import com.gmodelo.cyclicinventories.beans.E_Error_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Xtab6_SapEntity;

public class ZIACMF_I360_INV_MOV_3 {

	private List<E_Xtab6_SapEntity> xtab6_SapEntities;
	private E_Error_SapEntity eError_SapEntities;

	public List<E_Xtab6_SapEntity> getXtab6_SapEntities() {
		return xtab6_SapEntities;
	}

	public void setXtab6_SapEntities(List<E_Xtab6_SapEntity> xtab6_SapEntities) {
		this.xtab6_SapEntities = xtab6_SapEntities;
	}

	public E_Error_SapEntity geteError_SapEntities() {
		return eError_SapEntities;
	}

	public void seteError_SapEntities(E_Error_SapEntity eError_SapEntities) {
		this.eError_SapEntities = eError_SapEntities;
	}

	@Override
	public String toString() {
		return "ZIACMF_I360_INV_MOV_3 [xtab6_SapEntities=" + xtab6_SapEntities + ", eError_SapEntities="
				+ eError_SapEntities + "]";
	}

	public ZIACMF_I360_INV_MOV_3(List<E_Xtab6_SapEntity> xtab6_SapEntities, E_Error_SapEntity eError_SapEntities) {
		super();
		this.xtab6_SapEntities = xtab6_SapEntities;
		this.eError_SapEntities = eError_SapEntities;
	}

	public ZIACMF_I360_INV_MOV_3() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eError_SapEntities == null) ? 0 : eError_SapEntities.hashCode());
		result = prime * result + ((xtab6_SapEntities == null) ? 0 : xtab6_SapEntities.hashCode());
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
		ZIACMF_I360_INV_MOV_3 other = (ZIACMF_I360_INV_MOV_3) obj;
		if (eError_SapEntities == null) {
			if (other.eError_SapEntities != null)
				return false;
		} else if (!eError_SapEntities.equals(other.eError_SapEntities))
			return false;
		if (xtab6_SapEntities == null) {
			if (other.xtab6_SapEntities != null)
				return false;
		} else if (!xtab6_SapEntities.equals(other.xtab6_SapEntities))
			return false;
		return true;
	}

	
}
