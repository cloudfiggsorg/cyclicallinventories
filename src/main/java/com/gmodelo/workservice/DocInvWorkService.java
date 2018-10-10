package com.gmodelo.workservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.DocInvPositionBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.DocInvDao;
import com.gmodelo.dao.ZoneDao;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class DocInvWorkService {

	private Logger log = Logger.getLogger(DocInvWorkService.class.getName());
	Gson gson = new Gson();

	@SuppressWarnings("rawtypes")
	public Response<DocInvBean> addDocInv(Request request, User user) {

		log.info("[addDocInvWS] " + request.toString());
		DocInvBean docInvBean;
		Response<DocInvBean> res = new Response<DocInvBean>();

		try {
			docInvBean = gson.fromJson(gson.toJson(request.getLsObject()), DocInvBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[addDocInvWS] Error al pasar de Json a DocInvB");
			docInvBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		return new DocInvDao().addDocInv(docInvBean, user.getEntity().getIdentyId());

	}

	@SuppressWarnings("rawtypes")
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
		return new DocInvDao().deleteDocInvId(arrayIdDocInv);
	}

	@SuppressWarnings({ "rawtypes" })
	public Response<List<DocInvBean>> getDocInv(Request request) {

		log.info("[getDocInvWS] " + request.toString());
		DocInvBean tb = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();

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

		}
		return new DocInvDao().getDocInv(tb, searchFilter);
	}

	@SuppressWarnings("rawtypes")
	public Response conciDocInv(Request request, User user) {
		log.info("[conciDocInv] conciDocInv request");
		Response response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		try {
			DocInvBean docInv = new Gson().fromJson(request.getLsObject().toString(), DocInvBean.class);
			if (!docInv.getDocInvPositions().isEmpty()) {
				log.info("[conciDocInv] conciDocInv request is not Empty");
				Connection con = new ConnectionManager().createConnection();
				List<DocInvPositionBean> listToInsert = new ArrayList<>();
				try {
					for (DocInvPositionBean singleBean : docInv.getDocInvPositions()) {
						singleBean = new ZoneDao().getDataForConciliation(docInv, singleBean, con);
						listToInsert.add(singleBean);
					}
					if (!listToInsert.isEmpty()) {
						log.info("[conciDocInv] conciDocInv get ALl Data");
						result = new DocInvDao().addDocumentPosition(listToInsert, con);
						if (result.getResultId() == ReturnValues.ISUCCESS) {
							Response<DocInvBean> responseDao = new DocInvDao().addDocInv(docInv,
									user.getEntity().getIdentyId());
							result = responseDao.getAbstractResult();
						}
					} else {
						result.setResultId(ReturnValues.IEMPTY);
						result.setResultMsgAbs(
								"Las Posiciones de Inventario tienen un Error, Contacte Administrador del Sistema");
					}
				} catch (SQLException e) {
					result.setResultId(ReturnValues.IEXCEPTION);
					result.setResultMsgAbs(e.getMessage());
				} finally {
					try {
						con.close();
					} catch (Exception e) {
						log.log(Level.SEVERE, "[conciDocInv] - Finally Exception :", e);
					}
				}
			} else {
				result.setResultId(ReturnValues.IEMPTY);
				result.setResultMsgAbs(
						"El Conteo se encuentra Vacio, Favor de completar previo a cerrar el documento de Inventario");
			}
		} catch (Exception e) {
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		}
		log.info("[conciDocInv] expectedResult:" + result.toString());
		response.setAbstractResult(result);
		return response;
	}

}
