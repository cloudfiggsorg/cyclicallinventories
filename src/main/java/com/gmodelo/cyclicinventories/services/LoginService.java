package com.gmodelo.cyclicinventories.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.gmodelo.cyclicinventories.beans.LoginBean;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.LoginWorkService;

@Path("/LoginModule")
public class LoginService {
	@Context
	private HttpServletRequest request;
	private HttpSession session;
	private Logger log = Logger.getLogger(LoginService.class.getName());

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/login")
	public Response login(LoginBean loginBean) {

		log.warn((request.getSession().getAttribute("user") != null) ? "Hay sesion" : "NO sesion");

		return new LoginWorkService().login(loginBean, request, session);

	}
	

}
