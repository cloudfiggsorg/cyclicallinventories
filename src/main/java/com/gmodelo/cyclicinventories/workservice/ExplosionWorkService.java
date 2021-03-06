package com.gmodelo.cyclicinventories.workservice;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.ExplosionDetail;
import com.gmodelo.cyclicinventories.beans.MatExplReport;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.ExplosionDetailDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ExplosionWorkService {
	
	private Logger log = Logger.getLogger(ZoneWorkService.class.getName());
	private Gson gson = new Gson();
	
	public Response<ArrayList<ExplosionDetail>> getLsExplosionDetail(Request request) {
		log.info("[getLsExplosionDetail] " + request.toString());
		ExplosionDetail ed = null;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<ArrayList<ExplosionDetail>> res = new Response<>();
		try {
			ed = gson.fromJson(gson.toJson(request.getLsObject()), ExplosionDetail.class);
			res = new ExplosionDetailDao().getExplosionDetailByMatnr(ed);
		} catch (Exception e) {
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			log.info("[getLsExplosionDetail] Ocurrió un problema al intentar obtener la información.");
			res.setAbstractResult(abstractResult);
		}

		return res;

	}

	@SuppressWarnings("unchecked")
	public Response saveExplosionDetail(Request request, User user) {
		
		log.info("[saveExplosionDetail] " + request.toString());
		ArrayList<ExplosionDetail> lsEd = null;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<?> res = new Response<>();
		try {
			lsEd = gson.fromJson(gson.toJson(request.getLsObject()), new TypeToken<ArrayList<ExplosionDetail>>(){}.getType());
			res = new ExplosionDetailDao().saveExplosionDetail(lsEd, user.getEntity().getIdentyId());
		} catch (Exception e) {
			log.info("[saveExplosionDetail] Error al convertir json a objeto");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
		}

		return res;
	}
	
	public Response<ArrayList<MatExplReport>> getExplosionReportByWerks(Request request) {
		log.info("[getExplosionReportByWerks] " + request.toString());
		int docInvId = 0;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<ArrayList<MatExplReport>> res = new Response<>();
		try {
			docInvId = Integer.parseInt(request.getLsObject().toString());
			res = new ExplosionDetailDao().getExplosionReportByWerks(docInvId);
		} catch (Exception e) {
			log.info("[getExplosionReportByWerks] Error al convertir json a objeto");
			e.printStackTrace();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);	
		}

		return res;

	}
	
	public Response<ArrayList<MatExplReport>> getExplosionReportByLgpla(Request request) {
		log.info("[getExplosionReportByLgpla] " + request.toString());
		int docInvId = 0;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<ArrayList<MatExplReport>> res = new Response<>();
		try {
			docInvId = Integer.parseInt(request.getLsObject().toString());
			res = new ExplosionDetailDao().getExplosionReportByLgpla(docInvId);
		} catch (Exception e) {
			log.info("[getExplosionReportByLgpla] Error al convertir json a objeto");
			e.printStackTrace();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);	
		}

		return res;
	}
	
}
