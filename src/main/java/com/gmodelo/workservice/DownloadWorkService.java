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

import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import com.gmodelo.beans.LoginBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;

public class DownloadWorkService {

	Logger log = Logger.getLogger(getClass().getName());

	public String GetInfoTablesWS(Request<LoginBean<?>> request) {
		return null;
	}

	public Response<List<Object>> GetMasterDataWork(Request<LoginBean<?>> request) {

		InitialContext ctx;
		try {
			ctx = new InitialContext();
			NamingEnumeration<NameClassPair> list = ctx.list("");
			while (list.hasMore()) {
				System.out.println(list.next().getName());
			}
		} catch (NamingException e2) {
			log.log(Level.INFO, "naming", e2);
		}

		log.log(Level.WARNING, "Iniciando GetMasterDataWork");
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
