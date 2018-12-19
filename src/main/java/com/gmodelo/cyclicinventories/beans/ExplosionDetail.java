package com.gmodelo.cyclicinventories.beans;

public class ExplosionDetail {
	
	private String werks;
	private String matnr;
	private String component;
	private String quantity;
	private boolean relevant;
	
	public ExplosionDetail() {
		super();
	}
	
	public ExplosionDetail(String werks, String matnr, String component, String quantity, boolean relevant) {
		super();
		this.werks = werks;
		this.matnr = matnr;
		this.component = component;
		this.quantity = quantity;
		this.relevant = relevant;
	}
	
	public String getWerks() {
		return werks;
	}
	public void setWerks(String werks) {
		this.werks = werks;
	}
	public String getMatnr() {
		return matnr;
	}
	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public boolean isRelevant() {
		return relevant;
	}
	public void setRelevant(boolean relevant) {
		this.relevant = relevant;
	}

	@Override
	public String toString() {
		return "ExplosionDetail [werks=" + werks + ", matnr=" + matnr + ", component=" + component + ", quantity="
				+ quantity + ", relevant=" + relevant + "]";
	}
	
}
