package com.gmodelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gmodelo.utils.ConnectionManager;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

import data.inventarios.sincro.RFC_DATAMART;

public class JcoDao {

	final static String RFC_DESTINATION = "RFC_DESTINATION";
	final private static String RFC_NAME_PERSONNEL = "/BOA/ZRFC_READ_TABLE";
	final private static String RFC_COUNT_TABLE = "/BOA/ZGET_TABLE_SIZE_RFC";
	final static Integer masterRows = 5000;
	final static Integer masterBeginRow = 0;
	public static final String REQUESTTABLECONFIG = "SELECT TABLE_NAME, TABLE_VALUES, TABLE_SQL_FILL FROM RFC_TABLE_FILL WITH(NOLOCK)";
	public static final String REQUESTTABLECONFIGDELTA = "SELECT TABLE_NAME, TABLE_VALUES, TABLE_SQL_FILL, TABLE_REQUEST_FILTERS FROM RFC_TABLE_FILL WITH(NOLOCK) WHERE LAST_REQUEST IS NULL OR "
			+ " CONVERT(date,getdate()) > CONVERT(date,dateadd(day,7,LAST_REQUEST))";

	public void RequestFirstRun() {
		Connection con = new ConnectionManager().createConnection(ConnectionManager.connectionBean);
		try {
			PreparedStatement stm = con.prepareStatement(REQUESTTABLECONFIGDELTA);
			List<RFC_DATAMART> rfc_datamarts = new ArrayList<>();
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				rfc_datamarts.add(new RFC_DATAMART(rs));
			}
			if (rfc_datamarts.size() > 0) {
				for (RFC_DATAMART datamart : rfc_datamarts) {
					datamart.setMaxRows(RequestCurretRows(datamart.getTablename()));
					// datamart.setMaxRows(5000);
					System.out.println("Inicia Extraccion Tabla " + datamart.getTablename() + " : " + new Date());
					RequestTableViaDatabase(datamart, con, masterRows, masterBeginRow);
					System.out.println("Finalizo Extraccion Tabla " + datamart.getTablename() + " : " + new Date());
					UpdateLastExcecute(con, datamart);
				}
			} else {
				System.out.println("NODATA");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JCoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static final String UPDATETABLEDATAMART = "UPDATE RFC_TABLE_FILL SET LAST_REQUEST = getdate() WHERE TABLE_NAME = ?";

	public void UpdateLastExcecute(Connection con, RFC_DATAMART datamart) {
		try {
			PreparedStatement stm = con.prepareStatement(UPDATETABLEDATAMART);
			stm.setString(1, datamart.getTablename());
			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Integer RequestCurretRows(String tableName) throws JCoException {
		JCoDestination destination = JCoDestinationManager.getDestination(RFC_DESTINATION);
		JCoFunction function = destination.getRepository().getFunction(RFC_COUNT_TABLE);
		Integer currentRows = 0;
		if (function == null)
			throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
		function.getImportParameterList().setValue("TABLE_NAME", tableName);
		function.getImportParameterList().setValue("CROSS_CLIENT", "");
		try {
			function.execute(destination);
			currentRows = function.getExportParameterList().getInt("TABLE_SIZE");
		} catch (AbapException e) {
			e.printStackTrace();
			currentRows = 0;
		}

		return currentRows;
	}

	public void RequestTableViaDatabase(RFC_DATAMART rfc_datamart, Connection con, Integer rowCount, Integer rowSkip)
			throws JCoException, SQLException {
		if (rfc_datamart.getMaxRows() == 0) {
			for (; rowSkip <= 999999999; rowSkip += rowCount) {
				if (GetRFCTable(rfc_datamart, String.valueOf(rowSkip), String.valueOf(rowCount), con) == 9999) {
					break;
				}
			}
		} else {
			for (; rowSkip <= rfc_datamart.getMaxRows(); rowSkip += rowCount) {
				GetRFCTable(rfc_datamart, String.valueOf(rowSkip), String.valueOf(rowCount), con);
			}
		}
	}

	public Integer GetRFCTable(RFC_DATAMART rfc_datamart, String rowSkip, String rowCount, Connection con)
			throws JCoException {
		Integer success = 1;
		JCoDestination destination = JCoDestinationManager.getDestination(RFC_DESTINATION);
		JCoFunction function = destination.getRepository().getFunction(RFC_NAME_PERSONNEL);
		if (function == null)
			throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
		function.getImportParameterList().setValue("QUERY_TABLE", rfc_datamart.getTablename());
		function.getImportParameterList().setValue("DELIMITER", RFC_DATAMART.RFCDELIMITER);
		function.getImportParameterList().setValue("NO_DATA", "");
		function.getImportParameterList().setValue("ROWSKIPS", rowSkip);
		function.getImportParameterList().setValue("ROWCOUNT", rowCount);
		function.getImportParameterList().setValue("CROSS_CLIENT", "");
		rfc_datamart.StandarGetTableFields(function, rfc_datamart);
		if (rfc_datamart.getTable_filters() != null && !rfc_datamart.getTable_filters().isEmpty()) {
			rfc_datamart.StandartGetTableFilters(function, rfc_datamart);
		}
		try {
			function.execute(destination);
			JCoTable resultTable = null;
			resultTable = function.getTableParameterList().getTable(0);
			PreparedStatement stm = con.prepareStatement(rfc_datamart.getTable_Insert());
			try {
				do {
					try {
						rfc_datamart.setTable_values(rfc_datamart.getSapResourceValues(resultTable));
						rfc_datamart.fillSapResourceValues(rfc_datamart, stm);

					} catch (SQLException e) {
						e.printStackTrace();
						success = 99;
					} catch (Exception e) {
						if (rfc_datamart.getMaxRows() == 0) {
							return 9999;
						} else {
							success = 99;
						}
					}
				} while (resultTable.nextRow());
				stm.executeBatch();
			} catch (Exception e) {
				e.printStackTrace();
				success = 99;
			}
		} catch (AbapException e) {
			e.printStackTrace();
			success = 99;
		} catch (SQLException e) {
			e.printStackTrace();
			success = 99;
		}
		return success;

	}

}
