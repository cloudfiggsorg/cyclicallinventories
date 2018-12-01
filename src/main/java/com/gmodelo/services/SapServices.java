package com.gmodelo.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gmodelo.beans.Response;
import com.gmodelo.workservice.SapConciliationWorkService;

@Path("/services/SapServices")
public class SapServices {

	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/setClassification")
	public Response setClassification() {

		return new SapConciliationWorkService()._WS_SAPClassRuntime();
	}
	
	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getClassification")
	public Response getClassification() {

		return new SapConciliationWorkService().WS_getClassSystem();
	}

}
