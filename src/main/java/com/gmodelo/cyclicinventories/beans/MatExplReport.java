package com.gmodelo.cyclicinventories.beans;

public class MatExplReport {
	
	private int item;
	private String matnr;
	private String description;
	private String category;
	private String umb;
	private String counted;
	private String matnrExpl;
	private String descMantrExpl;
	private String umbExpl;
	private String quantity;
	
	public MatExplReport(){
		super();
	}
	
	public MatExplReport(int item, String matnr, String description, String category, String umb, String counted,
			String matnrExpl, String descMantrExpl, String umbExpl, String quantity) {
		super();
		this.item = item;
		this.matnr = matnr;
		this.description = description;
		this.category = category;
		this.umb = umb;
		this.counted = counted;
		this.matnrExpl = matnrExpl;
		this.descMantrExpl = descMantrExpl;
		this.umbExpl = umbExpl;
		this.quantity = quantity;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = matnr;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUmb() {
		return umb;
	}

	public void setUmb(String umb) {
		this.umb = umb;
	}

	public String getCounted() {
		return counted;
	}

	public void setCounted(String counted) {
		this.counted = counted;
	}

	public String getMatnrExpl() {
		return matnrExpl;
	}

	public void setMatnrExpl(String matnrExpl) {
		this.matnrExpl = matnrExpl;
	}

	public String getDescMantrExpl() {
		return descMantrExpl;
	}

	public void setDescMantrExpl(String descMantrExpl) {
		this.descMantrExpl = descMantrExpl;
	}

	public String getUmbExpl() {
		return umbExpl;
	}

	public void setUmbExpl(String umbExpl) {
		this.umbExpl = umbExpl;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "MatExplReport [item=" + item + ", matnr=" + matnr + ", description=" + description + ", category="
				+ category + ", umb=" + umb + ", counted=" + counted + ", matnrExpl=" + matnrExpl + ", descMantrExpl="
				+ descMantrExpl + ", umbExpl=" + umbExpl + ", quantity=" + quantity + "]";
	}
			
}
