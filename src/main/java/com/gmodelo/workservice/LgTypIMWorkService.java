package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.LgTypIM;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.LgTypIMDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LgTypIMWorkService {
	
	private Logger log = Logger.getLogger(LgTypIMWorkService.class.getName());
	Gson gson = new Gson();

	public Response<LgTypIM> saveLgTypIM(Request request, User user) {
		
		log.info("[addLgTypWS] " + request.toString());
		LgTypIM lgTypIM;
		Response<LgTypIM> res = new Response<LgTypIM>();
		
		try {								
			lgTypIM = gson.fromJson(gson.toJson(request.getLsObject()), LgTypIM.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[addLgTypWS] Error al pasar de Json a RouteBean");
			lgTypIM = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new LgTypIMDao().saveLgTypIM(lgTypIM, user.getEntity().getIdentyId());

	}
	
	public Response<List<LgTypIM>> getLgTypsIM(Request request) {
		
		log.info("[getLgTypIMService] " + request.toString());
		LgTypIM lgTypIM = null;
		String searchFilter = null;
		Response<List<LgTypIM>> res = new Response<List<LgTypIM>>();
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			try {
				
				lgTypIM = gson.fromJson(gson.toJson(request.getLsObject()), LgTypIM.class);

				log.info("Fue objeto");
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString();
				log.info("Fue cadena");
			}
		} else {
			searchFilter = "";
		}

		return new LgTypIMDao().getLgTypsIM(lgTypIM, searchFilter);
	}

	public Response<Object> deleteLgTypsIM(Request request) {

		log.info("[deleteRouteWS] " + request.toString());
		String arrayToDelete;
		arrayToDelete = request.getLsObject().toString();
		return new LgTypIMDao().deleteLgTypIM(arrayToDelete);
	}
	
}
