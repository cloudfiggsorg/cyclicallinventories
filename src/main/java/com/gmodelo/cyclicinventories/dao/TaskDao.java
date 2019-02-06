package com.gmodelo.cyclicinventories.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.TaskBean;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;

public class TaskDao {

	private Logger log = Logger.getLogger(TaskDao.class.getName());
	private ConnectionManager iConnectionManager = new ConnectionManager();
	private UMEDaoE ume;
	private User user;

	public Response<TaskBean> addTask(TaskBean taskBean, String createdBy) {
		Response<TaskBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		final String INV_SP_ADD_TASK = "INV_SP_ADD_TASK ?, ?, ?, ?, ?, ?, ?";
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		log.info("[addTask] Preparing sentence...");

		try {

			con.setAutoCommit(false);
			cs = con.prepareCall(INV_SP_ADD_TASK);
			cs.setString(1, taskBean.getTaskId());
			cs.setString(2, taskBean.getGroupId());
			cs.setInt(3, taskBean.getDocInvId().getDocInvId());
			if (taskBean.getRub() == null) {

				cs.setNull(4, Types.CHAR);
			} else {

				cs.setString(4, new Gson().toJson(taskBean.getRub()));
			}
			cs.setString(5, taskBean.getTaskIdFather());
			cs.setString(6, createdBy);

			cs.registerOutParameter(1, Types.INTEGER);
			cs.registerOutParameter(6, Types.VARCHAR);
			cs.registerOutParameter(7, Types.VARCHAR);

			log.info("[addTask] Executing query...");
			cs.execute();

			taskBean.setTaskId(cs.getString(1));
			user = new User();
			ume = new UMEDaoE();
			user.getEntity().setIdentyId(cs.getString(6));
			ArrayList<User> ls = new ArrayList<>();
			ls.add(user);
			ls = ume.getUsersLDAPByCredentials(ls);

			if (ls.size() > 0) {

				taskBean.setCreatedBy(cs.getString(6) + " - " + ls.get(0).getGenInf().getName() + " "
						+ ls.get(0).getGenInf().getLastName() + " - " + format.format(new Date()));
			} else {
				taskBean.setCreatedBy(cs.getString(6) + " - " + format.format(new Date()));
			}

			user.getEntity().setIdentyId(cs.getString(7));
			ls = new ArrayList<>();
			ls.add(user);
			ls = ume.getUsersLDAPByCredentials(ls);

			if (ls.size() > 0) {

				taskBean.setModifiedBy(cs.getString(7) + " - " + ls.get(0).getGenInf().getName() + " "
						+ ls.get(0).getGenInf().getLastName() + " - " + format.format(new Date()));
			} else {
				taskBean.setModifiedBy(cs.getString(7) + " - " + format.format(new Date()));
			}

			log.info("[addTask] After Excecute query..." + taskBean);
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, "[addTask] " + warning.getMessage());
				warning = warning.getNextWarning();
			}

