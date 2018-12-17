package com.gmodelo.cyclicinventories.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.Response;

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
	@Produces(MediaType.APPLICATION_JSON)
	public Response<List<Object>> getSessionData() {
		Response<List<Object>> resp = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<Object> listObject = new ArrayList<Object>();
		User user = (User) httpRequest.getSession().getAttribute("user");
		log.warn(user);
		ArrayList<String> roles = (ArrayList<String>) httpRequest.getSession().getAttribute("roles");
		log.warn(roles);
		user.getAccInf().setPassword(null);

		log.warn("SSessionData ...");
		log.warn("id: " + httpRequest.getSession().getId());
		log.warn("attribute user: " + httpRequest.getSession().getAttribute("user"));
		log.warn("attribute roles: " + httpRequest.getSession().getAttribute("roles"));

		listObject.add(user);
		listObject.add(roles);
		listObject.add(System.getProperty("java.version"));
		
		resp.setLsObject(listObject);
		resp.setAbstractResult(abstractResult);

		return resp;
	}

}
