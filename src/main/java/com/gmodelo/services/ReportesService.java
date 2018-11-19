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
import com.gmodelo.beans.ReporteCalidadBean;
import com.gmodelo.beans.ReporteConteosBean;
import com.gmodelo.beans.ReporteDocInvBeanHeader;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TareasTiemposLgplaBean;
import com.gmodelo.beans.TareasTiemposZonasBean;
import com.gmodelo.workservice.ReportesWorkService;

@Path("/services/ReportesService")
public class ReportesService {

	@Context
	private HttpServletRequest httpRequest;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getReporteApegos")
	public Response<List<ApegosBean>> getReporteApegos(Request request) {
		return new ReportesWorkService().getReporteApegos(request);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getReporteConteos")
	public Response<List<ReporteConteosBean>> getReporteConteos(Request request) {
		return new ReportesWorkService().getReporteConteos(request);
	}

	@POST//Cons SAP
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getReporteDocInv")
	public Response<ReporteDocInvBeanHeader> getReporteDocInv(Request request) {
		return new ReportesWorkService().getReporteDocInv(request);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getReporteTareasTiemposLgpla")
	public Response<List<TareasTiemposLgplaBean>> getReporteTareasTiempos(Request request) {
		return new ReportesWorkService().getReporteTiemposTareasLgpla(request);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getReporteTareasTiemposZonas")
	public Response<List<TareasTiemposZonasBean>> getReporteTareasTiemposZonas(Request request) {
		return new ReportesWorkService().getReporteTiemposTareasZonas(request);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getReporteCalidad")
	public Response<List<ReporteCalidadBean>> getReporteCalidad(Request request) {
		return new ReportesWorkService().getReporteCalidad(request);
	}
}
