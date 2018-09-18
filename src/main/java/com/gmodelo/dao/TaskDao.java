package com.gmodelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TaskBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class TaskDao {
	
	private Logger log = Logger.getLogger(TaskDao.class.getName());

	public Response<TaskBean> addTask(TaskBean taskBean, String createdBy) {
		Response<TaskBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		final String INV_SP_ADD_TASK = "INV_SP_ADD_TASK ?, ?, ?, ?"; 		
		int taskId = 0;
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		log.info("[addTask] Preparing sentence...");

		try {
			con.setAutoCommit(false);
			cs = con.prepareCall(INV_SP_ADD_TASK);
			cs.setInt(1, taskBean.getTaskId());
			cs.setString(2, taskBean.getGroupId());
			cs.setInt(3, taskBean.getDocInvId());
			cs.setInt(4, taskBean.getCount());			
			cs.registerOutParameter(1, Types.INTEGER);
			log.info("[addTask] Executing query...");
			cs.execute();
			taskBean.setTaskId(cs.getInt(1));
			
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, "[addTask] " + warning.getMessage());
				warning = warning.getNextWarning();
			}

			con.commit();
			cs.close();
			
		} catch (SQLException e) {
			try {
				log.log(Level.WARNING,"[addTask] Execute rollback");
				con.rollback();
			} catch (SQLException e1) {
				log.log(Level.SEVERE, "[addTask] Not rollback .", e);
			}
			log.log(Level.SEVERE,
					"[addTask] Some error occurred while was trying to execute the S.P.: " + INV_SP_ADD_TASK, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[addRoute] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(taskBean);
		return res;
	}

	public Response<Object> deleteTask(String arrayIdTask){
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		int ResultSP= 0;
		final String INV_SP_DELETE_TASK = "INV_SP_DELETE_TASK ?,? "; //The Store procedure to call
		log.info("[deleteTask] Preparing sentence...");
		try {
			cs = con.prepareCall(INV_SP_DELETE_TASK);
			cs.setString(1,arrayIdTask);
			cs.registerOutParameter(2, Types.INTEGER);
			log.info("[deleteTask] Executing query...");
			cs.execute();
			ResultSP = cs.getInt(2);
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[deleteTask] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			log.info("[deleteTask] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[deleteTask] Some error occurred while was trying to execute the S.P.: "+INV_SP_DELETE_TASK, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[deleteTask] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(ResultSP);
		return res ;
	}

}
