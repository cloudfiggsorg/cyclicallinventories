package com.gmodelo.cyclicinventories.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.ConciliacionBean;
import com.gmodelo.cyclicinventories.beans.ConciliationsIDsBean;
import com.gmodelo.cyclicinventories.beans.GroupBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.TaskBean;
import com.gmodelo.cyclicinventories.workservice.ConciliacionWorkService;

@Path("/services/ConciliationService")
public class ConciliacionService {
	
	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getConciliationsID")
	public Response<List<ConciliationsIDsBean>> getConciliationIDs(Request request){
		return new ConciliacionWorkService().getConciliationIDs(request);
	}
		
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getConciliation")
	public Response<ConciliacionBean> getConciliacion(Request request){
		return new ConciliacionWorkService().getConciliacion(request);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAvailableGroups")
	public Response<List<GroupBean>> getAvailableGroups(Request request){
		return new ConciliacionWorkService().getAvailableGroups(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFatherTaskByDocId")
	public Response<TaskBean> getFatherTaskByDocId(Request request){
		return new ConciliacionWorkService().getFatherTaskByDocId(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getZonePosition")
	public Response<String> getZonePosition(Request request){
		return new ConciliacionWorkService().getZonePosition(request);
	}
	
	@POST 
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getSpecialCount")
	public Response<TaskBean> getSpecialCount(Request request){
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new ConciliacionWorkService().getSpecialCount(request, user);
	}
	
}
