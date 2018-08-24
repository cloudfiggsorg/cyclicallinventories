package com.gmodelo.workservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.gmodelo.Exception.InvCicException;
import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RfcTablesBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

	@SuppressWarnings({ "rawtypes" })
	public String GetInfoTablesWS(Request request, HttpServletRequest httpRequest) {
		log.warning("Entrando al Workservice");
		log.warning(request.toString());
		Response<List<RfcTablesBean>> response = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		response.setAbstractResult(abstractResult);
		// CaFigueroa - Pending Code
		List<RfcTablesBean> listToReturn = new ArrayList<>();
		try {
			listToReturn = new RfcTablesBean().RfcTablesBeanData(request.getLsObject());
		} catch (InvCicException e) {
			listToReturn = null;
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());

		}
		log.warning("Saliendo del Workservice");
		response.setLsObject(listToReturn);
		abstractResult.setStrCom1(httpRequest.getSession().getId());
		abstractResult.setIntCom1(httpRequest.getSession().getMaxInactiveInterval());
		log.warning(response.toString());
		return new Gson().toJson(response);
	}

	/*
	 * GetMasterDataWS Expects a @Request with a @LoginBean
	 * with @List<RfcTablesBean> with the table names The expected Result is
	 * a @Response<#Object> where #Object will contain a
	 * List<RfcTablesBean<DataOfTable>>
	 * 
	 * @Param @List<RfcTablesBean> with #Table_name values to get info of the
	 * tables.
	 * 
	 * @Param @List<RfcTablesBean> with #Last_Request values to get info of the
	 * tables that are recently updated Pending TODO change the dynamic query
	 * for the views, but the view needs to be exact in names that table
	 */

	public String GetMasterDataWS(Request request, HttpServletRequest httpRequest) {
		log.log(Level.WARNING, "Init... GetMasterDataWS(Request<LoginBean<?>> request)");
		log.log(Level.WARNING, "Request Data" + request.toString());
		Response<String> response = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		try {
			Connection con = new ConnectionManager().createConnection(ConnectionManager.connectionBean);
			PreparedStatement stm = null;
			ResultSet rs = null;
			Type listType = new TypeToken<ArrayList<RfcTablesBean>>() {
			}.getType(); // Codigo para Castear a Lista
			List<RfcTablesBean> responseList = new ArrayList<>();
			List<RfcTablesBean> listOfTables = new Gson().fromJson(request.getLsObject().toString(), listType);
			for (RfcTablesBean rfcBean : listOfTables) {
				log.log(Level.WARNING, rfcBean.toString());
				try {
					String queryValuesString = rfcBean.getTable_value().replaceAll("\\|", "\\,");
					queryValuesString = queryValuesString.substring(0, queryValuesString.length() - 1);

					String executableQuery = "SELECT " + queryValuesString + " FROM " + rfcBean.getTable_name()
							+ " WITH(NOLOCK) "
					// + " ORDER BY " + queryValuesString + " OFFSET ("
					// + rfcBean.getTableValues().getCurrent_row() + ") " + "
					// ROWS FETCH NEXT ("
					// + rfcBean.getTableValues().getRow_skips() + ") ROWS
					// ONLY";
					;
					if (rfcBean.getLastUpdate() != null) {
						executableQuery += " WHERE CONVERT(DATE,LASTMODIFY) > '"
								+ new SimpleDateFormat("yyyy-MM-dd").format(new Date(rfcBean.getLastUpdate())) + "' ";
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
				log.log(Level.WARNING, "Before Adding to List" + rfcBean.getTable_name());

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos;

				try {
					oos = new ObjectOutputStream(baos);
					oos.writeObject(rfcBean);
					oos.close();
					response.setLsObject(Base64.getEncoder().encodeToString(baos.toByteArray()));

				} catch (IOException e) {
					response.setLsObject(null);
					// e.printStackTrace();
					log.log(Level.SEVERE, "Before Adding to List" + e);
				}

			}
			log.log(Level.WARNING, "Before Adding to ResponseList to LSOBJECT");
			abstractResult.setStrCom1(httpRequest.getSession().getId());
			abstractResult.setIntCom1(httpRequest.getSession().getMaxInactiveInterval());
			response.setAbstractResult(abstractResult);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Get Master Data WorkService Error: ", e);
			e.printStackTrace();
			response.setLsObject(null);
			abstractResult.setStrCom1(httpRequest.getSession().getId());
			abstractResult.setIntCom1(httpRequest.getSession().getMaxInactiveInterval());
		}
		log.log(Level.WARNING, "Before Response" + response.getAbstractResult());
		return new Gson().toJson(response);
	}

}
