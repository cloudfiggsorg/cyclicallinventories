package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.ApegosBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.DocInvBeanHeaderSAP;
import com.gmodelo.beans.ReporteCalidadBean;
import com.gmodelo.beans.ReporteConteosBean;
import com.gmodelo.beans.ReporteDocInvBeanHeader;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TareasTiemposLgplaBean;
import com.gmodelo.beans.TareasTiemposZonasBean;
import com.gmodelo.dao.ReportesDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;

public class ReportesWorkService {

	private Logger log = Logger.getLogger(ReportesWorkService.class.getName());
	Gson gson = new Gson();

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
		AbstractResultsBean result = new AbstractResultsBean();
		DocInvBean bean = null;
		try {
			log.info("[ReporteWorkService getReporteDocInv] try");
			bean = gson.fromJson(gson.toJson(request.getLsObject()), DocInvBean.class);
			response = new ReportesDao().getConsDocInv(bean);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[ReporteWorkService getReporteDocInv] catch", e);
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		}
		response.setAbstractResult(result);
		return response;
	}

	public Response<List<TareasTiemposLgplaBean>> getReporteTiemposTareasLgpla(Request request) {
		log.info("[getReporteTiemposTareasWorkService] " + request.toString());
		Response<List<TareasTiemposLgplaBean>> response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		TareasTiemposLgplaBean tareasBean = null;
		try {
			log.info("[getReporteTiemposTareasWorkService] try");
			tareasBean = gson.fromJson(gson.toJson(request.getLsObject()), TareasTiemposLgplaBean.class);
			response = new ReportesDao().getReporteTareasTiemposLgpla(tareasBean);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[getReporteTiemposTareasWorkService] catch", e );
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		}
		response.setAbstractResult(result);
		return response;
	}

	public Response<List<TareasTiemposZonasBean>> getReporteTiemposTareasZonas(Request request) {
		log.info("[getReporteTiemposTareasZonasWorkService] " + request.toString());
		Response<List<TareasTiemposZonasBean>> response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		TareasTiemposZonasBean tareasBean = null;
		try {
			log.info("[getReporteTiemposTareasZonasWorkService] try");
			tareasBean = gson.fromJson(gson.toJson(request.getLsObject()), TareasTiemposZonasBean.class);
			response = new ReportesDao().getReporteTareasTiemposZonas(tareasBean); 
		} catch (Exception e) {
			log.log(Level.SEVERE, "[getReporteTiemposTareasZonasWorkService] catch", e );
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		}
		response.setAbstractResult(result);
		return response;
	}

	public Response<List<ReporteCalidadBean>> getReporteCalidad(Request request) {
		log.info("[getReporteCalidadWorkService] " + request.toString());
		Response<List<ReporteCalidadBean>> response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		ReporteCalidadBean bean = null;
		try {
			log.info("[getReporteCalidadWorkService] try");
			bean = gson.fromJson(gson.toJson(request.getLsObject()), ReporteCalidadBean.class);
			response = new ReportesDao().getReporteCalidad(bean);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[getReporteCalidadWorkService] catch", e);
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		}
		response.setAbstractResult(result);
		return response;
	}
}
