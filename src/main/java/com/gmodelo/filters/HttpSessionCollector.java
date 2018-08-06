package com.gmodelo.filters;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.jboss.logging.Logger;


public class HttpSessionCollector implements HttpSessionListener{
	
	private static Logger log = Logger.getLogger(HttpSessionCollector.class.getName());
	public static final Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();
//	private static final int SESSION_TIME = 5 * 60; 

	public void sessionCreated(HttpSessionEvent arg0) {
		log.warn("Registering session...");
		HttpSession session = arg0.getSession();		
//		session.setMaxInactiveInterval(SESSION_TIME);
	    sessions.put(session.getId(), session);
		
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		log.warn("Deleting session...");
		sessions.remove(arg0.getSession().getId());
		
	}
	
	public static HttpSession find(String sessionId) {
	    return sessions.get(sessionId);
	}

	public static Map<String, HttpSession> getSessions() {
	    return sessions;
	}

}
