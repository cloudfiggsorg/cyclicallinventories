package com.gmodelo.cyclicinventories.filters;

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
import javax.ws.rs.core.Context;

import org.jboss.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.gmodelo.cyclicinventories.workservice.LoginWorkService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@WebFilter(urlPatterns = "/services/*")
public class FServices implements Filter {

	@Context
	private HttpSession session;
	private Logger log = Logger.getLogger(FServices.class.getName());

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
				log.error("Error trying to access information service without POST method. Anulating request...");
				@SuppressWarnings("rawtypes")
				Response resp = new Response();
				AbstractResultsBean abstractResult = new AbstractResultsBean();
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
				
				@SuppressWarnings("rawtypes")
				Request req = null;
				try {
					req = new Gson().fromJson(body, Request.class) ;
				} catch (JsonSyntaxException e) {
					log.error("[doFilter] Error al pasar de Json a Request");
					req = null;
				}
				
				if(req.getTokenObject() != null && req.getTokenObject().getLoginId() != null && req.getTokenObject().getLoginPass() != null){
					
					Response res = new LoginWorkService().login(req.getTokenObject(), (HttpServletRequest)sRequest, session);
					if(res.getAbstractResult().getResultId() == 1 || res.getAbstractResult().getResultId() == 2){
						log.info("[doFilter] Android. Renovated session!!. Forwading request..");
							filterChain.doFilter(myRequestWrapper, sResponse);
							
					}else{
						@SuppressWarnings("rawtypes")
						Response resp = new Response();
						AbstractResultsBean abstractResult = new AbstractResultsBean();
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
					if(req.getTokenObject() != null && req.getTokenObject().getRelationUUID() != null){
						if(HttpSessionCollector.sessions.containsKey(req.getTokenObject().getRelationUUID())){
							log.info("[doFilter] Android. Token exists. Forwading request...");
							filterChain.doFilter(myRequestWrapper, sResponse);
						}else{
							@SuppressWarnings("rawtypes")
							Response resp = new Response();
							AbstractResultsBean abstractResult = new AbstractResultsBean();
							abstractResult.setResultId(ReturnValues.IINVALIDTOKEN);
							abstractResult.setResultMsgAbs(ReturnValues.SINVALIDTOKEN);
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
							AbstractResultsBean abstractResult = new AbstractResultsBean();
							abstractResult.setResultId(ReturnValues.IUSERNOSESSION);
							abstractResult.setResultMsgAbs("NO SESSION FOR USER CONSOLE WITH PARAM");
							resp.setAbstractResult(abstractResult);
							
							// Using json to response
							String json = new Gson().toJson(resp);
							response.getWriter().write(json);
							
						} else{
							log.info("[doFilter] Consola. Forwading request...");
							filterChain.doFilter(myRequestWrapper, sResponse);	
						}
					}
					
					
				}else{
					@SuppressWarnings("rawtypes")
					Response resp = new Response();
					AbstractResultsBean abstractResult = new AbstractResultsBean();
					abstractResult.setResultId(ReturnValues.IUSERNOSESSION);
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
				AbstractResultsBean abstractResult = new AbstractResultsBean();
				abstractResult.setResultId(ReturnValues.IUSERNOSESSION);
				abstractResult.setResultMsgAbs("NO SESSION FOR USER CONSOLE");
				resp.setAbstractResult(abstractResult);
				
				// Using json to response
				String json = new Gson().toJson(resp);
				response.getWriter().write(json);
				
			} else{
				log.info("[doFilter] Consola. Forwading request...");
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