package com.gmodelo.cyclicinventories.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.MatnrBeanView;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.TmatnrBean;
import com.gmodelo.cyclicinventories.dao.MatnrDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class MatnrWorkService {
	private Logger log = Logger.getLogger( MatnrWorkService.class.getName());
	private Gson gson = new Gson();
	
	public Response<List<MatnrBeanView>> getMatnr(Request request){
		log.info("[getMatnrWorkService] "+request.toString());
		MatnrBeanView mantrBean  = null;
		Response<List<MatnrBeanView>> res = new Response<>();
		String req = request.getLsObject().toString().trim();
		
		if(!req.isEmpty()){
			try {
				mantrBean = gson.fromJson(gson.toJson(request.getLsObject()), MatnrBeanView.class);
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
			log.info("[getMatnrWorkService] Fue cadena vacía ");
		}
		
		return new MatnrDao().getMatnr(mantrBean);
	}
	
	public Response<List<TmatnrBean>> getTmatnr(Request request){
		log.info("[getTmatnrWorkService] "+request.toString());
		TmatnrBean tmatnrBean = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		
		if(!req.isEmpty()){
			try {
				tmatnrBean = gson.fromJson(gson.toJson(request.getLsObject()), TmatnrBean.class);
				log.info("[getTmatnrWorkService] Fue Objeto: " + tmatnrBean);
				
			}catch(JsonSyntaxException e){
			searchFilter = request.getLsObject().toString().trim();
			log.info("[getTmatnrWorkService] Intentando por String ");
		}
		
		}else{
			searchFilter = "";
			log.info("[getTmatnrWorkService] Fue cadena vacía ");
		}
		
		return new MatnrDao().getTmatnrWithMatnr(tmatnrBean,searchFilter);
	}

}
