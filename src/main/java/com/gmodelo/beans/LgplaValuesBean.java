package com.gmodelo.beans;

public class LgplaValuesBean {

	String matnr;
	String matkx;
	String sCantidad;
	Integer sec;
	Integer tarimas;
	Integer camas;
	Integer cajas;
	Integer totalConverted;
	Integer iCantidad;
	boolean locked;

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}

	public String getMatkx() {
		return matkx;
	}

	public void setMatkx(String matkx) {
		this.matkx = matkx;
	}

	public String getsCantidad() {
		return sCantidad;
	}

	public void setsCantidad(String sCantidad) {
		this.sCantidad = sCantidad;
	}

	public Integer getSec() {
		return sec;
	}

	public void setSec(Integer sec) {
		this.sec = sec;
	}

	public Integer getTarimas() {
		return tarimas;
	}

	public void setTarimas(Integer tarimas) {
		this.tarimas = tarimas;
	}

	public Integer getCamas() {
		return camas;
	}

	public void setCamas(Integer camas) {
		this.camas = camas;
	}

	public Integer getCajas() {
		return cajas;
	}

	public void setCajas(Integer cajas) {
		this.cajas = cajas;
	}

	public Integer getTotalConverted() {
		return totalConverted;
	}

	public void setTotalConverted(Integer totalConverted) {
		this.totalConverted = totalConverted;
	}

	public Integer getiCantidad() {
		return iCantidad;
	}

	public void setiCantidad(Integer iCantidad) {
		this.iCantidad = iCantidad;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public LgplaValuesBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LgplaValuesBean(String matnr, String matkx, String sCantidad, Integer sec, Integer tarimas, Integer camas,
			Integer cajas, Integer totalConverted, Integer iCantidad, boolean locked) {
		super();
		this.matnr = matnr;
		this.matkx = matkx;
		this.sCantidad = sCantidad;
		this.sec = sec;
		this.tarimas = tarimas;
		this.camas = camas;
		this.cajas = cajas;
		this.totalConverted = totalConverted;
		this.iCantidad = iCantidad;
		this.locked = locked;
	}

	@Override
	public String toString() {
		return "CarrilValuesBean [matnr=" + matnr + ", matkx=" + matkx + ", sCantidad=" + sCantidad + ", sec=" + sec
				+ ", tarimas=" + tarimas + ", camas=" + camas + ", cajas=" + cajas + ", totalConverted="
				+ totalConverted + ", iCantidad=" + iCantidad + ", locked=" + locked + "]";
	}

	public String toKey(String lgpla) {
		return lgpla + this.matnr + this.sec;
	}
}
