package com.gmodelo.structure;

import java.util.List;

import com.gmodelo.beans.ZIACST_I360_OBJECTDATA_SapEntity;

public class ZIACMF_I360_EXT_SIS_CLAS {

	List<ZIACST_I360_OBJECTDATA_SapEntity> objectData;

	public ZIACMF_I360_EXT_SIS_CLAS() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ZIACMF_I360_EXT_SIS_CLAS(List<ZIACST_I360_OBJECTDATA_SapEntity> objectData) {
		super();
		this.objectData = objectData;
	}

	@Override
	public String toString() {
		return "ZIACMF_I360_EXT_SIS_CLAS [objectData=" + objectData + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objectData == null) ? 0 : objectData.hashCode());
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
		ZIACMF_I360_EXT_SIS_CLAS other = (ZIACMF_I360_EXT_SIS_CLAS) obj;
		if (objectData == null) {
			if (other.objectData != null)
				return false;
		} else if (!objectData.equals(other.objectData))
			return false;
		return true;
	}

	public List<ZIACST_I360_OBJECTDATA_SapEntity> getObjectData() {
		return objectData;
	}

	public void setObjectData(List<ZIACST_I360_OBJECTDATA_SapEntity> objectData) {
		this.objectData = objectData;
	}

}
