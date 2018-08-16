package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.LgortBean;
import com.gmodelo.beans.NgortB;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.LgortDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LgortWorkService {

	private Logger log = Logger.getLogger( LgortWorkService.class.getName());
	
	public Response<List<LgortBean>> getLgortByWerks(Request request){
		
		log.log(Level.WARNING,"[LgortWorkService] "+request.toString());
		LgortBean lgortBean;
		Response<List<LgortBean>> res = new Response<List<LgortBean>>();
		
		try {
			lgortBean = new Gson().fromJson(request.getLsObject().toString(), LgortBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[LgortWorkService] Error al pasar de Json a LgortBean");
			lgortBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		return new LgortDao().getLgortByWerks(lgortBean);
		
	}
	
	public Response<List<NgortB>> getNgorts(Request request){
		
		log.log(Level.WARNING,"[NgortWorkService] "+request.toString());
		NgortB ngortBean;
		Response<List<NgortB>> res = new Response<List<NgortB>>();
		
		try {
			ngortBean = new Gson().fromJson(request.getLsObject().toString(), NgortB.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[NgortWorkService] Error al pasar de Json a NgortBean");
			ngortBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		return new LgortDao().getNgorts(ngortBean);
		
	}
}
