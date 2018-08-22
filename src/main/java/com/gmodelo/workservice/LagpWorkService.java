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
		log.log(Level.WARNING,"[getLagpWorkService] "+request.toString());
		LagpB lgplaBean = null;
		String searchFilter = null;
		Response<List<LagpB>> res = new Response<List<LagpB>>();
		String req = request.getLsObject().toString().trim();
		if(!req.isEmpty()){
			try {
				lgplaBean = new Gson().fromJson(request.getLsObject().toString(), LagpB.class) ;
				
				log.log(Level.WARNING,request.getLsObject().toString());
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString().trim();
				log.log(Level.WARNING, "[getLagpWorkService] jsyn Intentando por String ");
			}
		}else{
			searchFilter = "";
			log.log(Level.WARNING, "[getLagpWorkService] Fue cadena vacía ");
		}
		
		return new LagpDao().getLagp(lgplaBean, searchFilter);
	}

}
