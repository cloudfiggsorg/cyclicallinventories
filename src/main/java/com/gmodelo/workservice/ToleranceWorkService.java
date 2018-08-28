package com.gmodelo.workservice;

import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.ToleranceBean;
import com.gmodelo.dao.ToleranceDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

public class ToleranceWorkService {

	private Logger log = Logger.getLogger(ToleranceWorkService.class.getName());
	
	public Response<List<ToleranceBean>> getMATKL(Request request){
		
		log.log(Level.WARNING,"[getMATKLWS] "+request.toString());
		Response<List<ToleranceBean>> res = new Response<>();
		ToleranceBean tb = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		
		if(!req.isEmpty()){
			try {
				tb = new Gson().fromJson(request.getLsObject().toString(), ToleranceBean.class);
				log.log(Level.WARNING, "[getMATKLWS] Fue Objeto: " + tb);
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString().trim();
				log.log(Level.WARNING, "[getMATKLWS] jsyn Intentando por String ");
			}
		}else{
			searchFilter = "";
			log.log(Level.WARNING, "[getMATKLWS] Fue cadena vacía ");
		}
		return new ToleranceDao().getMATKL(tb, searchFilter);
	}
	
	public Response<Object> addTolerance(Request request, User user){
		
		log.log(Level.WARNING,"[addToleranceWorkService] "+request.toString());
		ToleranceBean toleranceBean = null;
		Response<Object> res = new Response<Object>();
		
		try {
			toleranceBean = new Gson().fromJson(request.getLsObject().toString(), ToleranceBean.class);
			log.log(Level.WARNING,"[addToleranceWS] ");
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[addToleranceWS] Error al pasar de Json a ToleranceBean",e);
			toleranceBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		return new ToleranceDao().addTolerance(toleranceBean, user.getEntity().getIdentyId());
		
		}
	
	public Response<Object> deleteTolerance(Request request){
		
		log.log(Level.WARNING,"[deleteToleranceWS] "+request.toString());
		String arrayIdTolerances;
		Response<Object> res = new Response<Object>();
		AbstractResults abstractResult = new AbstractResults();
		
		try {
			arrayIdTolerances = request.getLsObject().toString();
			
			if(arrayIdTolerances == null || arrayIdTolerances.isEmpty()){
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs("NULL OR EMPTY ARRAY");
				res.setAbstractResult(abstractResult);
				return res;
			}
			
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[deleteToleranceWS] Error al pasar de Json a String");
			
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
	
		return new ToleranceDao().deleteTolerance(arrayIdTolerances);
	
	}
	
	public Response<List<ToleranceBean>> getTolerances(Request request){
		
		log.log(Level.WARNING,"getTolerancesWS] "+request.toString());
		ToleranceBean tb = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		
		if(!req.isEmpty()){
			try {
				tb = new Gson().fromJson(request.getLsObject().toString(), ToleranceBean.class);
				log.log(Level.WARNING, "[getTolerancesWS] Fue Objeto: " + tb);
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString().trim();
				log.log(Level.WARNING, "[getTolerancesWS] jsyn Intentando por String ");
			}
		}else{
			searchFilter = "";
			log.log(Level.WARNING, "[getTolerancesWS] Fue cadena vacía ");
		}
		

		return new ToleranceDao().getTolerances(tb, searchFilter);
	}
}
