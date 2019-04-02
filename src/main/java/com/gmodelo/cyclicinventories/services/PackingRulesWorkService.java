package com.gmodelo.cyclicinventories.services;

import java.util.List;
import java.util.logging.Logger;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.PackingRule;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.PackingRuleDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;

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

}
