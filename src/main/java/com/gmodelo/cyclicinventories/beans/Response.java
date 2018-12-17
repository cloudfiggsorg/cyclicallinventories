package com.gmodelo.cyclicinventories.beans;

public class Response<K> {

	private AbstractResultsBean abstractResult;
	private K lsObject;

	public Response() {
	}

	public AbstractResultsBean getAbstractResult() {
		return abstractResult;
	}

	public void setAbstractResult(AbstractResultsBean abstractResult) {
		this.abstractResult = abstractResult;
	}

	public K getLsObject() {
		return lsObject;
	}

	public void setLsObject(K lsObject) {
		this.lsObject = lsObject;
	}

	public Response(AbstractResultsBean abstractResult, K lsObject) {
		super();
		this.abstractResult = abstractResult;
		this.lsObject = lsObject;
	}

	@Override
	public String toString() {
		return "Response [abstractResult=" + abstractResult + ", lsObject=" + lsObject + "]";
	}
	
	

}