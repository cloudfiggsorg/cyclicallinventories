package com.gmodelo.cyclicinventories.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gmodelo.cyclicinventories.beans.MatnrBeanView;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.TmatnrBean;
import com.gmodelo.cyclicinventories.workservice.MatnrWorkService;

@Path("/services/MatnrService")
public class MatnrService {
	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getMatnr")
	public Response<List<MatnrBeanView>> getMantr(Request request){
		return new MatnrWorkService().getMatnr(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTmatnr")
	public Response<List<TmatnrBean>> getTmatnr(Request request){
		return new MatnrWorkService().getTmatnr(request);
	}

}
