package com.gmodelo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.gmodelo.beans.ConnectionBean;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

public class ConnectionManager {
	@Resource
	DataSource dataSource;

	public static ConnectionBean connectionBean = new ConnectionBean();

	static {
		connectionBean.setDatasource("INVCIC_DATASOURCE");
		connectionBean.setDbName("INV_CIC_DB");
		connectionBean.setHostname("35.172.127.238");
		connectionBean.setPassword("#Inventarios@2018");
		connectionBean.setPort("1433");
		connectionBean.setUser("INV_CIC_ADMIN");
	}

	public Connection createConnection() {

		Connection con = null;
		try {
			dataSource = (DataSource) new InitialContext().lookup(connectionBean.getDatasource());
		} catch (Exception e) {
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				String connectionUrl = "jdbc:sqlserver://" + connectionBean.getHostname() + ":"
						+ connectionBean.getPort() + ";" + "databaseName=" + connectionBean.getDbName() + ";user="
						+ connectionBean.getUser() + ";password=" + connectionBean.getPassword() + ";";
				con = DriverManager.getConnection(connectionUrl);
			} catch (SQLException e1) {
				System.out.println("conectBDSQL.SQLException: " + e1.getMessage());
			} catch (ClassNotFoundException e1) {
				System.out.println("conectBDSQL.ClassNotFoundException: " + e1.getMessage());
			}
			return con;
		}
		if (dataSource != null) {
			try {
				con = dataSource.getConnection();
				return con;
			} catch (SQLException e) {
				return null;
			}
		} else {
			return null;
		}

	}

	public void CloseConnection(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JCoDestination getSapConnection(String destinationName) throws JCoException, RuntimeException, Exception {
		JCoDestination destination = null;
		try {
			destination = JCoDestinationManager.getDestination(destinationName);
		} catch (JCoException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return destination;
	}

}
