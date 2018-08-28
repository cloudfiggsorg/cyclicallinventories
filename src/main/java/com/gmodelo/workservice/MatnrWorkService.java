package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.MatnrBeanView;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TmatnrB;
import com.gmodelo.dao.MatnrDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class MatnrWorkService {
private Logger log = Logger.getLogger( MatnrWorkService.class.getName());
	
	public Response<List<MatnrBeanView>> getMatnr(Request request){
		log.log(Level.WARNING,"[getMatnrWorkService] "+request.toString());
		MatnrBeanView mantrBean  = null;
		String searchFilter = null;
		Response<List<MatnrBeanView>> res = new Response<List<MatnrBeanView>>();
		String req = request.getLsObject().toString().trim();
		
		if(!req.isEmpty()){
			try {
				mantrBean = new Gson().fromJson(request.getLsObject().toString(), MatnrBeanView.class) ;
				
				log.log(Level.WARNING,request.getLsObject().toString());
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE,"[getMatnrWorkService] Error al pasar de Json a MantrB");
				mantrBean = null;
				AbstractResultsBean abstractResult = new AbstractResultsBean();
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}else{
			searchFilter = "";
			log.log(Level.WARNING, "[getMatnrWorkService] Fue cadena vacía ");
		}
		
		return new MatnrDao().getMatnr(mantrBean, searchFilter);
	}
	
	public Response<List<TmatnrB>> getTmatnr(Request request){
		log.log(Level.WARNING,"[getTmatnrWorkService] "+request.toString());
		TmatnrB tmatnrBean = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		
		if(!req.isEmpty()){
			try {
				tmatnrBean = new Gson().fromJson(request.getLsObject().toString(), TmatnrB.class);
				log.log(Level.WARNING, "[getTmatnrWorkService] Fue Objeto: " + tmatnrBean);
				
			}catch(JsonSyntaxException e){
			searchFilter = request.getLsObject().toString().trim();
			log.log(Level.WARNING, "[getTmatnrWorkService] jsyn Intentando por String ");
		}
		
		}else{
			searchFilter = "";
			log.log(Level.WARNING, "[getTmatnrWorkService] Fue cadena vacía ");
		}
		
			
		
		return new MatnrDao().getTmatnrWithMatnr(tmatnrBean,searchFilter);
	}

}
