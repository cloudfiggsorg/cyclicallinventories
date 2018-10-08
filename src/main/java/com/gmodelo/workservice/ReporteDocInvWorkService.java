package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Logger;

import com.gmodelo.beans.ReporteDocInvBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.ReporteDocInvDao;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ReporteDocInvWorkService {
	
	private Logger log = Logger.getLogger(ReporteDocInvWorkService.class.getName());
	Gson gson = new Gson();
	
	public Response<List<ReporteDocInvBean>> getReporteDocInv(Request request) {
		log.info("[getReporteDocInvService] " + request.toString());
		ReporteDocInvBean bean = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			try {
				bean = gson.fromJson(gson.toJson(request.getLsObject()), ReporteDocInvBean.class);

				log.info("Fue objeto");
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString();
				log.info("Fue cadena");
			}
		} else {
			searchFilter = "";
		}

		return new ReporteDocInvDao().getReporteDocInv(bean, searchFilter);
	}


}
