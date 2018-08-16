package com.gmodelo.workservice;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.DocInvB;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.DocInvDao;
import com.gmodelo.dao.ZoneDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class DocInvWorkService {
	
	private Logger log = Logger.getLogger(DocInvWorkService.class.getName());
	
	public Response<Object> addDocInv(Request<?> request, User user){
	
		log.log(Level.WARNING,"[addDocInvWS] "+request.toString());
		DocInvB docInvBean;
		Response<Object> res = new Response<Object>();
		
		try {
			docInvBean = new Gson().fromJson(request.getLsObject().toString(), DocInvB.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[addDocInvWS] Error al pasar de Json a DocInvB");
			docInvBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		return new DocInvDao().addDocInv(docInvBean, user.getEntity().getIdentyId());
		
	}

}
