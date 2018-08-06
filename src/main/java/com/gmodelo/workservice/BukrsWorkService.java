package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.BukrsBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.BukrsDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class BukrsWorkService {

	private Logger log = Logger.getLogger( BukrsWorkService.class.getName());
	
	public Response<List<BukrsBean>> getBukrsWerks(Request request){
		log.log(Level.WARNING,"[BukrsWorkService] "+request.toString());
		BukrsBean bukrsBean;
		Response<List<BukrsBean>> res = new Response<List<BukrsBean>>();
		try {
			bukrsBean = new Gson().fromJson(request.getLsObject().toString(), BukrsBean.class) ;
			
			log.log(Level.WARNING,request.getLsObject().toString());
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"Error al pasar de Json a BukrsBean");
			bukrsBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult );
			return res;
		}
		return new BukrsDao().getBukrsWithWerks(bukrsBean);
	}
}
