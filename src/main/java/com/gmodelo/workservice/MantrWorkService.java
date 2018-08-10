package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.MantrB;
import com.gmodelo.dao.MantrDao;
import com.gmodelo.dao.TgortDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class MantrWorkService {
private Logger log = Logger.getLogger( MantrWorkService.class.getName());
	
	public Response<List<MantrB>> getMantr(Request request){
		log.log(Level.WARNING,"[MantrWorkService] "+request.toString());
		MantrB mantrBean;
		Response<List<MantrB>> res = new Response<List<MantrB>>();
		try {
			mantrBean = new Gson().fromJson(request.getLsObject().toString(), MantrB.class) ;
			
			log.log(Level.WARNING,request.getLsObject().toString());
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"Error al pasar de Json a MantrB");
			mantrBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult );
			return res;
		}
		return new MantrDao().getMantr(mantrBean);
	}

}
