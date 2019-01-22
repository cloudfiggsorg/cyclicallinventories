package com.gmodelo.cyclicinventories.beans;

public class CatByMatnr {
	
	private String matnr;
	private String werks;
	private int catId;
	
	public CatByMatnr(){
		
		super();
	}
	
	public CatByMatnr(String matnr, String werks, int catId) {
		super();
		this.matnr = matnr;
		this.werks = werks;
		this.catId = catId;
	}

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}

	public String getWerks() {
		return werks;
	}

	public void setWerks(String werks) {
		this.werks = werks;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	@Override
	public String toString() {
		return "CatByMatnr [matnr=" + matnr + ", werks=" + werks + ", catId=" + catId + "]";
	}	

}
