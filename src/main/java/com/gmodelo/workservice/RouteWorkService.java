package com.gmodelo.workservice;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.BurksBean;
import com.gmodelo.beans.HeaderBean;
import com.gmodelo.beans.LgberBean;
import com.gmodelo.beans.LgnumBean;
import com.gmodelo.beans.LgplaBean;
import com.gmodelo.beans.LgtypBean;
import com.gmodelo.beans.LoginBean;
import com.gmodelo.beans.MaterialToZoneBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteB;
import com.gmodelo.beans.RouteBean;
import com.gmodelo.beans.RoutePositionBean;
import com.gmodelo.beans.UserB;
import com.gmodelo.beans.WerksBean;
import com.gmodelo.dao.RouteDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class RouteWorkService {

	private Logger log = Logger.getLogger(RouteWorkService.class.getName());
	
	public String GetRouteService(Request<LoginBean<?>> request) {
		Response<HeaderBean> response = new Response<>();
		String[] almacen = { "LV01", "LV02", "LV03", "LV04" };
		String[] zona = { "A", "B", "C", "D" };
		String zonaStr = "LV";
		String areaStr = "FM";
		HeaderBean header = new HeaderBean();
		BurksBean burks = new BurksBean();
		WerksBean werks = new WerksBean();
		List<WerksBean> werskList = new ArrayList<>();
		burks.setBurks("2013");
		burks.setBurksDesc("Descripcion");
		header.setBurks(burks);
		werks.setWerks("PC13");
		werks.setWerksDesc("Zacatepunk");
		// To-Do Replace this for, XD For query list of Almacen
		List<LgnumBean> lgnumList = new ArrayList<>();
		for (int alm = 0; alm < 4; alm++) {
			LgnumBean lgnum = new LgnumBean();
			lgnum.setLgnum(almacen[alm]);
			lgnum.setLgnumDesc("El Almacen: " + almacen[alm]);
			List<LgtypBean> lgtypList = new ArrayList<>();
			for (int zonaAlm = 0; zonaAlm < 8; zonaAlm++) {
				LgtypBean lgtyp = new LgtypBean();
				lgtyp.setLgtyp(zonaStr + zona[alm] + zonaAlm);
				lgtyp.setLgtypDesc("La zona es: " + zonaStr + zona[alm] + zonaAlm);
				List<LgberBean> lgberList = new ArrayList<>();
				for (int areaZon = 0; areaZon < 4; areaZon++) {
					LgberBean lgber = new LgberBean();
					lgber.setLgber(areaStr + zona[alm] + areaZon);
					lgber.setLgberDesc("La area es: " + areaStr + zona[alm] + areaZon);
					List<LgplaBean> lgplaList = new ArrayList<>();
					for (int carril = 1; carril <= 200; carril++) {
						LgplaBean lgpla = new LgplaBean();
						if (carril > 0 && carril < 10) {
							lgpla.setLgpla(zona[alm] + "000" + carril);
						} else if (carril >= 10 && carril < 100) {
							lgpla.setLgpla(zona[alm] + "00" + carril);
						} else {
							lgpla.setLgpla(zona[alm] + "0" + carril);
						}
						lgplaList.add(lgpla);
					}
					lgber.setLgplaList(lgplaList);
					lgberList.add(lgber);

				}
				lgtyp.setLgberList(lgberList);
				lgtypList.add(lgtyp);

			}
			lgnum.setLgtypList(lgtypList);
			lgnumList.add(lgnum);
		}
		werks.setLgnumList(lgnumList);
		werskList.add(werks);
		burks.setWerskList(werskList);
		response.setLsObject(header);
		response.setAbstractResult(new AbstractResults());
		return new Gson().toJson(response);
	}
	
	public Response<Object> addRoute(Request request, User user){
		
		log.log(Level.WARNING,"[addRouteWS] "+request.toString());
		RouteB routeBean;
		Response<Object> res = new Response<Object>();
		String req = request.getLsObject().toString().trim();
		if(!req.isEmpty()){
			try {
				routeBean = new Gson().fromJson(request.getLsObject().toString(), RouteB.class);
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE,"[addRouteWS] Error al pasar de Json a ZoneBean");
				routeBean = null;
				AbstractResults abstractResult = new AbstractResults();
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}else{
			AbstractResults abstractResult = new AbstractResults();
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
		AbstractResults abstractResult = new AbstractResults();
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

	public Response<List<RouteB>> getRoutes(Request request){
		log.log(Level.WARNING,"[getRoutesService] "+request.toString());
		RouteB routeBean = null;
		String searchFilter = null;
		Response<List<RouteB>> res = new Response<List<RouteB>>();
		String req = request.getLsObject().toString().trim();
		if(!req.isEmpty()){
			try {
				routeBean = new Gson().fromJson(request.getLsObject().toString(), RouteB.class) ;
				
				log.log(Level.WARNING,request.getLsObject().toString());
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE,"Error al pasar de Json a RouteB");
				routeBean = null;
				AbstractResults abstractResult = new AbstractResults();
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult );
				return res;
			}
		}else{
			searchFilter = "";
		}
		
		return new RouteDao().getRoutes(routeBean,searchFilter);
	}

	public Response<List<RouteB>> getRoutesByUser(Request request){
		log.log(Level.WARNING,"[getRoutesByUserService] "+request.toString());
		UserB userBean = null;

		Response<List<RouteB>> res = new Response<List<RouteB>>();
		String req = request.getLsObject().toString().trim();
		if(!req.isEmpty()){
			try {
				userBean = new Gson().fromJson(request.getLsObject().toString(), UserB.class) ;
				
				log.log(Level.WARNING,request.getLsObject().toString());
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE,"Error al pasar de Json a UserB");
				userBean = null;
				AbstractResults abstractResult = new AbstractResults();
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult );
				return res;
			}
		}
		
		return new RouteDao().getRoutesByUser(userBean);
	}
}
