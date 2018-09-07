package com.gmodelo.workservice;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteBean;
import com.gmodelo.beans.RouteUserBean;
import com.gmodelo.dao.RouteDao;
import com.gmodelo.dao.RouteUserDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class RouteWorkService {

	private Logger log = Logger.getLogger(RouteWorkService.class.getName());
	Gson gson = new Gson();

	public Response<RouteBean> addRoute(Request request, User user) {
		
		log.info("[addRouteWS] " + request.toString());
		RouteBean routeBean;
		Response<RouteBean> res = new Response<RouteBean>();
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			try {								
				routeBean = gson.fromJson(gson.toJson(request.getLsObject()), RouteBean.class);
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE, "[addRouteWS] Error al pasar de Json a RouteBean");
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
			abstractResult.setResultMsgAbs("El json de tipo RouteBean viene vacio o nulo");
			res.setAbstractResult(abstractResult);
			log.log(Level.SEVERE, "[addRouteWS]  " + abstractResult.getResultMsgAbs());
			return res;
		}

		return new RouteDao().addRoute(routeBean, user.getEntity().getIdentyId());

	}

	public Response<Object> deleteRoute(Request request) {

		log.info("[deleteRouteWS] " + request.toString());
		String arrayIdRoutes;
		arrayIdRoutes = request.getLsObject().toString();
		return new RouteDao().deleteRoute(arrayIdRoutes);
	}

	public Response<List<RouteBean>> getRoutes(Request request) {
		log.info("[getRoutesService] " + request.toString());
		RouteBean routeBean = null;
		String searchFilter = null;
		Response<List<RouteBean>> res = new Response<List<RouteBean>>();
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			try {
				routeBean = gson.fromJson(gson.toJson(request.getLsObject()), RouteBean.class);

				log.info("Fue objeto");
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString();
				log.info("Fue cadena");
			}
		} else {
			searchFilter = "";
		}

		return new RouteDao().getRoutes(routeBean, searchFilter);
	}

	public String getRoutesByUser(User user, HttpSession userSession) {

		Response<RouteUserBean> routeResponse = new Response<>();
		RouteUserDao routeDao = new RouteUserDao();
		AbstractResultsBean result = new AbstractResultsBean();
		result.setIntCom1(userSession.getMaxInactiveInterval());
		result.setStrCom1(userSession.getId());
		routeResponse.setAbstractResult(result);
		try {
			RouteUserBean route = routeDao.getRoutesByUser(user);
			if (route != null) {
				route.setPositions(routeDao.getPositions(route.getRouteId()));
				routeResponse.setLsObject(route);
			} else {
				result.setResultId(ReturnValues.IUSERNOTTASK);
				result.setResultMsgAbs("Tarea no encontrada para usuario: " + user.getEntity().getIdentyId());
			}
		} catch (SQLException e) {
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgGen(e.getMessage());
			log.log(Level.SEVERE, "[getRoutesByUserService] ",e);
		}
		
		return new Gson().toJson(routeResponse);
	}
}
