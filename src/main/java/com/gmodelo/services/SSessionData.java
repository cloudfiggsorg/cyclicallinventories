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
import org.json.JSONObject;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.Response;

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
	public Response getSessionData() {
		AbstractResults abstractResult = new AbstractResults();
		
		log.warn("SSessionData ...");
		session = request.getSession();
		 String newSession = (String) session.getAttribute("newSession");
		 
		User user = (User) session.getAttribute("user");
		ArrayList<String> roles = (ArrayList<String>) session.getAttribute("roles");
		JSONObject job = new JSONObject();
		resp = new Response();
			
		job.put("user",user);
		job.put("roles", roles);
		if(newSession != null){
			job.put("newSession", newSession);
		 }
		
		resp.setAbstractResult(abstractResult);
		resp.setLsObject(job);
		return resp;
	}

	

}
