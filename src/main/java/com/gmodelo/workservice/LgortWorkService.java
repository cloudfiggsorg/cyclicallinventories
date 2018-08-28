package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.LgortBeanView;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.LgortDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LgortWorkService {

	private Logger log = Logger.getLogger( LgortWorkService.class.getName());
	
	public Response<List<LgortBeanView>> getLgortByWerks(Request request){
		
		log.log(Level.WARNING,"[LgortWorkService] "+request.toString());
		LgortBeanView lgortBean;
		Response<List<LgortBeanView>> res = new Response<List<LgortBeanView>>();
		
		try {
			lgortBean = new Gson().fromJson(request.getLsObject().toString(), LgortBeanView.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[LgortWorkService] Error al pasar de Json a LgortBean");
			lgortBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		return new LgortDao().getLgortByWerks(lgortBean);
		
	}
	
	public Response<List<LgortBeanView>> getNgorts(Request request){
		
		log.log(Level.WARNING,"[NgortWorkService] "+request.toString());
		LgortBeanView lgortBeanView;
		Response<List<LgortBeanView>> res = new Response<List<LgortBeanView>>();
		
		try {
			lgortBeanView = new Gson().fromJson(request.getLsObject().toString(), LgortBeanView.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[NgortWorkService] Error al pasar de Json a NgortBean");
			lgortBeanView = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		return new LgortDao().getNgorts(lgortBeanView);
		
	}
}
