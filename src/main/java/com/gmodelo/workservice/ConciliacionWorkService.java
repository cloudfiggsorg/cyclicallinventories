package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.ConciliacionBean;
import com.gmodelo.beans.ConciliationsIDsBean;
import com.gmodelo.beans.GroupBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TaskBean;
import com.gmodelo.dao.ConciliacionDao;
import com.gmodelo.dao.TaskDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ConciliacionWorkService {
	
	private Logger log = Logger.getLogger(ConciliacionWorkService.class.getName());
	Gson gson = new Gson();
	
	public Response<List<ConciliationsIDsBean>> getConciliationIDs(){
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
	
	public Response<String> getZonePosition(Request request){
		
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
