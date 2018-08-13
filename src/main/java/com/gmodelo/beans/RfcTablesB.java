package com.gmodelo.beans;

public class RfcTablesB {
	String name;
	String values;
	String date;
	String device;
	
	public RfcTablesB() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public RfcTablesB(String name, String values, String date, String device) {
		super();
		this.name = name;
		this.values = values;
		this.date = date;
		this.device = device;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	
	@Override
	public String toString() {
		return "RfcTablesB [name=" + name + ", values=" + values + ", date=" + date + ", device=" + device + "]";
	}
	
}
