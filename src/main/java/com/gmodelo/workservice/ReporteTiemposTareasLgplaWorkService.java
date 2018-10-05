package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Logger;

import com.gmodelo.beans.TareasTiemposLgplaBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.ReporteApegosDao;
import com.gmodelo.dao.ReporteTiemposTareasLgplaDao;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ReporteTiemposTareasLgplaWorkService {
	
	private Logger log = Logger.getLogger(ReporteTiemposTareasLgplaWorkService.class.getName());
	Gson gson = new Gson();
	
	public Response<List<TareasTiemposLgplaBean>> getReporteTiemposTareas(Request request) {
		log.info("[getReporteTiemposTareasService] " + request.toString());
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

		return new ReporteTiemposTareasLgplaDao().getReporteTareasTiempos(tareasBean, searchFilter);
	}


}
