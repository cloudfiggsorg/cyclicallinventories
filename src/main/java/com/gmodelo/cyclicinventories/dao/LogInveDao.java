package com.gmodelo.cyclicinventories.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.LogInve;
import com.gmodelo.cyclicinventories.beans.MessagesTypes;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.filters.FServices;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class LogInveDao {

	private Logger log = Logger.getLogger(LogInveDao.class.getName());

	public void log(MessagesTypes messageType, String title, String subtitle, String description) {

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		String userId = ((User) FServices.getSession().getAttribute("user")).getEntity().getIdentyId();
		String bukrs = ((User) FServices.getSession().getAttribute("user")).getBukrs();
		bukrs = (bukrs == null ? "" : bukrs);
		String werks = ((User) FServices.getSession().getAttribute("user")).getWerks();
		werks = (werks == null ? "" : werks);

		final String SP = "INV_SP_SAVE_LOG ?, ?, ?, ?, ?, ?, ?"; // The Store
																	// procedure
																	// to call

		try {
			cs = con.prepareCall(SP);

			cs.setString(1, messageType.name());
			cs.setString(2, title);
			cs.setString(3, subtitle);
			cs.setString(4, description);
			cs.setString(5, userId);
			cs.setString(6, bukrs);
			cs.setString(7, werks);
			cs.execute();

			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, "[logInve] " + warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			cs.close();

		} catch (SQLException e) {
			log.log(Level.SEVERE, "[logInve] Some error occurred while was trying to execute the S.P.: " + SP, e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[logInve] Some error occurred while was trying to close the connection.", e);
			}
		}
	}

	public Response<List<LogInve>> getLogByUser() {

		Response<List<LogInve>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<LogInve> lsLog = new ArrayList<>();
		LogInve li = new LogInve();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String userId = ((User) FServices.getSession().getAttribute("user")).getEntity().getIdentyId();
		String bukrs = ((User) FServices.getSession().getAttribute("user")).getBukrs();
		String werks = ((User) FServices.getSession().getAttribute("user")).getWerks();
		@SuppressWarnings("unchecked")
		boolean isAdmin = ((ArrayList<String>) FServices.getSession().getAttribute("roles")).contains("INV_CIC_ADMIN");

		String QUERY = "SELECT INV_LG_TYPE, INV_LG_TITLE, " + "INV_LG_SUB_TITLE, INV_LG_DESCRIPTION, "
				+ "INV_LG_USER_ID, INV_LD_DATE " + "FROM INV_LOG WITH (NOLOCK) "
				+ "WHERE INV_LD_DATE >= DATEADD(HOUR, -24, GETDATE()) ";
		if (!isAdmin) {
			QUERY += "AND INV_LG_USER_ID = ? " + "OR (INV_BUKRS = ? AND INV_WERKS = ?) ";
		}
		QUERY += "ORDER BY INV_LD_DATE DESC";

		log.info(QUERY);
		log.info("[getLogByUser] Preparing sentence...");

		try {

			stm = con.prepareStatement(QUERY);
			if (!isAdmin) {
				stm.setString(1, userId);
				stm.setString(2, bukrs);
				stm.setString(3, werks);
			}

			log.info("[getLogByUser] Executing query...");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				li = new LogInve();
				li.setType(rs.getString("INV_LG_TYPE"));
				li.setTitle(rs.getString("INV_LG_TITLE"));
				li.setSubtitle(rs.getString("INV_LG_SUB_TITLE"));
				li.setDescription(rs.getString("INV_LG_DESCRIPTION"));
				li.setDate(format.format(rs.getTimestamp("INV_LD_DATE")));
				lsLog.add(li);
			}

			// Get another notifications
			li = getOpenDocInv(con);
			if (li != null) {
				lsLog.add(li);
			}

			li = checkIfExplosionIsConfig(con);
			if (li != null) {
				lsLog.add(li);
			}

			li = getClosedDocInv(con);
			if (li != null) {
				lsLog.add(li);
			}

			li = getFinishedCountDocInv(con);
			if (li != null) {
				lsLog.add(li);
			}
			
			//Sort the list by date
			Collections.sort(lsLog);

			log.info("[getLogByUser] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getLogByUser] Some error occurred while was trying to execute the query: " + QUERY,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getLogByUser] Some error occurred while was trying to close the connection.",
						e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(lsLog);
		return res;
	}

	private LogInve getClosedDocInv(Connection con) {

		PreparedStatement stm = null;
		LogInve li = null;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		@SuppressWarnings("unchecked")
		boolean isAdmin = ((ArrayList<String>) FServices.getSession().getAttribute("roles")).contains("INV_CIC_ADMIN");
		User usr = (User) FServices.getSession().getAttribute("user");
		String userId = usr.getEntity().getIdentyId();
		String bukrs = usr.getBukrs();
		String werks = usr.getWerks();
		String lsDocIvids = "";

		String QUERY = "SELECT DOC_INV_ID FROM INV_DOC_INVENTORY_HEADER "
				+ "WHERE DIH_STATUS = 'T' AND DIH_MODIFIED_DATE >= DATEADD(HOUR, -12, GETDATE()) ";
		if (!isAdmin) {
			QUERY += "AND DIH_CREATED_BY = ?  AND DIH_BUKRS = ? AND DIH_WERKS = ?";
		}

		log.info(QUERY);
		log.info("[getClosedDocInv] Preparing sentence...");

		try {

			stm = con.prepareStatement(QUERY);
			if (!isAdmin) {
				stm.setString(1, userId);
				stm.setString(2, bukrs);
				stm.setString(3, werks);
			}

			log.info("[getClosedDocInv] Executing query...");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				lsDocIvids += rs.getString("DOC_INV_ID") + ", ";
			}

			if (lsDocIvids.length() > 0) {

				lsDocIvids = lsDocIvids.substring(0, lsDocIvids.length() - 3);

				li = new LogInve();
				li.setType(MessagesTypes.Error.name());
				li.setTitle("Documentos de Inventario");
				li.setSubtitle("Cerrados");
				li.setDescription(
						"Los siguientes documentos de inventario " + "fueron cerrados por inactividad: " + lsDocIvids);
				li.setDate(format.format(new Date()));
			}

			log.info("[getClosedDocInv] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getClosedDocInv] Some error occurred while was trying to execute the query: " + QUERY, e);
		}
		return li;
	}

	private LogInve getOpenDocInv(Connection con) {

		PreparedStatement stm = null;
		LogInve li = null;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		@SuppressWarnings("unchecked")
		boolean isAdmin = ((ArrayList<String>) FServices.getSession().getAttribute("roles")).contains("INV_CIC_ADMIN");
		User usr = (User) FServices.getSession().getAttribute("user");
		String userId = usr.getEntity().getIdentyId();
		String bukrs = usr.getBukrs();
		String werks = usr.getWerks();
		String lsDocIvids = "";

		String QUERY = "SELECT DOC_INV_ID FROM INV_DOC_INVENTORY_HEADER "
				+ "WHERE DIH_STATUS = '1' AND DIH_CREATED_DATE >= DATEADD(HOUR, -12, GETDATE()) ";
		if (!isAdmin) {
			QUERY += "AND DIH_CREATED_BY = ?  AND DIH_BUKRS = ? AND DIH_WERKS = ?";
		}

		log.info(QUERY);
		log.info("[getOpenDocInv] Preparing sentence...");

		try {

			stm = con.prepareStatement(QUERY);
			if (!isAdmin) {
				stm.setString(1, userId);
				stm.setString(2, bukrs);
				stm.setString(3, werks);
			}

			log.info("[getOpenDocInv] Executing query...");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				lsDocIvids += rs.getString("DOC_INV_ID") + ", ";
			}

			if (lsDocIvids.length() > 0) {

				lsDocIvids = lsDocIvids.substring(0, lsDocIvids.length() - 3);

				li = new LogInve();
				li.setType(MessagesTypes.Information.name());
				li.setTitle("Documentos de Inventario");
				li.setSubtitle("Pendientes de cierre");
				li.setDescription("Los siguientes documentos de inventario " + "se encuentran pendientes de cierre: "
						+ lsDocIvids);
				li.setDate(format.format(new Date()));
			}

			log.info("[getOpenDocInv] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getOpenDocInv] Some error occurred while was trying to execute the query: " + QUERY,
					e);
		}
		return li;
	}

	private LogInve checkIfExplosionIsConfig(Connection con) {

		Statement stm = null;
		LogInve li = null;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String QUERY = "SELECT COUNT(*) FROM INV_EXPLOSION WITH (NOLOCK) ";

		log.info(QUERY);
		log.info("[getLogByUser] Preparing sentence...");

		try {

			stm = con.createStatement();

			log.info("[getLogByUser] Executing query...");
			ResultSet rs = stm.executeQuery(QUERY);

			if (rs.next()) {

				if (rs.getInt(1) == 0) {

					li = new LogInve();
					li.setType(MessagesTypes.Information.name());
					li.setTitle("Explosión de Materiales");
					li.setSubtitle("Configuración");
					li.setDescription("No sea han configurado los materiales a explosionar.");
					li.setDate(format.format(new Date()));
				}
			}

			log.info("[getLogByUser] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getLogByUser] Some error occurred while was trying to execute the query: " + QUERY,
					e);
		}

		return li;
	}

	private LogInve getFinishedCountDocInv(Connection con) {

		PreparedStatement stm = null;
		LogInve li = null;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		@SuppressWarnings("unchecked")
		boolean isAdmin = ((ArrayList<String>) FServices.getSession().getAttribute("roles")).contains("INV_CIC_ADMIN");
		User usr = (User) FServices.getSession().getAttribute("user");
		String bukrs = usr.getBukrs();
		String werks = usr.getWerks();
		String lsTasks = "";

		String QUERY = "SELECT TASK_ID FROM INV_TASK AS A "
				+ "INNER JOIN INV_DOC_INVENTORY_HEADER AS B ON (A.TAS_DOC_INV_ID = B.DOC_INV_ID) "
				+ "WHERE TAS_UPLOAD_DATE >= DATEADD(HOUR, -12, GETDATE())";
		if (!isAdmin) {
			QUERY += "AND B.DIH_BUKRS = ? AND B.DIH_WERKS = ?";
		}

		log.info(QUERY);
		log.info("[getFinishedCountDocInv] Preparing sentence...");

		try {

			stm = con.prepareStatement(QUERY);
			if (!isAdmin) {
				stm.setString(1, bukrs);
				stm.setString(2, werks);
			}

			log.info("[getFinishedCountDocInv] Executing query...");
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				lsTasks += rs.getString("TASK_ID") + ", ";
			}

			if (lsTasks.length() > 0) {

				lsTasks = lsTasks.substring(0, lsTasks.length() - 3);

				li = new LogInve();
				li.setType(MessagesTypes.Information.name());
				li.setTitle("Tareas");
				li.setSubtitle("Finalizadas");
				li.setDescription("Las siguientes tareas han sido concluidas: " + lsTasks);
				li.setDate(format.format(new Date()));
			}

			log.info("[getFinishedCountDocInv] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getFinishedCountDocInv] Some error occurred while was trying to execute the query: " + QUERY, e);
		}

		return li;
	}

}
