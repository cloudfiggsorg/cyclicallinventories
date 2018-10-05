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
import com.gmodelo.beans.TareasTiemposZonasBean;
import com.gmodelo.workservice.ReporteApegosWorkService;
import com.gmodelo.workservice.ReporteTiemposTareasLgplaWorkService;
import com.gmodelo.workservice.ReporteTiemposTareasZonasWorkService;

@Path("/services/ReporteTareasTiemposZonasService")
public class ReporteTareasTiemposZonasService {
	
	@Context
	private HttpServletRequest httpRequest;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getReporteTareasTiemposZonas")
	public Response<List<TareasTiemposZonasBean>> getReporteTareasTiemposZonas(Request request) {
		return new ReporteTiemposTareasZonasWorkService().getReporteTiemposTareasZonas(request);
	}

}
