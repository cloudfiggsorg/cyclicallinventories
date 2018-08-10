package com.gmodelo.workservice;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.GroupBean;
import com.gmodelo.dao.GroupDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GroupWorkService {

	private Logger log = Logger.getLogger(GroupWorkService.class.getName());
	
	public Response<Object> addGroup(Request<?> request, User user){
		
		log.log(Level.WARNING,"[addGroupWS] "+request.toString());
		GroupBean groupBean;
		Response<Object> res = new Response<Object>();
		
		try {
			groupBean = new Gson().fromJson(request.getLsObject().toString(), GroupBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[addGroupWS] Error al pasar de Json a GroupBean");
			groupBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		return new GroupDao().addGroup(groupBean, user.getEntity().getIdentyId());
		
	}

}
