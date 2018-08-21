package com.gmodelo.beans;

public class ToleranceBean {

	Integer toleranceId;	//id de tolerancia
	String matkl;			//Grupo de artículos
	String desc;			//Descripcion
	String tp;				//Tolerancia pporcentual
	String tc;				//Tolerancia en cantidad
	
	public Integer getToleranceId() {
		return toleranceId;
	}
	public void setToleranceId(Integer toleranceId) {
		this.toleranceId = toleranceId;
	}
	public String getMatkl() {
		return matkl;
	}
	public void setMatkl(String matkl) {
		this.matkl = matkl;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTp() {
		return tp;
	}
	public void setTp(String tp) {
		this.tp = tp;
	}
	public String getTc() {
		return tc;
	}
	public void setTc(String tc) {
		this.tc = tc;
	}
	
	public ToleranceBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ToleranceBean(Integer toleranceId, String matkl, String desc, String tp, String tc) {
		super();
		this.toleranceId = toleranceId;
		this.matkl = matkl;
		this.desc = desc;
		this.tp = tp;
		this.tc = tc;
	}
	
	@Override
	public String toString() {
		return "ToleranceBean [toleranceId=" + toleranceId + ", matkl=" + matkl + ", desc=" + desc + ", tp=" + tp
				+ ", tc=" + tc + "]";
	}
	
	
	
}
