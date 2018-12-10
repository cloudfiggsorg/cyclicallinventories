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
import com.gmodelo.beans.ConciliationsIDsBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.workservice.ConciliationSAPWorkService;

@Path("/services/ConciliationSAPService")
public class ConciliacionSAPService {
	
	@Context
	private HttpServletRequest httpRequest;	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getClosedConciliationsID")
	public Response<List<ConciliationsIDsBean>> getClosedConciliationsID(Request request){
		
		return new ConciliationSAPWorkService().getClosedConciliationIDs(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveConciliation")
	public Response saveConciliation(Request request){
		
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new ConciliationSAPWorkService().saveConciliation(request, user.getEntity().getIdentyId());
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getjsFileBase64")
	public Response<String> getjsFileBase64(Request request){
		
		return new ConciliationSAPWorkService().getjsFileBase64(request);
	}
}
