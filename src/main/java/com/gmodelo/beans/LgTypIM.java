package com.gmodelo.beans;

import java.util.ArrayList;
import java.util.List;

public class LgTypIM {

	private String lgTyp;
	private String ltypt;
	private String bukrs;
	private String bDesc;
	private String werks;
	private String wDesc;
	private String lgort;
	private String gDesc;
	private String lgnum;
	private List<LgTypIM> lsLgPla;
	
	public LgTypIM(){
		
		super();
		this.lsLgPla = new ArrayList<LgTypIM>();
	}
	
	public LgTypIM(String lgTyp, String ltypt, String bukrs, String bDesc, String werks, String wDesc, String lgort,
			String gDesc, String lgnum, ArrayList<LgTypIM> lsLgPla) {
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
		this.lsLgPla = lsLgPla;
	}

	@Override
	public String toString() {
		return "LgTypIM [lgTyp=" + lgTyp + ", ltypt=" + ltypt + ", bukrs=" + bukrs + ", bDesc=" + bDesc + ", werks="
				+ werks + ", wDesc=" + wDesc + ", lgort=" + lgort + ", gDesc=" + gDesc + ", lgnum=" + lgnum
				+ ", lsLgPla=" + lsLgPla + "]";
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
	public List<LgTypIM> getLsLgPla() {
		return lsLgPla;
	}
	public void setLsLgPla(List<LgTypIM> lsLgPla) {
		this.lsLgPla = lsLgPla;
	}
		
}
