package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RfcTablesB;
import com.gmodelo.beans.TmatnrB;
import com.gmodelo.dao.RfcTablesDao;
import com.gmodelo.dao.TmatnrDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class RfcTablesWorkService {
	
	private Logger log = Logger.getLogger( RfcTablesWorkService.class.getName());
	
	public Response<List<RfcTablesB>> getRfcTables(Request request){
		log.log(Level.WARNING,"[RfcTablesWorkService] "+request.toString());
		RfcTablesB rfcBean;
		Response<List<RfcTablesB>> res = new Response<List<RfcTablesB>>();
		try {
			rfcBean = new Gson().fromJson(request.getLsObject().toString(), RfcTablesB.class) ;
			
			log.log(Level.WARNING,request.getLsObject().toString());
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"Error al pasar de Json a TmatnrBean");
			rfcBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult );
			return res;
		}
		return new RfcTablesDao().getRfcTables(rfcBean);
	}


}
