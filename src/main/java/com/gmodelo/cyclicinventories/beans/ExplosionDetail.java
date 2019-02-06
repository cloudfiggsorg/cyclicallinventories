package com.gmodelo.cyclicinventories.beans;

public class ExplosionDetail {
	
	private String werks;
	private String matnr;
	private String component;
	private String compDesc;
	private String umb;
	private boolean relevant;
	private String lgort;
	
	public ExplosionDetail() {
		super();
	}
	
	public ExplosionDetail(String werks, String matnr, String component, String compDesc, String umb,
			boolean relevant, String lgort) {
		super();
		this.werks = werks;
		this.matnr = matnr;
		this.component = component;
		this.compDesc = compDesc;
		this.umb = umb;
		this.relevant = relevant;
		this.lgort = lgort;
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
	public String getCompDesc() {
		return compDesc;
	}
	public void setCompDesc(String compDesc) {
		this.compDesc = compDesc;
	}	
	public String getUmb() {
		return umb;
	}
	public void setUmb(String quantity) {
		this.umb = quantity;
	}
	public boolean isRelevant() {
		return relevant;
	}
	public void setRelevant(boolean relevant) {
		this.relevant = relevant;
	}	
	public String getLgort() {
		return lgort;
	}

	public void setLgort(String lgort) {
		this.lgort = lgort;
	}

	@Override
	public String toString() {
		return "ExplosionDetail [werks=" + werks + ", matnr=" + matnr + ", component=" + component + ", compDesc="
				+ compDesc + ", umb=" + umb + ", relevant=" + relevant + ", lgort=" + lgort + "]";
	}

}
