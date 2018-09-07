package data.inventarios.sincro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class RFC_DATAMART {

	public static final String RFCDELIMITER = ";";
	public static final String SQLDELIMITER = "\\|";

	String tablename;
	String table_value;
	String table_Insert;
	String table_filters;
	String[] table_values;
	String table_field_filter;
	Integer maxRows;
	String table_type_filter;
	String last_request;

	
	public String getLast_request() {
		return last_request;
	}
	public void setLast_request(String last_request) {
		this.last_request = last_request;
	}
	public String getTable_type_filter() {
		return table_type_filter;
	}
	public void setTable_type_filter(String table_type_filter) {
		this.table_type_filter = table_type_filter;
	}
	public String getTable_field_filter() {
		return table_field_filter;
	}
	public void setTable_field_filter(String table_field_filter) {
		this.table_field_filter = table_field_filter;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getTable_value() {
		return table_value;
	}

	public void setTable_value(String table_value) {
		this.table_value = table_value;
	}

	public String getTable_Insert() {
		return table_Insert;
	}

	public void setTable_Insert(String table_Insert) {
		this.table_Insert = table_Insert;
	}

	public Integer getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(Integer maxRows) {
		this.maxRows = maxRows;
	}

	public String[] getTable_values() {
		return table_values;
	}

	public void setTable_values(String[] table_values) {
		this.table_values = table_values;
	}

	public String getTable_filters() {
		return table_filters;
	}

	public void setTable_filters(String table_filters) {
		this.table_filters = table_filters;
	}

	public RFC_DATAMART(String tablename, String table_value, String table_Insert, Integer maxRows) {
		super();
		this.tablename = tablename;
		this.table_value = table_value;
		this.table_Insert = table_Insert;
		this.maxRows = maxRows;
	}
	

	public RFC_DATAMART(String tablename, String table_value, String table_Insert, String table_filters,
			String[] table_values, String table_field_max, Integer maxRows) {
		super();
		this.tablename = tablename;
		this.table_value = table_value;
		this.table_Insert = table_Insert;
		this.table_filters = table_filters;
		this.table_values = table_values;
		this.table_field_filter = table_field_max;
		this.maxRows = maxRows;
	}

	public RFC_DATAMART() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RFC_DATAMART(ResultSet rs) throws SQLException {
		super();
		this.tablename = rs.getString("TABLE_NAME");
		this.table_value = rs.getString("TABLE_VALUES");
		this.table_Insert = rs.getString("TABLE_SQL_FILL");
		this.table_filters = rs.getString("TABLE_REQUEST_FILTERS");
		this.table_field_filter = rs.getString("TABLE_FIELD_FILTER");
		this.table_type_filter = rs.getString("TABLE_TYPE_FILTER");
		this.last_request = rs.getString("LAST_REQUEST");
	}

	@Override
	public String toString() {
		return "RFC_DATAMART [tablename=" + tablename + ", table_value=" + table_value + ", table_Insert="
				+ table_Insert + ", maxRows=" + maxRows + "]";
	}

	public String[] getResourceValues(String values) {
		String[] splittedValues = values.split(SQLDELIMITER);
		return splittedValues;
	}

	public String[] getSapResourceValues(JCoTable resultTable) {
		String[] sapSplitValues = resultTable.getString("WA").split(RFCDELIMITER);
		return sapSplitValues;
	}

	public void fillSapResourceValues(RFC_DATAMART rfc_datamart, PreparedStatement stm) throws SQLException {
		String[] tableValues = getResourceValues(rfc_datamart.table_value);
		for (int i = 0; i < tableValues.length; i++) {
			String valueOf = "";
			try {
				valueOf = rfc_datamart.getTable_values()[i].trim();
			} catch (Exception e) {
				valueOf = "";
			}
			stm.setString(i + 1, valueOf);
		}
		stm.addBatch();
	}

	public JCoTable StandarGetTableFields(JCoFunction function, RFC_DATAMART rfc_datamart) throws JCoException {
		JCoTable fields = function.getTableParameterList().getTable("FIELDS");
		String[] values = getResourceValues(rfc_datamart.getTable_value());
		for (String value : values) {
			fields.appendRow();
			fields.setValue("FIELDNAME", value);
		}
		return fields;
	}

	public JCoTable StandartGetTableFilters(JCoFunction function, RFC_DATAMART rfc_datamart) throws JCoException {
		JCoTable fields = function.getTableParameterList().getTable("OPTIONS");
		String[] filters = getResourceValues(rfc_datamart.getTable_filters());
		int filterNumber = 1;
		for (String filter : filters) {
			fields.appendRow();
			if (filterNumber > 1) {
				fields.setValue("TEXT", " AND " + filter);
			} else {
				fields.setValue("TEXT", filter);
			}
			filterNumber++;
		}
		return fields;
	}

}
