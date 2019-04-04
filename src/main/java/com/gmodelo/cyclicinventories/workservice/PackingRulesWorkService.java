package com.gmodelo.cyclicinventories.workservice;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.ExplosionDetail;
import com.gmodelo.cyclicinventories.beans.ExplosionDetailByPackingRule;
import com.gmodelo.cyclicinventories.beans.PackingRule;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.ExplosionDetailByPackingRuleDao;
import com.gmodelo.cyclicinventories.dao.ExplosionDetailDao;
import com.gmodelo.cyclicinventories.dao.PackingRuleDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PackingRulesWorkService {
	
	private Logger log = Logger.getLogger(PackingRulesWorkService.class.getName());
	private Gson gson = new Gson();

	public Response<List<PackingRule>> getPackingRules(Request request) {
		
		log.info("[getPackingRules] " + request.toString());
		String matnr = null;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<List<PackingRule>> res = new Response<>();
		try {
			matnr = request.getLsObject().toString();
			res = new PackingRuleDao().getPackingRuleByMatnr(matnr);
		} catch (Exception e) {
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			log.info("[getPackingRules] Ocurrió un problema al intentar obtener la información.");
			res.setAbstractResult(abstractResult);
		}

		return res;
	}
	
	public Response<List<ExplosionDetailByPackingRule>> getPackingRuleByMatnr(Request<List<Object>> request) {
		
		log.info("[getPackingRuleByMatnr] " + request.toString());
		String werks, matnr, packingRuleId = null;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<List<ExplosionDetailByPackingRule>> res = new Response<>();
		try {
			werks = (String)request.getLsObject().get(0);
			matnr = (String)request.getLsObject().get(1);
			packingRuleId = (String)request.getLsObject().get(2);
			res = new ExplosionDetailByPackingRuleDao().getPackingRuleByMatnr(werks, matnr, packingRuleId);
		} catch (Exception e) {
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			log.info("[getPackingRuleByMatnr] Ocurrió un problema al intentar obtener la información.");
			res.setAbstractResult(abstractResult);
		}

		return res;
	}
	
	@SuppressWarnings("unchecked")
	public Response savePackingRuleByMatnr(Request request, User user) {
		
		log.info("[savePackingRuleByMatnr] " + request.toString());
		ArrayList<ExplosionDetailByPackingRule> lsEdbpr = null;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<?> res = new Response<>();
		try {
			lsEdbpr = gson.fromJson(gson.toJson(request.getLsObject()), new TypeToken<ArrayList<ExplosionDetailByPackingRule>>(){}.getType());
			res = new ExplosionDetailByPackingRuleDao().saveExplosionDetailByPackingRule(lsEdbpr, user.getEntity().getIdentyId());
		} catch (Exception e) {
			log.info("[savePackingRuleByMatnr] Error al convertir json a objeto");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
		}

		return res;
	}

}
