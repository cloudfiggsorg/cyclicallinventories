package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.PositionZoneBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TmatnrB;
import com.gmodelo.beans.ZoneB;
import com.gmodelo.beans.ZoneBean;
import com.gmodelo.dao.RouteDao;
import com.gmodelo.dao.ZoneDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ZoneWorkService {

	private Logger log = Logger.getLogger(ZoneWorkService.class.getName());
	
	public Response<List<ZoneBean>> getLgortByZone(Request request){
			
			log.log(Level.WARNING,"[getZoneByLgortWorkService] "+request.toString());
			ZoneBean zoneBean = null;
			String searchFilter = null;
			Response<List<ZoneBean>> res = new Response<List<ZoneBean>>();
			String req = request.getLsObject().toString().trim();
			if(!req.isEmpty()){
				try {
//					aqui siempre se recibirá un objeto
					zoneBean = new Gson().fromJson(request.getLsObject().toString(), ZoneBean.class);
				} catch (JsonSyntaxException e) {
					log.log(Level.SEVERE,"[getZoneByLgortWorkService] Error al convertir json a ZoneBean");
					AbstractResults abstractResult = new AbstractResults();
					abstractResult.setResultId(ReturnValues.IEXCEPTION);
					abstractResult.setResultMsgAbs(e.getMessage());
					res.setAbstractResult(abstractResult);
					return res;
				}
			}else{
				searchFilter = "";
				log.log(Level.WARNING, "[getZoneByLgortWorkService] Fue cadena vacía ");
			}
			
			
			return new ZoneDao().getLgortByZone(zoneBean);
			
		}
	
	public Response<List<ZoneBean>> validateZone(Request request){
		
		log.log(Level.WARNING,"[validateZoneWorkService] "+request.toString());
		ZoneBean zoneBean = null;
		String searchFilter = null;
		Response<List<ZoneBean>> res = new Response<List<ZoneBean>>();
		String req = request.getLsObject().toString().trim();
		if(!req.isEmpty()){
			try {
//				aqui siempre se recibirá un objeto
				zoneBean = new Gson().fromJson(request.getLsObject().toString(), ZoneBean.class);
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE,"[validateZoneWorkService] Error al convertir json a ZoneBean");
				AbstractResults abstractResult = new AbstractResults();
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}else{
			searchFilter = "";
			log.log(Level.WARNING, "[validateZoneWorkService] Fue cadena vacía ");
		}
		
		
		return new ZoneDao().validateZone(zoneBean);
		
	}
	
	public Response<Object> addZone(Request<?> request, User user){
		
		log.log(Level.WARNING,"[addZone] "+request.toString());
		ZoneBean zoneBean;
		Response<Object> res = new Response<Object>();
		
		try {
			zoneBean = new Gson().fromJson(request.getLsObject().toString(), ZoneBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[addZone] Error al pasar de Json a ZoneBean");
			zoneBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		return new ZoneDao().addZone(zoneBean, user.getEntity().getIdentyId());
		
	}
	
	public Response<Object> addPositionZone(Request<?> request){
		
		log.log(Level.WARNING,"[addPositionZone] "+request.toString());
		PositionZoneBean positionZoneBean;
		Response<Object> res = new Response<Object>();
		
		try {
			positionZoneBean = new Gson().fromJson(request.getLsObject().toString(), PositionZoneBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[addPositionZone] Error al pasar de Json a PositionZoneBean");
			positionZoneBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		return new ZoneDao().addPositionZone(positionZoneBean);
		
	}
	
	public Response<Object> deleteZone(Request<?> request){
			
			log.log(Level.WARNING,"[deleteZoneWS] "+request.toString());
			String arrayIdZones;
			Response<Object> res = new Response<Object>();
			AbstractResults abstractResult = new AbstractResults();
			
			try {
				arrayIdZones = request.getLsObject().toString();
				
				if(arrayIdZones == null || arrayIdZones.isEmpty()){
					abstractResult.setResultId(ReturnValues.IEXCEPTION);
					abstractResult.setResultMsgAbs("NULL OR EMPTY ARRAY");
					res.setAbstractResult(abstractResult);
					return res;
				}
				
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE,"[deleteZoneWS] Error al pasar de Json a RoutePositionBean");
				
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		
			return new ZoneDao().deleteZone(arrayIdZones);
		
		}

	public Response<List<ZoneB>> getZones(Request<?> request){
		
		log.log(Level.WARNING,"[getZonesWorkService] "+request.toString());
		ZoneB zoneBean;
		Response<List<ZoneB>> res = new Response<List<ZoneB>>();
		
		try {
			zoneBean = new Gson().fromJson(request.getLsObject().toString(), ZoneB.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[getZonesWorkService] Error al pasar de Json a ZoneB");
			zoneBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		return new ZoneDao().getZones(zoneBean);
		
	}
}
