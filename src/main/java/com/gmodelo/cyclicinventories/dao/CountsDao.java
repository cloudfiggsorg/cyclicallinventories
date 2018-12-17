package com.gmodelo.cyclicinventories.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.LgplaValuesBean;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.RouteUserBean;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

public class CountsDao {

	private Logger log = Logger.getLogger(CountsDao.class.getName());

	public Response<Object> addCount(RouteUserBean routeBean, String user) {
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		final String INV_SP_ADD_COUNT = "INV_SP_ADD_COUNT ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
		String UPDATE_TASK = "SELECT TAS_UPLOAD_DATE FROM INV_TASK WHERE TASK_ID =? ";
		String INV_SP_UPDATE_TASK = "INV_SP_UPDATE_TASK ?,?,?,?";
		log.info("[addConteo] Preparing sentence...");
		log.info("[addConteo] Y continua.. ");
		res.setAbstractResult(abstractResult);
		try {
			con.setAutoCommit(false);
			stm = con.prepareStatement(UPDATE_TASK);
			log.info("[addConteo] Despues de preparar UPDATE_TASK");
			stm.setString(1, routeBean.getTaskId());
			rs = stm.executeQuery();
			Date nDate = null;
			if (rs.next()) {
				nDate = rs.getDate("TAS_UPLOAD_DATE");
			}
			if (nDate == null) {

				log.info("[addConteo] responseUpdateTask es 1");
				// INSERTAR CONTEOS
				for (int i = 0; i < routeBean.getPositions().size(); i++) {
					for (int j = 0; j < routeBean.getPositions().get(i).getZone().getPositionsB().size(); j++) {
						cs = con.prepareCall(INV_SP_ADD_COUNT);
						HashMap<String, LgplaValuesBean> materials = routeBean.getPositions().get(i).getZone()
								.getPositionsB().get(j).getLgplaValues();
						for (Entry<String, LgplaValuesBean> entrada : materials.entrySet()) {
							log.log(Level.WARNING, "RouteBean Task: " + routeBean.getTaskId());
							log.log(Level.WARNING, "RouteBean ZonePosition: "
									+ routeBean.getPositions().get(i).getZone().getPositionsB().get(j).getPkAsgId());
							cs.setString(1, routeBean.getTaskId());
							cs.setInt(2, routeBean.getPositions().get(i).getZone().getPositionsB().get(j).getPkAsgId());
							log.log(Level.WARNING, "values: " + entrada.getValue().toString());
							cs.setString(3, String.format("%018d", Integer.parseInt(entrada.getValue().getMatnr())));
							cs.setString(4, entrada.getValue().getVhilm());
							cs.setString(5, entrada.getValue().getSec() != null ? entrada.getValue().getSec() : "0");
							cs.setString(6,
									entrada.getValue().getTarimas() != null ? entrada.getValue().getTarimas() : "0");
							cs.setString(7,
									entrada.getValue().getCamas() != null ? entrada.getValue().getCamas() : "0");
							cs.setString(8, entrada.getValue().getUm() != null ? entrada.getValue().getUm() : "0");
							cs.setString(9, entrada.getValue().getTotalConverted() != null
									? entrada.getValue().getTotalConverted() : "0");
							cs.setString(10, user);
							cs.setLong(11, entrada.getValue().getDateStart());
							cs.setLong(12, entrada.getValue().getDateEnd());
							cs.setString(13, entrada.getValue().getProdDate() != null
									? entrada.getValue().getProdDate() : "");
							cs.setString(14, entrada.getValue().getMaterialNotes() != null
									? entrada.getValue().getMaterialNotes() : "");
							cs.registerOutParameter(15, Types.INTEGER);
							cs.execute();

							log.info("[addConteo] Executing query...");
							int responseAddCount = cs.getInt(15);

							if (responseAddCount != 1) {
								abstractResult.setResultId(ReturnValues.IERROR);
								break;
							}
						}
						// Retrive the warnings if there're
						log.info("[addConteo] Haciendo commit");
						con.commit();
					}
				}
				if (abstractResult.getResultId() == ReturnValues.ISUCCESS) {
					log.info("[addConteo] Se ingresaron los conteos correctamente ");
					cs = con.prepareCall(INV_SP_UPDATE_TASK);
					cs.setLong(1, routeBean.getDateIni());
					cs.setLong(2, routeBean.getDateEnd());
					cs.setString(3, routeBean.getTaskId());
					cs.registerOutParameter(4, Types.INTEGER);
					cs.execute();
					int responseUpdateTask = cs.getInt(4);
					con.commit();
					if (responseUpdateTask != ReturnValues.ISUCCESS) {
						abstractResult.setResultId(ReturnValues.IUSERTASKNOUPDATED);
						abstractResult.setResultMsgAbs("Tarea no actualizada, por favor consulte con el administrador");
						res.setAbstractResult(abstractResult);
					}
				} else {
					log.log(Level.SEVERE, "[addConteo] Task no update : " + INV_SP_UPDATE_TASK);
					abstractResult.setResultId(ReturnValues.IUSERTASKNOUPDATED);
					abstractResult.setResultMsgAbs(
							"Objetos de Tarea cargados Incorrectamente, por favor consulte con el administrador");
					res.setAbstractResult(abstractResult);
				}

			} else {
				log.log(Level.SEVERE, "[addConteo] Task counted : " + UPDATE_TASK);
				abstractResult.setResultId(ReturnValues.IUSERTASKFINISHED);
				abstractResult.setResultMsgAbs("Tarea ya contada, por favor consulte con el administrador");
				res.setAbstractResult(abstractResult);
			}
		} catch (SQLException e) {
			try {
				log.log(Level.WARNING, "[addConteo] Execute rollback");
				con.rollback();
			} catch (SQLException e1) {
				log.log(Level.SEVERE, "[addConteo] Not rollback .", e);
			}
			log.log(Level.SEVERE,
					"[addConteo] Some error occurred while was trying to execute the S.P.: " + INV_SP_ADD_COUNT, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				log.log(Level.SEVERE,
						"[addConteo] Some error occurred while was trying to execute the S.P.: " + INV_SP_ADD_COUNT, e);
			}
		}
		return res;
	}


}
