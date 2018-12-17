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
import com.gmodelo.cyclicinventories.beans.LgTypIMBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.LgTypIMWorkService;

@Path("/services/LgTypService")
public class LgTypIMService {

	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveLgTypIM")
	public Response<LgTypIMBean> addZone(Request request){
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new LgTypIMWorkService().saveLgTypIM(request, user);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getLgTypsOnly")
	public Response<List<LgTypIMBean>> getLgTypsOnly(Request request){
		return new LgTypIMWorkService().getLgTypsOnly(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getLgTypsIM")
	public Response<List<LgTypIMBean>> getLgTypsIM(Request request){
		return new LgTypIMWorkService().getLgTypsIM(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteLgTypsIM")
	public Response<Object> deleteZone(Request request){
		return new LgTypIMWorkService().deleteLgTypsIM(request);
	}	
}
