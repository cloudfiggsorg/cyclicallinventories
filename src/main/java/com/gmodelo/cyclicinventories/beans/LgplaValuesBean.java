package com.gmodelo.cyclicinventories.beans;

public class LgplaValuesBean {

	private String matnr;
	private String vhilm;
	private String matkx;
	private String sec;
	private String tarimas;
	private String camas;
	private String um;// Cantidad de Unidad de medida osea caja
	private String totalConverted;
	private String materialNotes;
	private String prodDate;
	private String vhilmQuan;
	private String cpc;
	private String cpp;
	
	private boolean locked;
	private long dateStart;
	private long dateEnd;

	public LgplaValuesBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}

	public String getVhilm() {
		return vhilm;
	}

	public void setVhilm(String vhilm) {
		this.vhilm = vhilm;
	}

	public String getMatkx() {
		return matkx;
	}

	public void setMatkx(String matkx) {
		this.matkx = matkx;
	}

	public String getSec() {
		return sec;
	}

	public void setSec(String sec) {
		this.sec = sec;
	}

	public String getTarimas() {
		return tarimas;
	}

	public void setTarimas(String tarimas) {
		this.tarimas = tarimas;
	}

	public String getCamas() {
		return camas;
	}

	public void setCamas(String camas) {
		this.camas = camas;
	}

	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	public String getTotalConverted() {
		return totalConverted;
	}

	public void setTotalConverted(String totalConverted) {
		this.totalConverted = totalConverted;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public long getDateStart() {
		return dateStart;
	}

	public void setDateStart(long dateStart) {
		this.dateStart = dateStart;
	}

	public long getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(long dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String toKey(String pkAsgId) {
		return pkAsgId + this.matnr;
	}

	public String getMaterialNotes() {
		return materialNotes;
	}

	public void setMaterialNotes(String materialNotes) {
		this.materialNotes = materialNotes;
	}

	public String getProdDate() {
		return prodDate;
	}

	public void setProdDate(String prodDate) {
		this.prodDate = prodDate;
	}

	public String getVhilmQuan() {
		return vhilmQuan;
	}

	public void setVhilmQuan(String vhilmQuan) {
		this.vhilmQuan = vhilmQuan;
	}

	public String getCpc() {
		return cpc;
	}

	public void setCpc(String cpc) {
		this.cpc = cpc;
	}

	public String getCpp() {
		return cpp;
	}

	public void setCpp(String cpp) {
		this.cpp = cpp;
	}

	

	@Override
	public String toString() {
		return "LgplaValuesBean [matnr=" + matnr + ", vhilm=" + vhilm + ", matkx=" + matkx + ", sec=" + sec
				+ ", tarimas=" + tarimas + ", camas=" + camas + ", um=" + um + ", totalConverted=" + totalConverted
				+ ", materialNotes=" + materialNotes + ", prodDate=" + prodDate + ", vhilmQuan=" + vhilmQuan + ", cpc="
				+ cpc + ", cpp=" + cpp + ", locked=" + locked + ", dateStart=" + dateStart + ", dateEnd=" + dateEnd
				+ "]";
	}

	public LgplaValuesBean(String matnr, String vhilm, String matkx, String sec, String tarimas, String camas,
			String um, String totalConverted, String materialNotes, String prodDate, String vhilmQuan, String cpc,
			String cpp, boolean locked, long dateStart, long dateEnd) {
		super();
		this.matnr = matnr;
		this.vhilm = vhilm;
		this.matkx = matkx;
		this.sec = sec;
		this.tarimas = tarimas;
		this.camas = camas;
		this.um = um;
		this.totalConverted = totalConverted;
		this.materialNotes = materialNotes;
		this.prodDate = prodDate;
		this.vhilmQuan = vhilmQuan;
		this.cpc = cpc;
		this.cpp = cpp;
		this.locked = locked;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}

	

}
