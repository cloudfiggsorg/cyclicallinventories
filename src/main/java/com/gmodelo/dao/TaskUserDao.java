package com.gmodelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.Entity;
import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteUserBean;
import com.gmodelo.beans.TaskBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class TaskUserDao {
	
	private Logger log = Logger.getLogger(TaskUserDao.class.getName());
	
	public Response<Object> createAutoTask(User user){
		
		Response<Object> response = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		
		final String INV_VW_TASK = "SELECT DOCINV, ROUTEID, BUKRS, DTYPE, RTYPE, WERKS, GROUPID, USER_ID FROM INV_VW_LAST_TASK WHERE RTYPE='1' AND USER_ID=?";
		log.info(INV_VW_TASK);

		try {
			stm = con.prepareCall(INV_VW_TASK);
			stm.setString(1, user.getEntity().getIdentyId());	
			
			log.info("[getLastTask] Executing query...");
			ResultSet rs = stm.executeQuery();
			
			if (rs.next()) {
				
				DocInvBean docInv = new DocInvBean(); 				
				docInv.setRoute(rs.getString("ROUTEID"));
				docInv.setBukrs(rs.getString("BUKRS"));
				docInv.setWerks(rs.getString("WERKS"));
				docInv.setType(rs.getString("DTYPE"));
				
				DocInvDao dao = new DocInvDao();
				Response<DocInvBean> res = dao.addDocInv(docInv, "SYSTEM");
				
				if(res.getAbstractResult().getResultId() == 1){
					
					System.out.println("doc gen: "+ res.getLsObject().toString());
					
					TaskBean taskBean = new TaskBean();
					taskBean.setDocInvId(res.getLsObject());
					taskBean.setGroupId(rs.getString("GROUPID"));
					TaskDao daoTask = new TaskDao();
					Response<TaskBean> resDaoTask = daoTask.addTask(taskBean , "SYSTEM");
					
					if(resDaoTask.getAbstractResult().getResultId() != 1){
						log.log(Level.SEVERE,"[getTaskByUserDao] NO GENERATED AUTO TASK");
						abstractResult.setResultId(0);
						abstractResult.setResultMsgAbs("NO GENERATED AUTO TASK");
						response.setAbstractResult(abstractResult);
					}
					
				}else{
					log.log(Level.SEVERE,"[getTaskByUserDao] NO GENERATED DOC INVENTORY");
					abstractResult.setResultId(0);
					abstractResult.setResultMsgAbs("NO GENERATED DOC INVENTORY");
					response.setAbstractResult(abstractResult);
				}
				
			}else{
				log.log(Level.SEVERE,"[getTaskByUserDao] USER NO RUOTE DIARY");
				abstractResult.setResultId(0);
				abstractResult.setResultMsgAbs("USER NO RUOTE DIARY");
				response.setAbstractResult(abstractResult);
			}
			rs.close();
			stm.close();
			log.info("[getTaskByUserDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.severe("Ocurred error try close the conection in createAutoTask: "+ e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.severe("Ocurred error try close the conection in createAutoTask: "+ e.getMessage());
			}
		}
		return response;
	}
	
	public static void main(String args[]){
		TaskUserDao dao =  new TaskUserDao();
		
		User user = new User(); 
		user.getEntity().getIdentyId();
		Entity entity = new Entity();
		entity.setIdentyId("ROD1986");
		user.setEntity(entity);
		
		dao.createAutoTask(user);
	}

}
