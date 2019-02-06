package com.gmodelo.cyclicinventories.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gmodelo.cyclicinventories.beans.ApegosBean;
import com.gmodelo.cyclicinventories.beans.ConciAccntReportBean;
import com.gmodelo.cyclicinventories.beans.ConciliationsIDsBean;
import com.gmodelo.cyclicinventories.beans.DocInvBeanHeaderSAP;
import com.gmodelo.cyclicinventories.beans.ProductivityBean;
import com.gmodelo.cyclicinventories.beans.ReporteConteosBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.ReportesWorkService;

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
	public Response<DocInvBeanHeaderSAP> getReporteDocInvSAP(Request request) {
		return new ReportesWorkService().getReporteDocInvSAP(request);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCountedProductivity")
	public Response<List<ProductivityBean>> getReporteTareasTiempos(Request request) {
		return new ReportesWorkService().getCountedProductivity(request);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getUserProductivity")
	public Response<List<ProductivityBean>> getUserProductivity(Request request) {
		return new ReportesWorkService().getUserProductivity(request);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getReporteCalidad")
	public Response<List<ConciliationsIDsBean>> getReporteCalidad(Request request) {
		return new ReportesWorkService().getReporteCalidad(request);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getReporteConciAccntReport")
	public Response<List<ConciAccntReportBean>> getReporteConciAccntReport(Request request) {
		return new ReportesWorkService().getReporteConciAccntReport(request);
	}
	
	@POST//Cons SAP
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getReporteDocInvByLgpla")
	public Response<DocInvBeanHeaderSAP> getReporteDocInvSAPByLgpla(Request request) {
		return new ReportesWorkService().getReporteDocInvSAPByLgpla(request);
	}

}
