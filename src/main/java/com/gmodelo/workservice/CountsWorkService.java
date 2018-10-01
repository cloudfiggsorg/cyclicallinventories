package com.gmodelo.workservice;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteUserBean;
import com.gmodelo.dao.CountsDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class CountsWorkService {

	private Logger log = Logger.getLogger( CountsWorkService.class.getName());
	Gson gson = new Gson();
	
	public Response<Object> addCount(Request request, HttpSession httpSession) {
		log.info("[addCountWS] " + request.toString());
		RouteUserBean routeBean = null;
		User user = (User) httpSession.getAttribute("user");
		
		Response<Object> res = new Response<Object>();
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			try {
				routeBean = gson.fromJson(gson.toJson(request.getLsObject()), RouteUserBean.class);
				log.info("[addCountWS] Fue Objeto: "+ routeBean.toString());
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

		return new CountsDao().addCount(routeBean, user);

	}

}
