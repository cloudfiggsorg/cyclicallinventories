package com.gmodelo.cyclicinventories.workservice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.ConciliacionBean;
import com.gmodelo.cyclicinventories.beans.ConciliationsIDsBean;
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.GroupBean;
import com.gmodelo.cyclicinventories.beans.LgplaValuesBean;
import com.gmodelo.cyclicinventories.beans.LoginBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.RouteUserBean;
import com.gmodelo.cyclicinventories.beans.RouteUserPositionBean;
import com.gmodelo.cyclicinventories.beans.TaskBean;
import com.gmodelo.cyclicinventories.beans.ZoneUserPositionsBean;
import com.gmodelo.cyclicinventories.dao.ConciliacionDao;
import com.gmodelo.cyclicinventories.dao.DocInvDao;
import com.gmodelo.cyclicinventories.dao.TaskDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;

public class ConciliacionWorkService {

	private Logger log = Logger.getLogger(ConciliacionWorkService.class.getName());
	Gson gson = new Gson();
	private RouteWorkService routeWorkService = new RouteWorkService();

	public Response<List<ConciliationsIDsBean>> getConciliationIDs(Request<List<Object>> request) {
		log.info("[getConciliationIDsWS] " + request.toString());
		String bukrs = (String) request.getLsObject().get(0);
		String werks = (String) request.getLsObject().get(1);
		return new ConciliacionDao().getConciliationIDs(bukrs, werks);
	}

	public Response<ConciliacionBean> getConciliacion(Request<?> request) {

		log.info("[getConciliacionWS] " + request.toString());
		ConciliacionBean tb = new Gson().fromJson(request.getLsObject().toString(), ConciliacionBean.class);
		return new ConciliacionDao().getConciliacion(tb);
	}

