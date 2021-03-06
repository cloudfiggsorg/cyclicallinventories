package com.gmodelo.cyclicinventories.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.MaterialToZoneBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.ZoneBean;
import com.gmodelo.cyclicinventories.dao.ZoneDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ZoneWorkService {

	private Logger log = Logger.getLogger(ZoneWorkService.class.getName());
	private Gson gson = new Gson();

	public Response<List<ZoneBean>> getLgortByZone(Request<?> request) {
		log.info("[getZoneByLgortWorkService] " + request.toString());
		ZoneBean zoneBean = null;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<List<ZoneBean>> res = new Response<>();
		try {
			zoneBean = gson.fromJson(gson.toJson(request.getLsObject()), ZoneBean.class);
			res = new ZoneDao().getLgortByZone(zoneBean);
		} catch (Exception e) {
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			log.info("[getZoneByLgortWorkService] Probando string");
		}

		return res;

	}

	public Response<List<ZoneBean>> validateZone(Request<?> request) {

		log.info("[validateZoneWorkService] " + request.toString());
		ZoneBean zoneBean = null;
		Response<List<ZoneBean>> res = new Response<>();
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			try {
				// aqui siempre se recibirá un objeto
				zoneBean = gson.fromJson(gson.toJson(request.getLsObject()), ZoneBean.class);
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE, "[validateZoneWorkService] Error al convertir json a ZoneBean");
				AbstractResultsBean abstractResult = new AbstractResultsBean();
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		} else {
			log.log(Level.WARNING, "[validateZoneWorkService] Fue cadena vacía ");
		}

		return new ZoneDao().validateZone(zoneBean);

	}

	public Response<ZoneBean> addZone(Request<?> request, User user) {

		log.info("[addZone] " + request.toString());
		ZoneBean zoneBean;
		Response<ZoneBean> res = new Response<>();

		try {
			zoneBean = gson.fromJson(gson.toJson(request.getLsObject()), ZoneBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[addZone] Error al pasar de Json a ZoneBean");
			zoneBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new ZoneDao().addZone(zoneBean, user.getEntity().getIdentyId());

	}

	public Response<Object> deleteZone(Request<?> request) {

		log.info("[deleteZoneWS] " + request.toString());
		String arrayIdZones;
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		try {
			arrayIdZones = request.getLsObject().toString();

			if (arrayIdZones == null || arrayIdZones.isEmpty()) {
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs("NULL OR EMPTY ARRAY");
				res.setAbstractResult(abstractResult);
				return res;
			}

		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[deleteZoneWS] Error al pasar de Json a RoutePositionBean");

			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new ZoneDao().deleteZone(arrayIdZones);

	}

	public Response<List<ZoneBean>> getZones(Request<?> request) {

		log.info("[getZonesWorkService] " + request.toString());
		ZoneBean zoneBean = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			try {
				zoneBean = gson.fromJson(gson.toJson(request.getLsObject()), ZoneBean.class);
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE, "[getZonesWorkService] Probando con String");
				searchFilter = request.getLsObject().toString();
			}
		} else {
			searchFilter = "";
			log.info("[getZonesWorkService] Fue cadena vacía ");
		}
		return new ZoneDao().getZones(zoneBean, searchFilter);

	}

	public Response<List<ZoneBean>> getZoneOnly(Request<?> request) {

		log.info("[getZonesOnlyWorkService] " + request.toString());
		ZoneBean zb = null;

		try {

			zb = gson.fromJson(gson.toJson(request.getLsObject()), ZoneBean.class);

		} catch (JsonSyntaxException e) {

			log.log(Level.SEVERE, "[validateZonePositions] Error al pasar de Json a ZonePositionsBean");
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			Response<List<ZoneBean>> res = new Response<>();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new ZoneDao().getZonesOnly(zb);

	}

	public Response<ZoneBean> validateZonePositions(Request<?> request) {

		log.info("[validateZonePositions] " + request.toString());
		ZoneBean zb = null;

		try {

			zb = gson.fromJson(gson.toJson(request.getLsObject()), ZoneBean.class);

		} catch (JsonSyntaxException e) {

			log.log(Level.SEVERE, "[validateZonePositions] Error al pasar de Json a ZonePositionsBean");
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			Response<ZoneBean> res = new Response<>();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new ZoneDao().validateZonePositions(zb);

	}

	public Response<Object> unassignMaterialToZone(Request<?> request) {

		log.info("[unassignMaterialToZoneWS] " + request.toString());
		MaterialToZoneBean materialToZoneBean;
		Response<Object> res = new Response<>();
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			try {
				materialToZoneBean = gson.fromJson(gson.toJson(request.getLsObject()), MaterialToZoneBean.class);
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE, "[unassignMaterialToRouteWS] Error al pasar de Json a MaterialToZoneBean");
				materialToZoneBean = null;
				AbstractResultsBean abstractResult = new AbstractResultsBean();
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		} else {
			materialToZoneBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs("Error al pasar de Json a MaterialToZoneBean");
			res.setAbstractResult(abstractResult);
			log.log(Level.SEVERE, "[unassignMaterialToRouteWS] " + abstractResult.getResultMsgAbs());
			return res;
		}

		return new ZoneDao().unassignMaterialToZone(materialToZoneBean);

	}
	
	
	public Response<List<ZoneBean>> getZoneByLgortAndWerk(Request<?> request) {
		log.info("[getZoneByLgortAndWerkWorkService] " + request.toString());
		ZoneBean zoneBean = null;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<List<ZoneBean>> res = new Response<>();
		try {
			zoneBean = gson.fromJson(gson.toJson(request.getLsObject()), ZoneBean.class);
			res = new ZoneDao().getZoneByLgortAndWerk(zoneBean);
		} catch (Exception e) {
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			
			log.log(Level.SEVERE,"[getZoneByLgortAndWerkWorkService] Error al pasar de json a objeto");
			e.printStackTrace();
		}

		return res;

	}

}
