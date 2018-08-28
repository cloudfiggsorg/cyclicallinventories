package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteBean;
import com.gmodelo.dao.RouteDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class RouteWorkService {

	private Logger log = Logger.getLogger(RouteWorkService.class.getName());

	public Response<Object> addRoute(Request request, User user){
		
		log.log(Level.WARNING,"[addRouteWS] "+request.toString());
		RouteBean routeBean;
		Response<Object> res = new Response<Object>();
		String req = request.getLsObject().toString().trim();
		if(!req.isEmpty()){
			try {
				routeBean = new Gson().fromJson(request.getLsObject().toString(), RouteBean.class);
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE,"[addRouteWS] Error al pasar de Json a ZoneBean");
				routeBean = null;
				AbstractResultsBean abstractResult = new AbstractResultsBean();
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}else{
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs("El json de tipo RouteBean viene vacio o nulo");
			res.setAbstractResult(abstractResult);
			log.log(Level.SEVERE,"[addRouteWS]  "+abstractResult.getResultMsgAbs());
			return res;
		}
		
		return new RouteDao().addRoute(routeBean, user.getEntity().getIdentyId());
		
	}
	public Response<Object> deleteRoute(Request request){
		
		log.log(Level.WARNING,"[deleteRouteWS] "+request.toString());
		String arrayIdRoutes;
		StringBuilder stringRoutes = new StringBuilder();
		Response<Object> res = new Response<Object>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		String req = request.getLsObject().toString().trim();
		if(!req.isEmpty()){
			try {
				arrayIdRoutes = request.getLsObject().toString();
				
				if(arrayIdRoutes == null || arrayIdRoutes.isEmpty()){
					abstractResult.setResultId(ReturnValues.IEXCEPTION);
					abstractResult.setResultMsgAbs("NULL OR EMPTY ARRAY");
					res.setAbstractResult(abstractResult);
					return res;
				}
				
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE,"[deleteRouteWS] Error al pasar de Json a RoutePositionBean");
				
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}else{
			
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs("Error al pasar de Json a RoutePositionBean");
			res.setAbstractResult(abstractResult);
			log.log(Level.SEVERE,"[deleteRouteWS] "+abstractResult.getResultMsgAbs());
			
			return res;
		}
		
		
	
		return new RouteDao().deleteRoute(stringRoutes.toString());
	
	}

	public Response<List<RouteBean>> getRoutes(Request request){
		log.log(Level.WARNING,"[getRoutesService] "+request.toString());
		RouteBean routeBean = null;
		String searchFilter = null;
		Response<List<RouteBean>> res = new Response<List<RouteBean>>();
		String req = request.getLsObject().toString().trim();
		if(!req.isEmpty()){
			try {
				routeBean = new Gson().fromJson(request.getLsObject().toString(), RouteBean.class) ;
				
				log.log(Level.WARNING,"Fue objeto");
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString();
				log.log(Level.WARNING,"Fue cadena");
			}
		}else{
			searchFilter = "";
		}
		
		return new RouteDao().getRoutes(routeBean,searchFilter);
	}

	public Response<List<RouteBean>> getRoutesByUser(User user){
		
		log.log(Level.WARNING,"[getRoutesByUserService] ");

		return new RouteDao().getRoutesByUser(user);
	}
}
