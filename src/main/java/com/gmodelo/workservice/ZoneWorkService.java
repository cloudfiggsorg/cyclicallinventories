package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.MaterialToZoneBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.ZoneBean;
import com.gmodelo.beans.ZoneBean;
import com.gmodelo.beans.ZonePositionsBean;
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
			Gson gson = new Gson();
			String req = request.getLsObject().toString().trim();
			if(!req.isEmpty()){
				try {

					//					aqui siempre se recibirá un objeto
					zoneBean = gson .fromJson(gson.toJson(request.getLsObject()), ZoneBean.class);
				} catch (JsonSyntaxException e) {
					log.info("[getZoneByLgortWorkService] Probando string");
					searchFilter = request.getLsObject().toString();
				}
			}else{
				searchFilter = "";
				log.log(Level.WARNING, "[getZoneByLgortWorkService] Fue cadena vacía ");
			}
			
			
			return new ZoneDao().getLgortByZone(zoneBean, searchFilter);
			
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
				AbstractResultsBean abstractResult = new AbstractResultsBean();
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
	
	public Response<ZoneBean> addZone(Request<?> request, User user){
		
		log.log(Level.WARNING,"[addZone] "+request.toString());
		ZoneBean zoneBean;
		Response<ZoneBean> res = new Response<ZoneBean>();
		
		try {
			zoneBean = new Gson().fromJson(request.getLsObject().toString(), ZoneBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[addZone] Error al pasar de Json a ZoneBean");
			zoneBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		return new ZoneDao().addZone(zoneBean, user.getEntity().getIdentyId());
		
	}
	
	public Response<Object> deleteZone(Request<?> request){
			
			log.log(Level.WARNING,"[deleteZoneWS] "+request.toString());
			String arrayIdZones;
			Response<Object> res = new Response<Object>();
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			
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

	public Response<List<ZoneBean>> getZones(Request<?> request){
		
		log.log(Level.WARNING,"[getZonesWorkService] "+request.toString());
		ZoneBean zoneBean = null;
		Response<List<ZoneBean>> res = new Response<List<ZoneBean>>();
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		if(!req.isEmpty()){
			try {
				zoneBean = new Gson().fromJson(request.getLsObject().toString(), ZoneBean.class);
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE,"[getZonesWorkService] Error al pasar de Json a ZoneB");
				zoneBean = null;
				AbstractResultsBean abstractResult = new AbstractResultsBean();
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}else{
			searchFilter = "";
			log.log(Level.WARNING, "[getZonesWorkService] Fue cadena vacía ");
		}
		return new ZoneDao().getZones(zoneBean,searchFilter);
		
	}
	
	public Response<Object> unassignMaterialToZone(Request request){
			
			log.log(Level.WARNING,"[unassignMaterialToZoneWS] "+request.toString());
			MaterialToZoneBean materialToZoneBean;
			Response<Object> res = new Response<Object>();
			String req = request.getLsObject().toString().trim();
			if(!req.isEmpty()){
				try {
					materialToZoneBean = new Gson().fromJson(request.getLsObject().toString(), MaterialToZoneBean.class);
				} catch (JsonSyntaxException e) {
					log.log(Level.SEVERE,"[unassignMaterialToRouteWS] Error al pasar de Json a MaterialToZoneBean");
					materialToZoneBean = null;
					AbstractResultsBean abstractResult = new AbstractResultsBean();
					abstractResult.setResultId(ReturnValues.IEXCEPTION);
					abstractResult.setResultMsgAbs(e.getMessage());
					res.setAbstractResult(abstractResult);
					return res;
				}
			}else{
				materialToZoneBean = null;
				AbstractResultsBean abstractResult = new AbstractResultsBean();
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs("Error al pasar de Json a MaterialToZoneBean");
				res.setAbstractResult(abstractResult);
				log.log(Level.SEVERE,"[unassignMaterialToRouteWS] "+abstractResult.getResultMsgAbs());
				return res;
			}
	
			return new ZoneDao().unassignMaterialToZone(materialToZoneBean);
		
		}
	
}
