package com.gmodelo.cyclicinventories.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gmodelo.cyclicinventories.beans.ExplosionDetailByPackingRule;
import com.gmodelo.cyclicinventories.beans.PackingRule;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.PackingRulesWorkService;

@Path("/services/PackingRuleService")
public class PackingRuleService {
	
	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getPackingRules")
	public Response<List<PackingRule>> getPackingRules(Request request){
		return new PackingRulesWorkService().getPackingRules(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getPackingRuleByMatnr")
	public Response<List<ExplosionDetailByPackingRule>> getPackingRuleByMatnr(Request request){
		return new PackingRulesWorkService().getPackingRuleByMatnr(request);
	}
		
}
