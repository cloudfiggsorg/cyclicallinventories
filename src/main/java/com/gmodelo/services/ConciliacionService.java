package com.gmodelo.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gmodelo.beans.ConciliacionBean;
import com.gmodelo.beans.ConciliationsIDsBean;
import com.gmodelo.beans.GroupBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.workservice.ConciliacionWorkService;

@Path("/services/ConciliationService")
public class ConciliacionService {
	
	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getConciliationsID")
	public Response<List<ConciliationsIDsBean>> getConciliationIDs(){
		return new ConciliacionWorkService().getConciliationIDs();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getConciliation")
	public Response<List<ConciliacionBean>> getConciliacion(Request request){
		return new ConciliacionWorkService().getConciliacion(request);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAvailableGroups")
	public Response<List<GroupBean>> getAvailableGroups(Request request){
		return new ConciliacionWorkService().getAvailableGroups(request);
	}
	
}
