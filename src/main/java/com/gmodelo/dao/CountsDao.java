package com.gmodelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	public Response<Object> addCount(RouteUserBean routeBean) {
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean(); 		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		final String INV_SP_ADD_COUNT = "INV_SP_ADD_COUNT ?, ?, ?, ?,?, ?, ?, ?, ?, ?";
		log.info("[addConteo] Preparing sentence...");
		
		try {
			con.setAutoCommit(false);
			// INSERTAR CONTEOS
			for (int i = 0; i < routeBean.getPositions().size(); i++) {
				for(int j=0; j < routeBean.getPositions().get(i).getZone().getPositionsB().size(); j ++){
					for(int k=0; k < routeBean.getPositions().get(i).getZone().getPositionsB().size(); k ++){
						
						cs = con.prepareCall(INV_SP_ADD_COUNT);
						
						HashMap<String, LgplaValuesBean> materials = routeBean.getPositions().get(i).getZone().getPositionsB().get(k).getLgplaValues();
						
						for (Entry<String, LgplaValuesBean> entrada : materials.entrySet()) {
							cs.setString(1, routeBean.getTaskId());
							cs.setInt(2, routeBean.getPositions().get(i).getZone().getPositionsB().get(k).getPkAsgId());
							
							System.out.println("values: "+ entrada.getValue().toString());
														
							cs.setString(3, entrada.getValue().getMatnr());
							cs.setString(4, entrada.getValue().getVhilm());
							cs.setInt(5, entrada.getValue().getSec());
							cs.setInt(6, entrada.getValue().getTarimas());
							cs.setInt(7, entrada.getValue().getCamas());
							cs.setInt(8, entrada.getValue().getUm());
							cs.setInt(9, entrada.getValue().getTotalConverted());
							cs.registerOutParameter(10, Types.INTEGER);
							cs.execute();
							log.info("[addConteo] Executing query...");			
							if(cs.getInt(10) != 1){
								abstractResult.setResultId(0);
								break;
							}
						}
					}
				}
			}
			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, "[addConteo] " + warning.getMessage());
				warning = warning.getNextWarning();
			}
			con.commit();
			// Free resources
			cs.close();
			con.close();
			abstractResult.setResultId(1);
			res.setAbstractResult(abstractResult);

		} catch (SQLException e) {
			try {
				//deshace todos los cambios realizados en los datos
				log.log(Level.WARNING,"[addConteo] Execute rollback");
				con.rollback();
			} catch (SQLException e1) {
				log.log(Level.SEVERE, "[addConteo] Not rollback .", e);
			}
			log.log(Level.SEVERE,"[addConteo] Some error occurred while was trying to execute the S.P.: " + INV_SP_ADD_COUNT, e);
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
