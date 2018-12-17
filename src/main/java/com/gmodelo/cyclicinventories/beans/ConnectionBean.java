package com.gmodelo.cyclicinventories.beans;

public class ConnectionBean {
	
	private String datasource;
	private String hostname;
	private String port;
	private String user;
	private String password;
	private String dbName;
	
	public String getDatasource() {
		return datasource;
	}
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public ConnectionBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ConnectionBean(String datasource, String hostname, String port, String user, String password,
			String dbName) {
		super();
		this.datasource = datasource;
		this.hostname = hostname;
		this.port = port;
		this.user = user;
		this.password = password;
		this.dbName = dbName;
	}
	@Override
	public String toString() {
		return "ConnectionBean [datasource=" + datasource + ", hostname=" + hostname + ", port=" + port + ", user="
				+ user + ", password=" + password + ", dbName=" + dbName + "]";
	}
	
}
