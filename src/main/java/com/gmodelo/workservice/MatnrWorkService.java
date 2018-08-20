package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.MantrB;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TmatnrB;
import com.gmodelo.dao.MatnrDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class MatnrWorkService {
private Logger log = Logger.getLogger( MatnrWorkService.class.getName());
	
	public Response<List<MantrB>> getMatnr(Request request){
		log.log(Level.WARNING,"[getMatnrWorkService] "+request.toString());
		MantrB mantrBean;
		Response<List<MantrB>> res = new Response<List<MantrB>>();
		try {
			mantrBean = new Gson().fromJson(request.getLsObject().toString(), MantrB.class) ;
			
			log.log(Level.WARNING,request.getLsObject().toString());
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[getMatnrWorkService] Error al pasar de Json a MantrB");
			mantrBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult );
			return res;
		}
		return new MatnrDao().getMatnr(mantrBean);
	}
	
	public Response<List<TmatnrB>> getTmatnr(Request request){
		log.log(Level.WARNING,"[getTmatnrWorkService] "+request.toString());
		TmatnrB tmatnrBean;
		String searchFilter = null;
		Response<List<TmatnrB>> res = new Response<List<TmatnrB>>();
		try {
			tmatnrBean = new Gson().fromJson(request.getLsObject().toString(), TmatnrB.class) ;
			
			log.log(Level.WARNING,request.getLsObject().toString());
		} catch (JsonSyntaxException e) {
			tmatnrBean = new TmatnrB();
			try {
				searchFilter = new Gson().fromJson(request.getLsObject().toString(), String.class) ;
				
				log.log(Level.WARNING,searchFilter);
			} catch (JsonSyntaxException e1) {
				log.log(Level.SEVERE,"[getTmatnrWorkService] Error al parsear el Json");
				tmatnrBean = null;
				AbstractResults abstractResult = new AbstractResults();
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult );
				return res;
			}
			
			
		}
		return new MatnrDao().getTmatnrWithMatnr(tmatnrBean,searchFilter);
	}

}
