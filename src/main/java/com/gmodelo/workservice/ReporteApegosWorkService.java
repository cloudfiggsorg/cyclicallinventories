package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Logger;

import com.gmodelo.beans.ApegosBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.ReporteApegosDao;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ReporteApegosWorkService {
	
	private Logger log = Logger.getLogger(ReporteApegosWorkService.class.getName());
	Gson gson = new Gson();
	
	public Response<List<ApegosBean>> getReporteApegos(Request request) {
		log.info("[getReporteApegosService] " + request.toString());
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

		return new ReporteApegosDao().getReporteApegos(apegosBean, searchFilter);
	}


}
