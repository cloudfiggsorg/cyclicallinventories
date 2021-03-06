package com.gmodelo.cyclicinventories.workservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.DocInvPositionBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.RoutePositionBean;
import com.gmodelo.cyclicinventories.dao.DocInvDao;
import com.gmodelo.cyclicinventories.dao.RouteDao;
import com.gmodelo.cyclicinventories.dao.ZoneDao;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class DocInvWorkService {

	private Logger log = Logger.getLogger(DocInvWorkService.class.getName());
	private Gson gson = new Gson();

	@SuppressWarnings("rawtypes")
	public Response<DocInvBean> addDocInv(Request request, User user) {
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		log.info("[addDocInvWS] " + request.toString());
		DocInvBean docInvBean;
		Response<DocInvBean> res = new Response<>();
		try {
			docInvBean = gson.fromJson(gson.toJson(request.getLsObject()), DocInvBean.class);
			List<RoutePositionBean> positionRoute = new RouteDao().getPositions(docInvBean.getRoute());
			if (!positionRoute.isEmpty()) {
				res = new DocInvDao().addDocInv(docInvBean, user.getEntity().getIdentyId());
				try {
					new SapConciliationWorkService().WS_RuntimeInventorySnapShot(docInvBean);
				} catch (Exception e) {
					log.log(Level.SEVERE,
							"[addDocInvWS] Error al generar la llamada al snapshot SapConciliationWorkService");
				}
			} else {
				abstractResult.setResultId(ReturnValues.IERROR);
				abstractResult.setResultMsgAbs("Ruta no seleccionable, favor de agregar posiciones de conteo");
				res.setAbstractResult(abstractResult);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "[addDocInvWS] Error al consultar Datos de la ruta");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);

		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[addDocInvWS] Error al pasar de Json a DocInvB");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
		}
		return res;

	}

	@SuppressWarnings("rawtypes")
	public Response<Object> deleteDocInv(Request request) {
		log.info("[deleteDocInvWS] " + request.toString());
		String arrayIdDocInv;
		Response<Object> res = new Response<>();
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

	public Response<List<DocInvBean>> getOnlyDocInv(Request<List<Object>> request) {

		log.info("[getOnlyDocInvWS] " + request.toString());
		DocInvBean tb = null;
		String searchFilter = "";
		String req = request.getLsObject().toString().trim();
		Response<List<DocInvBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		if (!req.isEmpty()) {
			try {
				log.info("[getOnlyDocInvWS] Getting Object DocInvBean ");
				tb = gson.fromJson(gson.toJson(request.getLsObject().get(0)), DocInvBean.class);
				log.info("[getOnlyDocInvWS] Getting String filter");
				searchFilter = request.getLsObject().get(1).toString().trim();
			} catch (JsonSyntaxException | JSONException e) {
				e.printStackTrace();
				log.log(Level.SEVERE, "[getOnlyDocInvWS] Error al pasar de Json a Objeto");
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs("Error al pasar de Json a Objeto.");
				res.setAbstractResult(abstractResult);
				return res;
			}
		} else {

			log.log(Level.SEVERE, "[getOnlyDocInvWS] Datos ausentes durante la recpeción.");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs("Datos ausentes durante la recepción");
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new DocInvDao().getOnlyDocInv(tb, searchFilter);
	}

	@SuppressWarnings("rawtypes")
	public Response conciDocInv(Request request, User user) {
		log.info("[conciDocInv] conciDocInv request " + request.toString());
		Response response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		try {
			DocInvBean docInv = gson.fromJson(gson.toJson(request.getLsObject()), DocInvBean.class);
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
							new SapConciliationWorkService().WS_RuntimeInventoryFinalSnapShot(docInv);
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

	public Response<List<DocInvBean>> getOnlyDocInvNoTask(Request<List<Object>> request) {


		log.info("[getOnlyDocInvNoTaskWS] " + request.toString());
		DocInvBean tb = null;
		String searchFilter = "";
		String req = request.getLsObject().toString().trim();
		Response<List<DocInvBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		if (!req.isEmpty()) {
			try {
				log.info("[getOnlyDocInvNoTaskWS] Getting Object DocInvBean ");
				tb = gson.fromJson(gson.toJson(request.getLsObject().get(0)), DocInvBean.class);
				log.info("[getOnlyDocInvNoTaskWS] Getting String filter");
				searchFilter = request.getLsObject().get(1).toString().trim();
			} catch (JsonSyntaxException | JSONException e) {
				e.printStackTrace();
				log.log(Level.SEVERE, "[getOnlyDocInvNoTaskWS] Error al pasar de Json a Objeto");
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs("Error al pasar de Json a Objeto.");
				res.setAbstractResult(abstractResult);
				return res;
			}
		} else {

			log.log(Level.SEVERE, "[getOnlyDocInvNoTaskWS] Datos ausentes durante la recpeción.");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs("Datos ausentes durante la recepción");
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new DocInvDao().getOnlyDocInvNoTask(tb, searchFilter);
	
	}

}
