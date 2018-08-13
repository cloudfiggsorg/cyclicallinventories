package com.gmodelo.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.LoginBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.workservice.DownloadWorkService;
import com.google.gson.Gson;

@Path("/services/DownloadService")
public class DownloadService {

	@Context
	private HttpServletRequest httpRequest;
	private Logger log = Logger.getLogger(DownloadService.class.getName());

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/GetMasterData")
	public String GetMasterData(Request<LoginBean<?>> request) {
		log.info("[DownloadService] " + request);
		AbstractResults abstractResult = new AbstractResults();
		abstractResult.setResultMsgAbs(httpRequest.getSession().getId());
		abstractResult.setIntCom1(httpRequest.getSession().getMaxInactiveInterval());

		Response<List<Object>> response = new DownloadWorkService().GetMasterDataWork(request);
		response.setAbstractResult(abstractResult);

		return new Gson().toJson(response);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/GetInfoTables")
	public Response GetInfoTables(Request<LoginBean<?>> request) {
		return new DownloadWorkService().GetInfoTablesWS(request);
	}

	@GET
	@Path("/GetMasterDataTest")
	@Produces(MediaType.APPLICATION_JSON)
	public String GetMasterDataTest() {
		return new Gson().toJson(new DownloadWorkService().GetMasterDataWork(new Request<LoginBean<?>>()));
	}
}
