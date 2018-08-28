package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TgortB;
import com.gmodelo.dao.TgortDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class TgortWorkService {
	private Logger log = Logger.getLogger( TgortWorkService.class.getName());
	
	public Response<List<TgortB>> getTgort(Request request){
		log.log(Level.WARNING,"[TgortWorkService] "+request.toString());
		TgortB tgortBean;
		Response<List<TgortB>> res = new Response<List<TgortB>>();
		try {
			tgortBean = new Gson().fromJson(request.getLsObject().toString(), TgortB.class) ;
			
			log.log(Level.WARNING,request.getLsObject().toString());
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"Error al pasar de Json a TgortBean");
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
