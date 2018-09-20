package com.gmodelo.beans;

public class LgplaIM {
	
	private int lgPlaId; //The lgpla id
	private String gltypId; // The lgtype id
	private String description; //The description
	private boolean status; // The status
	
	public LgplaIM(){
		super();
	}
	
	public LgplaIM(int lgPlaId, String gltypId, String description, boolean status) {
		
		super();
		this.lgPlaId = lgPlaId;
		this.gltypId = gltypId;
		this.description = description;
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "LgplaIM [lgPlaId=" + lgPlaId + ", gltypId=" + gltypId + ", description=" + description + ", status="
				+ status + "]";
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
