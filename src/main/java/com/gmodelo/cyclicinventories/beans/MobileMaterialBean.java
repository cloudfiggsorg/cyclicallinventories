package com.gmodelo.cyclicinventories.beans;

import java.io.Serializable;

public class MobileMaterialBean implements Serializable {

	private static final long serialVersionUID = 5684316925363247131L;
	private String matnr;
	private String maktx;
	private String meins;
	private String meinh;
	private String umrez;
	private String umren;
	private String eannr;
	private String ean11;

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}

	public String getMaktx() {
		return maktx;
	}

	public void setMaktx(String maktx) {
		this.maktx = maktx;
	}

	public String getMeins() {
		return meins;
	}

	public void setMeins(String meins) {
		this.meins = meins;
	}

	public String getMeinh() {
		return meinh;
	}

	public void setMeinh(String meinh) {
		this.meinh = meinh;
	}

	public String getUmrez() {
		return umrez;
	}

	public void setUmrez(String umrez) {
		this.umrez = umrez;
	}

	public String getUmren() {
		return umren;
	}

	public void setUmren(String umren) {
		this.umren = umren;
	}

	public String getEannr() {
		return eannr;
	}

	public void setEannr(String eannr) {
		this.eannr = eannr;
	}

	public String getEan11() {
		return ean11;
	}

	public void setEan11(String ean11) {
		this.ean11 = ean11;
	}

	public MobileMaterialBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MobileMaterialBean(String matnr, String maktx, String meins, String meinh, String umrez, String umren,
			String eannr, String ean11) {
		super();
		this.matnr = matnr;
		this.maktx = maktx;
		this.meins = meins;
		this.meinh = meinh;
		this.umrez = umrez;
		this.umren = umren;
		this.eannr = eannr;
		this.ean11 = ean11;
	}

	@Override
	public String toString() {
		return "MobileMaterialBean [matnr=" + matnr + ", maktx=" + maktx + ", meins=" + meins + ", meinh=" + meinh
				+ ", umrez=" + umrez + ", umren=" + umren + ", eannr=" + eannr + ", ean11=" + ean11 + "]";
	}

}
