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
	Gson gson = new Gson();

	public Response<List<ToleranceBean>> getMATKL(Request request) {

		log.info("[getMATKLWS] " + request.toString());
		Response<List<ToleranceBean>> res = new Response<>();
		ToleranceBean tb = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();

		if (!req.isEmpty()) {
			try {
				tb = gson.fromJson(gson.toJson(request.getLsObject()), ToleranceBean.class);
				log.info("[getMATKLWS] Fue Objeto: ");
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString().trim();
				log.info("[getMATKLWS] Intentando por String ");
			}
		} else {
			searchFilter = "";
			log.info("[getMATKLWS] Fue cadena vacía ");
		}
		return new ToleranceDao().getMATKL(tb, searchFilter);
	}

	public Response<Object> addTolerance(Request request, User user) {

		log.info("[addToleranceWorkService] " + request.toString());
		ToleranceBean toleranceBean = null;
		Response<Object> res = new Response<Object>();

		try {
			toleranceBean = gson.fromJson(gson.toJson(request.getLsObject()), ToleranceBean.class);
			log.log(Level.WARNING, "[addToleranceWS] ");
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[addToleranceWS] Error al pasar de Json a ToleranceBean", e);
			toleranceBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new ToleranceDao().addTolerance(toleranceBean, user.getEntity().getIdentyId());

	}

	public Response<Object> deleteTolerance(Request request) {

		log.info("[deleteToleranceWS] " + request.toString());
		String arrayIdTolerances;
		Response<Object> res = new Response<Object>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		try {
			arrayIdTolerances = request.getLsObject().toString();

			if (arrayIdTolerances == null || arrayIdTolerances.isEmpty()) {
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs("NULL OR EMPTY ARRAY");
				res.setAbstractResult(abstractResult);
				return res;
			}

		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[deleteToleranceWS] Error al pasar de Json a String");

			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new ToleranceDao().deleteTolerance(arrayIdTolerances);

	}

	public Response<List<ToleranceBean>> getTolerances(Request request) {

		log.info("getTolerancesWS] " + request.toString());
		ToleranceBean tb = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();
		if (!req.isEmpty()) {
			try {
				tb = gson.fromJson(gson.toJson(request.getLsObject()), ToleranceBean.class);
				log.info("[getTolerancesWS] Fue Objeto: " + tb);
			} catch (JsonSyntaxException e) {
				searchFilter = request.getLsObject().toString().trim();
				log.info("[getTolerancesWS] jsyn Intentando por String ");
			}
		} else {
			searchFilter = "";
			log.info("[getTolerancesWS] Fue cadena vacía ");
			
		}
		return new ToleranceDao().getTolerances(tb, searchFilter);
	}
	
	public Response<ToleranceBean> getToleranceByMatnr(Request request){
		String matnr;
		log.info("getToleranceByMatnrWS] matnr = " +request.getLsObject().toString());
		matnr = request.getLsObject().toString().trim();
		return new ToleranceDao().getToleranceByMatnr(matnr);
	}
}
