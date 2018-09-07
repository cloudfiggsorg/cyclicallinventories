package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.Lgnum;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.LgnumDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LgnumWorkService {
	
	private Logger log = Logger.getLogger(LgnumWorkService.class.getName());
	Gson gson = new Gson();
	
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
