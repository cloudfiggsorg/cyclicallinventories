package com.gmodelo.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gmodelo.beans.MantrB;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TgortB;
import com.gmodelo.workservice.MantrWorkService;
import com.gmodelo.workservice.TgortWorkService;

@Path("/services/MantrService")
public class MantrService {
	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getMantr")
	public Response<List<MantrB>> getMantr(Request request){
		return new MantrWorkService().getMantr(request);
	}

}