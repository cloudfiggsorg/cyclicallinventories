package com.gmodelo.cyclicinventories.beans;

public class PackingRule {
	
	private String ruleId;
	private String ruleDesc;
	
	public PackingRule(){
		
	}

	public PackingRule(String ruleId, String ruleDesc) {
		super();
		this.ruleId = ruleId;
		this.ruleDesc = ruleDesc;
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

	@Override
	public String toString() {
		return "PackingRule [ruleId=" + ruleId + ", ruleDesc=" + ruleDesc + "]";
	}
	
}
