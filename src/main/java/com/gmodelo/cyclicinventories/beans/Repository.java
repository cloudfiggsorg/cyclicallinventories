package com.gmodelo.cyclicinventories.beans;

public class Repository {
	
	private String key;
	private String value;
	private boolean encoded;
	
	public Repository(){
		
		super();
	}

	public Repository(String key, String value, boolean encoded) {
		super();
		this.key = key;
		this.value = value;
		this.encoded = encoded;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isEncoded() {
		return encoded;
	}

	public void setEncoded(boolean encoded) {
		this.encoded = encoded;
	}

	@Override
	public String toString() {
		return "Repository [key=" + key + ", value=" + value + ", encoded=" + encoded + "]";
	}

}
