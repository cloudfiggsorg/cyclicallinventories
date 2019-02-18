package com.gmodelo.cyclicinventories.beans;

public class RelMatnrCategory {
	
	private Category category;
	private CatByMatnr catByMatnr;
	
	public RelMatnrCategory(){		
		super();
		
		this.category = new Category();
		this.catByMatnr = new CatByMatnr();
	}

	public RelMatnrCategory(Category category, CatByMatnr catByMatnr) {
		
		super();
		this.category = category;
		this.catByMatnr = catByMatnr;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public CatByMatnr getCatByMatnr() {
		return catByMatnr;
	}

	public void setCatByMatnr(CatByMatnr catByMatnr) {
		this.catByMatnr = catByMatnr;
	}

	@Override
	public String toString() {
		return "RelMatnrCategory [category=" + category + ", catByMatnr=" + catByMatnr + "]";
	}

}
