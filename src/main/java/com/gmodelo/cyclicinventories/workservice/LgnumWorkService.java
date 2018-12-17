package com.gmodelo.cyclicinventories.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.Lgnum;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.LgnumDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LgnumWorkService {
	
	private Logger log = Logger.getLogger(LgnumWorkService.class.getName());
	private Gson gson = new Gson();
	
	public Response<List<Lgnum>> getLgnumByLgort(Request request){
		
		log.info("[getLgnumByLgort] "+request.toString());
		Lgnum lgnum;
		Response<List<Lgnum>> res = new Response<List<Lgnum>>();
		
		try {
			lgnum = gson.fromJson(gson.toJson(request.getLsObject()), Lgnum.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[getLgnumByLgort] Error al pasar de Json a LgortBean");
			lgnum = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		return new LgnumDao().getLgnumByLgort(lgnum);
		
	}

}
