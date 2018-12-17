package com.gmodelo.cyclicinventories.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.ZoneBean;
import com.gmodelo.cyclicinventories.workservice.ZoneWorkService;

@Path("/services/ZoneService")
public class ZoneService {

	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getZone")
	public Response<List<ZoneBean>> getZone(Request request){
		return new ZoneWorkService().getLgortByZone(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/validateZone")
	public Response<List<ZoneBean>> validateZone(Request request){
		return new ZoneWorkService().validateZone(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addZone")
	public Response<ZoneBean> addZone(Request request){
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new ZoneWorkService().addZone(request, user);
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteZone")
	public Response<Object> deleteZone(Request request){
		return new ZoneWorkService().deleteZone(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getZones")
	public Response<List<ZoneBean>> getZones(Request request){
		return new ZoneWorkService().getZones(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getZoneOnly")
	public Response<List<ZoneBean>> getZoneOnly(Request request){
		return new ZoneWorkService().getZoneOnly(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/validateZonePositions")
	public Response<ZoneBean> validateZonePositions(Request request){
		return new ZoneWorkService().validateZonePositions(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getZoneByLgortAndWerk")
	public Response<List<ZoneBean>> getZoneByLgortAndWerk(Request request){
		return new ZoneWorkService().getZoneByLgortAndWerk(request);
	}
		
}
