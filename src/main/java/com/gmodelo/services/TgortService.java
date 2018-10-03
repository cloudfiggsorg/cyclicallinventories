package com.gmodelo.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TgortB;
import com.gmodelo.workservice.TgortWorkService;

@Path("/services/TgortService")
public class TgortService {
	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTgort")
	public Response<List<TgortB>> getTgort(Request request){
		return new TgortWorkService().getTgort(request);
	}

}
