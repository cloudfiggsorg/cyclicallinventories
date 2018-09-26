package com.gmodelo.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.gmodelo.beans.Request;
import com.gmodelo.filters.HttpSessionCollector;
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
		HttpSession s = HttpSessionCollector.find(request.getTokenObject().getRelationUUID());
		return new DownloadWorkService().GetInfoTablesWS(request, s);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/GetMasterData")
	public String GetMasterData(Request request) {
		HttpSession s = HttpSessionCollector.find(request.getTokenObject().getRelationUUID());
		return new DownloadWorkService().GetMasterDataWS(request, s);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/GetMobileData")
	public String GetMobileDataMaterial(Request request) {
		HttpSession s = HttpSessionCollector.find(request.getTokenObject().getRelationUUID());
		return new DownloadWorkService().GetMobileDataWS(request, s);
	}

}
