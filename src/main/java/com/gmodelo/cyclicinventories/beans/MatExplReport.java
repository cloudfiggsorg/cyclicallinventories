package com.gmodelo.cyclicinventories.beans;

public class MatExplReport {
	
	private int item;
	private String lgort;
	private String lgtyp;
	private String ltypt;
	private String lgpla;
	private String matnr;
	private String description;
	private String category;
	private String umb;
	private String counted;
	private String matnrExpl;
	private String descMantrExpl;
	private String lgortExpl;
	private String catExpl;
	private String umbExpl;
	private String quantity;
	
	public MatExplReport(){
		super();
	}

	public MatExplReport(int item, String lgort, String lgtyp, String ltypt, String lgpla, String matnr,
			String description, String category, String umb, String counted, String matnrExpl, String descMantrExpl,
			String lgortExpl, String catExpl, String umbExpl, String quantity) {
		super();
		this.item = item;
		this.lgort = lgort;
		this.lgtyp = lgtyp;
		this.ltypt = ltypt;
		this.lgpla = lgpla;
		this.matnr = matnr;
		this.description = description;
		this.category = category;
		this.umb = umb;
		this.counted = counted;
		this.matnrExpl = matnrExpl;
		this.descMantrExpl = descMantrExpl;
		this.lgortExpl = lgortExpl;
		this.catExpl = catExpl;
		this.umbExpl = umbExpl;
		this.quantity = quantity;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public String getLgort() {
		return lgort;
	}

	public void setLgort(String lgort) {
		this.lgort = lgort;
	}

	public String getLgtyp() {
		return lgtyp;
	}

	public void setLgtyp(String lgtyp) {
		this.lgtyp = lgtyp;
	}

	public String getLtypt() {
		return ltypt;
	}

	public void setLtypt(String ltypt) {
		this.ltypt = ltypt;
	}

	public String getLgpla() {
		return lgpla;
	}

	public void setLgpla(String lgpla) {
		this.lgpla = lgpla;
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

	public String getLgortExpl() {
		return lgortExpl;
	}

	public void setLgortExpl(String lgortExpl) {
		this.lgortExpl = lgortExpl;
	}

	public String getCatExpl() {
		return catExpl;
	}

	public void setCatExpl(String catExpl) {
		this.catExpl = catExpl;
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
		return "MatExplReport [item=" + item + ", lgort=" + lgort + ", lgtyp=" + lgtyp + ", ltypt=" + ltypt + ", lgpla="
				+ lgpla + ", matnr=" + matnr + ", description=" + description + ", category=" + category + ", umb="
				+ umb + ", counted=" + counted + ", matnrExpl=" + matnrExpl + ", descMantrExpl=" + descMantrExpl
				+ ", lgortExpl=" + lgortExpl + ", catExpl=" + catExpl + ", umbExpl=" + umbExpl + ", quantity="
				+ quantity + "]";
	}
			
}
