package com.gmodelo.cyclicinventories.services;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.ExplosionDetail;
import com.gmodelo.cyclicinventories.beans.MatExplReport;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.ExplosionWorkService;

@Path("/services/ExplosionService")
public class ExplosionService {
	
	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getExplosionDetail")
	public Response<ArrayList<ExplosionDetail>> getExplosionDetail(Request request){
		return new ExplosionWorkService().getLsExplosionDetail(request);
	}
		
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveExplosionDetail")
	public Response saveExplosionDetail(Request request){
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new ExplosionWorkService().saveExplosionDetail(request, user);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getExplosionReportByWerks")
	public Response<ArrayList<MatExplReport>> getExplosionReportByWerks(Request request){
		return new ExplosionWorkService().getExplosionReportByWerks(request);
	}
		
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getExplosionReportByLgpla")
	public Response<ArrayList<MatExplReport>> getExplosionReportByLgpla(Request request){
		return new ExplosionWorkService().getExplosionReportByLgpla(request);
	}

}
