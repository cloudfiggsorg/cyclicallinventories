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
import com.gmodelo.beans.RfcTablesB;
import com.gmodelo.workservice.RfcTablesWorkService;

@Path("/services/RfcTablesService")
public class RfcTablesService {
	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTmatnr")
	public Response<List<RfcTablesB>> getTmatnr(Request request){
		return new RfcTablesWorkService().getRfcTables(request);
	}


}