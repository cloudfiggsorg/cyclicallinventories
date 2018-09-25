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
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteBean;
import com.gmodelo.beans.TaskBean;
import com.gmodelo.beans.RoutePositionBean;
import com.gmodelo.beans.TaskBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class TaskDao {
	
	private Logger log = Logger.getLogger(TaskDao.class.getName());

	public Response<TaskBean> addTask(TaskBean taskBean, String createdBy) {
		Response<TaskBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		final String INV_SP_ADD_TASK = "INV_SP_ADD_TASK ?, ?, ?"; 		
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
			cs.setInt(3, taskBean.getDocInvId().getDocInvId());			
			cs.registerOutParameter(1, Types.INTEGER);
			log.info("[addTask] Executing query...");
			cs.execute();
			taskBean.setTaskId(cs.getInt(1));
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, "[addTask] " + warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			DocInvDao dao = new DocInvDao(); 
			dao.updateStatusDocInv(taskBean.getDocInvId().getDocInvId());
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
	
	public Response<List<TaskBean>> getTasks(TaskBean taskBean, String searchFilter) {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		Response<List<TaskBean>> res = new Response<List<TaskBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<TaskBean> listTaskBean = new ArrayList<TaskBean>();
		String INV_VW_TASK = null;
		int aux;		
		String searchFilterNumber = "";
		
		try {
			aux = Integer.parseInt(searchFilter); 
			searchFilterNumber += aux;
		} catch (Exception e) {
			searchFilterNumber = searchFilter;
			log.info("[getTaskDao] Trying to convert String to Int");
		}		

		INV_VW_TASK = "SELECT TASK_ID, TAS_GROUP_ID, TAS_DOC_INV_ID FROM INV_VW_TASK WITH(NOLOCK) ";

		if (searchFilter != null) {
			INV_VW_TASK += "WHERE TASK_ID LIKE '%" + searchFilterNumber + "%' OR TAS_DOC_INV_ID LIKE '%" + searchFilter + "%'";
		} else {
			String condition = buildCondition(taskBean);
			if (condition != null) {
				INV_VW_TASK += condition;
			}
		}
		INV_VW_TASK += "GROUP BY TASK_ID, TAS_GROUP_ID, TAS_DOC_INV_ID ORDER BY TASK_ID ASC";
		
		log.info(INV_VW_TASK);
		log.info("[getTaskDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_TASK);

			log.info("[getTaskDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				taskBean = new TaskBean();
				taskBean.setTaskId(rs.getInt("TASK_ID"));
				taskBean.setGroupId(rs.getString("TAS_GROUP_ID"));
				taskBean.setDocInvId(this.getDocInv(rs.getInt("TAS_DOC_INV_ID")));
				listTaskBean.add(taskBean);
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
			log.info("[getTaskDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getTaskDao] Some error occurred while was trying to execute the query: " + INV_VW_TASK, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getTaskDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listTaskBean);
		return res;
	}

	public long updateDowloadTask(String idTask) throws SQLException {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		final String INV_SP_UPDATE_DOWLOAD_TASK = "INV_SP_UPDATE_DOWLOAD_TASK ?, ?, ?";
		long date = 0;

		log.info("[updateDowloadTaskDao] Preparing sentence...");
		try {
			cs = con.prepareCall(INV_SP_UPDATE_DOWLOAD_TASK);
			cs.setString(1, idTask);
			cs.registerOutParameter(2, Types.INTEGER);
			cs.registerOutParameter(3, Types.DATE);
			log.info("[updateDowloadTaskDao] Executing query...");
			cs.execute();
			date = cs.getDate(3).getTime();
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[updateDowloadTask] Some error occurred while was trying to execute the query: "
					+ INV_SP_UPDATE_DOWLOAD_TASK, e);
			throw e;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[updateDowloadTask] Some error occurred while was trying to close the connection.", e);
			}
		}
		return date;
	}
	
	private String buildCondition(TaskBean taskB) {
		String TASK_ID = "";
		String TAS_GROUP_ID = "";
		String TAS_DOC_INV_ID = "";
		String type = "";
		String condition = "";
		/*TASK_ID, TAS_GROUP_ID, TAS_DOC_INV_ID, TAS_COUNT_ID*/
		TASK_ID = (taskB.getTaskId() != 0) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " TASK_ID = '" + taskB.getTaskId() + "' ": "";
		condition += TASK_ID;
		TAS_GROUP_ID = (taskB.getGroupId() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " TAS_GROUP_ID = '" + taskB.getGroupId() + "' " : "";
		condition += TAS_GROUP_ID;
		TAS_DOC_INV_ID = (taskB.getDocInvId() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " TAS_DOC_INV_ID = '" + taskB.getDocInvId() + "' " : "";
		condition += TAS_DOC_INV_ID;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}
	
	private DocInvBean getDocInv(int docInvId){
		DocInvBean bean= new DocInvBean();
		bean.setDocInvId(docInvId);
		DocInvDao dao = new DocInvDao();
		String searchFilter = "";
		Response<List<DocInvBean>> list = dao.getDocInv(bean, searchFilter);
		if (list.getLsObject().size() > 0){
			bean  = list.getLsObject().get(1);
		}
		return bean;
	}
	
}