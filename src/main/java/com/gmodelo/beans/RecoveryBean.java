package com.gmodelo.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class RecoveryBean {

	String logOnValue;
	String logOnCredential;
	Date logOnDate;
	Date logOnValid;

	public String getLogOnValue() {
		return logOnValue;
	}

	public void setLogOnValue(String logOnValue) {
		this.logOnValue = logOnValue;
	}

	public String getLogOnCredential() {
		return logOnCredential;
	}

	public void setLogOnCredential(String logOnCredential) {
		this.logOnCredential = logOnCredential;
	}

	public Date getLogOnDate() {
		return logOnDate;
	}

	public void setLogOnDate(Date logOnDate) {
		this.logOnDate = logOnDate;
	}

	public Date getLogOnValid() {
		return logOnValid;
	}

	public void setLogOnValid(Date logOnValid) {
		this.logOnValid = logOnValid;
	}

	public RecoveryBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RecoveryBean(String logOnValue, String logOnCredential, Date logOnDate, Date logOnValid) {
		super();
		this.logOnValue = logOnValue;
		this.logOnCredential = logOnCredential;
		this.logOnDate = logOnDate;
		this.logOnValid = logOnValid;
	}

	public RecoveryBean(ResultSet rs) throws SQLException {
		if (rs.next()) {
			this.logOnValue = rs.getString("logOnValue");
			this.logOnCredential = rs.getString("logOnCredential");
			this.logOnDate = rs.getDate("logOnDate");
			this.logOnValid = rs.getDate("logOnValid");
		}
	}

}
