package com.gmodelo.cyclicinventories.beans;

public class Category {
	
	private int catId;
	private String category;
	
	public Category(){
		
		super();
	}

	public Category(int catId, String category) {
		super();
		this.catId = catId;
		this.category = category;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Category [catId=" + catId + ", category=" + category + "]";
	}
	
}
