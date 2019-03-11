package com.gmodelo.cyclicinventories.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.Repository;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.gmodelo.cyclicinventories.utils.Utilities;

public class RepositoryDao {
	
private Logger log = Logger.getLogger(RepositoryDao.class.getName());
	
	public Response<Repository> saveOption(Repository option){
		
		Response<Repository> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		
		final String SP = "INV_SP_SAVE_OPTIONS ?, ?, ?"; //The Store procedure to call
		
		log.info("[saveOption] Preparing sentence...");

		try {
			cs = con.prepareCall(SP);
			
			cs.setString(1, option.getKey());
			if(option.isEncoded()){
				cs.setString(2, new Utilities().encodeB64(option.getValue()));
			}else{
				cs.setString(2, option.getValue());
			}			
			cs.setByte(3, (byte)(option.isEncoded() ? 1 : 0));
			
			log.info("[saveOption] Executing query...");
			
			cs.execute();
			abstractResult.setResultId(ReturnValues.ISUCCESS);
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[saveOption] "+ warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.info("[saveOption] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[saveOption] Some error occurred while was trying to execute the S.P.: " + SP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[addJustify] Some error occurred while was trying to close the connection.", e);
			}
		}
	
		res.setAbstractResult(abstractResult);
		//res.setLsObject(option);
		return res ;
	}
	
	public Response<Object> deleteOptions(String ids){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		
		final String SP = "INV_SP_DEL_OPTIONS ?"; //The Store procedure to call
		
		log.info("[deleteOptions] Preparing sentence...");
		
		try {
			
			cs = con.prepareCall(SP);			
			cs.setString(1, ids);
			
			log.log(Level.WARNING,"[deleteOptions] Executing query...");
			
			cs.execute();
			
			abstractResult.setResultId(ReturnValues.ISUCCESS);
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[deleteOptions] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.info("[deleteOptions] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[deleteOptions] Some error occurred while was trying to execute the S.P.: "+ SP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[deleteOptions] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}
	
	public Response<List<Repository>> getOptions() {
		
		Response<List<Repository>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		Statement stm = null;		
		List<Repository> lsOptions = new ArrayList<>();
		Repository option = new Repository();
		Utilities utils = new Utilities();
		
		String QUERY = "SELECT STORED_KEY, STORED_VALUE, STORED_ENCODED FROM INV_CIC_REPOSITORY ";		
		log.info(QUERY);
		log.info("[getOptions] Preparing sentence...");
		
		try {
			stm = con.createStatement();
			log.info("[getOptions] Executing query...");
			ResultSet rs = stm.executeQuery(QUERY);
			
			while (rs.next()) {
				
				option = new Repository();
				option.setKey(rs.getString("STORED_KEY"));
				option.setEncoded(rs.getBoolean("STORED_ENCODED"));
				if(option.isEncoded()){
					option.setValue(utils.decodeB64(rs.getString("STORED_VALUE")));
				}else{
					option.setValue(rs.getString("STORED_VALUE"));
				}
				lsOptions.add(option);
			}
			log.info("[getOptions] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getOptions] Some error occurred while was trying to execute the query: "
					+ QUERY, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getOptions] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(lsOptions);
		return res;
	}


}
