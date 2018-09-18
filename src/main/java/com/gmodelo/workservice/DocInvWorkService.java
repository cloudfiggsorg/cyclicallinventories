package com.gmodelo.workservice;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.DocInvDao;
import com.gmodelo.dao.ZoneDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class DocInvWorkService {
	
	private Logger log = Logger.getLogger(DocInvWorkService.class.getName());
	Gson gson = new Gson();
	
	public Response<Object> addDocInv(Request request, User user){
	
		log.info("[addDocInvWS] "+request.toString());
		DocInvBean docInvBean;
		Response<Object> res = new Response<Object>();
		
		try {
			docInvBean = gson.fromJson(gson.toJson(request.getLsObject()), DocInvBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[addDocInvWS] Error al pasar de Json a DocInvB");
			docInvBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		return null;
	//	return new DocInvDao().addDocInv(docInvBean, user.getEntity().getIdentyId());
		
	}

}
