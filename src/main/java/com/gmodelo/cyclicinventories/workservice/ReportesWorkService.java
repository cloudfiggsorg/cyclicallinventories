package com.gmodelo.cyclicinventories.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.ApegosBean;
import com.gmodelo.cyclicinventories.beans.ConciliationsIDsBean;
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.DocInvBeanHeaderSAP;
import com.gmodelo.cyclicinventories.beans.ProductivityBean;
import com.gmodelo.cyclicinventories.beans.ReporteCalidadBean;
import com.gmodelo.cyclicinventories.beans.ReporteConteosBean;
import com.gmodelo.cyclicinventories.beans.ReporteDocInvBeanHeader;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.ConciliacionDao;
import com.gmodelo.cyclicinventories.dao.ReportesDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;

public class ReportesWorkService {

	private Logger log = Logger.getLogger(ReportesWorkService.class.getName());
	private Gson gson = new Gson();

	public Response<List<ApegosBean>> getReporteApegos(Request request) {
		log.info("[getReporteApegosWorkService] " + request.toString());
		Response<List<ApegosBean>> response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		ApegosBean apegosBean = null;
		try {
			log.info("[getReporteApegosWorkService] try");
			apegosBean = gson.fromJson(gson.toJson(request.getLsObject()), ApegosBean.class);
			response = new ReportesDao().getReporteApegos(apegosBean);
		} catch (Exception e) {
			log.log(Level.SEVERE, "getReporteApegosWorkService] catch", e);
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		}
		response.setAbstractResult(result);
		return response;
	}

	public Response<List<ReporteConteosBean>> getReporteConteos(Request request) {
		log.info("[getReporteConteosWorkService] " + request.toString());
		Response<List<ReporteConteosBean>> response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		ReporteConteosBean bean = null;
		try {
			log.info("[getReporteConteosWorkService] try");
			bean = gson.fromJson(gson.toJson(request.getLsObject()), ReporteConteosBean.class);
			response = new ReportesDao().getReporteConteos(bean);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[getReporteConteosWorkService] catch", e);
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		}
		response.setAbstractResult(result);
		return response;
	}

	public Response<ReporteDocInvBeanHeader> getReporteDocInv(Request request) {
		log.info("[ReporteWorkService getReporteDocInv] " + request.toString());
		Response<ReporteDocInvBeanHeader> response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		DocInvBean bean = null;
		try {
			log.info("[ReporteWorkService getReporteDocInv] try");
			bean = gson.fromJson(gson.toJson(request.getLsObject()), DocInvBean.class);
			response = new ReportesDao().getReporteDocInv(bean);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[ReporteWorkService getReporteDocInv] catch", e);
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		}
		response.setAbstractResult(result);
		return response;
	}
	
	public Response<DocInvBeanHeaderSAP> getReporteDocInvSAP(Request request) {
		log.info("[ReporteWorkService getReporteDocInv] " + request.toString());
		Response<DocInvBeanHeaderSAP> response = new Response<>();		
		DocInvBean bean = null;
		try {
			log.info("[ReporteWorkService getReporteDocInv] try");
			bean = gson.fromJson(gson.toJson(request.getLsObject()), DocInvBean.class);
			response = new ReportesDao().getConsDocInv(bean);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[ReporteWorkService getReporteDocInv] catch", e);
			AbstractResultsBean result = new AbstractResultsBean();
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
			response.setAbstractResult(result);
		}
		return response;
	}

	public Response<List<ProductivityBean>> getCountedProductivity(Request request) {
		log.info("[getCountedProductivityWorkService] " + request.toString());
		Response<List<ProductivityBean>> response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		ProductivityBean tareasBean = null;
		try {
			log.info("[getCountedProductivityWorkService] try");
			tareasBean = gson.fromJson(gson.toJson(request.getLsObject()), ProductivityBean.class);
			response = new ReportesDao().getCountedProductivityDao(tareasBean);
		} catch (Exception e) {
			log.log(Level.SEVERE, "getCountedProductivityTareasWorkService] catch", e );
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
			Response<List<ProductivityBean>> badResponse = new Response<>();
			badResponse.setAbstractResult(result);
			return badResponse ;
		}
		return response;
	}

	public Response<List<ProductivityBean>> getUserProductivity(Request request) {
		log.info("[getReporteTiemposTareasZonasWorkService] " + request.toString());
		Response<List<ProductivityBean>> response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		ProductivityBean tareasBean = null;
		try {
			log.info("[getUserProductivityWorkService] try");
			tareasBean = gson.fromJson(gson.toJson(request.getLsObject()), ProductivityBean.class);
			response = new ReportesDao().getUserProductivityDao(tareasBean); 
		} catch (Exception e) {
			log.log(Level.SEVERE, "[getUserProductivityWorkService] catch", e );
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
			Response<List<ProductivityBean>> badResponse = new Response<>();
			badResponse.setAbstractResult(result);
			return badResponse ;
		}
		return response;
	}

	public Response<List<ConciliationsIDsBean>> getReporteCalidad(Request<List<Object>> request) {
		log.info("[getReporteCalidadWorkService] " + request.toString());
		String bukrs = (String) request.getLsObject().get(0);
		String werks = (String) request.getLsObject().get(1);
		return new ReportesDao().getReporteCalidad(bukrs, werks);
	}
}
