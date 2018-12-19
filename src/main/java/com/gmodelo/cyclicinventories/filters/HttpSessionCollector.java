package com.gmodelo.cyclicinventories.filters;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.jboss.logging.Logger;

import com.bmore.ume001.beans.User;


public class HttpSessionCollector implements HttpSessionListener{
	
	private static Logger log = Logger.getLogger(HttpSessionCollector.class.getName());
	public static final Map<String, HttpSession> sessions = new HashMap<>(); 

	public void sessionCreated(HttpSessionEvent arg0) {
		log.warn("Registering session...");
		HttpSession session = arg0.getSession();		
	    sessions.put(session.getId(), session);
		
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		log.warn("Deleting session...");
		log.warn(arg0.getSession().getId());
		log.warn(arg0.getSession().getAttribute("user"));
		if(arg0.getSession().getAttribute("user") == null){
			log.warn("No trae user");
		}else{
			User user = (User) arg0.getSession().getAttribute("user");
			log.warn(user.getEntity().getIdentyId());
		}
		sessions.remove(arg0.getSession().getId());
		
	}
	
	public static HttpSession find(String sessionId) {
	    return sessions.get(sessionId);
	}

	public static Map<String, HttpSession> getSessions() {
	    return sessions;
	}

}
