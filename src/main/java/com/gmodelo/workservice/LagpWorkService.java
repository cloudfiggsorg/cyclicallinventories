package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.LagpB;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.LagpDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LagpWorkService {
	private Logger log = Logger.getLogger( BukrsWorkService.class.getName());
	
	public Response<List<LagpB>> getLagp(Request request){
		log.log(Level.WARNING,"[LagpWorkService] "+request.toString());
		LagpB lgplaBean;
		Response<List<LagpB>> res = new Response<List<LagpB>>();
		try {
			lgplaBean = new Gson().fromJson(request.getLsObject().toString(), LagpB.class) ;
			
			log.log(Level.WARNING,request.getLsObject().toString());
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"Error al pasar de Json a LagpBean");
			lgplaBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult );
			return res;
		}
		return new LagpDao().getLagp(lgplaBean);
	}

}
