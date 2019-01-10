package com.gmodelo.cyclicinventories.workservice;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.ContingencyTaskBean;
import com.gmodelo.cyclicinventories.beans.LgplaValuesBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.RouteUserBean;
import com.gmodelo.cyclicinventories.beans.RouteUserPositionBean;
import com.gmodelo.cyclicinventories.beans.ZoneUserBean;
import com.gmodelo.cyclicinventories.beans.ZoneUserPositionsBean;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ContingencyTaskWorkService {
	
	private Logger log = Logger.getLogger(ConciliacionWorkService.class.getName());
	Gson gson = new Gson();
	
	public Response<RouteUserBean> buildBean(Request request){
		
		Response<RouteUserBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		res.setAbstractResult(abstractResult);
		log.info("[buildBeanWS] toString -> " + request.getLsObject().toString());
		try {
			Type listType = new TypeToken<ArrayList<ContingencyTaskBean>>() {
			}.getType();
			List<ContingencyTaskBean> requestBeanList = new Gson().fromJson(new Gson().toJson(request.getLsObject()),
					listType);
			if (!requestBeanList.isEmpty()) {
				
				List<RouteUserPositionBean> listPositions = new ArrayList<>();
				List<String> listZoneId = new ArrayList<>();
				List<ZoneUserPositionsBean> listAllPositionsB = new ArrayList<>();
				
				//for para obtener lista de positions (positionsId y zoneId asociados),obtener lista de ZoneIds unicos y obtener lista de todos los positionsB
				for(ContingencyTaskBean ctb : requestBeanList){
				
					if(listPositions.size() == 0){
						RouteUserPositionBean positions = new RouteUserPositionBean();
						positions.setPositionId(ctb.getPositionId());
						positions.setLgort(ctb.getLgort());
						positions.setRouteId(ctb.getRouteId());
						positions.setSecuency(ctb.getZoneSecuency());
						ZoneUserBean zone = new ZoneUserBean();
						zone.setZoneId(ctb.getZoneId());
						positions.setZone(zone);
						listPositions.add(positions);
					}else{
						boolean inListPositions = false;
						for(RouteUserPositionBean p : listPositions){
							if(p.getPositionId() == ctb.getPositionId()){
								inListPositions = true;
								break;
							}
						}
						if(!inListPositions){
							RouteUserPositionBean positions = new RouteUserPositionBean();
							positions.setPositionId(ctb.getPositionId());
							positions.setLgort(ctb.getLgort());
							positions.setRouteId(ctb.getRouteId());
							positions.setSecuency(ctb.getZoneSecuency());
							ZoneUserBean zone = new ZoneUserBean();
							zone.setZoneId(ctb.getZoneId());
							positions.setZone(zone);
							listPositions.add(positions);
						}
					}
					////////////////////////////////////////////////////////////////
					if(listZoneId.size() == 0){
						listZoneId.add(ctb.getZoneId());
					}else{
						boolean inListZone = false;
						for(String z : listZoneId){
							if(z.equalsIgnoreCase(ctb.getZoneId()) ){
								inListZone = true;
								break;
							}
						}
						if(!inListZone){
							listZoneId.add(ctb.getZoneId());
						}
					}
					
					/////////////////////////////////////////////////////////////
					// positionsB
					if(listAllPositionsB.size() == 0){
						ZoneUserPositionsBean posi = new ZoneUserPositionsBean();
						posi.setLgpla(ctb.getLgpla());
						posi.setLgtyp(ctb.getLgtyp());
						posi.setPkAsgId(ctb.getPkAsgId());
						posi.setSecuency(ctb.getLgplaSecuency());
						posi.setZoneId(ctb.getZoneId());
						HashMap<String, LgplaValuesBean> lgplaValues = new HashMap<>();
						posi.setLgplaValues(lgplaValues);
						
						listAllPositionsB.add(posi);
					}else{
						boolean inListpositionsB = false;
						for(ZoneUserPositionsBean p : listAllPositionsB){
							if(p.getLgpla().equalsIgnoreCase(ctb.getLgpla())){
								inListpositionsB = true;
								break;
							}
						}
						if(!inListpositionsB){
							ZoneUserPositionsBean posi = new ZoneUserPositionsBean();
							posi.setLgpla(ctb.getLgpla());
							posi.setLgtyp(ctb.getLgtyp());
							posi.setPkAsgId(ctb.getPkAsgId());
							posi.setSecuency(ctb.getLgplaSecuency());
							posi.setZoneId(ctb.getZoneId());
							HashMap<String, LgplaValuesBean> lgplaValues = new HashMap<>();
							posi.setLgplaValues(lgplaValues);
							
							listAllPositionsB.add(posi);
						}
					}
					
				}

				//for para setear los valores del hashMap para cada positionsB
				for(ContingencyTaskBean ctb : requestBeanList){
					for(ZoneUserPositionsBean pos : listAllPositionsB){
						if(ctb.getLgpla().equalsIgnoreCase(pos.getLgpla())){
							
							//put de lgplaValues
							LgplaValuesBean lvb = new LgplaValuesBean();
							lvb.setMatnr(ctb.getMatnr());
							lvb.setVhilm(ctb.getVhilm());
							lvb.setVhilmQuan(ctb.getVhilmQuan());
							lvb.setTotalConverted(ctb.getTotalConverted());
							pos.getLgplaValues().put(ctb.getPkAsgId()+ctb.getMatnr(), lvb);
						}
					}
				}
				/////////////////////////////////////////////////////////////////////////////

				//for para crear ZoneUserBeans
				List<ZoneUserPositionsBean> listPositionsB = new ArrayList<>();
				List<ZoneUserBean> listzone = new ArrayList<>();
				for(String z : listZoneId){
					ZoneUserBean zone = new ZoneUserBean();
					for(ZoneUserPositionsBean positionB :listAllPositionsB){
						if(positionB.getZoneId().equalsIgnoreCase(z)){
							listPositionsB.add(positionB);
						}
					}
					zone.setZoneId(z);
					zone.setPositionsB(listPositionsB);
					
					listzone.add(zone);
				}
				
				//for para agregar cada bean zone con su positionId
				for(RouteUserPositionBean p :listPositions){
					for(ZoneUserBean z : listzone){
						if(p.getZone().getZoneId().equalsIgnoreCase(z.getZoneId())){
							p.setZone(z);
						}
					}
				}
				
				RouteUserBean rub = new RouteUserBean(); 
				rub.setBukrs(requestBeanList.get(0).getBukrs());
				rub.setRouteId(requestBeanList.get(0).getRouteId());
				rub.setTaskId(requestBeanList.get(0).getTaskId());
				rub.setWerks(requestBeanList.get(0).getWerks());
				rub.setPositions(listPositions);
				
				res.setLsObject(rub);

			} else {
				log.log(Level.WARNING, "[buildBeanWS]: Lista Vacia");
				abstractResult.setResultId(ReturnValues.IEMPTY);
				abstractResult.setResultMsgAbs("La tarea enviada viene vacia");
				res.setAbstractResult(abstractResult);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "[buildBeanWS]", e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
		}
		
		return res;
	}

}
