package com.gmodelo.cyclicinventories.beans;

public class ConciliationsIDsBean {
	
	private String id;
	private String desc;
	private boolean status;
	private boolean available;
	
	public ConciliationsIDsBean() {
		super();
	}
	
	public ConciliationsIDsBean(String id, String desc, boolean status, boolean available) {
		super();
		this.id = id;
		this.desc = desc;
		this.status = status;
		this.available = available;
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

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@Override
	public String toString() {
		return "ConciliationsIDsBean [id=" + id + ", desc=" + desc + ", status=" + status + ", available=" + available
				+ "]";
	}
		
}
