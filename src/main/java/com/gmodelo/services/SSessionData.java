package com.gmodelo.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.Response;
import com.google.gson.Gson;

/**
 * Root resource (exposed at "sessiondata" path)
 */
@Path("/services/sessiondata")
public class SSessionData {

	@Context
	private HttpServletRequest request;
	@SuppressWarnings("rawtypes")
	private Response resp;
	private HttpSession session;
	private static Logger log = Logger.getLogger(SSessionData.class.getName());

	/**
	 * Method handling HTTP POST requests. The returned object will be sent to
	 * the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String getSessionData() {
		Gson gson = new Gson();
		log.info("SSessionData ...");
		session = request.getSession();
		 String newSession = (String) session.getAttribute("newSession");
		 
		User user = (User) session.getAttribute("user");
		ArrayList<String> roles = (ArrayList<String>) session.getAttribute("roles");

		resp = new Response();
			
		List<Object> lista = new ArrayList<Object>();
		lista.add(user);
		lista.add(roles);
		if(newSession != null){
			lista.add(newSession);
		 }
		resp.setLsObject(lista);
		String jsonString = gson.toJson(resp);
		return jsonString;
	}

	

}
