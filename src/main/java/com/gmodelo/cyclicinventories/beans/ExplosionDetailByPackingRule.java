package com.gmodelo.cyclicinventories.beans;

public class ExplosionDetailByPackingRule {
	
	private String werks;
	private String matnr;
	private String ruleId;
	private String ruleDesc;
	private String mantr;
	private String maktx;
	private String umb;
	private String quantity;
	private boolean relevant;
	private String lgort;
	private String lastUpdate;	
		
	public ExplosionDetailByPackingRule() {
		super();
	}
	
	public ExplosionDetailByPackingRule(String werks, String matnr, String ruleId, String ruleDesc, String mantr, String maktx,
			String umb, String quantity, boolean relevant, String lgort, String lastUpdate) {
		super();
		this.werks = werks;
		this.matnr = matnr;
		this.ruleId = ruleId;
		this.ruleDesc = ruleDesc;
		this.mantr = mantr;
		this.maktx = maktx;
		this.umb = umb;
		this.quantity = quantity;
		this.relevant = relevant;
		this.lgort = lgort;
		this.lastUpdate = lastUpdate;
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


	public String getRuleId() {
		return ruleId;
	}


	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}


	public String getRuleDesc() {
		return ruleDesc;
	}


	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}


	public String getMantr() {
		return mantr;
	}


	public void setMantr(String mantr) {
		this.mantr = mantr;
	}


	public String getMaktx() {
		return maktx;
	}


	public void setMaktx(String maktx) {
		this.maktx = maktx;
	}


	public String getUmb() {
		return umb;
	}


	public void setUmb(String umb) {
		this.umb = umb;
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


	public String getLgort() {
		return lgort;
	}


	public void setLgort(String lgort) {
		this.lgort = lgort;
	}


	public String getLastUpdate() {
		return lastUpdate;
	}


	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return "ExplosionDetailByPackingRule [werks=" + werks + ", matnr=" + matnr + ", ruleId=" + ruleId + ", ruleDesc="
				+ ruleDesc + ", mantr=" + mantr + ", maktx=" + maktx + ", umb=" + umb + ", quantity=" + quantity
				+ ", relevant=" + relevant + ", lgort=" + lgort + ", lastUpdate=" + lastUpdate + "]";
	}
	
}
