package com.gmodelo.beans;

public class CostByMatnr {
	
	String Matnr; 
	String cost;
	
	public CostByMatnr(){
		super();
	}

	public CostByMatnr(String matnr, String cost) {
		super();
		Matnr = matnr;
		this.cost = cost;
	}

	public String getMatnr() {
		return Matnr;
	}

	public void setMatnr(String matnr) {
		Matnr = matnr;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "CostByMatnr [Matnr=" + Matnr + ", cost=" + cost + "]";
	}
	
}
