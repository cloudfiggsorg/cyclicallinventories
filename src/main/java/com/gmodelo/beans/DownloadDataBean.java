package com.gmodelo.beans;

import java.io.Serializable;
import java.util.List;

public class DownloadDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4032005834320492589L;
	List<MaterialTarimasBean> listMaterialTarimas;
	List<MobileMaterialBean> listMobileMaterial;

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

	public DownloadDataBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DownloadDataBean(List<MaterialTarimasBean> listMaterialTarimas,
			List<MobileMaterialBean> listMobileMaterial) {
		super();
		this.listMaterialTarimas = listMaterialTarimas;
		this.listMobileMaterial = listMobileMaterial;
	}

	@Override
	public String toString() {
		return "DownloadDataBean [listMaterialTarimas=" + listMaterialTarimas + ", listMobileMaterial="
				+ listMobileMaterial + "]";
	}

}
