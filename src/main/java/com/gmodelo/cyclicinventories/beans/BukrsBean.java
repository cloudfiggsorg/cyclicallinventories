package com.gmodelo.cyclicinventories.beans;

public class BukrsBean {

	private String bukrs; //society
	private String bukrsDesc; // society description
	private String werks; //ceco
	private String werksDesc; //ceco description
	
	public String getBukrs() {
		return bukrs;
	}
	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}
	public String getBukrsDesc() {
		return bukrsDesc;
	}
	public void setBukrsDesc(String bukrsDesc) {
		this.bukrsDesc = bukrsDesc;
	}
	public String getWerks() {
		return werks;
	}
	public void setWerks(String werks) {
		this.werks = werks;
	}
	public String getWerksDesc() {
		return werksDesc;
	}
	public void setWerksDesc(String werksDesc) {
		this.werksDesc = werksDesc;
	}
	
	public BukrsBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BukrsBean(String bukrs, String bukrsDesc, String werks, String werksDesc) {
		super();
		this.bukrs = bukrs;
		this.bukrsDesc = bukrsDesc;
		this.werks = werks;
		this.werksDesc = werksDesc;
	}
	@Override
	public String toString() {
		return "BukrsBean [bukrs=" + bukrs + ", bukrsDesc=" + bukrsDesc + ", werks=" + werks + ", werksDesc="
				+ werksDesc + "]";
	}
		
}
