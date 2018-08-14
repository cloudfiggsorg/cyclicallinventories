package com.gmodelo.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.gmodelo.beans.Request;
import com.gmodelo.workservice.DownloadWorkService;

@Path("/services/DownloadService")
public class DownloadService {

	@Context
	private HttpServletRequest httpRequest;
	private Logger log = Logger.getLogger(DownloadService.class.getName());

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/GetInfoTables")
	public String GetInfoTables(Request request) {
		
		return new DownloadWorkService().GetInfoTablesWS(request, httpRequest);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/GetMasterData")
	public String GetMasterData(Request request) {
		
		return new DownloadWorkService().GetMasterDataWS(request, httpRequest);
	}


}
