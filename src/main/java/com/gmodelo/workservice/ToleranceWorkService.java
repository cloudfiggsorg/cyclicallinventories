package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.ToleranceBean;
import com.gmodelo.dao.ToleranceDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ToleranceWorkService {

	private Logger log = Logger.getLogger(ToleranceWorkService.class.getName());
	
	public Response<Object> addTolerance(Request request, User user){
		
		log.log(Level.WARNING,"[addToleranceWorkService] "+request.toString());
		ToleranceBean toleranceBean;
		Response<Object> res = new Response<Object>();
		
		try {
			toleranceBean = new Gson().fromJson(request.getLsObject().toString(), ToleranceBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[addToleranceWS] Error al pasar de Json a ToleranceBean",e);
			toleranceBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
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
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		
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
		Response<List<ToleranceBean>> res = new Response<>();
		ToleranceBean tb = new ToleranceBean();
		
		try {
			tb = new Gson().fromJson(request.getLsObject().toString(), ToleranceBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[getTolerancesWS] Error al pasar de Json a ZoneB");
			tb = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		return new ToleranceDao().getTolerances(tb);
	}
}
