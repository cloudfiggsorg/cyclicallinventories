package com.gmodelo.beans;

public class LagpEntity {
	
	String lgNum;	//
	String lgTyp;	//NUM DE TYPO
	String lgPla;	//NUM DE 
	String lpTyp;	
	String skzua;
	String skzue;
	String imwm;
	
	public String getImwm() {
		return imwm;
	}
	public void setImwm(String imwm) {
		this.imwm = imwm;
	}
	public String getLgNum() {
		return lgNum;
	}
	public void setLgNum(String lgNum) {
		this.lgNum = lgNum;
	}
	public String getLgTyp() {
		return lgTyp;
	}
	public void setLgTyp(String lgTyp) {
		this.lgTyp = lgTyp;
	}
	public String getLgPla() {
		return lgPla;
	}
	public void setLgPla(String lgPla) {
		this.lgPla = lgPla;
	}
	public String getLpTyp() {
		return lpTyp;
	}
	public void setLpTyp(String lpTyp) {
		this.lpTyp = lpTyp;
	}
	public String getSkzua() {
		return skzua;
	}
	public void setSkzua(String skzua) {
		this.skzua = skzua;
	}
	public String getSkzue() {
		return skzue;
	}
	public void setSkzue(String skzue) {
		this.skzue = skzue;
	}
	public LagpEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public LagpEntity(String lgNum, String lgTyp, String lgPla, String lpTyp, String skzua, String skzue, String imwm) {
		super();
		this.lgNum = lgNum;
		this.lgTyp = lgTyp;
		this.lgPla = lgPla;
		this.lpTyp = lpTyp;
		this.skzua = skzua;
		this.skzue = skzue;
		this.imwm = imwm;
	}
	
	@Override
	public String toString() {
		return "LagpB [lgNum=" + lgNum + ", lgTyp=" + lgTyp + ", lgPla=" + lgPla + ", lpTyp=" + lpTyp + ", skzua="
				+ skzua + ", skzue=" + skzue + ", imwm=" + imwm + "]";
	}
	
	

}
