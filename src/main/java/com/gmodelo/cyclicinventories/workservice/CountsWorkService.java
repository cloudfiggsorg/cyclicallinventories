package com.gmodelo.cyclicinventories.workservice;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.RouteUserBean;
import com.gmodelo.cyclicinventories.dao.CountsDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class CountsWorkService {

	private Logger log = Logger.getLogger(CountsWorkService.class.getName());
	private Gson gson = new Gson();

	@SuppressWarnings("rawtypes")
	public Response<Object> addCount(Request request) {
		log.info("[addCountWS] " + request.toString());
		RouteUserBean routeBean = null;

		Response<Object> res = new Response<Object>();
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			try {
				routeBean = gson.fromJson(gson.toJson(request.getLsObject()), RouteUserBean.class);
				log.info("[addCountWS] Fue Objeto: " + routeBean.toString());
				System.out.println(routeBean.getPositions().get(0).toString());
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE, "[addCountWS] Error al pasar de Json a RouteUserBean");
				routeBean = null;
				AbstractResultsBean abstractResult = new AbstractResultsBean();
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		} else {
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs("El json de tipo RouteUserBean viene vacio o nulo");
			res.setAbstractResult(abstractResult);
			log.log(Level.SEVERE, "[addCountWS]  " + abstractResult.getResultMsgAbs());
			return res;
		}

		return new CountsDao().addCount(routeBean, request.getTokenObject().getLoginId());

	}

}
