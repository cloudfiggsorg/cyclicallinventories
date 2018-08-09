package com.gmodelo.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gmodelo.beans.Response;
import com.gmodelo.workservice.LoginWorkService;

@Path("/services/logout")
public class LogoutService {
	
	@Context
    private HttpServletRequest request;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response<?> logout() {
		
		return new LoginWorkService().logout(request);
		
	}

}
