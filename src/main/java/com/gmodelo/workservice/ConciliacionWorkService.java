package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.ConciliacionBean;
import com.gmodelo.beans.ConciliationsIDsBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.ConciliacionDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ConciliacionWorkService {
	
	private Logger log = Logger.getLogger(ConciliacionWorkService.class.getName());
	Gson gson = new Gson();
	
	public Response<List<ConciliationsIDsBean>> getConciliationIDs(){
		return new ConciliacionDao().getConciliationIDs();
	}
	
	public Response<List<ConciliacionBean>> getConciliacion(Request request) {

		log.info("[getConciliacionWS] " + request.toString());
		ConciliacionBean tb = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		Response<List<ConciliacionBean>> res = new Response<List<ConciliacionBean>>();

		if (!req.isEmpty()) {
			try {
				tb = gson.fromJson(gson.toJson(request.getLsObject()), ConciliacionBean.class);
				log.info("[getConciliacionWS] Fue Objeto: " + tb);
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString().trim();
				log.info("[getConciliacionWS] jsyn Intentando por String ");
			}
		} else {
			searchFilter = "";
			log.info("[getConciliacionWS] Fue cadena vacía ");
			
		}
		return new ConciliacionDao().getConciliacion(tb, searchFilter);
	}

}
