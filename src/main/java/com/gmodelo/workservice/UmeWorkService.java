package com.gmodelo.workservice;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.UMEDaoE;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;

public class UmeWorkService {
	
	private Logger log = Logger.getLogger(ToleranceWorkService.class.getName());
	Gson gson = new Gson();

	public Response<Object> saveUserWerks(Request request) {

		log.info("[deleteTaskWS] " + request.toString());
		String user;
		String werks;
		String req;
		req = request.getLsObject().toString();
		req = req.replaceAll("=", ":");

		JSONObject jsonObj = new JSONObject(req);

		try {

			user = jsonObj.getString("bukrs");
		} catch (JSONException e) {
			user = "";
		}

		try {

			werks = jsonObj.getString("werks");
		} catch (JSONException e) {
			werks = "";
		}
		
		if(user.trim().length() == 0 || werks.trim().length() == 0){
			log.log(Level.SEVERE, "[saveUserWerks] Error al obtener el usuario y/o centro");
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs("Error al obtener el usuario y/o centro");
			Response<Object> res = new Response<>();
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new UMEDaoE().saveUserWerks(user, werks);
	}

}
