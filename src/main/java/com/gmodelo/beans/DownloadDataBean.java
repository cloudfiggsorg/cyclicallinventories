package com.gmodelo.beans;

import java.io.Serializable;
import java.util.List;

import com.gmodelo.structure.ZIACMF_I360_EXT_SIS_CLAS;

public class DownloadDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4032005834320492589L;
	List<MaterialTarimasBean> listMaterialTarimas;
	List<MobileMaterialBean> listMobileMaterial;
	ZIACMF_I360_EXT_SIS_CLAS ziacmf_I360_EXT_SIS_CLAS;

	public List<MaterialTarimasBean> getListMaterialTarimas() {
		return listMaterialTarimas;
	}

	public void setListMaterialTarimas(List<MaterialTarimasBean> listMaterialTarimas) {
		this.listMaterialTarimas = listMaterialTarimas;
	}

	public List<MobileMaterialBean> getListMobileMaterial() {
		return listMobileMaterial;
	}

	public void setListMobileMaterial(List<MobileMaterialBean> listMobileMaterial) {
		this.listMobileMaterial = listMobileMaterial;
	}

	public ZIACMF_I360_EXT_SIS_CLAS getZiacmf_I360_EXT_SIS_CLAS() {
		return ziacmf_I360_EXT_SIS_CLAS;
	}

	public void setZiacmf_I360_EXT_SIS_CLAS(ZIACMF_I360_EXT_SIS_CLAS ziacmf_I360_EXT_SIS_CLAS) {
		this.ziacmf_I360_EXT_SIS_CLAS = ziacmf_I360_EXT_SIS_CLAS;
	}

	public DownloadDataBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DownloadDataBean(List<MaterialTarimasBean> listMaterialTarimas, List<MobileMaterialBean> listMobileMaterial,
			ZIACMF_I360_EXT_SIS_CLAS ziacmf_I360_EXT_SIS_CLAS) {
		super();
		this.listMaterialTarimas = listMaterialTarimas;
		this.listMobileMaterial = listMobileMaterial;
		this.ziacmf_I360_EXT_SIS_CLAS = ziacmf_I360_EXT_SIS_CLAS;
	}

	@Override
	public String toString() {
		return "DownloadDataBean [listMaterialTarimas=" + listMaterialTarimas + ", listMobileMaterial="
				+ listMobileMaterial + ", ziacmf_I360_EXT_SIS_CLAS=" + ziacmf_I360_EXT_SIS_CLAS + "]";
	}

	
	

}
