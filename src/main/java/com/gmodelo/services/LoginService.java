package com.gmodelo.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.gmodelo.beans.LoginBean;
import com.gmodelo.utils.CommunicationObjects;
import com.gmodelo.workservice.LoginWorkService;

@Path("/LoginModule")
public class LoginService {
	@Context
	private HttpServletRequest request;

	private Logger log = Logger.getLogger(LoginService.class.getName());

//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/RequestLogin")
//	public String RequestLogin(@FormParam(CommunicationObjects.LOGINOBJECT) String login) {
//		return new LoginWorkService().validateLogin(login);
//	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/RequestNewSession")
	public String RequestNewLogin(@FormParam(CommunicationObjects.LOGINOBJECT) String login) {
		return null;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/login")
	public String login(LoginBean loginBean) {

		log.warn((request.getSession().getAttribute("user") != null) ? "Hay sesion" : "NO sesion");

		return new LoginWorkService().login(loginBean, request);

	}

}
