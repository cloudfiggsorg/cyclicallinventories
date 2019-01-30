package com.gmodelo.cyclicinventories.beans;

public class JustificationFile {
	
	private int docInvId;
	private int jsId;
	private String fileName;
	private String base64;
	
	public JustificationFile(){
		
		super();
	}

	public JustificationFile(int docInvId, int jsId, String fileName, String base64) {
		super();
		this.docInvId = docInvId;
		this.jsId = jsId;
		this.fileName = fileName;
		this.base64 = base64;
	}

	public int getDocInvId() {
		return docInvId;
	}

	public void setDocInvId(int docInvId) {
		this.docInvId = docInvId;
	}

	public int getJsId() {
		return jsId;
	}

	public void setJsId(int jsId) {
		this.jsId = jsId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	@Override
	public String toString() {
		return "JustificationFile [docInvId=" + docInvId + ", jsId=" + jsId + ", fileName=" + fileName + ", base64="
				+ base64 + "]";
	}
	
}
