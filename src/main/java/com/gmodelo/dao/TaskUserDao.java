package com.gmodelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TaskBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class TaskUserDao {

	private Logger log = Logger.getLogger(TaskUserDao.class.getName());

	private static final String INV_LAST_TASK = "SELECT TOP 1 ROUTE_ID, BUKRS, WERKS,  DTYPE, GROUP_ID, max(TAS_UPLOAD_DATE) AS LAST_UPLOAD "
			+ " FROM dbo.INV_VW_TASK_ROUTES_USER_UPLOAD WITH(NOLOCK) WHERE USER_ID = ? AND TAS_UPLOAD_DATE is not null "
			+ " GROUP BY ROUTE_ID, BUKRS, WERKS, DTYPE, GROUP_ID  ORDER BY ROUTE_ID DESC  ";

	public int createAutoTask(String user) {

		int response = 0;
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		final String INV_VW_TASK = "INV_SP_ROUTE_USER_UPLOAD ?, ?, ?, ?, ?, ?, ?, ?";
		log.info(INV_VW_TASK);

		try {

			stm = con.prepareStatement(INV_LAST_TASK);
			stm.setString(1, user);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				DocInvBean docInv = new DocInvBean();
				docInv.setRoute(rs.getString("ROUTE_ID"));
				docInv.setBukrs(rs.getString("BUKRS"));
				docInv.setWerks(rs.getString("WERKS"));
				docInv.setType(rs.getString("DTYPE"));
				DocInvDao dao = new DocInvDao();
				Response<DocInvBean> res = dao.addDocInv(docInv, user);
				if (res.getAbstractResult().getResultId() == ReturnValues.ISUCCESS) {
					TaskBean taskBean = new TaskBean();
					taskBean.setDocInvId(res.getLsObject());
					taskBean.setGroupId(rs.getString("GROUP_ID"));
					TaskDao daoTask = new TaskDao();
					Response<TaskBean> resDaoTask = daoTask.addTask(taskBean, user);
					if (resDaoTask.getAbstractResult().getResultId() == ReturnValues.ISUCCESS) {
						response = ReturnValues.ISUCCESS;
					}
				}
			}

			log.info("[getTaskByUserDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.severe("Ocurred error try close the conection in createAutoTask: " + e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.severe("Ocurred error try close the conection in createAutoTask: " + e.getMessage());
			}
		}
		return response;
	}

	public int createAutoTaskLegacy(User user) {

		int response = 0;
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;

		final String INV_VW_TASK = "INV_SP_ROUTE_USER_UPLOAD ?, ?, ?, ?, ?, ?, ?, ?";
		log.info(INV_VW_TASK);

		try {
			cs = con.prepareCall(INV_VW_TASK);

			cs.setString(1, user.getEntity().getIdentyId());
			cs.registerOutParameter(2, Types.VARCHAR); // routeId
			cs.registerOutParameter(3, Types.VARCHAR); // bukrs
			cs.registerOutParameter(4, Types.VARCHAR); // werks
			cs.registerOutParameter(5, Types.VARCHAR); // TYPODOCINV
			cs.registerOutParameter(6, Types.VARCHAR); // GRUPO
			cs.registerOutParameter(7, Types.VARCHAR); // WERKSD
			cs.registerOutParameter(8, Types.INTEGER); // return

			log.info("[getLastTask] Executing query...");
			cs.execute();

			if (cs.getInt(8) == 1) {

				DocInvBean docInv = new DocInvBean();
				docInv.setRoute(cs.getString(2));
				docInv.setBukrs(cs.getString(3));
				docInv.setWerks(cs.getString(4));
				docInv.setType(cs.getString(5));
				docInv.setWerksD(cs.getString(7));

				DocInvDao dao = new DocInvDao();
				Response<DocInvBean> res = dao.addDocInv(docInv, user.getEntity().getIdentyId());

				if (res.getAbstractResult().getResultId() == 1) {

					TaskBean taskBean = new TaskBean();
					taskBean.setDocInvId(res.getLsObject());
					taskBean.setGroupId(cs.getString(6));
					TaskDao daoTask = new TaskDao();
					Response<TaskBean> resDaoTask = daoTask.addTask(taskBean, user.getEntity().getIdentyId());

					if (resDaoTask.getAbstractResult().getResultId() == 1) {
						response = 1;
					}

				}
			}

			cs.close();
			log.info("[getTaskByUserDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.severe("Ocurred error try close the conection in createAutoTask: " + e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.severe("Ocurred error try close the conection in createAutoTask: " + e.getMessage());
			}
		}
		return response;
	}
	/*
	 * public static void main(String args[]){ TaskUserDao dao = new
	 * TaskUserDao(); int x = dao.createAutoTask("ROD1986");
	 * System.out.println("x: "+ x); }
	 */
}
