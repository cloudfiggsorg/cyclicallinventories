package com.gmodelo.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.workservice.UmeWorkService;

@Path("/services/umeService")
public class UmeService {

	@Context
    private HttpServletRequest request;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTgort")
	public Response<Object> getTgort(Request request){
		return new UmeWorkService().saveUserWerks(request);
	}
	
}
