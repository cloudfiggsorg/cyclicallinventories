package com.gmodelo.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gmodelo.beans.ApegosBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TareasTiemposLgplaBean;
import com.gmodelo.workservice.ReporteApegosWorkService;
import com.gmodelo.workservice.ReporteTiemposTareasLgplaWorkService;

@Path("/services/ReporteTareasTiemposLgplaService")
public class ReporteTareasTiemposLgplaService {
	
	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getReporteTareasTiemposLgpla")
	public Response<List<TareasTiemposLgplaBean>> getReporteTareasTiempos(Request request) {
		return new ReporteTiemposTareasLgplaWorkService().getReporteTiemposTareas(request);
	}

}
