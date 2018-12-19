package com.gmodelo.cyclicinventories.dao;

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

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.LgTypIMBean;
import com.gmodelo.cyclicinventories.beans.LgplaIMBean;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class LgTypIMDao {

	private Logger log = Logger.getLogger(LgTypIMDao.class.getName());

	public Response<LgTypIMBean> saveLgTypIM(LgTypIMBean lgTypIMBean, String createdBy) {
		Response<LgTypIMBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		final String INV_SP_ADD_LGTYPE_IM = "INV_SP_ADD_LGTYPE_IM ?, ?, ?, ?, ?, ?, ?";
		final String INV_SP_DEL_LGPLA_IM = "INV_SP_DEL_LGPLA_IM ?";
		final String INV_SP_ADD_LGPLA_IM = "INV_SP_ADD_LGPLA_IM ?, ?, ?, ?, ?, ?";
		final String INV_GET_POSITION_LGPLA_IM = "SELECT LGPLA_ID, LGP_DESC, LGP_STATUS FROM INV_LGPLA_IM WITH(NOLOCK) WHERE LGTYP_ID = ?";

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		PreparedStatement stm = null;

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
			if (lgTypIMBean.getLgnum().isEmpty()) {
				cs.setNull(6, Types.VARCHAR);
			} else {
				cs.setString(6, lgTypIMBean.getLgnum());
			}
			cs.setString(7, createdBy);

			cs.registerOutParameter(1, Types.VARCHAR);
			cs.registerOutParameter(6, Types.VARCHAR);

			log.info("[addLGTYP] Executing query...");
			cs.execute();

			lgTypIMBean.setLgTyp(cs.getString(1));
			lgTypIMBean.setLgnum(cs.getString(6));

			// Eliminar posiciones
			cs = null;
			cs = con.prepareCall(INV_SP_DEL_LGPLA_IM);
			cs.setString(1, lgTypIMBean.getLgTyp());
			cs.execute();

			// INSERTAR POSICIONES
			cs = null;
			cs = con.prepareCall(INV_SP_ADD_LGPLA_IM);
			log.info("[addLGTYPPosition] Preparing sentence..." + INV_SP_ADD_LGPLA_IM);
			for (LgplaIMBean lgplaIm : lgTypIMBean.getLsLgPla()) {
				if (lgplaIm.getDescription() != null) {
					cs.setInt(1, lgplaIm.getLgPlaId());
					cs.setString(2, lgTypIMBean.getLgTyp());
					cs.setString(3, lgTypIMBean.getLgnum());
					cs.setString(4, lgplaIm.getDescription());
					cs.setByte(5, (byte) (lgplaIm.isStatus() ? 1 : 0));
					cs.setString(6, createdBy);
					cs.addBatch();
				} else {
					log.info("[addLGTYPPosition] The null One..." + lgplaIm);
				}

			}
			log.info("[addLGTYPPosition] Executing query...");
			cs.executeBatch();
			log.info("[addLGTYP] Sentence successfully executed.");
			// Retrive the warnings if there're
			con.commit();
			cs.close();

			log.info("[addLGTYPPosition] Executing query..." + INV_GET_POSITION_LGPLA_IM);
			stm = con.prepareStatement(INV_GET_POSITION_LGPLA_IM);
			stm.setString(1, lgTypIMBean.getLgTyp());
			ResultSet rs = stm.executeQuery();
			List<LgplaIMBean> lgplaIMBeans = new ArrayList<>();
			while (rs.next()) {
				LgplaIMBean bean = new LgplaIMBean();
				bean.setDescription(rs.getString("LGP_DESC"));
				bean.setLgPlaId(rs.getInt("LGPLA_ID"));
				bean.setStatus(rs.getBoolean("LGP_STATUS"));
				lgplaIMBeans.add(bean);
			}
			if (!lgplaIMBeans.isEmpty()) {
				lgTypIMBean.setLsLgPla(lgplaIMBeans);
			}
		} catch (SQLException e) {
			try {
				// deshace todos los cambios realizados en los datos
				log.log(Level.WARNING, "[addLGTYP] Execute rollback");
				con.rollback();
			} catch (SQLException e1) {
				log.log(Level.SEVERE, "[addLGTYP] Not rollback .", e);
			}
			log.log(Level.SEVERE, "[addLGTYP] Some error occurred while was trying to insert an LGTYP", e);
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
	
	public Response<List<LgTypIMBean>> getLgTypsOnly(LgTypIMBean ltib) {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<LgTypIMBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<LgTypIMBean> listLgTypIM = new ArrayList<>();
		String INV_VW_LGTYPE_IM = null;
		LgTypIMBean lgTypIMAux;

		INV_VW_LGTYPE_IM = "SELECT LGTYP, LTYPT FROM dbo.INV_VW_LGTYPE_IM WITH(NOLOCK) ";
		INV_VW_LGTYPE_IM = buildCondition(ltib, INV_VW_LGTYPE_IM);
		INV_VW_LGTYPE_IM += INV_VW_LGTYPE_IM.contains("WHERE")?" AND ":" ";
		INV_VW_LGTYPE_IM += "(LGTYP LIKE '%" + (ltib.getLgTyp()==null?"":ltib.getLgTyp()) + "%' "; 
		INV_VW_LGTYPE_IM += "OR LTYPT LIKE '%" + (ltib.getLtypt()==null?"":ltib.getLtypt()) + "%') ";
		INV_VW_LGTYPE_IM += "GROUP BY LGTYP, LTYPT ";
		
		log.info(INV_VW_LGTYPE_IM);
		log.info("[LgTypImDAo getLgTypsOnly] Preparing sentence...");
		
		try {
			stm = con.prepareStatement(INV_VW_LGTYPE_IM);

			log.info("[LgTypImDAo getLgTypsOnly] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				lgTypIMAux = new LgTypIMBean();
				lgTypIMAux.setLgTyp(rs.getString(1));
				lgTypIMAux.setLtypt(rs.getString(2));
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
			log.info("[LgTypImDAo getLgTypsOnly] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[LgTypImDAo getLgTypsOnly] Some error occurred while was trying to execute the query: "
					+ INV_VW_LGTYPE_IM, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[LgTypImDAo getLgTypsOnly] Some error occurred while was trying to close the connection.", e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listLgTypIM);
		return res;
	}	

	public Response<List<LgTypIMBean>> getLgTypsIM(LgTypIMBean lgTypIMBean) {
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<LgTypIMBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<LgTypIMBean> listLgTypIM = new ArrayList<>();
		String INV_VW_LGTYPE_IM = null;
		LgTypIMBean lgTypIMAux;
		
		INV_VW_LGTYPE_IM = "SELECT LGTYP, LTYPT, BUKRS, BDESC, WERKS, WDESC, LGORT, LGOBE, LGNUM, IMWM, (";
		INV_VW_LGTYPE_IM +=	"SELECT (CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END) AS CDOC_INV_ID ";
		INV_VW_LGTYPE_IM +=	"FROM INV_DOC_INVENTORY_HEADER AS IDIH ";
		INV_VW_LGTYPE_IM +=	"INNER JOIN INV_ROUTE_POSITION AS IRP ON (IDIH.DIH_ROUTE_ID = IRP.RPO_ROUTE_ID) ";
		INV_VW_LGTYPE_IM +=	"INNER JOIN INV_ZONE_POSITION AS IZP ON (IRP.RPO_ZONE_ID = IZP.ZPO_ZONE_ID) ";
		INV_VW_LGTYPE_IM +=	"INNER JOIN INV_VW_LGTYPE_IM AS IVLI ON (IVLI.LGTYP = IZP.ZPO_LGTYP) ";
		INV_VW_LGTYPE_IM +=	"WHERE IVLI.LGTYP = '" + lgTypIMBean.getLgTyp() + "' ";
		INV_VW_LGTYPE_IM +=	"AND IDIH.DIH_STATUS IS NULL) ";
		INV_VW_LGTYPE_IM +=	"AS STATUS ";
		INV_VW_LGTYPE_IM += "FROM dbo.INV_VW_LGTYPE_IM WITH(NOLOCK) WHERE  STATUS = 1 ";
		INV_VW_LGTYPE_IM += "AND (LGTYP = '" + lgTypIMBean.getLgTyp() + "') ";
		INV_VW_LGTYPE_IM = buildCondition(lgTypIMBean, INV_VW_LGTYPE_IM);

		INV_VW_LGTYPE_IM += " GROUP BY LGTYP, LTYPT, BUKRS, BDESC, WERKS, WDESC, LGORT, LGOBE, LGNUM, IMWM, STATUS";
		log.info(INV_VW_LGTYPE_IM);
		log.info("[LgTypImDAo getLgTypsIM] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_LGTYPE_IM);

			log.info("[LgTypImDAo getLgTypsIM] Executing query...");

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
				lgTypIMAux.setImwm(rs.getString(10));
				lgTypIMAux.setStatus(rs.getBoolean(11));
				lgTypIMAux.setLsLgPla(this.getPositions(rs.getString(1), con));

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
			log.info("[LgTypImDAo getLgTypsIM] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[LgTypImDAo getLgTypsIM] Some error occurred while was trying to execute the query: "
					+ INV_VW_LGTYPE_IM, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[LgTypImDAo getLgTypsIM] Some error occurred while was trying to close the connection.", e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listLgTypIM);
		return res;
	}

	private static final String INV_VW_LGPLA_IM = "SELECT LGPLA_ID, LGP_DESC, LGP_STATUS FROM dbo.INV_VW_LGPLA_IM WITH(NOLOCK) WHERE LGTYP_ID = ?";

	public List<LgplaIMBean> getPositions(String lgtyp, Connection con) throws SQLException {

		PreparedStatement stm = null;
		List<LgplaIMBean> listPositions = new ArrayList<>();
		log.info(INV_VW_LGPLA_IM);
		log.info("[LgTypImDAo getPositions] Preparing sentence...");
		stm = con.prepareStatement(INV_VW_LGPLA_IM);
		stm.setString(1, lgtyp);
		log.info("[LgTypImDAo getPositions] Executing query...");
		log.info("[LgTypImDAo getPositions]..." + lgtyp);
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
		log.info("[LgTypImDAo getPositions] Sentence successfully executed.");
		return listPositions;
	}

	public Response<Object> deleteLgTypIM(String arrayToDelete) {

		log.info("[deleteLgTyp] " + arrayToDelete);

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

	private String buildCondition(LgTypIMBean lgTypIMBean, String query) {

		String bukrs; // The society Id
		String bDesc; // The society description
		String werks; // The werks Id
		String wDesc; // The werks description
		String lgort; // The warehouse Id
		String gDesc; // The warehouse description
		String lgnum; // The lgnum
		String condition = query;

		bukrs = (lgTypIMBean.getBukrs() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")
				+ " BUKRS LIKE '%" + lgTypIMBean.getBukrs() + "%' " : "";
		condition += bukrs;
		bDesc = (lgTypIMBean.getbDesc() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")
				+ " BDESC LIKE '%" + lgTypIMBean.getbDesc() + "%' " : "";
		condition += bDesc;
		werks = (lgTypIMBean.getWerks() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")
				+ " WERKS LIKE '%" + lgTypIMBean.getWerks() + "%' " : "";
		condition += werks;
		wDesc = (lgTypIMBean.getwDesc() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")
				+ " WDESC LIKE '%" + lgTypIMBean.getwDesc() + "%' " : "";
		condition += wDesc;
		lgort = (lgTypIMBean.getLgort() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")
				+ " LGORT LIKE '%" + lgTypIMBean.getLgort() + "%' " : "";
		condition += lgort;
		gDesc = (lgTypIMBean.getgDesc() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")
				+ " LGOBE LIKE '%" + lgTypIMBean.getgDesc() + "%' " : "";
		condition += gDesc;
		lgnum = (lgTypIMBean.getLgnum() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGNUM = '" + lgTypIMBean.getLgnum() + "' "
				: "";
		condition += lgnum;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

}
