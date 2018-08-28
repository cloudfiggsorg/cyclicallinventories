package com.gmodelo.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import com.gmodelo.beans.LgortBeanView;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.workservice.LgortWorkService;

@Path("/services/LgortService")
public class LgortService {

	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getLgortAndWerks")
	public Response<List<LgortBeanView>> getBukrsAndWerks(Request request){
		return new LgortWorkService().getLgortByWerks(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getNgorts")
	public Response<List<LgortBeanView>> getNgorts(Request request){
		return new LgortWorkService().getNgorts(request);
	}
}
