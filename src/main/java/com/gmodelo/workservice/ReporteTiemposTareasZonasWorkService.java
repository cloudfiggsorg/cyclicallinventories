package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Logger;

import com.gmodelo.beans.TareasTiemposZonasBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.ReporteApegosDao;
import com.gmodelo.dao.ReporteTiemposTareasLgplaDao;
import com.gmodelo.dao.ReporteTiemposTareasZonasDao;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ReporteTiemposTareasZonasWorkService {
	
	private Logger log = Logger.getLogger(ReporteTiemposTareasZonasWorkService.class.getName());
	Gson gson = new Gson();
	
	public Response<List<TareasTiemposZonasBean>> getReporteTiemposTareasZonas(Request request) {
		log.info("[getReporteTiemposTareasZonasService] " + request.toString());
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

		return new ReporteTiemposTareasZonasDao().getReporteTareasTiemposZonas(tareasBean, searchFilter);
	}


}
