package com.gmodelo.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.ManagedBean;

import com.gmodelo.Exception.InvCicException;
import com.gmodelo.utils.ConnectionManager;
import com.google.gson.Gson;

@ManagedBean
public class RfcTablesBean {

	String table_name;
	String table_value;
	String table_sql;
	Date lastUpdate;
	Boolean device;

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getTable_value() {
		return table_value;
	}

	public void setTable_value(String table_value) {
		this.table_value = table_value;
	}

	public String getTable_sql() {
		return table_sql;
	}

	public void setTable_sql(String table_sql) {
		this.table_sql = table_sql;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Boolean getDevice() {
		return device;
	}

	public void setDevice(Boolean device) {
		this.device = device;
	}

	@Override
	public String toString() {
		return "RfcTablesBean [table_name=" + table_name + ", table_value=" + table_value + ", table_sql=" + table_sql
				+ ", lastUpdate=" + lastUpdate + ", device=" + device + "]";
	}

	public RfcTablesBean(String table_name, String table_value, String table_sql, Date lastUpdate, Boolean device) {
		super();
		this.table_name = table_name;
		this.table_value = table_value;
		this.table_sql = table_sql;
		this.lastUpdate = lastUpdate;
		this.device = device;
	}

	public RfcTablesBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RfcTablesBean(ResultSet rs) throws SQLException {
		super();
		try {
			this.table_name = rs.getString("TABLE_NAME");
		} catch (SQLException e) {
			this.table_name = null;
		}

		try {
			this.table_value = rs.getString("TABLE_VALUES");
		} catch (SQLException e) {
			this.table_value = null;
		}

		try {
			this.table_sql = rs.getString("TABLE_SQL_FILL");
		} catch (SQLException e) {
			this.table_sql = null;
		}

		try {
			this.lastUpdate = rs.getDate("LAST_REQUEST");
		} catch (SQLException e) {
			this.lastUpdate = null;
		}

		try {
			this.device = rs.getBoolean("DEVICE");
		} catch (SQLException e) {
			this.device = null;
		}
	}

	private static final String GET_INFO_RFC_TABLES = "SELECT TABLE_NAME, TABLE_VALUES, TABLE_SQL_FILL, LAST_REQUEST, DEVICE FROM rfc_table_fill WITH(NOLOCK)";

	@SuppressWarnings("resource")
	public List<RfcTablesBean> RfcTablesBeanData(Object rfcTableFilter) throws InvCicException {

		Logger log = Logger.getLogger(this.getClass().getName());

		List<RfcTablesBean> rfcTablesBeans = new ArrayList<>();
		Connection con = null;
		try {
			con = new ConnectionManager().createConnection(ConnectionManager.connectionBean);
			PreparedStatement stm = null;
			ResultSet rs = null;
			if (rfcTableFilter == null) {
				stm = con.prepareStatement(GET_INFO_RFC_TABLES);
				rs = stm.executeQuery();
				while (rs.next()) {
					rfcTablesBeans.add(new RfcTablesBean(rs));
				}
			} else {
				try {
					// ParseObject to Material ListNames
					List<RfcTablesBean> filter = (List<RfcTablesBean>) rfcTableFilter;
					String nameFilters = "";
					for (RfcTablesBean bean : filter) {
						nameFilters += "'" + bean.getTable_name() + "'";
					}
					if (!nameFilters.isEmpty()) {
						stm = con.prepareStatement(GET_INFO_RFC_TABLES + " WHERE TABLE_NAME IN (" + nameFilters + ")");
					} else {
						throw new InvCicException(
								"The Refecenced Filter doesn't contiain any info.... use a @null object instead");
					}
					rs = stm.executeQuery();
					while (rs.next()) {
						rfcTablesBeans.add(new RfcTablesBean(rs));
					}
				} catch (Exception e) {
					try {
						RfcTablesBean filter = (RfcTablesBean) rfcTableFilter;
						String multiFilter = " WHERE ";
						Boolean hasFilters = Boolean.FALSE;
						if (!filter.getTable_name().isEmpty() && filter.getTable_name() != null) {
							multiFilter += " TABLE_NAME ='" + filter.getTable_name() + "' ";
							hasFilters = Boolean.TRUE;
						}
						if (filter.getLastUpdate() != null) {
							multiFilter += " LAST_REQUEST >= '" + filter.getLastUpdate() + "' ";
							hasFilters = Boolean.TRUE;
						}
						if (filter.getDevice() != null) {
							multiFilter += " DEVICE =  ";
							hasFilters = Boolean.TRUE;
							if (filter.getDevice())
								multiFilter += "1";
							else
								multiFilter += "0";
						}
						if (hasFilters) {
							stm = con.prepareStatement(GET_INFO_RFC_TABLES + multiFilter);
						} else {
							stm = con.prepareStatement(GET_INFO_RFC_TABLES);
						}

						rs = stm.executeQuery();
						while (rs.next()) {
							rfcTablesBeans.add(new RfcTablesBean(rs));
						}

					} catch (Exception e1) {
						throw new InvCicException(
								"RfcTablesBean Object Cast Cannot Be Done.... use @List of RfcTableBean with Names or a @Bean - RfcTableBean with data to filter");
					}
				}
			}
		} catch (SQLException sql) {
			throw new InvCicException(sql);
		} finally {
			try {
				con.close();
			} catch (Exception e) {

			}
		}
		return rfcTablesBeans;
	}

}
