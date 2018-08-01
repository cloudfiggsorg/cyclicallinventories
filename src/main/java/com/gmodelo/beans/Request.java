package com.gmodelo.beans;

public class Request<K> {

	private LoginBean tokenObject;
	private K lsObject;

	public LoginBean getTokenObject() {
		return tokenObject;
	}

	public void setTokenObject(LoginBean tokenObject) {
		this.tokenObject = tokenObject;
	}

	public K getLsObject() {
		return lsObject;
	}

	public void setLsObject(K lsObject) {
		this.lsObject = lsObject;
	}

	public Request(LoginBean tokenObject, K lsObject) {
		super();
		this.tokenObject = tokenObject;
		this.lsObject = lsObject;
	}

	public Request() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Request [tokenObject=" + tokenObject + ", lsObject=" + lsObject + "]";
	}

}
