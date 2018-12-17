package com.gmodelo.cyclicinventories.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.LoginWorkService;

@Path("/services/logout")
public class LogoutService {
	
	@Context
    private HttpServletRequest request;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response<Object> logout() {
		
		return new LoginWorkService().logout(request);
		
	}

}
