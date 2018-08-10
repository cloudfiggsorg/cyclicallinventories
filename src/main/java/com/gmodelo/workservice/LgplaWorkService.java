package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.LgplaB;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.LgplaDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LgplaWorkService {
	private Logger log = Logger.getLogger( BukrsWorkService.class.getName());
	
	public Response<List<LgplaB>> getLagp(Request request){
		log.log(Level.WARNING,"[LgplaWorkService] "+request.toString());
		LgplaB lgplaBean;
		Response<List<LgplaB>> res = new Response<List<LgplaB>>();
		try {
			lgplaBean = new Gson().fromJson(request.getLsObject().toString(), LgplaB.class) ;
			
			log.log(Level.WARNING,request.getLsObject().toString());
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"Error al pasar de Json a LgplaBean");
			lgplaBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult );
			return res;
		}
		return new LgplaDao().getLagp(lgplaBean);
	}

}
