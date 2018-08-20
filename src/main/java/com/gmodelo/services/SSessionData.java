package com.gmodelo.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;
import org.json.JSONObject;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.google.gson.Gson;

/**
 * Root resource (exposed at "sessiondata" path)
 */
@Path("/services/sessiondata")
public class SSessionData {

	@Context
	private HttpServletRequest httpRequest;
	private static Logger log = Logger.getLogger(SSessionData.class.getName());

	/**
	 * Method handling HTTP POST requests. The returned object will be sent to
	 * the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@SuppressWarnings("unchecked")
	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response<List<Object>> getSessionData() {
		Response<List<Object>> resp = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		List<Object> listObject = new ArrayList<Object>();
//		log.warn(request);		
//		session = httpRequest.getSession();
////		 String newSession = (String) session.getAttribute("newSession");
//		 
		User user = (User) httpRequest.getSession().getAttribute("user");
		log.warn(user);
		ArrayList<String> roles = (ArrayList<String>) httpRequest.getSession().getAttribute("roles");
		log.warn(roles);
//		JSONObject job = new JSONObject();
		user.getAccInf().setPassword(null);
		
		log.warn("SSessionData ...");
		log.warn("id: "+httpRequest.getSession().getId());
		log.warn("attribute user: "+httpRequest.getSession().getAttribute("user"));
		log.warn("attribute roles: "+httpRequest.getSession().getAttribute("roles"));
		
//		job.put("user",user);
//		job.put("roles",roles);
		listObject.add(user);
		listObject.add(roles);
//		listObject.add(job);
		resp.setLsObject(listObject);
		resp.setAbstractResult(abstractResult);
		
		return resp;
	}

	

}
