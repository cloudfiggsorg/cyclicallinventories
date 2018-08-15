package com.gmodelo.beans;

public class TgortB {
	String werks;
	String lgort;
	String lgNum;
	String lgTyp;
	String ltypt;
	String imwm;
	
	public String getImwm() {
		return imwm;
	}
	public void setImwm(String imwm) {
		this.imwm = imwm;
	}
	public String getWerks() {
		return werks;
	}
	public void setWerks(String werks) {
		this.werks = werks;
	}
	public String getLgort() {
		return lgort;
	}
	public void setLgort(String lgort) {
		this.lgort = lgort;
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
	public String getLtypt() {
		return ltypt;
	}
	public void setLtypt(String ltypt) {
		this.ltypt = ltypt;
	}
	public TgortB() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TgortB(String werks, String lgort, String lgNum, String lgTyp, String ltypt, String imwm) {
		super();
		this.werks = werks;
		this.lgort = lgort;
		this.lgNum = lgNum;
		this.lgTyp = lgTyp;
		this.ltypt = ltypt;
		this.imwm = imwm;
	}
	@Override
	public String toString() {
		return "TgortB [werks=" + werks + ", lgort=" + lgort + ", lgNum=" + lgNum + ", lgTyp=" + lgTyp + ", ltypt="
				+ ltypt + ", imwm=" + imwm + "]";
	}

}
