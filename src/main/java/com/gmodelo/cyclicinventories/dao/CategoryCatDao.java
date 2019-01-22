package com.gmodelo.cyclicinventories.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.Category;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class CategoryCatDao {
	
	private Logger log = Logger.getLogger(CategoryCatDao.class.getName());
	
	public Response<Category> saveCategory(Category category, String userId){
		
		Response<Category> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		
		final String SP = "INV_SP_ADD_CATEGORY ?, ?, ?"; //The Store procedure to call
		
		log.info("[addGroup] Preparing sentence...");

		try {
			cs = con.prepareCall(SP);
			
			cs.setInt(1, category.getCatId());			
			cs.setString(2, category.getCategory());	
			cs.setString(3, userId);
			
			cs.registerOutParameter(1, Types.INTEGER);
			log.info("[saveCategory] Executing query...");
			
			cs.execute();
			category.setCatId(cs.getInt(1));
			abstractResult.setResultId(ReturnValues.ISUCCESS);
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[saveCategory] "+ warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.info("[saveCategory] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[saveCategory] Some error occurred while was trying to execute the S.P.: " + SP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[saveCategory] Some error occurred while was trying to close the connection.", e);
			}
		}
	
		res.setAbstractResult(abstractResult);
		res.setLsObject(category);
		return res ;
	}
	
	public Response<Object> deleteCategories(String catIds){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		
		final String SP = "INV_SP_DEL_CAT ?"; //The Store procedure to call
		
		log.info("[deleteCategories] Preparing sentence...");
		
		try {
			
			cs = con.prepareCall(SP);			
			cs.setString(1, catIds);
			
			log.log(Level.WARNING,"[deleteCategories] Executing query...");
			
			cs.execute();
			
			abstractResult.setResultId(ReturnValues.ISUCCESS);
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[deleteCategories] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.info("[deleteCategories] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[deleteCategories] Some error occurred while was trying to execute the S.P.: "+ SP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[deleteCategories] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}
	
	public Response<List<Category>> getCategories() {
		
		Response<List<Category>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		Statement stm = null;		
		List<Category> lsCategory = new ArrayList<>();
		Category category = new Category();
		
		String QUERY = "SELECT CAT_ID, CATEGORY FROM INV_CAT_CATEGORY ";		
		log.info(QUERY);
		log.info("[getCategories] Preparing sentence...");
		
		try {
			stm = con.createStatement();
			log.info("[getCategories] Executing query...");
			ResultSet rs = stm.executeQuery(QUERY);
			
			while (rs.next()) {
				
				category = new Category();
				category.setCatId(rs.getInt("CAT_ID"));
				category.setCategory(rs.getString("CATEGORY"));				
				lsCategory.add(category);
			}
			log.info("[getCategories] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getCategories] Some error occurred while was trying to execute the query: "
					+ QUERY, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getCategories] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(lsCategory);
		return res;
	}

}