			con.commit();
			cs.close();

		} catch (SQLException | NamingException e) {
			try {
				log.log(Level.WARNING, "[addTask] Execute rollback");
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

	public Response<Object> deleteTask(String arrayIdTask) {
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		int ResultSP = 0;
		final String INV_SP_DELETE_TASK = "INV_SP_DELETE_TASK ?,? "; // The Store procedure to call
		log.info("[deleteTask] Preparing sentence...");
		try {
			cs = con.prepareCall(INV_SP_DELETE_TASK);
			cs.setString(1, arrayIdTask);
			cs.registerOutParameter(2, Types.INTEGER);
			log.info("[deleteTask] Executing query...");
			cs.execute();
			ResultSP = cs.getInt(2);
			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, "[deleteTask] " + warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			cs.close();
			log.info("[deleteTask] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[deleteTask] Some error occurred while was trying to execute the S.P.: " + INV_SP_DELETE_TASK, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[deleteTask] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(ResultSP);
		return res;
	}

	public Response<List<TaskBean>> getTasksbyBukrsAndWerks(String bukrs, String werks) {

		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		TaskBean taskBean;
		Response<List<TaskBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<TaskBean> listTaskBean = new ArrayList<>();
		String INV_VW_TASK = null;

		INV_VW_TASK = "SELECT TASK_ID, TAS_GROUP_ID, TAS_DOC_INV_ID, "
				+ "TAS_CREATED_DATE, TAS_DOWLOAD_DATE, TAS_UPLOAD_DATE, "
				+ "TAS_STATUS, TASK_ID_PARENT, CREATED_BY, MODIFIED_BY FROM INV_VW_TASK WITH(NOLOCK) ";

		INV_VW_TASK += "WHERE DIH_BUKRS LIKE '%" + bukrs + "%' AND DIH_WERKS LIKE '%" + werks
				+ "%'  AND TASK_ID_PARENT IS NULL ";

		INV_VW_TASK += "GROUP BY TASK_ID, TAS_GROUP_ID, TAS_DOC_INV_ID, "
				+ "TAS_CREATED_DATE, TAS_DOWLOAD_DATE, TAS_UPLOAD_DATE, "
				+ "TAS_STATUS, TASK_ID_PARENT, CREATED_BY, MODIFIED_BY " + "ORDER BY TASK_ID ASC";

		log.info(INV_VW_TASK);
		log.info("[getTasksbyBukrsAndWerksDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_TASK);

			log.info("[getTasksbyBukrsAndWerksDao] Executing query...");

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				taskBean = new TaskBean();
				taskBean.setTaskId(rs.getString("TASK_ID"));
				taskBean.setGroupId(rs.getString("TAS_GROUP_ID"));

				try {
					taskBean.setdCreated(rs.getDate("TAS_CREATED_DATE").getTime());
				} catch (NullPointerException e) {
					taskBean.setdCreated(0);
				}

				try {
					taskBean.setdDownlad(rs.getDate("TAS_DOWLOAD_DATE").getTime());
				} catch (NullPointerException e) {
					taskBean.setdDownlad(0);
				}

				try {
					taskBean.setdUpload(rs.getDate("TAS_UPLOAD_DATE").getTime());
				} catch (Exception e) {
					taskBean.setdUpload(0);
				}

				taskBean.setStatus(rs.getBoolean("TAS_STATUS"));

				try {
					taskBean.setTaskIdFather(rs.getString("TASK_ID_PARENT"));
				} catch (Exception e) {
					taskBean.setTaskIdFather(null);
				}

				taskBean.setCreatedBy(rs.getString("CREATED_BY"));
				taskBean.setModifiedBy(rs.getString("MODIFIED_BY"));
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
			log.info("[getTasksbyBukrsAndWerksDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getTasksbyBukrsAndWerksDao] Some error occurred while was trying to execute the query: "
							+ INV_VW_TASK,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getTasksbyBukrsAndWerksDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listTaskBean);
		return res;
	}

	public Response<List<TaskBean>> getTasks(TaskBean taskBean, String searchFilter) {

		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		Response<List<TaskBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<TaskBean> listTaskBean = new ArrayList<>();
		String INV_VW_TASK = null;
		DocInvDao didao = new DocInvDao();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		INV_VW_TASK = "SELECT TASK_ID, TAS_GROUP_ID, TAS_DOC_INV_ID, "
				+ "TAS_CREATED_DATE, TAS_DOWLOAD_DATE, TAS_UPLOAD_DATE, "
				+ "TAS_STATUS, TASK_ID_PARENT, CREATED_BY, MODIFIED_BY FROM INV_VW_TASK WITH(NOLOCK) ";

		if (searchFilter != null) {

			INV_VW_TASK += "WHERE TASK_ID LIKE '%" + searchFilter + "%' OR TAS_DOC_INV_ID LIKE '%" + searchFilter
					+ "%' ";
		} else {
			String condition = buildCondition(taskBean);
			if (condition != null) {
				INV_VW_TASK += condition;
			}
		}
		INV_VW_TASK += " AND TASK_ID_PARENT IS NULL GROUP BY TASK_ID, TAS_GROUP_ID, TAS_DOC_INV_ID, "
				+ "TAS_CREATED_DATE, TAS_DOWLOAD_DATE, TAS_UPLOAD_DATE, "
				+ "TAS_STATUS, TASK_ID_PARENT, CREATED_BY, MODIFIED_BY " + "ORDER BY TASK_ID ASC";

		log.info(INV_VW_TASK);
		log.info("[getTaskDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_TASK);

			log.info("[getTaskDao] Executing query...");

			ResultSet rs = stm.executeQuery();
			ume = new UMEDaoE();

			while (rs.next()) {
				taskBean = new TaskBean();
				taskBean.setTaskId(rs.getString("TASK_ID"));
				taskBean.setGroupId(rs.getString("TAS_GROUP_ID"));
				taskBean.setDocInvId(didao.getDocInvById(rs.getInt("TAS_DOC_INV_ID")));
				try {
					taskBean.setdCreated(rs.getDate("AS_CREATED_DATE").getTime());
				} catch (Exception e) {
					taskBean.setdCreated(0);
				}

				try {
					taskBean.setdDownlad(rs.getDate("TAS_DOWLOAD_DATE").getTime());
				} catch (Exception e) {
					taskBean.setdDownlad(0);
				}

				try {
					taskBean.setdUpload(rs.getDate("TAS_UPLOAD_DATE").getTime());
				} catch (Exception e) {
					taskBean.setdUpload(0);
				}

				try {
					taskBean.setStatus(rs.getString("TAS_STATUS").equals("1") ? true : false);
				} catch (Exception e) {
					taskBean.setStatus(false);
				}

				taskBean.setTaskIdFather(rs.getString("TASK_ID_PARENT"));

				user = new User();
				user.getEntity().setIdentyId(rs.getString("CREATED_BY"));
				ArrayList<User> ls = new ArrayList<>();
				ls.add(user);
				ls = ume.getUsersLDAPByCredentials(ls);

				if (ls.size() > 0) {

					taskBean.setCreatedBy(rs.getString("CREATED_BY") + " - " + ls.get(0).getGenInf().getName() + " "
							+ ls.get(0).getGenInf().getLastName() + " - "
							+ format.format(rs.getTimestamp("TAS_DOWLOAD_DATE")));
				} else {
					taskBean.setCreatedBy(
							rs.getString("CREATED_BY") + " - " + format.format(rs.getTimestamp("TAS_DOWLOAD_DATE")));
				}

				try {

					user.getEntity().setIdentyId(rs.getString("MODIFIED_BY"));
					ls = new ArrayList<>();
					ls.add(user);
					ls = ume.getUsersLDAPByCredentials(ls);

					if (ls.size() > 0) {

						taskBean.setModifiedBy(rs.getString("MODIFIED_BY") + " - " + ls.get(0).getGenInf().getName()
								+ " " + ls.get(0).getGenInf().getLastName() + " - "
								+ format.format(rs.getTimestamp("TAS_DOWLOAD_DATE")));
					} else {
						taskBean.setModifiedBy(rs.getString("MODIFIED_BY") + " - "
								+ format.format(rs.getTimestamp("TAS_DOWLOAD_DATE")));
					}
				} catch (Exception e) {
					taskBean.setCreatedBy(null);
				}

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
		} catch (SQLException | NamingException e) {
			log.log(Level.SEVERE,
					"[getTaskDao] Some error occurred while was trying to execute the query: " + INV_VW_TASK, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getTaskDao] Some error occurred while was trying to close the connection.", e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(listTaskBean);
		return res;
	}

	public long updateDowloadTask(String idTask) throws SQLException {

		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		final String INV_SP_UPDATE_DOWLOAD_TASK = "INV_SP_UPDATE_DOWLOAD_TASK ?, ?, ?";
		long date = 0;

		log.info("[updateDowloadTaskDao] Preparing sentence...");
		log.info(INV_SP_UPDATE_DOWLOAD_TASK);
		try {
			log.info("idTask " + idTask);
			cs = con.prepareCall(INV_SP_UPDATE_DOWLOAD_TASK);
			cs.setString(1, idTask);
			cs.registerOutParameter(2, Types.INTEGER);
			cs.registerOutParameter(3, Types.DATE);
			log.info("[updateDowloadTaskDao] Executing query...");

			cs.execute();
			log.info("[updateDowloadTaskDao] Despues del execute");
			log.info("con: " + con.toString());
			try {
				date = cs.getDate(3).getTime();
			} catch (Exception e) {
				log.info("Si era eso jajajaja");
			}

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

	private static final String GET_USERS_FROM_GROUP = "SELECT GRU_USER_ID FROM INV_GROUPS_USER WITH(NOLOCK) WHERE GRU_GROUP_ID = ? ";

	public List<String> getUsersFromTaskGroup(String userGroup) throws SQLException {
		Connection con = iConnectionManager.createConnection();
		List<String> userList = new ArrayList<>();
		try {
			PreparedStatement stm = con.prepareStatement(GET_USERS_FROM_GROUP);
			stm.setString(1, userGroup);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				if (!userList.contains(rs.getString("GRU_USER_ID"))) {
					userList.add(rs.getString("GRU_USER_ID"));
				}
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[TaskDao - getUsersFromTaskGroup] Some error occurred while was trying to execute the query: ");
			throw e;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[TaskDao -getUsersFromTaskGroup] Some error occurred while was trying to close the connection.",
						e);
			}
		}
		return userList;
	}

	public List<String> getUsersFromTaskGroup(String userGroup, Connection con) throws SQLException {
		List<String> userList = new ArrayList<>();
		try {
			PreparedStatement stm = con.prepareStatement(GET_USERS_FROM_GROUP);
			stm.setString(1, userGroup);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				if (!userList.contains(rs.getString("GRU_USER_ID"))) {
					userList.add(rs.getString("GRU_USER_ID"));
				}
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[TaskDao - getUsersFromTaskGroup] Some error occurred while was trying to execute the query: ");
			throw e;
		}
		return userList;
	}

	private String buildCondition(TaskBean taskB) {

		String TASK_ID = "";
		String TAS_GROUP_ID = "";
		String TAS_DOC_INV_ID = "";
		String condition = "";

		TASK_ID = (taskB.getTaskId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " TASK_ID = '" + taskB.getTaskId() + "' "
				: "";
		condition += TASK_ID;
		TAS_GROUP_ID = (taskB.getGroupId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " TAS_GROUP_ID = '" + taskB.getGroupId() + "' "
				: "";
		condition += TAS_GROUP_ID;
		TAS_DOC_INV_ID = (taskB.getDocInvId() != null)
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + " TAS_DOC_INV_ID ='" + taskB.getDocInvId()
						+ "' "
				: "";
		condition += TAS_DOC_INV_ID;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

}
