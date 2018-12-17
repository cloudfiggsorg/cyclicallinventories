package com.gmodelo.cyclicinventories.beans;

public class LgplaIMBean {
	
	private int lgPlaId; //The lgpla id
	private String gltypId; // The lgtype id
	private String lgNum; //The lgNum
	private String description; //The description
	private boolean status; // The status
	
	public LgplaIMBean(){
		super();
	}
	
	public LgplaIMBean(int lgPlaId, String gltypId, String lgNum, String description, boolean status) {
		super();
		this.lgPlaId = lgPlaId;
		this.gltypId = gltypId;
		this.lgNum = lgNum;
		this.description = description;
		this.status = status;
	}

	@Override
	public String toString() {
		return "LgplaIMBean [lgPlaId=" + lgPlaId + ", gltypId=" + gltypId + ", lgNum=" + lgNum + ", description="
				+ description + ", status=" + status + "]";
	}

	public int getLgPlaId() {
		return lgPlaId;
	}
	public void setLgPlaId(int lgPlaId) {
		this.lgPlaId = lgPlaId;
	}
	public String getGltypId() {
		return gltypId;
	}
	public void setGltypId(String gltypId) {
		this.gltypId = gltypId;
	}
	public String getLgNum() {
		return lgNum;
	}
	public void setLgNum(String lgNum) {
		this.lgNum = lgNum;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

}
