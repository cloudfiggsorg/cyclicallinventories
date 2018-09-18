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
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.ToleranceBean;
import com.gmodelo.workservice.DocInvWorkService;
import com.gmodelo.workservice.ToleranceWorkService;

public class DocInvService {
	
	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addDocInv")
	public Response<DocInvBean> addDocInv(Request request){
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new DocInvWorkService().addDocInv(request, user);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteDocInv")
	public Response<Object> deleteDocInv(Request request){
		return new DocInvWorkService().deleteDocInv(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getDocInv")
	public Response<List<DocInvBean>> getDocInv(Request request){
		return new DocInvWorkService().getDocInv(request);
	}

}
