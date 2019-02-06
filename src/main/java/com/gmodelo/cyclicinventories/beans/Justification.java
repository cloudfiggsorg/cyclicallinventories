package com.gmodelo.cyclicinventories.beans;

public class Justification {

	private int consPosSAPId;
	private int jsId;
	private int jsCount;
	private String quantity;
	private String justify;
	private String fileName;
	private String base64File;

	public Justification() {
		super();
	}

	public Justification(int consPosSAPId, int jsId, int jsCount, String quantity, String justify, String fileName,
			String base64File) {
		super();
		this.consPosSAPId = consPosSAPId;
		this.jsId = jsId;
		this.jsCount = jsCount;
		this.quantity = quantity;
		this.justify = justify;
		this.fileName = fileName;
		this.base64File = base64File;
	}

	public int getConsPosSAPId() {
		return consPosSAPId;
	}

	public void setConsPosSAPId(int consPosSAPId) {
		this.consPosSAPId = consPosSAPId;
	}

	public int getJsId() {
		return jsId;
	}

	public void setJsId(int jsId) {
		this.jsId = jsId;
	}

	public int getJsCount() {
		return jsCount;
	}

	public void setJsCount(int jsCount) {
		this.jsCount = jsCount;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getJustify() {
		return justify;
	}

	public void setJustify(String justify) {
		this.justify = justify;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBase64File() {
		return base64File;
	}

	public void setBase64File(String base64File) {
		this.base64File = base64File;
	}

	@Override
	public String toString() {
		return "Justification [consPosSAPId=" + consPosSAPId + ", jsId=" + jsId + ", jsCount=" + jsCount + ", quantity="
				+ quantity + ", justify=" + justify + ", fileName=" + fileName + ", base64File=" + base64File + "]";
	}
	
}
