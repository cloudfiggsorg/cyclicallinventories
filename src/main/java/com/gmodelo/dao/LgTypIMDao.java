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
import com.gmodelo.beans.LgTypIMBean;
import com.gmodelo.beans.LgplaIMBean;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class LgTypIMDao {
	
	private Logger log = Logger.getLogger(LgTypIMDao.class.getName());

	public Response<LgTypIMBean> saveLgTypIM(LgTypIMBean lgTypIMBean, String createdBy) {
		Response<LgTypIMBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		final String INV_SP_ADD_LGTYPE_IM = "INV_SP_ADD_LGTYPE_IM ?, ?, ?, ?, ?, ?, ?"; 		
		final String INV_SP_DEL_LGPLA_IM = "INV_SP_DEL_LGPLA_IM ?, ?";				
		final String INV_SP_ADD_LGPLA_IM = "INV_SP_ADD_LGPLA_IM ?, ?, ?, ?, ?, ?";
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		log.info("[addRoute] Preparing sentence...");

		try {
			
			con.setAutoCommit(false);
			// ADD LGTYP
			
			cs = con.prepareCall(INV_SP_ADD_LGTYPE_IM);
			cs.setString(1, lgTypIMBean.getLgTyp());
			cs.setString(2, lgTypIMBean.getLtypt());
			cs.setString(3, lgTypIMBean.getBukrs());
			cs.setString(4, lgTypIMBean.getWerks());
			cs.setString(5, lgTypIMBean.getLgort());
			cs.setString(6, lgTypIMBean.getLgnum());
			cs.setString(7, createdBy);
			cs.registerOutParameter(1, Types.VARCHAR);

			log.info("[addLGTYP] Executing query...");
			cs.execute();
			
			lgTypIMBean.setLgTyp(cs.getString(1));
						
			//Eliminar posiciones
			String ids = "";
			for (int i = 0; i < lgTypIMBean.getLsLgPla().size(); i++) {
								
				if(lgTypIMBean.getLsLgPla().get(i).getLgPlaId() > 0){
					ids += lgTypIMBean.getLsLgPla().get(i).getLgPlaId() + ",";
				}				
			}
												
			cs = null;
			cs = con.prepareCall(INV_SP_DEL_LGPLA_IM);
			cs.setString(1, lgTypIMBean.getLgTyp());
			cs.setString(2, ids);
			cs.execute();
									
			// INSERTAR POSICIONES
			for (int i = 0; i < lgTypIMBean.getLsLgPla().size(); i++) {
				
				cs = null;
				log.info("[addLGTYPPosition] Preparing sentence...");
				cs = con.prepareCall(INV_SP_ADD_LGPLA_IM);
				cs.setInt(1, lgTypIMBean.getLsLgPla().get(i).getLgPlaId());
				cs.setString(2, lgTypIMBean.getLgTyp());
				cs.setString(3, lgTypIMBean.getLgnum());
				cs.setString(4, lgTypIMBean.getLsLgPla().get(i).getDescription());
				cs.setByte(5, (byte) (lgTypIMBean.getLsLgPla().get(i).isStatus()? 1 : 0));
				cs.setString(6, createdBy);
				cs.registerOutParameter(1, Types.INTEGER);

				log.info("[addLGTYPPosition] Executing query...");
				cs.execute(); 
				lgTypIMBean.getLsLgPla().get(i).setLgPlaId(cs.getInt(1));
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
		res.setLsObject(lgTypIMBean);
		return res;
	}
	
	public Response<List<LgTypIMBean>> getLgTypsIM(LgTypIMBean lgTypIMBean, String searchFilter) {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<LgTypIMBean>> res = new Response<List<LgTypIMBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<LgTypIMBean> listLgTypIM = new ArrayList<LgTypIMBean>();
		String INV_VW_LGTYPE_IM = null;
		LgTypIMBean lgTypIMAux;

		INV_VW_LGTYPE_IM = "SELECT LGTYP_ID, LGT_LTYPT, LGT_BUKRS, BUTXT, LGT_WERKS, NAME1, LGT_LGORT, LGOBE, LGT_LGNUM FROM dbo.INV_VW_LGTYPE_IM WITH(NOLOCK) ";

		if (searchFilter != null) {
			INV_VW_LGTYPE_IM += "WHERE (LGTYP_ID LIKE '%" + searchFilter + "%' OR LGT_LTYPT LIKE '%" + searchFilter + "%') AND LGT_STATUS = 1";
		} else {
			String condition = buildCondition(lgTypIMBean);
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
				
				lgTypIMAux = new LgTypIMBean();
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
	
	public List<LgplaIMBean> getPositions(String lgtyp) throws SQLException {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		List<LgplaIMBean> listPositions = new ArrayList<LgplaIMBean>();

		String INV_VW_LGPLA_IM = "SELECT LGPLA_ID, LGP_DESC, LGP_STATUS FROM dbo.INV_VW_LGPLA_IM WITH(NOLOCK) WHERE LGTYP_ID = ?";

		log.info(INV_VW_LGPLA_IM);
		log.info("[getPositionsDao] Preparing sentence...");

		stm = con.prepareStatement(INV_VW_LGPLA_IM);
		stm.setString(1, lgtyp);
		log.info("[getPositionsDao] Executing query...");

		ResultSet rs = stm.executeQuery();

		while (rs.next()) {
			
			LgplaIMBean position = new LgplaIMBean();
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
	
	private String buildCondition(LgTypIMBean lgTypIMBean) {
		
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
		
		lgTyp = (lgTypIMBean.getLgTyp() != null)? 
				(condition.contains("WHERE") ? " AND " : " WHERE ") + " LGTYP_ID = '" + lgTypIMBean.getLgTyp() + "' "	: "";
		condition += lgTyp;
		ltypt = (lgTypIMBean.getLtypt() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGT_LTYPT LIKE '%" + lgTypIMBean.getLtypt() + "%' " : "";
		condition += ltypt;
		bukrs = (lgTypIMBean.getBukrs() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGT_BUKRS = '" + lgTypIMBean.getBukrs() + "' " : "";
		condition += bukrs;
		bDesc = (lgTypIMBean.getbDesc() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " BUTXT LIKE '%" + lgTypIMBean.getbDesc() + "%' " : "";
		condition += bDesc;
		werks = (lgTypIMBean.getWerks() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " WERKS = '" + lgTypIMBean.getWerks() + "' " : "";
		condition += werks;
		wDesc = (lgTypIMBean.getwDesc() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " NAME1 LIKE '%" + lgTypIMBean.getwDesc() + "%' " : "";
		condition += wDesc;		
		lgort = (lgTypIMBean.getLgort() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGT_LGORT = '" + lgTypIMBean.getLgort() + "' " : "";
		condition += lgort;
		gDesc = (lgTypIMBean.getgDesc() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGOBE LIKE '%" + lgTypIMBean.getgDesc() + "%' " : "";
		condition += gDesc;		
		lgnum = (lgTypIMBean.getLgnum() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGT_LGNUM = '" + lgTypIMBean.getLgnum() + "' " : "";
		condition += lgnum;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

}
