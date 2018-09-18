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
import com.gmodelo.beans.ConciliacionBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.ToleranceBean;
import com.gmodelo.workservice.ConciliacionWorkService;
import com.gmodelo.workservice.DocInvWorkService;
import com.gmodelo.workservice.ToleranceWorkService;

public class ConciliacionService {
	
	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getConciliacion")
	public Response<List<ConciliacionBean>> getConciliacion(Request request){
		return new ConciliacionWorkService().getConciliacion(request);
	}

}
