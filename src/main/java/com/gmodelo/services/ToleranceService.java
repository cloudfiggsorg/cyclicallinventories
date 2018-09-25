package com.gmodelo.services;

import java.util.List;

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
import com.gmodelo.beans.ToleranceBean;
import com.gmodelo.workservice.ToleranceWorkService;

@Path("/services/ToleranceService")
public class ToleranceService {

	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getMATKL")
	public Response<List<ToleranceBean>> getMATKL(Request request){
		return new ToleranceWorkService().getMATKL(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addTolerance")
	public Response<Object> addTolerance(Request request){
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new ToleranceWorkService().addTolerance(request, user);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteTolerance")
	public Response<Object> deleteTolerance(Request request){
		return new ToleranceWorkService().deleteTolerance(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTolerances")
	public Response<List<ToleranceBean>> getTolerances(Request request){
		return new ToleranceWorkService().getTolerances(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getToleranceByMatnr")
	public Response<ToleranceBean> getToleranceByMatnr(Request request){
		return new ToleranceWorkService().getToleranceByMatnr(request);
	}
}
