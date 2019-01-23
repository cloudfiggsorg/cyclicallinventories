package com.gmodelo.cyclicinventories.beans;

public class CatByMatnr {
	
	private String matnr;
	private int catId;
	
	public CatByMatnr(){
		
		super();
	}
	
	public CatByMatnr(String matnr, int catId) {
		super();
		this.matnr = matnr;
		this.catId = catId;
	}

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	@Override
	public String toString() {
		return "CatByMatnr [matnr=" + matnr + ", catId=" + catId + "]";
	}	

}
