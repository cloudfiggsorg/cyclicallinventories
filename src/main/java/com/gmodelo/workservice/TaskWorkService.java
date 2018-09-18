package com.gmodelo.workservice;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TaskBean;
import com.gmodelo.dao.TaskDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class TaskWorkService {
	
	private Logger log = Logger.getLogger(TaskWorkService.class.getName());
	Gson gson = new Gson();
	
	public Response<TaskBean> addTask(Request request, User user) {
		log.info("[addTaskWS] " + request.toString());
		TaskBean taskBean = null;
		Response<TaskBean> res = new Response<TaskBean>();
		try {
			taskBean = gson.fromJson(gson.toJson(request.getLsObject()), TaskBean.class);
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
		return new TaskDao().addTask(taskBean, user.getEntity().getIdentyId());
	}

	public Response<Object> deleteTask(Request request) {
		log.info("[deleteTaskWS] " + request.toString());
		String arrayIdTask;
		Response<Object> res = new Response<Object>();
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


}
