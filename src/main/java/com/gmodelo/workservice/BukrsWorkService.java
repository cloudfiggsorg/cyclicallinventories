package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.BukrsBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.BukrsDao;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class BukrsWorkService {

	private Logger log = Logger.getLogger( BukrsWorkService.class.getName());
	
	
	public Response<List<BukrsBean>> getBukrs(Request request){
		log.log(Level.WARNING,"[BukrsWorkService] "+request.toString());
		BukrsBean bukrsBean = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		
		if(!req.isEmpty()){
			
			try {
				
				bukrsBean = new Gson().fromJson(request.getLsObject().toString(),BukrsBean.class);
				log.log(Level.WARNING, "[BukrsWorkService] Fue Objeto: " + request.getLsObject().toString());
				
			}catch (JsonSyntaxException e1){
				
				log.log(Level.WARNING, "[BukrsWorkService] Intentando por String");
				searchFilter = request.getLsObject().toString().trim();
			}
			
		}else{
			
			searchFilter = "";
			log.log(Level.WARNING, "[BukrsWorkService] Fue cadena vacía ");
		}
		
		return new BukrsDao().getBukrs(bukrsBean, searchFilter);
	}
	
	public Response<List<BukrsBean>> getBukrsWerks(Request request){
		log.log(Level.WARNING,"[getBukrsWerksWorkService] "+request.toString());
		BukrsBean bukrsBean = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		
		if(!req.isEmpty()){
			
			try {
				
				bukrsBean = new Gson().fromJson(request.getLsObject().toString(), BukrsBean.class);
				log.log(Level.WARNING, "[getBukrsWerksWorkService] Fue Objeto: " + bukrsBean.toString());
				
			}catch (JsonSyntaxException e1){
				
				log.log(Level.WARNING, "[getBukrsWerksWorkService] Intentando por String");
				searchFilter = request.getLsObject().toString().trim();
			}
			
		}else{
			
			searchFilter = "";
			log.log(Level.WARNING, "[getBukrsWerksWorkService] Fue cadena vacía ");
		}
		
		return new BukrsDao().getBukrsWithWerks(bukrsBean, searchFilter);
	}
}
