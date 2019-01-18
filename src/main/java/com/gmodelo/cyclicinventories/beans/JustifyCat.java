package com.gmodelo.cyclicinventories.beans;

public class JustifyCat {
	
	private int jsId;
	private String justification;
	
	public JustifyCat(){
		super();
	}
		
	public JustifyCat(int jsId, String justification) {
		super();
		this.jsId = jsId;
		this.justification = justification;
	}

	public int getJsId() {
		return jsId;
	}
	public void setJsId(int jsId) {
		this.jsId = jsId;
	}
	public String getJustification() {
		return justification;
	}
	public void setJustification(String justification) {
		this.justification = justification;
	}

	@Override
	public String toString() {
		return "JustifyCat [jsId=" + jsId + ", justification=" + justification + "]";
	}
	
}
