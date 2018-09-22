package com.gmodelo.beans;

public class LgplaValuesBean {

	String matnr;
	String vhilm;
	String matkx;
	Integer sec;
	Integer tarimas;
	Integer camas;
	Integer um;//Cantidad de Unidad de medida
	Integer totalConverted;
	boolean locked;

	public String getMatnr() {
		return matnr;
	}

	public Integer getUm() {
		return um;
	}

	public void setUm(Integer um) {
		this.um = um;
	}

	public String getVhilm() {
		return vhilm;
	}

	public void setVhilm(String vhilm) {
		this.vhilm = vhilm;
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

	public Integer getTotalConverted() {
		return totalConverted;
	}

	public void setTotalConverted(Integer totalConverted) {
		this.totalConverted = totalConverted;
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

	public String toKey(String pkAsgId) {
		return pkAsgId + this.matnr;
	}

	public LgplaValuesBean(String matnr, String vhilm, String matkx, Integer sec, Integer tarimas, Integer camas,
			Integer um, Integer totalConverted, boolean locked) {
		super();
		this.matnr = matnr;
		this.vhilm = vhilm;
		this.matkx = matkx;
		this.sec = sec;
		this.tarimas = tarimas;
		this.camas = camas;
		this.um = um;
		this.totalConverted = totalConverted;
		this.locked = locked;
	}

	@Override
	public String toString() {
		return "LgplaValuesBean [matnr=" + matnr + ", vhilm=" + vhilm + ", matkx=" + matkx + ", sec=" + sec
				+ ", tarimas=" + tarimas + ", camas=" + camas + ", um=" + um + ", totalConverted=" + totalConverted
				+ ", locked=" + locked + "]";
	}
	
}
