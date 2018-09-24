package com.gmodelo.beans;

import java.util.ArrayList;
import java.util.List;

public class LgTypIMBean {

	private String lgTyp; //The hold type
	private String ltypt; //The hold description
	private String bukrs; //The society Id
	private String bDesc; //The society description
	private String werks; //The werks Id	
	private String wDesc; //The werks description
	private String lgort; //The warehouse Id
	private String gDesc; //The warehouse description
	private String lgnum; //The lgnum
	private String imwm; //Indicates if is IM OR WM
	private boolean status;
	private List<LgplaIMBean> lsLgPla; //The location positions
	
	public LgTypIMBean(){
		
		super();
		this.lsLgPla = new ArrayList<LgplaIMBean>();
	}

	public LgTypIMBean(String lgTyp, String ltypt, String bukrs, String bDesc, String werks, String wDesc, String lgort,
			String gDesc, String lgnum, String iMWM, boolean status, List<LgplaIMBean> lsLgPla) {
		super();
		this.lgTyp = lgTyp;
		this.ltypt = ltypt;
		this.bukrs = bukrs;
		this.bDesc = bDesc;
		this.werks = werks;
		this.wDesc = wDesc;
		this.lgort = lgort;
		this.gDesc = gDesc;
		this.lgnum = lgnum;
		imwm = iMWM;
		this.status = status;
		this.lsLgPla = lsLgPla;
	}

	@Override
	public String toString() {
		return "LgTypIMBean [lgTyp=" + lgTyp + ", ltypt=" + ltypt + ", bukrs=" + bukrs + ", bDesc=" + bDesc + ", werks="
				+ werks + ", wDesc=" + wDesc + ", lgort=" + lgort + ", gDesc=" + gDesc + ", lgnum=" + lgnum + ", IMWM="
				+ imwm + ", status=" + status + ", lsLgPla=" + lsLgPla + "]";
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

	public String getBukrs() {
		return bukrs;
	}

	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}

	public String getbDesc() {
		return bDesc;
	}

	public void setbDesc(String bDesc) {
		this.bDesc = bDesc;
	}

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public String getwDesc() {
		return wDesc;
	}

	public void setwDesc(String wDesc) {
		this.wDesc = wDesc;
	}

	public String getLgort() {
		return lgort;
	}

	public void setLgort(String lgort) {
		this.lgort = lgort;
	}

	public String getgDesc() {
		return gDesc;
	}

	public void setgDesc(String gDesc) {
		this.gDesc = gDesc;
	}

	public String getLgnum() {
		return lgnum;
	}

	public void setLgnum(String lgnum) {
		this.lgnum = lgnum;
	}

	public String getImwm() {
		return imwm;
	}

	public void setImwm(String iMWM) {
		imwm = iMWM;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<LgplaIMBean> getLsLgPla() {
		return lsLgPla;
	}

	public void setLsLgPla(List<LgplaIMBean> lsLgPla) {
		this.lsLgPla = lsLgPla;
	}
	
}
