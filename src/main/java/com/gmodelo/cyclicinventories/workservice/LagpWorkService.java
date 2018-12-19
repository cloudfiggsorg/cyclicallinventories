package com.gmodelo.cyclicinventories.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.LagpEntity;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.LagpDao;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LagpWorkService {
	private Logger log = Logger.getLogger( BukrsWorkService.class.getName());
	private Gson gson = new Gson();
	
	public Response<List<LagpEntity>> getLagp(Request request){
		log.info("[getLagpWorkService] "+request.toString());
		LagpEntity lgplaBean = null;
		String searchFilter = null;
		Response<List<LagpEntity>> res = new Response<>();
		String req = request.getLsObject().toString().trim();
		if(!req.isEmpty()){
			try {
				lgplaBean = gson.fromJson(gson.toJson(request.getLsObject()), LagpEntity.class);
				log.info("Fue objeto");
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString().trim();
				log.log(Level.WARNING, "[getLagpWorkService] Intentando por String ");
			}
		}else{
			searchFilter = "";
			log.info("[getLagpWorkService] Fue cadena vacía ");
		}
		
		return new LagpDao().getLagp(lgplaBean, searchFilter);
	}

}
