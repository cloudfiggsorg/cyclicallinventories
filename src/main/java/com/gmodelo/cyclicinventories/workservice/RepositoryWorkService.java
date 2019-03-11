package com.gmodelo.cyclicinventories.workservice;

import java.util.List;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.Repository;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.RepositoryDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;

public class RepositoryWorkService {
	
	private Logger log = Logger.getLogger(RepositoryWorkService.class.getName());
	private Gson gson = new Gson();

	public Response<Repository> saveOption(Request<?> request) {
		log.info("[saveOptionWS] " + request.toString());
		Repository option = null;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<Repository> res = new Response<>();
		try {
			option = gson.fromJson(gson.toJson(request.getLsObject()), Repository.class);
			res = new RepositoryDao().saveOption(option);
		} catch (Exception e) {
			log.info("[saveOptionWS] Error al convertir a objeto.");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);			
		}

		return res;
	}

	public Response<Object> deleteOptions(Request<?> request) {
		log.info("[deleteOptionsWS] " + request.toString());
		String ids = null;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<Object> res = new Response<>();
		try {
			ids = request.getLsObject().toString();
			res = new RepositoryDao().deleteOptions(ids);
		} catch (Exception e) {
			log.info("[deleteOptionsWS] Error al obtener los Ids");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);			
		}

		return res;
	}
	
	public Response<List<Repository>> getOptions() {
		
		log.info("[getOptionsWS]... ");
		Response<List<Repository>> res = new RepositoryDao().getOptions();
		return res;
	}

}
