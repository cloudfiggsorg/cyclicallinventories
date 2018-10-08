package com.gmodelo.workservice;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.ConciliacionBean;
import com.gmodelo.beans.ConciliationsIDsBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.GroupBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TaskBean;
import com.gmodelo.dao.ConciliacionDao;
import com.gmodelo.dao.DocInvDao;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;

public class ConciliacionWorkService {

	private Logger log = Logger.getLogger(ConciliacionWorkService.class.getName());
	Gson gson = new Gson();

	public Response<List<ConciliationsIDsBean>> getConciliationIDs() {
		return new ConciliacionDao().getConciliationIDs();
	}

	public Response<List<ConciliacionBean>> getConciliacion(Request request) {

		log.info("[getConciliacionWS] " + request.toString());
		ConciliacionBean tb = new Gson().fromJson(request.getLsObject().toString(), ConciliacionBean.class);
		Response<List<ConciliacionBean>> res = new Response<List<ConciliacionBean>>();
		return new ConciliacionDao().getConciliacion(tb);
	}

	public Response<List<GroupBean>> getAvailableGroups(Request request) {

		log.info("[getAvailableGroupsWS] " + request.toString());
		String req = request.getLsObject().toString().trim();
		Response<List<GroupBean>> res = new Response<List<GroupBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		int docInvId;

		try {
			docInvId = Integer.parseInt(req);
		} catch (NumberFormatException e) {
			log.log(Level.SEVERE, "[getAvailableGroupsWS] Cadena no válida");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new ConciliacionDao().getAvailableGroups(docInvId);
	}

	public Response<TaskBean> getFatherTaskByDocId(Request request) {

		log.info("[getFatherTaskByDocIdWS] " + request.toString());
		String req = request.getLsObject().toString().trim();
		Response<TaskBean> res = new Response<TaskBean>();
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
		TaskBean taskBean = new Gson().fromJson(request.getLsObject().toString(), TaskBean.class);
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

		}
		return res;
	}

	public Response<String> getZonePosition(Request request) {

		log.info("[getZonePositionWS] " + request.toString());
		int zoneId = 0;
		String lgpla;
		Response<String> res = new Response<String>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		String req;
		req = request.getLsObject().toString();
		req = req.replaceAll("=", ":");

		JSONObject jsonObj = new JSONObject(req);

		System.out.println(jsonObj.getInt("zoneId"));

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

}
