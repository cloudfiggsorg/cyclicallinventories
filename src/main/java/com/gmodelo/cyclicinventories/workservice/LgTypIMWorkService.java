package com.gmodelo.cyclicinventories.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.LgTypIMBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.LgTypIMDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LgTypIMWorkService {

	private Logger log = Logger.getLogger(LgTypIMWorkService.class.getName());
	private Gson gson = new Gson();

	public Response<LgTypIMBean> saveLgTypIM(Request request, User user) {

		log.info("[addLgTypWS] " + request.toString());
		LgTypIMBean lgTypIMBean;
		Response<LgTypIMBean> res = new Response<>();

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
	
	public Response<List<LgTypIMBean>> getLgTypsOnly(Request request) {

		log.info("[getLgTypIMService] " + request.toString());
		LgTypIMBean lgTypIMBean = null;
		Response<List<LgTypIMBean>> res = new Response<>();
		
		try {
			
			lgTypIMBean = gson.fromJson(gson.toJson(request.getLsObject()), LgTypIMBean.class);
			log.info("[getLgTypsIM] Fue objeto");
		} catch (JsonSyntaxException e) {
			
			log.log(Level.SEVERE, "[getLgTypsIM] Error al pasar de Json a LgTypIMBean");
			lgTypIMBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new LgTypIMDao().getLgTypsOnly(lgTypIMBean);
	}

	public Response<List<LgTypIMBean>> getLgTypsIM(Request request) {

		log.info("[getLgTypIMService] " + request.toString());
		LgTypIMBean lgTypIMBean = null;
		Response<List<LgTypIMBean>> res = new Response<>();
		
		try {
			lgTypIMBean = gson.fromJson(gson.toJson(request.getLsObject()), LgTypIMBean.class);
			log.info("[getLgTypsIM] Fue objeto");
		} catch (JsonSyntaxException e) {
			
			log.log(Level.SEVERE, "[getLgTypIMService] Error al pasar de Json a LgTypIMBean");
			lgTypIMBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new LgTypIMDao().getLgTypsIM(lgTypIMBean);
	}
	
	public Response<Object> deleteLgTypsIM(Request request) {

		log.info("[deleteRouteWS] " + request.toString());
		String arrayToDelete;
		arrayToDelete = request.getLsObject().toString();
		return new LgTypIMDao().deleteLgTypIM(arrayToDelete);
	}

}
