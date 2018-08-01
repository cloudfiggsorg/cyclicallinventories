package com.gmodelo.beans;

public class Response<K> {

	private AbstractResults abstractResult;
	private K lsObject;

	public Response() {
	}

	public AbstractResults getAbstractResult() {
		return abstractResult;
	}

	public void setAbstractResult(AbstractResults abstractResult) {
		this.abstractResult = abstractResult;
	}

	public K getLsObject() {
		return lsObject;
	}

	public void setLsObject(K lsObject) {
		this.lsObject = lsObject;
	}

	public Response(AbstractResults abstractResult, K lsObject) {
		super();
		this.abstractResult = abstractResult;
		this.lsObject = lsObject;
	}

	@Override
	public String toString() {
		return "Response [abstractResult=" + abstractResult + ", lsObject=" + lsObject + "]";
	}
	
	

}