package com.gmodelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.utils.ConnectionManager;

public class DeltasDao {

	private Logger log = Logger.getLogger(DeltasDao.class.getName());
	
	public String getCronDelta(){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;

		String INV_CIC_REPOSITORY = "SELECT STORED_VALUE FROM INV_CIC_REPOSITORY WITH(NOLOCK) WHERE STORED_KEY = ?";
		log.warning(INV_CIC_REPOSITORY);
		String cron = "";
		log.log(Level.WARNING, "[getCronDeltaDao] Preparing sentence...");
		
		try {
			stm = con.prepareStatement(INV_CIC_REPOSITORY);
			stm.setString(1, "CRON_DELTA");
			log.log(Level.WARNING, "[getCronDeltaDao] Executing query...");

			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				cron = rs.getString("STORED_VALUE");
			}

			// Retrive the warnings if there're
			SQLWarning warning = stm.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			rs.close();
			stm.close();
			log.log(Level.WARNING, "[getCronDeltaDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getCronDeltaDao] Some error occurred while was trying to execute the query: " + INV_CIC_REPOSITORY, e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getCronDeltaDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		return cron;
	}
	
}
