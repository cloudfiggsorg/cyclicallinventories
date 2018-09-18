package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.ConciliacionDao;
import com.gmodelo.dao.DocInvDao;
import com.gmodelo.dao.ToleranceDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class DocInvWorkService {
	
	private Logger log = Logger.getLogger(DocInvWorkService.class.getName());
	Gson gson = new Gson();
	
	public Response<DocInvBean> addDocInv(Request request, User user){
	
		log.info("[addDocInvWS] "+request.toString());
		DocInvBean docInvBean;
		Response<DocInvBean> res = new Response<DocInvBean>();
		
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
		return new DocInvDao().addDocInv(docInvBean, user.getEntity().getIdentyId());
		
	}
	
	public Response<Object> deleteDocInv(Request request) {
		log.info("[deleteDocInvWS] " + request.toString());
		String arrayIdDocInv;
		Response<Object> res = new Response<Object>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		try {
			arrayIdDocInv = request.getLsObject().toString();
			if (arrayIdDocInv == null || arrayIdDocInv.isEmpty()) {
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs("NULL OR EMPTY ARRAY");
				res.setAbstractResult(abstractResult);
				return res;
			}
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[deleteDocInvWS] Error al pasar de Json a String");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		return new ToleranceDao().deleteTolerance(arrayIdDocInv);
	}

	public Response<List<DocInvBean>> getDocInv(Request request) {

		log.info("[getDocInvWS] " + request.toString());
		DocInvBean tb = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		Response<List<DocInvBean>> res = new Response<List<DocInvBean>>();

		if (!req.isEmpty()) {
			try {
				tb = gson.fromJson(gson.toJson(request.getLsObject()), DocInvBean.class);
				log.info("[getDocInvWS] Fue Objeto: " + tb);
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString().trim();
				log.info("[getDocInvWS] jsyn Intentando por String ");
			}
		} else {
			searchFilter = "";
			log.info("[getDocInvWS] Fue cadena vacía ");

			try {
				tb = gson.fromJson(gson.toJson(request.getLsObject()), DocInvBean.class);
			} catch (JsonSyntaxException e) {
				log.log(Level.SEVERE, "[getDocInvWS] Error al pasar de Json a ZoneB");
				tb = null;
				AbstractResultsBean abstractResult = new AbstractResultsBean();
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
			
		}
		return new DocInvDao().getDocInv(tb, searchFilter);
	}

}
