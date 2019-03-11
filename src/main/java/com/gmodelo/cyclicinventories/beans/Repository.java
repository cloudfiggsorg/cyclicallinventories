package com.gmodelo.cyclicinventories.beans;

public class Repository {
	
	private String key;
	private String value;
	private boolean stored;
	
	public Repository(){
		
		super();
	}

	public Repository(String key, String value, boolean stored) {
		super();
		this.key = key;
		this.value = value;
		this.stored = stored;
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

	public boolean isStored() {
		return stored;
	}

	public void setStored(boolean stored) {
		this.stored = stored;
	}

	@Override
	public String toString() {
		return "Repository [key=" + key + ", value=" + value + ", stored=" + stored + "]";
	}

}
