package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.ApegosBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.ReporteCalidadBean;
import com.gmodelo.beans.ReporteConteosBean;
import com.gmodelo.beans.ReporteDocInvBean;
import com.gmodelo.beans.ReporteDocInvBeanHeader;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TareasTiemposLgplaBean;
import com.gmodelo.beans.TareasTiemposZonasBean;
import com.gmodelo.dao.ReportesDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ReportesWorkService {

	private Logger log = Logger.getLogger(ReportesWorkService.class.getName());
	Gson gson = new Gson();

	public Response<List<ApegosBean>> getReporteApegos(Request request) {
		log.info("[getReporteApegosWorkService] " + request.toString());
		ApegosBean apegosBean = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			try {
				apegosBean = gson.fromJson(gson.toJson(request.getLsObject()), ApegosBean.class);

				log.info("Fue objeto");
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString();
				log.info("Fue cadena");
			}
		} else {
			searchFilter = "";
		}

		return new ReportesDao().getReporteApegos(apegosBean, searchFilter);
	}

	public Response<List<ReporteConteosBean>> getReporteConteos(Request request) {

		log.info("[getReporteConteosWorkService] " + request.toString());
		ReporteConteosBean bean = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			try {
				bean = gson.fromJson(gson.toJson(request.getLsObject()), ReporteConteosBean.class);

				log.info("Fue objeto");
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString();
				log.info("Fue cadena");
			}
		} else {
			searchFilter = "";
		}

		return new ReportesDao().getReporteConteos(bean, searchFilter);

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

	public Response<List<TareasTiemposLgplaBean>> getReporteTiemposTareasLgpla(Request request) {
		log.info("[getReporteTiemposTareasWorkService] " + request.toString());
		TareasTiemposLgplaBean tareasBean = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			try {
				tareasBean = gson.fromJson(gson.toJson(request.getLsObject()), TareasTiemposLgplaBean.class);

				log.info("Fue objeto");
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString();
				log.info("Fue cadena");
			}
		} else {
			searchFilter = "";
		}

		return new ReportesDao().getReporteTareasTiemposLgpla(tareasBean, searchFilter);
	}

	public Response<List<TareasTiemposZonasBean>> getReporteTiemposTareasZonas(Request request) {
		log.info("[getReporteTiemposTareasZonasWorkService] " + request.toString());
		TareasTiemposZonasBean tareasBean = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			try {
				tareasBean = gson.fromJson(gson.toJson(request.getLsObject()), TareasTiemposZonasBean.class);

				log.info("Fue objeto");
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString();
				log.info("Fue cadena");
			}
		} else {
			searchFilter = "";
		}

		return new ReportesDao().getReporteTareasTiemposZonas(tareasBean, searchFilter);
	}

	public Response<List<ReporteCalidadBean>> getReporteCalidad(Request request) {
		log.info("[getReporteCalidadWorkService] " + request.toString());
		ReporteCalidadBean bean = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			System.out.println("bean wos: " +bean);
			try {System.out.println("entro al try");
				
				bean = gson.fromJson(gson.toJson(request.getLsObject()), ReporteCalidadBean.class);

				log.info("Fue objeto");
			} catch (JsonSyntaxException e) {
				System.out.println("entro al cathc");
				searchFilter = request.getLsObject().toString();
				log.info("Fue cadena");
			}
		} else {
			searchFilter = "";
		}

		return new ReportesDao().getReporteCalidad(bean, searchFilter);
	}
}
