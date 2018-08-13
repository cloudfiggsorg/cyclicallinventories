package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TmatnrB;
import com.gmodelo.dao.TmatnrDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class TmatnrWorkService {
	private Logger log = Logger.getLogger( TmatnrWorkService.class.getName());
	
	public Response<List<TmatnrB>> getTmatnr(Request request){
		log.log(Level.WARNING,"[TmatnrWorkService] "+request.toString());
		TmatnrB tmatnrBean;
		Response<List<TmatnrB>> res = new Response<List<TmatnrB>>();
		try {
			tmatnrBean = new Gson().fromJson(request.getLsObject().toString(), TmatnrB.class) ;
			
			log.log(Level.WARNING,request.getLsObject().toString());
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"Error al pasar de Json a TmatnrBean");
			tmatnrBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult );
			return res;
		}
		return new TmatnrDao().getTmatnrWithMatnr(tmatnrBean);
	}


}
