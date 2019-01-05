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

	public Response<RouteUserBean> buildBean(Request request) {
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
				//buildBean
				RouteUserBean rub = new RouteUserBean(); 
				rub.setBukrs(requestBeanList.get(0).getBukrs());
				rub.setRouteId(requestBeanList.get(0).getRouteId());
				rub.setTaskId(requestBeanList.get(0).getTaskId());
				rub.setWerks(requestBeanList.get(0).getWerks());
				
				
				RouteUserPositionBean positions = new RouteUserPositionBean();
				HashMap<Integer, List<RouteUserPositionBean>> positionsMap = new HashMap<>();

				ZoneUserBean zone = new ZoneUserBean();
				
				ZoneUserPositionsBean positionsB = new ZoneUserPositionsBean();
				HashMap<String, List<ZoneUserPositionsBean>> positionsBMap = new HashMap<>();
				
				for(ContingencyTaskBean ctb : requestBeanList){
				
					if(positionsBMap.containsKey(ctb.getZoneId())){
						List<ZoneUserPositionsBean> listPositionsB = positionsBMap.get(ctb.getZoneId());
						listPositionsB.add(positionsB);
						positionsBMap.put(ctb.getZoneId(), listPositionsB);
					}else{
						List<ZoneUserPositionsBean> newlistpositionsB = new ArrayList<>();
						positionsB = new ZoneUserPositionsBean();
						newlistpositionsB.add(positionsB);
						positionsBMap.put(ctb.getZoneId(), newlistpositionsB);
					}
					
					if(positionsMap.containsKey(ctb.getPositionId())){
						List<RouteUserPositionBean> listPositions = positionsMap.get(ctb.getPositionId());
						listPositions.add(positions);
						positionsMap.put(ctb.getPositionId(), listPositions);
					}else{
						List<RouteUserPositionBean> newlistPositions = new ArrayList<>();
						positions = new RouteUserPositionBean();
						zone = new ZoneUserBean();
						newlistPositions.add(positions);
						positionsMap.put(ctb.getPositionId(), newlistPositions);
					}
					
					
					
					positions.setLgort(ctb.getLgort());
					positions.setPositionId(ctb.getPositionId());
					positions.setRouteId(ctb.getRouteId());
					positions.setSecuency(ctb.getZoneSecuency());
					positions.setZone(zone);
				
				
					zone.setZoneId(ctb.getZoneId());
				
				
					positionsB.setSecuency(ctb.getLgplaSecuency());
					positionsB.setZoneId(ctb.getZoneId());
					positionsB.setPkAsgId(ctb.getPkAsgId());
					positionsB.setLgtyp(ctb.getLgtyp());
					positionsB.setLgpla(ctb.getLgpla());
				
					//creacion de lgplaValues
					HashMap<String, LgplaValuesBean> lgplaValues = new HashMap<>();
					LgplaValuesBean lvb = new LgplaValuesBean();
					lvb.setMatnr(ctb.getMatnr());
					lvb.setVhilm(ctb.getVhilm());
					lvb.setVhilmQuan(ctb.getVhilmQuan());
					lvb.setTotalConverted(ctb.getTotalConverted());
					lgplaValues.put(ctb.getPkAsgId()+ctb.getMatnr(), lvb);
					positionsB.setLgplaValues(lgplaValues);

				}
				
//				int[] uniquePositionId = new int[positionsMap.size()];
//				int k =0;
//				for (Integer key : positionsMap.keySet()) {
//					uniquePositionId[k] = key;
//					k++;
//				}
//				
//				int[] uniquePositionsB = new int[positionsBMap.size()];
//				int m =0;
//				for (Integer key : positionsMap.keySet()) {
//					uniquePositionsB[m] = key;
//					m++;
//				}
				List<RouteUserPositionBean> listPos = new ArrayList<>();
				for (List<RouteUserPositionBean> list : positionsMap.values()) {
					listPos.addAll(list);
				}
				
				for(RouteUserPositionBean item : listPos){
					item.getZone().setPositionsB(positionsBMap.get(item.getZone().getZoneId()));
				}
				rub.setPositions(listPos);
				res.setLsObject(rub);
			} else {
				log.log(Level.WARNING, "[buildBeanWS]: Lista Vacia");
				abstractResult.setResultId(ReturnValues.IEMPTY);
				abstractResult.setResultMsgAbs("La lista solicitada viene vacia");
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
