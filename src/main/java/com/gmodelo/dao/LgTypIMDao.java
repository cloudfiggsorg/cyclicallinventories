package com.gmodelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.LgTypIM;
import com.gmodelo.beans.LgplaIM;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class LgTypIMDao {
	
	private Logger log = Logger.getLogger(LgTypIMDao.class.getName());

	public Response<LgTypIM> saveLgTypIM(LgTypIM lgTypIM, String createdBy) {
		Response<LgTypIM> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		final String INV_SP_ADD_LGTYPE_IM = "INV_SP_ADD_LGTYPE_IM ?, ?, ?, ?, ?, ?, ?"; 		
		final String INV_SP_DEL_LGPLA_IM = "INV_SP_DEL_LGPLA_IM ?, ?";				
		final String INV_SP_ADD_LGPLA_IM = "INV_SP_ADD_LGPLA_IM ?, ?, ?, ?, ?";
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		log.info("[addRoute] Preparing sentence...");

		try {
			
			con.setAutoCommit(false);
			// ADD ROUTE
			
			cs = con.prepareCall(INV_SP_ADD_LGTYPE_IM);
			cs.setString(1, lgTypIM.getLgTyp());
			cs.setString(2, lgTypIM.getLtypt());
			cs.setString(3, lgTypIM.getBukrs());
			cs.setString(4, lgTypIM.getWerks());
			cs.setString(5, lgTypIM.getLgort());
			cs.setString(6, lgTypIM.getLgnum());
			cs.setString(7, createdBy);
			cs.registerOutParameter(1, Types.INTEGER);

			log.info("[addLGTYP] Executing query...");
			cs.execute();
			
			lgTypIM.setLgTyp(cs.getString(1));
			
			//Eliminar posiciones
			String ids = "";
			for (int i = 0; i < lgTypIM.getLsLgPla().size(); i++) {
								
				if(lgTypIM.getLsLgPla().get(i).getLgPlaId() > 0){
					ids += lgTypIM.getLsLgPla().get(i).getLgPlaId() + ",";
				}				
			}
												
			cs = null;
			cs = con.prepareCall(INV_SP_DEL_LGPLA_IM);
			cs.setString(1, lgTypIM.getLgTyp());
			cs.setString(2, ids);
			cs.execute();
			
			// INSERTAR POSICIONES
			for (int i = 0; i < lgTypIM.getLsLgPla().size(); i++) {
				
				cs = null;
				log.info("[addLGTYPPosition] Preparing sentence...");
				cs = con.prepareCall(INV_SP_ADD_LGPLA_IM);
				cs.setInt(1, lgTypIM.getLsLgPla().get(i).getLgPlaId());
				cs.setString(2, lgTypIM.getLsLgPla().get(i).getGltypId());
				cs.setString(3, lgTypIM.getLsLgPla().get(i).getDescription());
				cs.setByte(4, (byte) (lgTypIM.getLsLgPla().get(i).isStatus()? 1 : 0));
				cs.setString(5, createdBy);
				cs.registerOutParameter(1, Types.INTEGER);

				log.info("[addLGTYPPosition] Executing query...");
				cs.execute(); 
				lgTypIM.getLsLgPla().get(i).setLgPlaId(cs.getInt(1));
			}
			
			log.info("[addLGTYP] Sentence successfully executed.");

			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, "[addRoute] " + warning.getMessage());
				warning = warning.getNextWarning();
			}

			con.commit();
			// Free resources
			cs.close();

		} catch (SQLException e) {
			try {
				//deshace todos los cambios realizados en los datos
				log.log(Level.WARNING,"[addLGTYP] Execute rollback");
				con.rollback();
			} catch (SQLException e1) {
				log.log(Level.SEVERE, "[addLGTYP] Not rollback .", e);
			}
			log.log(Level.SEVERE,
					"[addLGTYP] Some error occurred while was trying to insert an LGTYP", e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[addLGTYP] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(lgTypIM);
		return res;
	}
	
	public Response<List<LgTypIM>> getLgTypsIM(LgTypIM lgTypIM, String searchFilter) {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<LgTypIM>> res = new Response<List<LgTypIM>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<LgTypIM> listLgTypIM = new ArrayList<LgTypIM>();
		String INV_VW_LGTYPE_IM = null;
		LgTypIM lgTypIMAux;

		INV_VW_LGTYPE_IM = "SELECT LGTYP_ID, LGT_LTYPT, LGT_BUKRS, BUTXT, LGT_WERKS, NAME1, LGT_LGORT, LGOBE, LGT_LGNUM, LGT_STATUS FROM dbo.INV_VW_LGTYPE_IM WITH(NOLOCK) ";

		if (searchFilter != null) {
			INV_VW_LGTYPE_IM += "WHERE LGTYP_ID LIKE '%" + searchFilter + "%' OR LGT_LTYPT LIKE '%" + searchFilter + "%'";
		} else {
			String condition = buildCondition(lgTypIM);
			if (condition != null) {
				INV_VW_LGTYPE_IM += condition;
			}
		}
		log.info(INV_VW_LGTYPE_IM);
		log.info("[getRoutesDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_LGTYPE_IM);

			log.info("[getRoutesDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				
				lgTypIMAux = new LgTypIM();
				lgTypIMAux.setLgTyp(rs.getString(1));
				lgTypIMAux.setLtypt(rs.getString(2));
				lgTypIMAux.setBukrs(rs.getString(3));
				lgTypIMAux.setbDesc(rs.getString(4));
				lgTypIMAux.setWerks(rs.getString(5));
				lgTypIMAux.setwDesc(rs.getString(6));
				lgTypIMAux.setLgort(rs.getString(7));
				lgTypIMAux.setgDesc(rs.getString(8));
				lgTypIMAux.setLgnum(rs.getString(9));								
				lgTypIMAux.setLsLgPla(this.getPositions(rs.getString(1)));

				listLgTypIM.add(lgTypIMAux);
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
			log.info("[getRoutesDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getRoutesDao] Some error occurred while was trying to execute the query: " + INV_VW_LGTYPE_IM, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getRoutesDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listLgTypIM);
		return res;
	}
	
	public List<LgplaIM> getPositions(String lgtyp) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		List<LgplaIM> listPositions = new ArrayList<LgplaIM>();

		String INV_VW_LGPLA_IM = "SELECT LGPLA_ID, LGP_DESC, LGP_STATUS FROM dbo.INV_VW_LGPLA_IM WITH(NOLOCK) WHERE LGTYP_ID = ?";

		log.info(INV_VW_LGPLA_IM);
		log.info("[getPositionsDao] Preparing sentence...");

		stm = con.prepareStatement(INV_VW_LGPLA_IM);
		stm.setString(1, lgtyp);
		log.info("[getPositionsDao] Executing query...");

		ResultSet rs = stm.executeQuery();

		while (rs.next()) {
			
			LgplaIM position = new LgplaIM();
			position.setGltypId(lgtyp);
			position.setLgPlaId(rs.getInt(1));
			position.setDescription(rs.getString(2));
			position.setStatus(rs.getBoolean(3));
			listPositions.add(position);
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
		log.info("[getPositionsDao] Sentence successfully executed.");
		con.close();

		return listPositions;
	}
	
	public Response<Object> deleteLgTypIM(String arrayToDelete) {
		
		log.info("[deleteLgTyp] "+ arrayToDelete);

		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		final String INV_SP_DEL_LGTYPE_IM = "INV_SP_DEL_LGTYPE_IM ?";

		log.info("[deleteLgTyp] Preparing sentence...");

		try {
			cs = con.prepareCall(INV_SP_DEL_LGTYPE_IM);
			cs.setString(1, arrayToDelete);
			log.info("[deleteLgTyp] Executing query...");

			cs.execute();

			abstractResult.setResultId(1);

			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, "[deleteLgTyp] " + warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			cs.close();

			log.info("[deleteLgTyp] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[deleteLgTyp] Some error occurred while was trying to execute the S.P.: " + INV_SP_DEL_LGTYPE_IM,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[deleteRouteDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res;
	}
	
	private String buildCondition(LgTypIM lgTypIM) {
		
		String lgTyp; //The hold type
		String ltypt; //The hold description
		String bukrs; //The society Id
		String bDesc; //The society description
		String werks; //The werks Id	
		String wDesc; //The werks description
		String lgort; //The warehouse Id
		String gDesc; //The warehouse description
		String lgnum; //The lgnum
		String condition = "";
		
		lgTyp = (lgTypIM.getLgTyp() != null)? 
				(condition.contains("WHERE") ? " AND " : " WHERE ") + " LGTYP_ID = '" + lgTypIM.getLgTyp() + "' "	: "";
		condition += lgTyp;
		ltypt = (lgTypIM.getLtypt() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGT_LTYPT LIKE '%" + lgTypIM.getLtypt() + "%' " : "";
		condition += ltypt;
		bukrs = (lgTypIM.getBukrs() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGT_BUKRS = '" + lgTypIM.getBukrs() + "' " : "";
		condition += bukrs;
		bDesc = (lgTypIM.getbDesc() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " BUTXT LIKE '%" + lgTypIM.getbDesc() + "%' " : "";
		condition += bDesc;
		werks = (lgTypIM.getWerks() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " WERKS = '" + lgTypIM.getWerks() + "' " : "";
		condition += werks;
		wDesc = (lgTypIM.getwDesc() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " NAME1 LIKE '%" + lgTypIM.getwDesc() + "%' " : "";
		condition += wDesc;		
		lgort = (lgTypIM.getLgort() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGT_LGORT = '" + lgTypIM.getLgort() + "' " : "";
		condition += lgort;
		gDesc = (lgTypIM.getgDesc() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGOBE LIKE '%" + lgTypIM.getgDesc() + "%' " : "";
		condition += gDesc;		
		lgnum = (lgTypIM.getLgnum() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGT_LGNUM = '" + lgTypIM.getLgnum() + "' " : "";
		condition += lgnum;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

}
