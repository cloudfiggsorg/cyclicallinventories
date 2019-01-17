package com.gmodelo.cyclicinventories.workservice;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.ContingencyTaskBean;
import com.gmodelo.cyclicinventories.beans.LgplaValuesBean;
import com.gmodelo.cyclicinventories.beans.LoginBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.RouteUserBean;
import com.gmodelo.cyclicinventories.beans.RouteUserPositionBean;
import com.gmodelo.cyclicinventories.beans.ZoneUserBean;
import com.gmodelo.cyclicinventories.beans.ZoneUserPositionsBean;
import com.gmodelo.cyclicinventories.dao.CountsDao;
import com.gmodelo.cyclicinventories.dao.MatnrDao;
import com.gmodelo.cyclicinventories.dao.TaskUserDao;
import com.gmodelo.cyclicinventories.dao.ZoneDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class CountsWorkService {

	private Logger log = Logger.getLogger(CountsWorkService.class.getName());
	private Gson gson = new Gson();

	@SuppressWarnings("rawtypes")
	public Response<Object> addCount(Request request) {
		log.info("[addCountWS] " + request.toString());
		RouteUserBean routeBean = null;

		Response<Object> res = new Response<>();
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
	
	public Response<Object> addCountFromContingency(Request request, User user){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		res.setAbstractResult(abstractResult);
		RouteUserBean rub = new RouteUserBean();
		log.info("[addCountFromContingencyWS] toString -> " + request.getLsObject().toString());
		try {
			Type listType = new TypeToken<ArrayList<ContingencyTaskBean>>() {
			}.getType();
			List<ContingencyTaskBean> requestBeanList = new Gson().fromJson(new Gson().toJson(request.getLsObject()),
					listType);
			if (!requestBeanList.isEmpty()) {
				
				//Verificar que el usuario asignado a la tarea, sea quien la envió y obtener fecha de descarga de tarea
				Response responseTask = validateTaskId(requestBeanList.get(0).getTaskId(), user.getEntity().getIdentyId());
				if(responseTask.getAbstractResult().getResultId() != 1){
					res.getAbstractResult().setResultId(responseTask.getAbstractResult().getResultId());
					res.getAbstractResult().setResultMsgAbs(responseTask.getAbstractResult().getResultMsgAbs());
					return res;
				}
				
				List<String> listMatnr = new ArrayList<>();
				//Extraer matnr unicos 
				for(ContingencyTaskBean ctb : requestBeanList){
					if(listMatnr.size() == 0){
						listMatnr.add(ctb.getMatnr());
					}else{
						boolean inListMatnr = false;
						for(String m : listMatnr){
							if(m.equalsIgnoreCase(ctb.getMatnr()) ){
								inListMatnr = true;
								break;
							}
						}
						if(!inListMatnr){
							listMatnr.add(ctb.getMatnr());
						}
					}
				}
				
//				verificar si los matnr existen para el centro dado
				Response<List<String>> response = validateMatnr(listMatnr, requestBeanList.get(0).getWerks());
				if(response.getAbstractResult().getResultId() != 1){
					res.getAbstractResult().setResultId(response.getAbstractResult().getResultId());
					res.getAbstractResult().setResultMsgAbs(response.getAbstractResult().getResultMsgAbs());
					return res;
				}
//				verificar si las posiciones del excel son correctas para los campos bukrs, werks, routeId, positionOd, zoneId, lgpla, pkAsgId
				Response<List<ContingencyTaskBean>> responsePos = validatePositions(requestBeanList, requestBeanList.get(0).getBukrs(), 
						requestBeanList.get(0).getWerks(), requestBeanList.get(0).getRouteId());	
				if(responsePos.getAbstractResult().getResultId() != 1){
					res.getAbstractResult().setResultId(responsePos.getAbstractResult().getResultId());
					res.getAbstractResult().setResultMsgAbs(responsePos.getAbstractResult().getResultMsgAbs());
					return res;
				}
				
				//for para obtener lista de positions (positionsId y zoneId asociados),obtener lista de ZoneIds unicos y obtener lista de todos los positionsB
				List<RouteUserPositionBean> listPositions = new ArrayList<>();
				List<String> listZoneId = new ArrayList<>();
				List<ZoneUserPositionsBean> listAllPositionsB = new ArrayList<>();
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
							lvb.setDateStart(new Date().getTime());
							lvb.setDateEnd(new Date().getTime());
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
				 
				rub.setBukrs(requestBeanList.get(0).getBukrs());
				rub.setRouteId(requestBeanList.get(0).getRouteId());
				rub.setTaskId(requestBeanList.get(0).getTaskId());
				rub.setWerks(requestBeanList.get(0).getWerks());
				rub.setDateIni(Long.parseLong(responseTask.getAbstractResult().getResultMsgGen()));
				rub.setDateEnd(new Date().getTime());
				rub.setPositions(listPositions);
				
				res.setLsObject(rub);

			} else {
				log.log(Level.WARNING, "[addCountFromContingencyWS]: Lista Vacia");
				abstractResult.setResultId(ReturnValues.IEMPTY);
				abstractResult.setResultMsgAbs("La tarea enviada viene vacia");
				res.setAbstractResult(abstractResult);
				return res;
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "[addCountFromContingencyWS]", e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		LoginBean loginBean = new LoginBean<>();
		request.setTokenObject(loginBean);
		request.getTokenObject().setLoginId(user.getEntity().getIdentyId());
		request.setLsObject(res.getLsObject());
		res = new CountsWorkService().addCount(request);
		if(res.getAbstractResult().getResultId() == 1){
			res.setLsObject(rub);
			log.info("[addCountFromContingencyWS] Tarea de Contingencia "+ rub.getTaskId());
		}
		
		return res;
	}
	
	private Response validateTaskId(String task, String user){
		
		Response<List<String>> response = new TaskUserDao().validateTaskFromContingency(task);
		if(response.getAbstractResult().getResultId() != 1){
			return response;
		}
		
		List<String> listUsers = response.getLsObject();
		if(listUsers == null){
			response.getAbstractResult().setResultId(ReturnValues.IERROR);
			response.getAbstractResult().setResultMsgAbs("No existen usuarios para la tarea "+task +" Favor de verificar el id de tarea");
			return response;
		}
		
		if(!listUsers.contains(user)){
			response.getAbstractResult().setResultId(ReturnValues.IERROR);
			response.getAbstractResult().setResultMsgAbs("La tarea "+task+" debe ser enviada desde la sesión del usuario asignado para su conteo");
			return response;
		}
		
		return response;
	}
	
	private Response<List<String>> validateMatnr(List<String> listoToValidate, String werks){
		
		Response<List<String>> response = new MatnrDao().validateMatnr(werks);
		if(response.getAbstractResult().getResultId() != 1){
			return response;
		}
		
		List<String> listMatnr = response.getLsObject();
		if(listMatnr == null){
			response.getAbstractResult().setResultId(ReturnValues.IERROR);
			response.getAbstractResult().setResultMsgAbs("No existen materiales para el centro "+werks);
			return response;
		}
		
		String msg = "Para el centro "+werks+" no existen los materiales:  ";
		int initialMsgLen = msg.length();
		boolean wrongMatnr = false;
		for(String item : listoToValidate){
			if(!listMatnr.contains(item)){
				msg+=  msg.length() > initialMsgLen ?  ",  "+item : item;
				wrongMatnr = true;
			}
		}
		if(wrongMatnr){
			response.getAbstractResult().setResultId(ReturnValues.IERROR);
			response.getAbstractResult().setResultMsgAbs(msg);
		}
		
		return response ;
	}
	
	private Response<List<ContingencyTaskBean>> validatePositions(List<ContingencyTaskBean> listToValidate, String bukrs, String werks, String routeId){
		
		Response<List<ContingencyTaskBean>> response = new ZoneDao().validateTaskContingencyPositions(bukrs, werks, routeId);
		if(response.getAbstractResult().getResultId() != 1){
			return response;
		}
		
		List<ContingencyTaskBean> listCTB = response.getLsObject();
		if(listCTB == null){
			response.getAbstractResult().setResultId(ReturnValues.IERROR);
			response.getAbstractResult().setResultMsgAbs("No existen posiciones para la sociedad "+bukrs+" con  centro "+werks+" y ruta "+routeId);
			return response;
		}
		
		for(ContingencyTaskBean item : listToValidate){
			boolean existPkAsgId = false;
			boolean existLgpla = false;
			boolean existZoneId = false;
			boolean existPositionId = false;
			for(ContingencyTaskBean ctb : listCTB){
				
				if(item.getPkAsgId() == ctb.getPkAsgId()){
					existPkAsgId = true;
					if(item.getLgpla().equalsIgnoreCase(ctb.getLgpla())){
						existLgpla = true;
					}
					if(item.getZoneId().equalsIgnoreCase(ctb.getZoneId())){
						existZoneId = true;
					}
					if(item.getPositionId() == ctb.getPositionId()){
						existPositionId = true;
					}
					break;
				}
			}
			if(!existPkAsgId){
				String msgPkAsgId = "No existe el Id de Relación "+item.getPkAsgId()+" favor de verificarlo";
				response.getAbstractResult().setResultId(ReturnValues.IERROR);
				response.getAbstractResult().setResultMsgAbs(msgPkAsgId);
				return response ;
			}
			if(!existLgpla){
				String msgLgpla = "No existe el carril "+item.getLgpla()+" para el Id de Relación "+item.getPkAsgId();
				response.getAbstractResult().setResultId(ReturnValues.IERROR);
				response.getAbstractResult().setResultMsgAbs(msgLgpla);
				return response ;
			}
			if(!existZoneId){
				String msgZoneId = "No existe el Id de Zona "+item.getZoneId()+" para la ruta "+item.getRouteId();
				response.getAbstractResult().setResultId(ReturnValues.IERROR);
				response.getAbstractResult().setResultMsgAbs(msgZoneId);
				return response ;
			}
			if(!existPositionId){
				String msgPositionId = "No existe el Id de Posición "+item.getPositionId()+" para la ruta "+item.getRouteId();
				response.getAbstractResult().setResultId(ReturnValues.IERROR);
				response.getAbstractResult().setResultMsgAbs(msgPositionId);
				return response ;
			}
		}
		
		return response ;
	}

}
