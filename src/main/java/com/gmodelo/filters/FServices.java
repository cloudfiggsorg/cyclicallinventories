package com.gmodelo.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.workservice.LoginWorkService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


@WebFilter(urlPatterns = "/services/*")
public class FServices implements Filter {

	private Logger log = Logger.getLogger(FServices.class.getName());
//	private Logger LOGGER = Logger.getLogger(FServices.class.getName());

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest sRequest, ServletResponse sResponse, FilterChain filterChain)
			throws IOException, ServletException {
		
		HttpServletResponse response = (HttpServletResponse)sResponse;
		
		//Setting headers to response & prevent cache
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0 
		response.setDateHeader("Expires", 0); // prevents caching at the proxy server
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		 
		try {
			HttpServletRequest httpRequest = (HttpServletRequest) sRequest;
			if(!httpRequest.getMethod().equalsIgnoreCase("POST")){
//				LOGGER.log( Level.SEVERE, "Error trying to access information service without POST method. Anulating request...");
				log.error("[doFilter] Error trying to access information service without POST method. Anulating request...");
				
				@SuppressWarnings("rawtypes")
				Response resp = new Response();
				AbstractResults abstractResult = new AbstractResults();
				abstractResult.setResultId(-1);
				abstractResult.setResultMsgAbs("'GET' METHOD NOT ALLOWED");
				resp.setAbstractResult(abstractResult);
				
				// Using json to response
				String json = new Gson().toJson(resp);
				response.getWriter().write(json);
				
				return;
			}
			
			MyRequestWrapper myRequestWrapper = new MyRequestWrapper((HttpServletRequest) sRequest);
			 String body = myRequestWrapper.getBody();
			if(body != null && !body.isEmpty()){
//				 log.warn(body);
				
				@SuppressWarnings("rawtypes")
				Request req = null;
				try {
					req = new Gson().fromJson(body, Request.class) ;
				} catch (JsonSyntaxException e) {
					log.error("[doFilter] Error al pasar de Json a Request");
					req = null;
				}
//				log.warn(req);
				
				if(req.getTokenObject().getLoginId() != null && req.getTokenObject().getLoginPass() != null){
					HttpSession androidSession = HttpSessionCollector.find(req.getTokenObject().getRelationUUID());
					if(androidSession != null){
						log.warn("[doFilter] Android. Invalidating session...");
						androidSession.invalidate();
					}
					Response res = new LoginWorkService().login(req.getTokenObject(), (HttpServletRequest)sRequest);
					log.warn(res);
					if(res.getAbstractResult().getResultId() == 1){
						log.warn("[doFilter] Android. Renovated session!!. Forwading request..");
							filterChain.doFilter(myRequestWrapper, sResponse);
							
					}else{
						@SuppressWarnings("rawtypes")
						Response resp = new Response();
						AbstractResults abstractResult = new AbstractResults();
						abstractResult.setResultId(-1);
						abstractResult.setResultMsgAbs("SOME PROBLEM WHILE AUTHENTICATING");
						resp.setAbstractResult(abstractResult);
						
						log.error(abstractResult.getResultMsgAbs());
						// Using json to response
						String json = new Gson().toJson(resp);
						response.getWriter().write(json);
					}
					
					return;
					
				}

				//Check if token exist
				if(!HttpSessionCollector.sessions.isEmpty()){
					if(req.getTokenObject().getRelationUUID() != null){
						if(HttpSessionCollector.sessions.containsKey(req.getTokenObject().getRelationUUID())){
							log.warn("[doFilter] Android. Token exists. Forwading request...");
							filterChain.doFilter(myRequestWrapper, sResponse);
						}else{
							@SuppressWarnings("rawtypes")
							Response resp = new Response();
							AbstractResults abstractResult = new AbstractResults();
							abstractResult.setResultId(-1);
							abstractResult.setResultMsgAbs("INVALID TOKEN");
							resp.setAbstractResult(abstractResult);
							
							log.error(abstractResult.getResultMsgAbs());
							// Using json to response
							String json = new Gson().toJson(resp);
							response.getWriter().write(json);
						}
					}else{
						// Check if session is avaible for console users
						if (((HttpServletRequest) sRequest).getSession().getAttribute("user") == null) {
							log.error("[doFilter] Consola. Error trying to Sending params and access information service without session. Anulating request...");
							
							@SuppressWarnings("rawtypes")
							Response resp = new Response();
							AbstractResults abstractResult = new AbstractResults();
							abstractResult.setResultId(-1);
							abstractResult.setResultMsgAbs("NO SESSION FOR USER CONSOLE WITH PARAM");
							resp.setAbstractResult(abstractResult);
							
							// Using json to response
							String json = new Gson().toJson(resp);
							response.getWriter().write(json);
							
						} else{
							log.warn("[doFilter] Consola. Forwading request...");
							filterChain.doFilter(myRequestWrapper, sResponse);	
						}
					}
					
					
				}else{
					@SuppressWarnings("rawtypes")
					Response resp = new Response();
					AbstractResults abstractResult = new AbstractResults();
					abstractResult.setResultId(-1);
					abstractResult.setResultMsgAbs("THERE ARE NOT AVAILABLE SESSIONS ");
					resp.setAbstractResult(abstractResult);
					
					log.error(abstractResult.getResultMsgAbs());
					// Using json to response
					String json = new Gson().toJson(resp);
					response.getWriter().write(json);
				}
				
			}else
			// Check if session is avaible
			if (((HttpServletRequest) sRequest).getSession().getAttribute("user") == null) {
				log.error("[doFilter] Consola. Error trying to access information service without session. Anulating request...");
				
				@SuppressWarnings("rawtypes")
				Response resp = new Response();
				AbstractResults abstractResult = new AbstractResults();
				abstractResult.setResultId(-1);
				abstractResult.setResultMsgAbs("NO SESSION FOR USER CONSOLE");
				resp.setAbstractResult(abstractResult);
				
				// Using json to response
				String json = new Gson().toJson(resp);
				response.getWriter().write(json);
				
			} else{
				log.warn("[doFilter] Consola. Forwading request...");
				filterChain.doFilter(myRequestWrapper, sResponse);	
			}
		}catch (NullPointerException e) {
			log.error("Algo se fue nulo",e);
		} 
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}