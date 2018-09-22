package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.LgTypIMBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.LgTypIMDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LgTypIMWorkService {
	
	private Logger log = Logger.getLogger(LgTypIMWorkService.class.getName());
	Gson gson = new Gson();

	public Response<LgTypIMBean> saveLgTypIM(Request request, User user) {
		
		log.info("[addLgTypWS] " + request.toString());
		LgTypIMBean lgTypIMBean;
		Response<LgTypIMBean> res = new Response<LgTypIMBean>();
		
		try {								
			lgTypIMBean = gson.fromJson(gson.toJson(request.getLsObject()), LgTypIMBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[addLgTypWS] Error al pasar de Json a RouteBean");
			lgTypIMBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new LgTypIMDao().saveLgTypIM(lgTypIMBean, user.getEntity().getIdentyId());

	}
	
	public Response<List<LgTypIMBean>> getLgTypsIM(Request request) {
		
		log.info("[getLgTypIMService] " + request.toString());
		LgTypIMBean lgTypIMBean = null;
		String searchFilter = null;
		Response<List<LgTypIMBean>> res = new Response<List<LgTypIMBean>>();
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			try {
				
				lgTypIMBean = gson.fromJson(gson.toJson(request.getLsObject()), LgTypIMBean.class);

				log.info("Fue objeto");
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString();
				log.info("Fue cadena");
			}
		} else {
			searchFilter = "";
		}

		return new LgTypIMDao().getLgTypsIM(lgTypIMBean, searchFilter);
	}

	public Response<Object> deleteLgTypsIM(Request request) {

		log.info("[deleteRouteWS] " + request.toString());
		String arrayToDelete;
		arrayToDelete = request.getLsObject().toString();
		return new LgTypIMDao().deleteLgTypIM(arrayToDelete);
	}
	
}
