package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Logger;

import com.gmodelo.beans.ReporteConteosBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.ReporteConteosDao;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ReporteConteosWorkService {
	
	private Logger log = Logger.getLogger(ReporteConteosWorkService.class.getName());
	Gson gson = new Gson();
	
	public Response<List<ReporteConteosBean>> getReporteConteos(Request request) {
		log.info("[getReporteConteosService] " + request.toString());
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

		return new ReporteConteosDao().getReporteConteos(bean, searchFilter);
	}


}
