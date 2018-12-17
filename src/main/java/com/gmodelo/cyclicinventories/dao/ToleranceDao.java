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
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.ToleranceBean;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class ToleranceDao {

	private Logger log = Logger.getLogger(ToleranceDao.class.getName());

	public Response<List<ToleranceBean>> getMATKL(ToleranceBean toleranceBean, String searchFilter) {

		Response<List<ToleranceBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ToleranceBean> listMATKL = new ArrayList<ToleranceBean>();
		// verificar si es necesario agregar mas campos al select para
		// agregarlos al ToleranceBean
		String INV_VW_MATKL = "SELECT MATKL, WGBEZ FROM [INV_CIC_DB].[dbo].[INV_VW_MATKL] "; // query

		if (searchFilter != null) {
			INV_VW_MATKL += "WHERE MATKL LIKE '%" + searchFilter + "%' OR WGBEZ LIKE '%" + searchFilter + "%'";
		}

		log.info("[getMATKLDao] Preparing sentence...");

		try {
			stm = con.prepareCall(INV_VW_MATKL);

			log.info("[getMATKLDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				toleranceBean = new ToleranceBean();

				toleranceBean.setMatkl(rs.getString(1));
				toleranceBean.setDesc(rs.getString(2));

				listMATKL.add(toleranceBean);

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

			log.info("[getMATKLDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getMATKLDao] Some error occurred while was trying to execute the query: " + INV_VW_MATKL, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getMATKLDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listMATKL);
		return res;
	}

	public Response<Object> addTolerance(ToleranceBean toleranceBean, String createdBy) {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		final String INV_SP_ADD_TOLERANCE = "INV_SP_ADD_TOLERANCE ?, ?, ?, ?, ?"; // The Store procedure to call

		log.info("[addToleranceDao] Preparing sentence...");

		try {
			cs = con.prepareCall(INV_SP_ADD_TOLERANCE);
			if (toleranceBean.getToleranceId() != null) {
				cs.setInt(1, toleranceBean.getToleranceId());
			} else {
				cs.setNull(1, Types.INTEGER);
			}

			cs.setString(2, toleranceBean.getMatkl());
			cs.setString(3, toleranceBean.getTp());
			cs.setString(4, toleranceBean.getTc());
			cs.setString(5, createdBy);
			cs.registerOutParameter(1, Types.INTEGER);

			log.info("[addToleranceDao] Executing query...");

			cs.execute();

			abstractResult.setResultId(ReturnValues.ISUCCESS);
			abstractResult.setResultMsgAbs("Tolerance id: " + cs.getInt(1));
			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			cs.close();

			log.info("[addToleranceDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE, "[addToleranceDao] Some error occurred while was trying to execute the S.P.: "
					+ INV_SP_ADD_TOLERANCE, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[addToleranceDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}
		res.setAbstractResult(abstractResult);

		return res;
	}

	public Response<Object> deleteTolerance(String arrayIdZones) {

		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		final String INV_SP_DEL_TOLERANCE = "INV_SP_DEL_TOLERANCE ?, ?"; // The Store procedure to call

		log.info("[deleteToleranceDao] Preparing sentence...");

		try {
			cs = con.prepareCall(INV_SP_DEL_TOLERANCE);

			cs.setString(1, arrayIdZones);
			cs.registerOutParameter(2, Types.INTEGER);

			log.info("[deleteToleranceDao] Executing query...");

			cs.execute();

			abstractResult.setResultId(cs.getInt(2));

			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, "[deleteToleranceDao] " + warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			cs.close();

			log.info("[deleteToleranceDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE, "[deleteToleranceDao] Some error occurred while was trying to execute the S.P.: "
					+ INV_SP_DEL_TOLERANCE, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[deleteToleranceDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res;
	}

	public Response<List<ToleranceBean>> getTolerances(ToleranceBean toleranceBean, String searchFilter) {

		Response<List<ToleranceBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ToleranceBean> listTolerance = new ArrayList<ToleranceBean>();
		// verificar si es necesario agregar mas campos al select para
		// agregarlos al ToleranceBean
		String INV_VW_TOLERANCES = "SELECT TOLERANCE_ID, MATKL, TP, TC FROM [INV_CIC_DB].[dbo].[INV_VW_TOLERANCES] "; // query

		if (searchFilter != null) {
			INV_VW_TOLERANCES += "WHERE TOLERANCE_ID LIKE '%" + searchFilter + "%' OR MATKL LIKE '%" + searchFilter
					+ "%'";
		} else {
			String condition = buildCondition(toleranceBean);
			if (condition != null) {
				INV_VW_TOLERANCES += condition;

			}
		}

		log.info(INV_VW_TOLERANCES);
		log.info("[getTolerancesDao] Preparing sentence...");

		try {
			stm = con.prepareCall(INV_VW_TOLERANCES);

			log.info("[getTolerancesDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				toleranceBean = new ToleranceBean();

				toleranceBean.setToleranceId(rs.getInt(1));
				toleranceBean.setMatkl(rs.getString(2));
				toleranceBean.setTp(rs.getString(3));
				toleranceBean.setTc(rs.getString(4));

				listTolerance.add(toleranceBean);

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

			log.info("[getTolerancesDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getTolerancesDao] Some error occurred while was trying to execute the query: "
					+ INV_VW_TOLERANCES, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getTolerancesDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listTolerance);
		return res;
	}

	private static final String TOLERANCE_BY_MATERIAL_LIST = "SELECT MATNR, MATKL, TOL_TP, TOL_TC from INV_VW_TOLERANCE_BY_MATNR WHERE MATNR LIKE ?";

	public Response<List<ToleranceBean>> getToleranceByMatnrs(List<ToleranceBean> beanList) {
		Response<List<ToleranceBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ToleranceBean> tbList = new ArrayList<>();
		ToleranceBean tb = null;
		log.info(TOLERANCE_BY_MATERIAL_LIST);
		log.info("[getToleranceByMatnrDao] Preparing sentence...");

		try {
			for (ToleranceBean singleBean : beanList) {
				tb = singleBean;
				stm = con.prepareStatement(TOLERANCE_BY_MATERIAL_LIST);
				stm.setString(1, "%" + tb.getMatnr() + "%");
				log.info("[getToleranceByMatnrDao] Executing query...");
				ResultSet rs = stm.executeQuery();
				if (rs.next()) {
					tb.setTp(rs.getString("TOL_TP"));
					tb.setTc(rs.getString("TOL_TC"));
				}
				tbList.add(tb);
				log.info("[getToleranceByMatnrDao] Sentence successfully executed.");
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getToleranceByMatnrDao] Some error occurred while was trying to execute the query: "
					+ TOLERANCE_BY_MATERIAL_LIST, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getToleranceByMatnrDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(tbList);
		return res;
	}

	public Response<ToleranceBean> getToleranceByMatnr(String matnr) {
		Response<ToleranceBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		ToleranceBean tb = null;
		String TOLERANCE_BY_MATNR = "SELECT MATNR, MATKL, TOL_TP, TOL_TC from INV_VW_TOLERANCE_BY_MATNR WHERE MATNR LIKE '%"
				+ matnr + "%'";

		log.info(TOLERANCE_BY_MATNR);
		log.info("[getToleranceByMatnrDao] Preparing sentence...");

		try {
			stm = con.prepareCall(TOLERANCE_BY_MATNR);

			log.info("[getToleranceByMatnrDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				tb = new ToleranceBean();

				tb.setTp(rs.getString("TOL_TP"));
				tb.setTc(rs.getString("TOL_TC"));

				log.info("tp " + tb.getTp());
				log.info("tc " + tb.getTc());

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

			log.info("[getToleranceByMatnrDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getToleranceByMatnrDao] Some error occurred while was trying to execute the query: "
					+ TOLERANCE_BY_MATNR, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getToleranceByMatnrDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		abstractResult.setResultId(tb != null ? ReturnValues.ISUCCESS : ReturnValues.IEMPTY);
		res.setAbstractResult(abstractResult);
		res.setLsObject(tb);
		return res;
	}

	private String buildCondition(ToleranceBean toleranceBean) {

		String condition = "";
		String toleranceId = "";
		String matkl = "";
		String desc = "";
		toleranceId = (toleranceBean.getToleranceId() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ")
				+ "TOLERANCE_ID = " + toleranceBean.getToleranceId() : "");
		condition += toleranceId;
		matkl = (toleranceBean.getMatkl() != null
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + "MATKL = '" + toleranceBean.getMatkl() + "' "
				: "");
		condition += matkl;
		desc = (toleranceBean.getDesc() != null
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + "TDESC = '" + toleranceBean.getDesc() + "' "
				: "");
		condition += desc;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

}
