package com.gmodelo.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TaskBean;
import com.gmodelo.workservice.TaskWorkService;

@Path("/services/TaskService")
public class TaskService {
	@Context
	private HttpServletRequest httpRequest;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addTask")
	public Response<TaskBean> addTask(Request request) {
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new TaskWorkService().addTask(request, user);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteTask")
	public Response<Object> deleteTask(Request request) {
		return new TaskWorkService().deleteTask(request);
	}


}
