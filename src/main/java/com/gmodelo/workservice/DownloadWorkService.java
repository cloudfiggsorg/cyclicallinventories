package com.gmodelo.workservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.Exception.InvCicException;
import com.gmodelo.beans.LoginBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RfcTablesBean;
import com.gmodelo.utils.ConnectionManager;
import com.google.gson.Gson;

public class DownloadWorkService {

	Logger log = Logger.getLogger(getClass().getName());

	/*
	 * * * This Method Is used for the background data to the smartphone In
	 * the @Request it will accept as follows, the @Return value is always a
	 * List<RfcTableBean>
	 * 
	 * @ParamType1 @Null this will return all data in the table
	 * 
	 * @ParamType2 @List<RfcTableBean> this will return all the tables
	 * enumerated on the table_name value in the RfcTableBean object contained
	 * in the list
	 * 
	 * @ParamType3 @RfcTableBean object, this object will pass the following
	 * filters one or all may applies.
	 * 
	 * @ParamType3 @FilterValues #TABLE_NAME if you want only information for
	 * the defined table.
	 * 
	 * @ParamType3 @FilterValues #DEVICE this will give you information of wich
	 * tables will be downloaded to device.
	 * 
	 * @ParamType3 @FilterValues #LAST_UPDATE will give you the information that
	 * is updated after the specified date.
	 * 
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String GetInfoTablesWS(Request<LoginBean<?>> request) {
		Response<List<RfcTablesBean<?>>> response = new Response<>();
		// CaFigueroa - Pending Code
		List<RfcTablesBean<?>> listToReturn = new ArrayList<>();
		try {
			listToReturn = new RfcTablesBean().RfcTablesBeanData(request.getLsObject());
		} catch (InvCicException e) {
			listToReturn = null;
		}
		response.setLsObject(listToReturn);
		return new Gson().toJson(response);
	}

	
	/*
	 *	GetMasterDataWS 
	 *	Expects a @Request with a @LoginBean with @List<RfcTablesBean> with the table names
	 * 	The expected Result is a @Response<#Object>
	 * 	where #Object will contain a List<RfcTablesBean<DataOfTable>>
	 * 	@Param @List<RfcTablesBean> with #Table_name values to get info of the tables.
	 * 	@Param @List<RfcTablesBean> with #Last_Request values to get info of the tables that are recently updated
	 * 	Pending TODO change the dynamic query for the views, but the view needs to be
	 * 	exact in names that table 
	 */
	 
	@SuppressWarnings("unchecked")
	public String GetMasterDataWS(Request<LoginBean<?>> request) {
		log.log(Level.WARNING, "Init... GetMasterDataWS(Request<LoginBean<?>> request)");
		Response<Object> response = new Response<>();
		try {
			Connection con = new ConnectionManager().createConnection(ConnectionManager.connectionBean);
			PreparedStatement stm = null;
			ResultSet rs = null;
			List<RfcTablesBean<?>> responseList = new ArrayList<>();
			List<RfcTablesBean<Object>> listOfTables = (List<RfcTablesBean<Object>>) request.getLsObject();
			for (RfcTablesBean<Object> rfcBean : listOfTables) {
				try {
					String queryValuesString = rfcBean.getTable_value().replaceAll("\\|", "\\,");
					queryValuesString = queryValuesString.substring(0, queryValuesString.length() - 1);

					String executableQuery = "SELECT " + queryValuesString + " FROM " + rfcBean.getTable_name()
							+ " WITH(NOLOCK) ";

					if (rfcBean.getLastUpdate() != null) {
						executableQuery += " WHERE LASTMODIFY > '" + rfcBean.getLastUpdate() + "' ";
					}

					stm = con.prepareStatement(executableQuery);
					rs = stm.executeQuery();
					List<HashMap<String, String>> mappedColumnData = new ArrayList<>();
					ResultSetMetaData rsMeta = rs.getMetaData();
					List<String> columnNames = new ArrayList<>();
					for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
						columnNames.add(rsMeta.getColumnName(i));
					}
					while (rs.next()) {
						HashMap<String, String> columnsMap = new HashMap<>();
						for (String colName : columnNames) {
							columnsMap.put(colName, rs.getString(colName));
						}
						if (!columnsMap.isEmpty()) {
							mappedColumnData.add(columnsMap);
						}
					}
					rfcBean.setStoredValues(mappedColumnData);
				} catch (SQLException e) {
					rfcBean.setStoredValues(null);
				}
				responseList.add(rfcBean);
			}
			response.setLsObject(responseList);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Get Master Data WorkService Error: ", e);
			response.setLsObject(null);
		}
		return new Gson().toJson(response);
	}

	public Response<List<Object>> GetMasterDataWork(Request<LoginBean<?>> request) {

		log.log(Level.SEVERE, "Iniciando GetMasterDataWork");
		Response<List<Object>> response = new Response<List<Object>>();
		Connection con = new ConnectionManager().createConnection(ConnectionManager.connectionBean);
		List<Object> tableList = new ArrayList<>();
		try {
			PreparedStatement stm = con.prepareStatement(
					"SELECT TABLE_NAME, TABLE_VALUES FROM RFC_TABLE_FILL WITH(NOLOCK) WHERE TABLE_NAME IN('T134T')");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {

				HashMap<String, Object> tableJson = new HashMap<>();

				String stm2QueryString = rs.getString("TABLE_VALUES").replaceAll("\\|", "\\,");
				stm2QueryString = stm2QueryString.substring(0, stm2QueryString.length() - 1);
				PreparedStatement stm2 = con.prepareStatement(
						"SELECT " + stm2QueryString + "  FROM " + rs.getString("TABLE_NAME") + "  WITH(NOLOCK) ");
				ResultSet rs2 = stm2.executeQuery();
				List<HashMap<String, String>> jsonObjectList = new ArrayList<>();
				ResultSetMetaData objectMetadata = rs2.getMetaData();
				List<String> columnNames = new ArrayList<>();
				for (int i = 0; i < objectMetadata.getColumnCount(); i++) {
					columnNames.add(objectMetadata.getColumnName(i + 1));
				}
				if (!columnNames.isEmpty()) {
					while (rs2.next()) {
						HashMap<String, String> columnsJson = new HashMap<>();
						for (String columName : columnNames) {
							columnsJson.put(columName, rs2.getString(columName));
						}
						if (!columnsJson.isEmpty()) {
							jsonObjectList.add(columnsJson);
						}
					}
				}
				if (!jsonObjectList.isEmpty()) {
					tableJson.put(rs.getString("TABLE_NAME"), jsonObjectList);
					tableList.add(tableJson);
				}
			}
			response.setLsObject(tableList);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return response;
	}
}
