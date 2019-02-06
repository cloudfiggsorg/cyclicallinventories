package com.gmodelo.cyclicinventories.runtime;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.LoginBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.RouteUserBean;
import com.gmodelo.cyclicinventories.beans.TaskBean;
import com.gmodelo.cyclicinventories.dao.TaskDao;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.gmodelo.cyclicinventories.workservice.RouteWorkService;

public class TaskGsonRuntime extends Thread {

	private Connection asyncConnection;
	private TaskBean asyncTaskBean;
	private User asyncUser;
	private Logger log = Logger.getLogger(TaskGsonRuntime.class.getName());

	public TaskGsonRuntime(Connection asyncConnection, TaskBean asyncTaskBean, User asyncUser) {
		super();
		this.asyncConnection = asyncConnection;
		this.asyncTaskBean = asyncTaskBean;
		this.asyncUser = asyncUser;
	}

	@Override
	public void run() {
		executeGsonGeneration(asyncConnection, asyncTaskBean, asyncUser);
	}

	public void executeGsonGeneration(Connection con, TaskBean taskBean, User user) {
		try {
			if (!con.isValid(0)) {
				con = new ConnectionManager().createConnection();
			}
			List<String> users = new TaskDao().getUsersFromTaskGroup(taskBean.getGroupId(), con);
			Request<?> req = new Request<>();
			LoginBean<?> tokenObject = new LoginBean<>();
			req.setTokenObject(tokenObject);
			req.getTokenObject().setLoginId(users.get(0));
			Response<RouteUserBean> respRUB = new RouteWorkService().getRoutesByUserSAP(req);
			if (respRUB.getAbstractResult().getResultId() == ReturnValues.ISUCCESS) {
				taskBean.setRub(respRUB.getLsObject());
				Response<TaskBean> resAsyncTask = new TaskDao().addTask(taskBean, user.getEntity().getIdentyId());
				if (resAsyncTask.getAbstractResult().getResultId() == ReturnValues.ISUCCESS) {
					log.severe("[TaskGsonRuntime - executeGsonGeneration] -  Taskid: "
							+ resAsyncTask.getLsObject().getTaskId() + " - TaskJson Aassigned correctly!");
				} else {
					log.severe("[TaskGsonRuntime - executeGsonGeneration] - "
							+ resAsyncTask.getAbstractResult().getResultMsgAbs());
				}
			} else {
				log.severe(
						"[TaskGsonRuntime - executeGsonGeneration] - " + respRUB.getAbstractResult().getResultMsgAbs());
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[TaskGsonRuntime - executeGsonGeneration - SQLException]", e);
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "[TaskGsonRuntime - executeGsonGeneration - RuntimeException]", e);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[TaskGsonRuntime - executeGsonGeneration - Exception]", e);
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				log.log(Level.SEVERE, "[TaskGsonRuntime - executeGsonGeneration - Exception]", e);
			}
		}
	}

}