	public Response<List<GroupBean>> getAvailableGroups(Request<ArrayList<Object>> request) {

		log.info("[getAvailableGroupsWS] " + request.toString());
		Response<List<GroupBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		DocInvBean docInv;
		String type = "";
		res.setAbstractResult(abstractResult);

		try {
			docInv = gson.fromJson(gson.toJson(request.getLsObject().get(0)), DocInvBean.class);
			type = (String) request.getLsObject().get(1);
			new ConciliacionDao().fillCurrectDocInv(docInv);
			res = new ConciliacionDao().getAvailableGroups(docInv, type);
		} catch (NumberFormatException e) {
			log.log(Level.SEVERE, "[getAvailableGroupsWS] Objeto no válido.");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[getAvailableGroupsWS] SQLException - ", e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
		} catch (Exception e) {
			log.log(Level.SEVERE, "[getAvailableGroupsWS] Exception - ", e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
		}
		return res;
	}

	public Response<TaskBean> getFatherTaskByDocId(Request request) {

		log.info("[getFatherTaskByDocIdWS] " + request.toString());
		String req = request.getLsObject().toString().trim();
		Response<TaskBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		int docInvId;

		try {
			docInvId = Integer.parseInt(req);
		} catch (NumberFormatException e) {
			log.log(Level.SEVERE, "[getFatherTaskByDocIdWS] Cadena no válida");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new ConciliacionDao().getFatherTaskByDocId(docInvId);
	}

	@SuppressWarnings("rawtypes")
	public Response<TaskBean> getSpecialCount(Request request, User user) {
		log.info("[getSpecialCount] " + request.toString());
		Response<TaskBean> res = new Response<>();
		TaskBean taskBean = new Gson().fromJson(new Gson().toJson(request.getLsObject()), TaskBean.class);
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		res.setAbstractResult(abstractResult);
		DocInvBean docInvBean = new DocInvBean();
		try {
			docInvBean = taskBean.getDocInvId();
			docInvBean.setCreatedBy(user.getEntity().getIdentyId());
			docInvBean.setType(String.valueOf(ReturnValues.CONTEO_ESPECIAL));
			docInvBean.setDocFatherInvId(docInvBean.getDocInvId());
			docInvBean.setDocInvId(null);
			Response<DocInvBean> resDao = new DocInvDao().addDocInv(docInvBean, docInvBean.getCreatedBy());
			if (resDao.getAbstractResult().getResultId() == ReturnValues.ISUCCESS) {
				taskBean.setDocInvId(resDao.getLsObject());
				Response<TaskBean> resTask = new TaskWorkService().addTaskSpecial(taskBean, user);
				if (resTask.getAbstractResult().getResultId() == ReturnValues.ISUCCESS) {
					res = resTask;
				} else {
					abstractResult.setResultId(resTask.getAbstractResult().getResultId());
					abstractResult.setResultMsgAbs(resTask.getAbstractResult().getResultMsgAbs());
				}
			} else {
				abstractResult.setResultId(resDao.getAbstractResult().getResultId());
				abstractResult.setResultMsgAbs(resDao.getAbstractResult().getResultMsgAbs());
			}
		} catch (Exception e) {
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			e.printStackTrace();

		}
		return res;
	}

	public Response<String> getZonePosition(Request request) {

		log.info("[getZonePositionWS] " + request.toString());
		int zoneId = 0;
		String lgpla;
		Response<String> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		String req;
		req = request.getLsObject().toString();
		req = req.replaceAll("=", ":");

		JSONObject jsonObj = new JSONObject(req);

		try {
			zoneId = jsonObj.getInt("zoneId");
		} catch (JSONException e) {
			log.log(Level.SEVERE, "[getTasksWS] wrong zonId");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		try {

			lgpla = jsonObj.getString("lgpla");
		} catch (JSONException e) {
			log.log(Level.SEVERE, "[getTasksWS] Missing lgpla");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new ConciliacionDao().getZonePosition(zoneId, lgpla);

	}

	public Response<TaskBean> getSpecialSAPCount(Request request, User user) {
		log.info("[getSpecialSAPCount] " + request.toString());
		Response<TaskBean> res = new Response<>();
		TaskBean taskBean = gson.fromJson(gson.toJson(request.getLsObject()), TaskBean.class);
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		res.setAbstractResult(abstractResult);
		DocInvBean docInvBean = new DocInvBean();
		HashMap<String, LgplaValuesBean> mapLVB = new HashMap<>();

		try {

			List<String> listUser = new TaskDao().getUsersFromTaskGroup(taskBean.getGroupId());
			if (!listUser.isEmpty()) {
				docInvBean = taskBean.getDocInvId();
				docInvBean.setCreatedBy(user.getEntity().getIdentyId());
				String[] arrMatnr = taskBean.getTaskJSON().split("\\|");
				taskBean.setTaskJSON(null);
				Response<DocInvBean> resDao = new DocInvDao().addDocInv(docInvBean, docInvBean.getCreatedBy());
				if (resDao.getAbstractResult().getResultId() == ReturnValues.ISUCCESS) {
					// INICIO foto inventario
					new SapConciliationWorkService().WS_RuntimeInventorySnapShot(resDao.getLsObject());
					// FIN foto inventario
					taskBean.setDocInvId(resDao.getLsObject());
					Response<TaskBean> resTask = new TaskWorkService().addTaskSpecial(taskBean, user);
					if (resTask.getAbstractResult().getResultId() == ReturnValues.ISUCCESS) {
						Request<?> req = new Request<>();
						LoginBean<?> tokenObject = new LoginBean<>();
						req.setTokenObject(tokenObject);
						req.getTokenObject().setLoginId(listUser.get(0));
						Response<RouteUserBean> respRUB = routeWorkService.getRoutesByUserSAP(req);
						RouteUserBean rub = respRUB.getLsObject();
						for (RouteUserPositionBean rubPos : rub.getPositions()) {
							for (ZoneUserPositionsBean zonePos : rubPos.getZone().getPositionsB()) {
								mapLVB = new HashMap<>();
								zonePos.setLgplaValues(mapLVB);
								for (String matnr : arrMatnr) {
									if (!matnr.isEmpty()) {
										LgplaValuesBean lgplaVB = new LgplaValuesBean();
										lgplaVB.setMatnr(matnr.split("\\#")[0]);
										lgplaVB.setMatkx(matnr.split("\\#")[1]);
										lgplaVB.setLocked(true);
										zonePos.getLgplaValues().put(zonePos.getPkAsgId() + matnr.split("\\#")[0],
												lgplaVB);
									}

								}

							}
						}
						resTask.getLsObject().setRub(rub);
						Response<TaskBean> resSAPTask = new TaskDao().addTask(resTask.getLsObject(),
								user.getEntity().getIdentyId());
						if (resSAPTask.getAbstractResult().getResultId() == ReturnValues.ISUCCESS) {
							res = resSAPTask;
						} else {
							abstractResult.setResultId(resSAPTask.getAbstractResult().getResultId());
							abstractResult.setResultMsgAbs(resSAPTask.getAbstractResult().getResultMsgAbs());
						}

					} else {
						abstractResult.setResultId(resTask.getAbstractResult().getResultId());
						abstractResult.setResultMsgAbs(resTask.getAbstractResult().getResultMsgAbs());
					}
				} else {
					abstractResult.setResultId(resDao.getAbstractResult().getResultId());
					abstractResult.setResultMsgAbs(resDao.getAbstractResult().getResultMsgAbs());
				}
			} else {
				abstractResult.setResultId(ReturnValues.IERROR);
				abstractResult.setResultMsgAbs("No existen usuarios en el grupo. favor de validar");
			}
		} catch (Exception e) {
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			e.printStackTrace();

		}
		return res;
	}

}
