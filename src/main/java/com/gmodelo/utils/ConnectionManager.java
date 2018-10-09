package com.gmodelo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.sql.DataSource;

import com.gmodelo.Exception.InvCicException;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.ConnectionBean;
import com.gmodelo.beans.LoginBean;

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

}
