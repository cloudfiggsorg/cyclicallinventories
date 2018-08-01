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
import com.gmodelo.beans.AbstractResults;
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
	static DirContext ldapContext;

	public AbstractResults ValidateLDAPLogin(LoginBean login, Connection con) throws InvCicException {
		AbstractResults result = new AbstractResults();
		Utilities iUtilities = new Utilities();
		try {
			Hashtable<String, String> ldapEnv = new Hashtable<String, String>(11);
			ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			ldapEnv.put(Context.PROVIDER_URL,
					iUtilities.GetValueRepByKey(con, ReturnValues.REP_LDAP_RPOVIDER).getStrCom1());
			ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
			ldapEnv.put(Context.SECURITY_PRINCIPAL,
					iUtilities.GetValueRepByKey(con, ReturnValues.REP_LDAP_DOMAIN).getStrCom1() + "\\"
							+ login.getLoginId());
			ldapEnv.put(Context.SECURITY_CREDENTIALS, login.getLoginPass());
			ldapContext = new InitialDirContext(ldapEnv);
			ldapContext.close();
		} catch (NamingException e) {
			try {
				System.out.println(e.getMessage());
				if (e.getMessage().lastIndexOf("AcceptSecurityContext") != -1) {
					result.setResultId(ReturnValues.IUSERNOTEXISTS);
					result.setResultMsgAbs(iUtilities
							.GetLangByKey(con, ReturnValues.SUSERNOTEXISTS, login.getLoginLang()).getStrCom1());

				} else if (e.getCause().toString().lastIndexOf("SECURITY") != -1) {
					result.setResultId(ReturnValues.IPASSWORDNOTMATCH);
					result.setResultMsgAbs(iUtilities
							.GetLangByKey(con, ReturnValues.SPASSWORDNOTMATCH, login.getLoginLang()).getStrCom1());
				} else if (e.getCause().toString().lastIndexOf("ConnectException") != -1) {
					result.setResultId(ReturnValues.ILDAPTIMEOUT);
					result.setResultMsgAbs(
							iUtilities.GetLangByKey(con, ReturnValues.SLDAPTIMEOUT, login.getLoginLang()).getStrCom1());
				} else {
					throw new InvCicException(e);
				}
			} catch (Exception ex) {
				throw new InvCicException(ex);
			}
		} catch (InvCicException e) {
			throw new InvCicException(e);
		} catch (NullPointerException e) {
			throw new InvCicException(e);
		}
		return result;
	}

	public Connection createConnection(ConnectionBean connectionBean) {

		Connection con = null;
		try {
			dataSource = (DataSource) new InitialContext().lookup(connectionBean.getDatasource());
		} catch (Exception e) {

			System.out.println("Entre por Datos de Conexion");
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
			System.out.println("Entre por Datasource");
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
