package com.gmodelo.cyclicinventories.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.LgortBeanView;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.LgortDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LgortWorkService {

	private Logger log = Logger.getLogger(LgortWorkService.class.getName());
	private Gson gson = new Gson();

	public Response<List<LgortBeanView>> getLgortByWerks(Request request) {

		log.info("[LgortWorkService] " + request.toString());
		LgortBeanView lgortBean;
		Response<List<LgortBeanView>> res = new Response<List<LgortBeanView>>();

		try {
			lgortBean = gson.fromJson(gson.toJson(request.getLsObject()), LgortBeanView.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[LgortWorkService] Error al pasar de Json a LgortBean");
			lgortBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new LgortDao().getLgortByWerks(lgortBean);

	}

	public Response<List<LgortBeanView>> getNgorts(Request request) {

		log.info("[NgortWorkService] " + request.toString());
		LgortBeanView lgortBeanView;
		Response<List<LgortBeanView>> res = new Response<List<LgortBeanView>>();

		try {
			lgortBeanView = gson.fromJson(gson.toJson(request.getLsObject()), LgortBeanView.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[NgortWorkService] Error al pasar de Json a NgortBean");
			lgortBeanView = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new LgortDao().getNgorts(lgortBeanView);

	}

	public Response<List<LgortBeanView>> getNgortsIM(Request request) {
		log.info("[NgortWorkService] " + request.toString());
		LgortBeanView lgortBeanView;
		Response<List<LgortBeanView>> res = new Response<List<LgortBeanView>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		try {
			lgortBeanView = gson.fromJson(gson.toJson(request.getLsObject()), LgortBeanView.class);
			res = new LgortDao().getNgortsIM(lgortBeanView);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[NgortWorkService] Error al pasar de Json a NgortBean");
			lgortBeanView = null;
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
		}
		return res;
	}

}
