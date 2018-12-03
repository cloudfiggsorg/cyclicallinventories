package com.gmodelo.beans;

public class ConciliationsIDsBean {
	
	String id;
	String desc;
	boolean status;
	
	public ConciliationsIDsBean() {
		super();
	}
	
	public ConciliationsIDsBean(String id, String desc, boolean status) {
		super();
		this.id = id;
		this.desc = desc;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ConciliationsIDsBean [id=" + id + ", desc=" + desc + ", status=" + status + "]";
	}
	
}
