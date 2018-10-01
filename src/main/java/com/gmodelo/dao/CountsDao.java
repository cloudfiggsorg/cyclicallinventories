package com.gmodelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.Entity;
import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.LgplaValuesBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteUserBean;
import com.gmodelo.beans.RouteUserPositionBean;
import com.gmodelo.beans.ZoneUserBean;
import com.gmodelo.beans.ZoneUserPositionsBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class CountsDao {

	private Logger log = Logger.getLogger(CountsDao.class.getName());

	public Response<Object> addCount(RouteUserBean routeBean, User user) {
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		final String INV_SP_ADD_COUNT = "INV_SP_ADD_COUNT ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?";
		String UPDATE_TASK = "SELECT CAST(ISNULL(TAS_UPLOAD_DATE,0) as bigint)  UPLOAD_DATE FROM INV_TASK WHERE TASK_ID =? ";
		log.info("[addConteo] Preparing sentence...");

		try {
			con.setAutoCommit(false);
			//VALIDAR TAREA
			stm = con.prepareStatement(UPDATE_TASK);
			stm.setString(1, routeBean.getTaskId());
			rs = stm.executeQuery();
			if(rs.next()){
				
				if(Long.parseLong(rs.getString("UPLOAD_DATE")) == 0){
					
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
								cs.setInt(5, entrada.getValue().getSec() != null ? entrada.getValue().getSec() : 0);
								cs.setInt(6, entrada.getValue().getTarimas() != null ? entrada.getValue().getTarimas() : 0);
								cs.setInt(7, entrada.getValue().getCamas() != null ? entrada.getValue().getCamas() : 0);
								cs.setInt(8, entrada.getValue().getUm() != null ? entrada.getValue().getUm() : 0);
								cs.setInt(9, entrada.getValue().getTotalConverted() != null
										? entrada.getValue().getTotalConverted() : 0);
								cs.setString(10, user.getEntity().getIdentyId());
								cs.registerOutParameter(11, Types.INTEGER);
								cs.execute();
					
								log.info("[addConteo] Executing query...");
								
								if (cs.getInt(11) != 1) {
									abstractResult.setResultId(0);
									break;
								}

							}
							
							// Retrive the warnings if there're
							SQLWarning warning = cs.getWarnings();
							while (warning != null) {
								log.log(Level.WARNING, "[addConteo] " + warning.getMessage());
								warning = warning.getNextWarning();
							}
							con.commit();
						}
					}
					
				}else{
					log.log(Level.SEVERE, "[addConteo] Task finished : " + UPDATE_TASK);
					abstractResult.setResultId(ReturnValues.IUSERTASKFINISHED);
					res.setAbstractResult(abstractResult);
					return res;
				}
			}else{
				log.log(Level.SEVERE, "[addConteo] Not validated Task in : " + UPDATE_TASK);
				abstractResult.setResultId(ReturnValues.IUSERNOTVALIDATEDTASK);
				res.setAbstractResult(abstractResult);
				return res;	
			}

			// Free resources
			cs.close();
			con.close();
			abstractResult.setResultId(1);
			res.setAbstractResult(abstractResult);

		} catch (SQLException e) {
			try {
				// deshace todos los cambios realizados en los datos
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
			return res;
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(routeBean);
		return res;
	}
	
}
