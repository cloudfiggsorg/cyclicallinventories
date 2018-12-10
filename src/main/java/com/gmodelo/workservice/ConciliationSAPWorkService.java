package com.gmodelo.workservice;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.ConciliationsIDsBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.DocInvBeanHeaderSAP;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.ConciliacionDao;
import com.gmodelo.dao.SapConciliationDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;

public class ConciliationSAPWorkService {

	private Logger log = Logger.getLogger(ConciliacionWorkService.class.getName());
	Gson gson = new Gson();

	@SuppressWarnings("rawtypes")
	public Response saveConciliation(Request request, String userId) {

		log.info("[saveConciliation] " + request.toString());

		Response res = new Response();
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
		String req = request.getLsObject().toString().trim();
		Response<List<ConciliationsIDsBean>> res = new Response<List<ConciliationsIDsBean>>();
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
		Response<String> res = new Response<String>();
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
