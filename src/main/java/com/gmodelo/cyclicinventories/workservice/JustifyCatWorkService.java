package com.gmodelo.cyclicinventories.workservice;

import java.util.List;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.JustifyCat;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.JustifyCatDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;

public class JustifyCatWorkService {
	
	private Logger log = Logger.getLogger(ZoneWorkService.class.getName());
	private Gson gson = new Gson();

	public Response<JustifyCat> addJustify(Request<?> request, String userId) {
		log.info("[addJustifyWS] " + request.toString());
		JustifyCat justify = null;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<JustifyCat> res = new Response<>();
		try {
			justify = gson.fromJson(gson.toJson(request.getLsObject()), JustifyCat.class);
			res = new JustifyCatDao().addJustify(justify, userId);
		} catch (Exception e) {
			log.info("[addJustifyWS] Error al convertir a objeto.");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);			
		}

		return res;
	}

	public Response<Object> deleteJustifies(Request<?> request) {
		log.info("[deleteJustifiesWS] " + request.toString());
		String jsIds = null;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<Object> res = new Response<>();
		try {
			jsIds = request.getLsObject().toString();
			res = new JustifyCatDao().deleteJustify(jsIds);
		} catch (Exception e) {
			log.info("[deleteJustifiesWS] Error al obtener los Ids");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);			
		}

		return res;
	}
	
	public Response<List<JustifyCat>> getJustifies() {
		
		log.info("[getJustifiesWS]... ");
		Response<List<JustifyCat>> res = new JustifyCatDao().getJustifies();
		return res;
	}

}
