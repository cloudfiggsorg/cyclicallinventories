package com.gmodelo.cyclicinventories.workservice;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.TaskBean;
import com.gmodelo.cyclicinventories.dao.RouteDao;
import com.gmodelo.cyclicinventories.dao.TaskDao;
import com.gmodelo.cyclicinventories.runtime.TaskGsonRuntime;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class TaskWorkService {

	private Logger log = Logger.getLogger(TaskWorkService.class.getName());
	private Gson gson = new Gson();
	private ConnectionManager iConnectionManager = new ConnectionManager();

	public Response<TaskBean> addTaskSpecial(TaskBean taskBean, User user) {
		log.info("[addTaskWS] " + taskBean.toString());
		Response<TaskBean> res = new Response<>();
		try {
			if (taskBean.getRub() != null) {
				log.log(Level.WARNING, "[addTaskWS] Ingreso con JSON " + taskBean.toString());
				TaskBean subBean = taskBean;
				subBean.setRub(null);
				res = new TaskDao().addTask(subBean, user.getEntity().getIdentyId());
				if (res.getAbstractResult().getResultId() == ReturnValues.ISUCCESS) {
					taskBean.setTaskId(res.getLsObject().getTaskId());
					taskBean.getRub().setTaskId(res.getLsObject().getTaskId());
					log.log(Level.WARNING, "[addTaskWS] Previo a ejecutar New Task" + taskBean.toString());
					res = new TaskDao().addTask(taskBean, user.getEntity().getIdentyId());
				}
			} else {
				log.log(Level.WARNING, "[addTaskWS] Ingreso sin JSON " + taskBean.toString());
				res = new TaskDao().addTask(taskBean, user.getEntity().getIdentyId());
			}
			log.log(Level.WARNING, "[addTaskWS] ");
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[addTaskWS] Error al pasar de Json a TaskBean", e);
			taskBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		return res;
	}

	public Response<TaskBean> addTask(Request request, User user) {
		log.info("[addTaskWS] " + request.toString());
		TaskBean taskBean = null;
		Response<TaskBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		try {
			taskBean = gson.fromJson(gson.toJson(request.getLsObject()), TaskBean.class);
			if (taskBean.getRub() != null) {
				log.log(Level.WARNING, "[TaskWorkService - addTaskWS] Ingreso con JSON " + taskBean.toString());
				if ((new RouteDao().assingRecountGroup(taskBean, user)).getResultId() == ReturnValues.ISUCCESS) {
					TaskBean subBean = gson.fromJson(gson.toJson(request.getLsObject()), TaskBean.class);
					subBean.setRub(null);
					res = new TaskDao().addTask(subBean, user.getEntity().getIdentyId());
					if (res.getAbstractResult().getResultId() == ReturnValues.ISUCCESS) {
						taskBean.setTaskId(res.getLsObject().getTaskId());
						taskBean.getRub().setTaskId(res.getLsObject().getTaskId());
						log.log(Level.WARNING,
								"[TaskWorkService - addTaskWS] Previo a ejecutar New Task" + taskBean.toString());
						res = new TaskDao().addTask(taskBean, user.getEntity().getIdentyId());
					}
				} else {
					log.log(Level.WARNING, "[TaskWorkService - addTaskWS] Fallo al Ingresar el grupo a la ruta "
							+ taskBean.toString());
					abstractResult.setResultId(ReturnValues.IERROR);
					abstractResult.setResultMsgAbs("Fallo al Ingresar el grupo a la ruta");
					res.setAbstractResult(abstractResult);
				}
			} else {
				log.log(Level.WARNING, "[TaskWorkService - addTaskWS] Ingreso sin JSON " + taskBean.toString());
				res = new TaskDao().addTask(taskBean, user.getEntity().getIdentyId());
				if (res.getAbstractResult().getResultId() == ReturnValues.ISUCCESS) {
					WS_RuntimeTaskGson(taskBean, user);
				}
			}
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[TaskWorkService - addTaskWS] Error al pasar de Json a TaskBean", e);
			taskBean = null;
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		return res;
	}

	public Response<Object> deleteTask(Request request) {
		log.info("[deleteTaskWS] " + request.toString());
		String arrayIdTask;
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		try {
			arrayIdTask = request.getLsObject().toString();
			if (arrayIdTask == null || arrayIdTask.isEmpty()) {
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs("NULL OR EMPTY ARRAY");
				res.setAbstractResult(abstractResult);
				return res;
			}
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[deleteTaskWS] Error al pasar de Json a String");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		return new TaskDao().deleteTask(arrayIdTask);
	}

	public Response<List<TaskBean>> getTasks(Request request) {

		log.info("getTasksWS] " + request.toString());
		TaskBean tb = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		Response<List<TaskBean>> res = new Response<>();

		if (!req.isEmpty()) {
			try {
				tb = gson.fromJson(gson.toJson(request.getLsObject()), TaskBean.class);
				log.info("[getTasksWS] Fue Objeto: " + tb);
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString().trim();
				log.info("[getTasksWS] json Intentando por String ");
			}
		} else {
			searchFilter = "";
			log.info("[getTasksWS] Fue cadena vacía ");

			try {
				tb = gson.fromJson(gson.toJson(request.getLsObject()), TaskBean.class);
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE, "[getTasksWS] Error al pasar de Json a TaskB");
				tb = null;
				AbstractResultsBean abstractResult = new AbstractResultsBean();
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		return new TaskDao().getTasks(tb, searchFilter);
	}

	public Response<List<TaskBean>> getTasksByBukrsAndWerks(Request request) {

		log.info("[getTasksByBukrsAndWerks] " + request.toString());
		String bukrs;
		String werks;
		String req;
		req = request.getLsObject().toString();
		req = req.replaceAll("=", ":");

		JSONObject jsonObj = new JSONObject(req);

		try {

			bukrs = jsonObj.getString("bukrs");
		} catch (JSONException e) {
			bukrs = "";
		}

		try {

			werks = jsonObj.getString("werks");
		} catch (JSONException e) {
			werks = "";
		}

		return new TaskDao().getTasksbyBukrsAndWerks(bukrs, werks);
	}

	public AbstractResultsBean WS_RuntimeTaskGson(TaskBean taskBean, User user) {
		AbstractResultsBean abstractResultsBean = new AbstractResultsBean();
		Connection con = iConnectionManager.createConnection();
		try {
			new TaskGsonRuntime(con, taskBean, user).start();
		} catch (RuntimeException e) {
			abstractResultsBean.setResultId(ReturnValues.IEXCEPTION);
			abstractResultsBean.setResultMsgAbs(e.getMessage());
		} catch (Exception e) {
			abstractResultsBean.setResultId(ReturnValues.IEXCEPTION);
			abstractResultsBean.setResultMsgAbs(e.getMessage());
		}
		return abstractResultsBean;
	}

}
