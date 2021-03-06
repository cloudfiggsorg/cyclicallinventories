package com.gmodelo.cyclicinventories.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.TgortB;
import com.gmodelo.cyclicinventories.dao.TgortDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class TgortWorkService {
	private Logger log = Logger.getLogger( TgortWorkService.class.getName());
	private Gson gson = new Gson();
	
	public Response<List<TgortB>> getTgort(Request request){
		log.info("[TgortWorkService] "+request.toString());
		TgortB tgortBean;
		Response<List<TgortB>> res = new Response<>();
		try {
			
			tgortBean = gson.fromJson(gson.toJson(request.getLsObject()), TgortB.class) ;
		} catch (JsonSyntaxException e	) {
			log.log(Level.SEVERE,"Error al pasar de Json a TgortBean", e);
			tgortBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult );
			return res;
		}
		return new TgortDao().getTgortWithNgort(tgortBean);
	}


}
