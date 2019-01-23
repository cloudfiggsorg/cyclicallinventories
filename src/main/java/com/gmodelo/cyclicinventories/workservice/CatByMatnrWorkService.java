package com.gmodelo.cyclicinventories.workservice;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.CatByMatnr;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.CatByMatnrDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class CatByMatnrWorkService {

	private Logger log = Logger.getLogger(CountsWorkService.class.getName());
	
	@SuppressWarnings("rawtypes")
	public Response<Object> saveMatnrByCat(Request request, String userId) {
		log.info("[saveMatnrByCatWS] " + request.toString());
		Response<Object> res = new Response<>();		
		Type listType = new TypeToken<ArrayList<CatByMatnr>>(){}.getType();
		List<CatByMatnr> lsCatByMatnr = null;		
		
		try {
			lsCatByMatnr = new Gson().fromJson(new Gson().toJson(request.getLsObject()), listType);
			log.info("[saveMatnrByCatWS] Fue Objeto: " + lsCatByMatnr.toString());
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[saveMatnrByCatWS] Error al pasar de Json a List<ContingencyTaskBean>");
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new CatByMatnrDao().saveCatByMatrnSAP(lsCatByMatnr, userId);
	}
}
