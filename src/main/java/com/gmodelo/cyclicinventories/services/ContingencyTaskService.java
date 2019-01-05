package com.gmodelo.cyclicinventories.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.RouteUserBean;
import com.gmodelo.cyclicinventories.workservice.ContingencyTaskWorkService;

@Path("/services/ContingencyTaskService")
public class ContingencyTaskService {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/sendContingencyTask")
	public Response<RouteUserBean> sendContingencyTask(Request request){
		return new ContingencyTaskWorkService().buildBean(request);
	}
}
