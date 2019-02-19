package com.gmodelo.cyclicinventories.workservice;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.ConciliationsIDsBean;
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.DocInvBeanHeaderSAP;
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
import com.gmodelo.cyclicinventories.dao.SapOperationDao;
import com.gmodelo.cyclicinventories.dao.TaskDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;

public class ConciliationSAPWorkService {

	private Logger log = Logger.getLogger(ConciliacionWorkService.class.getName());
	private Gson gson = new Gson();
	private RouteWorkService routeWorkService = new RouteWorkService();

	@SuppressWarnings("rawtypes")
	public Response<DocInvBeanHeaderSAP> saveConciliation(Request request, String userId) {

		log.info("[saveConciliation] " + request.toString());

		Response<DocInvBeanHeaderSAP> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		DocInvBeanHeaderSAP dibhSAP;

		try {
			dibhSAP = gson.fromJson(gson.toJson(request.getLsObject()), DocInvBeanHeaderSAP.class);
		} catch (JSONException e) {
			log.log(Level.SEVERE, "[saveConciliation] Objeto no válido.");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new SapOperationDao().saveConciliationSAP(dibhSAP, userId);
	}

	public Response<List<ConciliationsIDsBean>> getClosedConciliationIDs(Request<?> request) {

		log.info("[getClosedConciliationIDs] " + request.toString());
		Response<List<ConciliationsIDsBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		DocInvBean docInv;

		try {
			docInv = gson.fromJson(gson.toJson(request.getLsObject()), DocInvBean.class);
		} catch (JSONException e) {
			log.log(Level.SEVERE, "[getClosedConciliationIDs] Objeto no válido.");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new ConciliacionDao().getClosedConciliationIDs(docInv);
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
				docInvBean.setSapRecount(true);
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
						rub.setSapSpecial(true);//indicador de tarea de reconteo SAP
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
