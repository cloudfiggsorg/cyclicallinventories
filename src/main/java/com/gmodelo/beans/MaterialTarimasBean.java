package com.gmodelo.beans;

import java.io.Serializable;

public class MaterialTarimasBean implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3824661176421017891L;
	String packnr;
	String vhilm;
	String maktx;
	String trgqty;
	String baseunit;
	String packitem;
	String paitemtype;

	public String getPacknr() {
		return packnr;
	}

	public void setPacknr(String packnr) {
		this.packnr = packnr;
	}

	public String getVhilm() {
		return vhilm;
	}

	public void setVhilm(String vhilm) {
		this.vhilm = vhilm;
	}

	public String getMaktx() {
		return maktx;
	}

	public void setMaktx(String maktx) {
		this.maktx = maktx;
	}

	public String getTrgqty() {
		return trgqty;
	}

	public void setTrgqty(String trgqty) {
		this.trgqty = trgqty;
	}

	public String getBaseunit() {
		return baseunit;
	}

	public void setBaseunit(String baseunit) {
		this.baseunit = baseunit;
	}

	public String getPackitem() {
		return packitem;
	}

	public void setPackitem(String packitem) {
		this.packitem = packitem;
	}

	public String getPaitemtype() {
		return paitemtype;
	}

	public void setPaitemtype(String paitemtype) {
		this.paitemtype = paitemtype;
	}

	public MaterialTarimasBean(String packnr, String vhilm, String maktx, String trgqty, String baseunit,
			String packitem, String paitemtype) {
		super();
		this.packnr = packnr;
		this.vhilm = vhilm;
		this.maktx = maktx;
		this.trgqty = trgqty;
		this.baseunit = baseunit;
		this.packitem = packitem;
		this.paitemtype = paitemtype;
	}

	public MaterialTarimasBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "MaterialTarimasBean [packnr=" + packnr + ", vhilm=" + vhilm + ", maktx=" + maktx + ", trgqty=" + trgqty
				+ ", baseunit=" + baseunit + ", packitem=" + packitem + ", paitemtype=" + paitemtype + "]";
	}

}
