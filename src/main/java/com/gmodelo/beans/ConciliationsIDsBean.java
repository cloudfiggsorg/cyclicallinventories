package com.gmodelo.beans;

public class ConciliationsIDsBean {
	String id;
	String desc;
	
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
	
	public ConciliationsIDsBean(String id, String desc) {
		super();
		this.id = id;
		this.desc = desc;
	}
	
	public ConciliationsIDsBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "ConciliationsIDsBean [id=" + id + ", desc=" + desc + "]";
	}
	
	

}
