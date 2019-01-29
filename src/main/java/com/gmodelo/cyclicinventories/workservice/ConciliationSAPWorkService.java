package com.gmodelo.cyclicinventories.workservice;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.ConciliationsIDsBean;
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.DocInvBeanHeaderSAP;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.ConciliacionDao;
import com.gmodelo.cyclicinventories.dao.SapConciliationDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;

public class ConciliationSAPWorkService {

	private Logger log = Logger.getLogger(ConciliacionWorkService.class.getName());
	private Gson gson = new Gson();

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

		return new SapConciliationDao().saveConciliationSAP(dibhSAP, userId);
	}

	public Response<List<ConciliationsIDsBean>> getClosedConciliationIDs(Request request) {

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

	public Response<String> getjsFileBase64(Request<ArrayList<String>> request) {

		log.info("[getjsFileBase64] " + request.toString());
		Response<String> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		int docInvId = 0;
		int jsId = 0;
		String fileName = "";

		try {
			docInvId = Integer.parseInt(request.getLsObject().get(0));
			jsId = Integer.parseInt(request.getLsObject().get(1));
			fileName = (String) request.getLsObject().get(2);
		} catch (JSONException e) {
			log.log(Level.SEVERE, "[getjsFileBase64] Objeto no válido.");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new SapConciliationDao().getjsFileBase64(docInvId, jsId, fileName);
	}

}
