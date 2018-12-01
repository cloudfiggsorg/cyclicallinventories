package com.gmodelo.beans;

public class Justification {
	
	int consPosSAPId;
	String quantity;
	String justify;
	String fileName;
	
	public Justification(){
		super();
	}

	public Justification(int consPosSAPId, String quantity, String justify, String fileName) {
		super();
		this.consPosSAPId = consPosSAPId;
		this.quantity = quantity;
		this.justify = justify;
		this.fileName = fileName;
	}

	public int getConsPosSAPId() {
		return consPosSAPId;
	}

	public void setConsPosSAPId(int consPosSAPId) {
		this.consPosSAPId = consPosSAPId;
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

	@Override
	public String toString() {
		return "Justification [consPosSAPId=" + consPosSAPId + ", quantity=" + quantity + ", justify=" + justify
				+ ", fileName=" + fileName + "]";
	}
		
}
