package com.gmodelo.beans;

public class RfcTableValues {

	Integer table_count;
	Integer row_skips;
	Integer current_row;

	public Integer getTable_count() {
		return table_count;
	}

	public void setTable_count(Integer table_count) {
		this.table_count = table_count;
	}

	public Integer getRow_skips() {
		return row_skips;
	}

	public void setRow_skips(Integer row_skips) {
		this.row_skips = row_skips;
	}

	public Integer getCurrent_row() {
		return current_row;
	}

	public void setCurrent_row(Integer current_row) {
		this.current_row = current_row;
	}

	@Override
	public String toString() {
		return "RfcTableValues [table_count=" + table_count + ", row_skips=" + row_skips + ", current_row="
				+ current_row + "]";
	}

	public RfcTableValues(Integer table_count, Integer row_skips, Integer current_row) {
		super();
		this.table_count = table_count;
		this.row_skips = row_skips;
		this.current_row = current_row;
	}

	public RfcTableValues() {
		super();
		// TODO Auto-generated constructor stub
	}


	

}
