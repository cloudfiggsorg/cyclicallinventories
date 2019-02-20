package com.gmodelo.cyclicinventories.workservice;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.ApegosBean;
import com.gmodelo.cyclicinventories.beans.ConciAccntReportBean;
import com.gmodelo.cyclicinventories.beans.ConciliationsIDsBean;
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.DocInvBeanHeaderSAP;
import com.gmodelo.cyclicinventories.beans.E_Lqua_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mard_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mseg_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Msku_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Salida_SapEntity;
import com.gmodelo.cyclicinventories.beans.MaterialExplosionBean;
import com.gmodelo.cyclicinventories.beans.PosDocInvBean;
import com.gmodelo.cyclicinventories.beans.ProductivityBean;
import com.gmodelo.cyclicinventories.beans.RelMatnrCategory;
import com.gmodelo.cyclicinventories.beans.ReporteConteosBean;
import com.gmodelo.cyclicinventories.beans.ReporteDocInvBeanHeader;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.ReportesDao;
import com.gmodelo.cyclicinventories.dao.SapOperationDao;
import com.gmodelo.cyclicinventories.dao.UMEDaoE;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;

public class ReportesWorkService {

	private Logger log = Logger.getLogger(ReportesWorkService.class.getName());
	private Gson gson = new Gson();
	private ConnectionManager iConnectionManager = new ConnectionManager();

	public Response<List<ApegosBean>> getReporteApegos(Request request) {
		log.info("[getReporteApegosWorkService] " + request.toString());
		Response<List<ApegosBean>> response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		ApegosBean apegosBean = null;
		try {
			log.info("[getReporteApegosWorkService] try");
			apegosBean = gson.fromJson(gson.toJson(request.getLsObject()), ApegosBean.class);
			response = new ReportesDao().getReporteApegos(apegosBean);
		} catch (Exception e) {
			log.log(Level.SEVERE, "getReporteApegosWorkService] catch", e);
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		}
		response.setAbstractResult(result);
		return response;
	}

	public Response<List<ReporteConteosBean>> getReporteConteos(Request request) {
		log.info("[getReporteConteosWorkService] " + request.toString());
		Response<List<ReporteConteosBean>> response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		ReporteConteosBean bean = null;
		try {
			log.info("[getReporteConteosWorkService] try");
			bean = gson.fromJson(gson.toJson(request.getLsObject()), ReporteConteosBean.class);
			response = new ReportesDao().getReporteConteos(bean);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[getReporteConteosWorkService] catch", e);
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		}
		response.setAbstractResult(result);
		return response;
	}

	public Response<ReporteDocInvBeanHeader> getReporteDocInv(Request request) {
		log.info("[ReporteWorkService getReporteDocInv] " + request.toString());
		Response<ReporteDocInvBeanHeader> response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		DocInvBean bean = null;
		try {
			log.info("[ReporteWorkService getReporteDocInv] try");
			bean = gson.fromJson(gson.toJson(request.getLsObject()), DocInvBean.class);
			response = new ReportesDao().getReporteDocInv(bean);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[ReporteWorkService getReporteDocInv] catch", e);
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		}
		response.setAbstractResult(result);
		return response;
	}

	public Response<List<ProductivityBean>> getCountedProductivity(Request request) {
		log.info("[getCountedProductivityWorkService] " + request.toString());
		Response<List<ProductivityBean>> response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		ProductivityBean tareasBean = null;
		try {
			log.info("[getCountedProductivityWorkService] try");
			tareasBean = gson.fromJson(gson.toJson(request.getLsObject()), ProductivityBean.class);
			response = new ReportesDao().getCountedProductivityDao(tareasBean);
		} catch (Exception e) {
			log.log(Level.SEVERE, "getCountedProductivityTareasWorkService] catch", e);
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
			Response<List<ProductivityBean>> badResponse = new Response<>();
			badResponse.setAbstractResult(result);
			return badResponse;
		}
		return response;
	}

	public Response<List<ProductivityBean>> getUserProductivity(Request request) {
		log.info("[getReporteTiemposTareasZonasWorkService] " + request.toString());
		Response<List<ProductivityBean>> response = new Response<>();
		AbstractResultsBean result = new AbstractResultsBean();
		ProductivityBean tareasBean = null;
		try {
			log.info("[getUserProductivityWorkService] try");
			tareasBean = gson.fromJson(gson.toJson(request.getLsObject()), ProductivityBean.class);
			response = new ReportesDao().getUserProductivityDao(tareasBean);
			if (response.getAbstractResult().getResultId() != 1) {
				log.log(Level.SEVERE,
						"[getUserProductivityWorkService] " + response.getAbstractResult().getResultMsgAbs());
				return response;
			}
			List<ProductivityBean> listBean = response.getLsObject();
			ArrayList<User> listUniqueUser = new ArrayList<>();
			User user;
			for (ProductivityBean b : listBean) {
				if (listUniqueUser.isEmpty()) {
					user = new User();
					user.getEntity().setIdentyId(b.getUser().trim());
					user.getGenInf().setName(b.getUser().trim());

					listUniqueUser.add(user);

				} else {
					boolean existUser = false;
					for (User u : listUniqueUser) {
						if (u.getEntity().getIdentyId().equalsIgnoreCase(b.getUser().trim())) {
							existUser = true;
						}
					}
					if (!existUser) {
						user = new User();
						user.getEntity().setIdentyId(b.getUser().trim());
						user.getGenInf().setName(b.getUser().trim());

						listUniqueUser.add(user);
					}
				}
			}

			UMEDaoE umeDao = new UMEDaoE();

			listUniqueUser = umeDao.getUsersLDAPByCredentials(listUniqueUser);

			for (ProductivityBean pb : listBean) {
				for (User u : listUniqueUser) {
					if (pb.getUser().trim().equalsIgnoreCase(u.getEntity().getIdentyId().trim())) {
						pb.setUser(u.getGenInf().getName() + " " + u.getGenInf().getLastName());
						break;
					}
				}
			}

			response.setLsObject(listBean);

		} catch (Exception e) {
			log.log(Level.SEVERE, "[getUserProductivityWorkService] catch", e);
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
			Response<List<ProductivityBean>> badResponse = new Response<>();
			badResponse.setAbstractResult(result);
			return badResponse;
		}
		return response;
	}

	public Response<List<ConciliationsIDsBean>> getReporteCalidad(Request<List<Object>> request) {
		log.info("[getReporteCalidadWorkService] " + request.toString());
		String bukrs = (String) request.getLsObject().get(0);
		String werks = (String) request.getLsObject().get(1);
		return new ReportesDao().getReporteCalidad(bukrs, werks);
	}

	public Response<List<ConciAccntReportBean>> getReporteConciAccntReport(Request request) {
		log.info("[getReporteConciAccntReportWorkService] " + request.toString());
		Response<List<ConciAccntReportBean>> response = new Response<>();
		List<ConciAccntReportBean> list = new ArrayList<>();
		ConciAccntReportBean conciAccntReportBean = gson.fromJson(gson.toJson(request.getLsObject()),
				ConciAccntReportBean.class);
		response = new ReportesDao().getReporteConciAccntReport(conciAccntReportBean);
		if (response.getAbstractResult().getResultId() != 1) {
			return response;
		} else {
			list = response.getLsObject();

			List<String> listDocInv = new ArrayList<>();
			HashMap<String, List<ConciAccntReportBean>> map = new HashMap<>();

			for (ConciAccntReportBean item : list) {
				item.setType(item.getType().equalsIgnoreCase("1") ? "Diario" : "Mensual");
				// Obteniendo los DocId unicos y generando listas para cada doc
				// inv existente
				if (listDocInv.size() == 0) {
					listDocInv.add(item.getDocInvId());
					List<ConciAccntReportBean> listBean = new ArrayList<>();
					map.put(item.getDocInvId(), listBean);
				} else {
					boolean existDocId = false;
					if (listDocInv.contains(item.getDocInvId())) {
						existDocId = true;
					}
					if (!existDocId) {
						listDocInv.add(item.getDocInvId());
						List<ConciAccntReportBean> listBean = new ArrayList<>();
						map.put(item.getDocInvId(), listBean);
					}
				}

			}

			// Agregando cada item en su lista por doc inv correspondiente
			for (String id : listDocInv) {
				for (ConciAccntReportBean item : list) {
					if (id.equalsIgnoreCase(item.getDocInvId())) {
						map.get(id).add(item);
					}
				}
			}

			List<ConciAccntReportBean> finalList = new ArrayList<>();

			for (String id : listDocInv) {
				List<Integer> catIdList = new ArrayList<>();
				// obteniendo todas las categorias de un docInv y creando los
				// beans final
				for (ConciAccntReportBean carb : map.get(id)) {
					if (catIdList.size() == 0) {
						catIdList.add(carb.getCatId());
						ConciAccntReportBean finalCArb = new ConciAccntReportBean();
						finalCArb.setCatId(carb.getCatId());
						finalCArb.setDocInvId(carb.getDocInvId());
						finalCArb.setBukrs(carb.getBukrs());
						finalCArb.setWerks(carb.getWerks());
						finalCArb.setType(carb.getType());
						finalCArb.setDateIni(carb.getDateIni());
						finalCArb.setCategory(carb.getCategory());
						finalCArb.setAccountant(0D);
						finalCArb.setJustification(0D);
						finalCArb.setAccDiff(0D);

						finalList.add(finalCArb);
					} else {
						boolean existCatId = false;
						if (catIdList.contains(carb.getCatId())) {
							existCatId = true;
						}
						if (!existCatId) {
							catIdList.add(carb.getCatId());
							ConciAccntReportBean finalCArb = new ConciAccntReportBean();
							finalCArb.setCatId(carb.getCatId());
							finalCArb.setDocInvId(carb.getDocInvId());
							finalCArb.setBukrs(carb.getBukrs());
							finalCArb.setWerks(carb.getWerks());
							finalCArb.setType(carb.getType());
							finalCArb.setDateIni(carb.getDateIni());
							finalCArb.setCategory(carb.getCategory());
							finalCArb.setAccountant(0D);
							finalCArb.setJustification(0D);
							finalCArb.setAccDiff(0D);

							finalList.add(finalCArb);
						}
					}
				}

				// usando listaFinal para sumar accountant, justification y
				// difference en los beans finales
				for (ConciAccntReportBean finalBean : finalList) {
					for (ConciAccntReportBean carb : map.get(id)) {

						if (finalBean.getDocInvId().equalsIgnoreCase(id) && finalBean.getCatId() == carb.getCatId()) {
							finalBean.setAccountant(finalBean.getAccountant() + carb.getAccountant());
							finalBean.setJustification(finalBean.getJustification() + carb.getJustification());
							finalBean.setAccDiff(finalBean.getAccDiff() + carb.getAccDiff());
						}
					}
				}

			}

			for (ConciAccntReportBean item : finalList) {
				if (item.getAccountant() != 0D && item.getAccDiff() != 0D) {
					item.setPercAccDiff(String.valueOf((item.getAccDiff() * 100) / item.getAccountant()));
				} else if (item.getAccountant() == 0D && item.getAccDiff() != 0D) {
					item.setPercAccDiff("100");
				} else if (item.getAccountant() == 0D && item.getAccDiff() == 0D) {
					item.setPercAccDiff("");
				}

				if (item.getAccountant() != 0D && item.getJustification() != 0D) {
					item.setPercJustification(String.valueOf((item.getJustification() * 100) / item.getAccountant()));
				} else if (item.getAccountant() == 0D && item.getJustification() != 0D) {
					item.setPercJustification("100");
				} else if (item.getAccountant() == 0D && item.getJustification() == 0D) {
					item.setPercJustification("");
				}

			}
			response.setLsObject(finalList);
		}
		return response;
	}

	@SuppressWarnings({ "rawtypes" })
	public Response<DocInvBeanHeaderSAP> getReporteDocInvSAPByWerks(Request request) {

		log.info("[getReporteDocInvSAPByWerks] " + request.toString());
		Response<DocInvBeanHeaderSAP> resp = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		DocInvBeanHeaderSAP reportBylgPla, reportByWerks;
		DocInvBean docInvBean = null;

		try {
			log.info("[getReporteDocInvSAPByWerks] try");
			docInvBean = gson.fromJson(gson.toJson(request.getLsObject()), DocInvBean.class);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[getReporteDocInvSAPByWerks] catch", e);
			AbstractResultsBean result = new AbstractResultsBean();
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
			resp.setAbstractResult(result);
			return resp;
		}

		if (docInvBean.getStatus().equalsIgnoreCase("TRUE")) {

			log.info("[getReporteDocInvSAPByWerks] Getting closed object...");
			return new SapOperationDao().getClosedConsSapReportByWerks(docInvBean);

		} else {

			log.info("[getReporteDocInvSAPByWerks] Getting the report...");
			resp = getReporteDocInvSAPByLgpla(request);

			// Check if there was an error
			if (resp.getAbstractResult().getResultId() != 1) {

				return resp;
			}

			reportBylgPla = resp.getLsObject();
			reportByWerks = resp.getLsObject();
		}

		ArrayList<E_Mseg_SapEntity> lsTransit = new SapOperationDao()
				.getMatnrOnTransitByWerks(docInvBean.getDocInvId().intValue());
		ArrayList<E_Msku_SapEntity> lsCons = new SapOperationDao()
				.getMatnrOnConsByWerks(docInvBean.getDocInvId().intValue());

		HashMap<String, PosDocInvBean> mapByMatNr = new HashMap<>();
		PosDocInvBean pbAux = null;
		double sumCounted = 0.0D;
		double sumCountedExpl = 0.0D;
		double sumTeoric = 0.0D;
		List<PosDocInvBean> listBean = reportBylgPla.getDocInvPosition();

		// Create a map by matnr
		for (PosDocInvBean pb : listBean) {

			if (pb.isGrouped()) {

				if (mapByMatNr.containsKey(pb.getMatnr())) {

					pbAux = (PosDocInvBean) mapByMatNr.get(pb.getMatnr());
					sumCounted = Double.parseDouble(pbAux.getCounted());
					sumCounted += Double.parseDouble(pb.getCounted());
					pbAux.setCounted(Double.toString(sumCounted));

					sumCountedExpl = Double.parseDouble(pbAux.getCountedExpl());
					sumCountedExpl += Double.parseDouble(pb.getCountedExpl());
					pbAux.setCountedExpl(Double.toString(sumCountedExpl));

					sumTeoric = Double.parseDouble(pbAux.getTheoric() == null ? "0" : pbAux.getTheoric());
					sumTeoric += Double.parseDouble(pb.getTheoric() == null ? "0" : pb.getTheoric());
					pbAux.setTheoric(Double.toString(sumTeoric));

					mapByMatNr.replace(pb.getMatnr(), pbAux);

				} else {

					pb.setCategory("");
					pb.setConsignation("0");
					pb.setTransit("0");
					mapByMatNr.put(pb.getMatnr(), pb);
				}
			}
		}

		listBean.clear();
		PosDocInvBean pdibAux = null;
		String ldIds = "";

		for (Map.Entry<String, PosDocInvBean> mapEntry : mapByMatNr.entrySet()) {

			pdibAux = mapEntry.getValue();
			listBean.add(pdibAux);
			ldIds += pdibAux.getMatnr() + ",";
		}

		ldIds = ldIds.substring(0, ldIds.length() - 1);

		// Get the categories
		ArrayList<RelMatnrCategory> relCatByMatnr = new SapOperationDao().getCatByMatnr(ldIds);

		for (PosDocInvBean pb : listBean) {

			for (RelMatnrCategory relCat : relCatByMatnr) {

				if (relCat.getCatByMatnr().getMatnr().contentEquals(pb.getMatnr())) {

					pb.setCategory(relCat.getCategory().getCategory());
					break;
				}
			}

			for (E_Mseg_SapEntity ojb : lsTransit) {

				if (ojb.getMatnr().contentEquals(pb.getMatnr())) {

					pb.setTransit(ojb.getMeins());
					break;
				}
			}

			for (E_Msku_SapEntity ojb : lsCons) {

				if (ojb.getMatnr().contentEquals(pb.getMatnr())) {

					pb.setConsignation(ojb.getKulab());
					break;
				}
			}
		}

		reportByWerks.setDocInvPosition(listBean);
		resp.setAbstractResult(abstractResult);
		resp.setLsObject(reportByWerks);

		return resp;
	}

	private final SapOperationDao operationDao = new SapOperationDao();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response<DocInvBeanHeaderSAP> getReporteDocInvSAPByLgpla(Request request) {
		
		log.info("[ReporteWorkService getReporteDocInvSAPByLgpla] " + request.toString());
		
		DocInvBean bean = new DocInvBean();		
		bean = gson.fromJson(gson.toJson(request.getLsObject()), DocInvBean.class);
		
		if (bean.getStatus() != null && bean.getStatus().equalsIgnoreCase("TRUE")) {

			log.info("[getReporteDocInvSAPByLgpla] Getting closed object...");
			return new SapOperationDao().getClosedConsSapReportByLgpla(bean);

		}
		
		Response<DocInvBeanHeaderSAP> response = new Response<>();
		DocInvBeanHeaderSAP headerSap = new DocInvBeanHeaderSAP();
		AbstractResultsBean result = new AbstractResultsBean();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		response.setAbstractResult(result);
		
		List<PosDocInvBean> docInvBeanList = new ArrayList<>();
		Connection con = iConnectionManager.createConnection();
		try {
			log.info("[ReporteWorkService getReporteDocInvSAPByLgpla] try");
			// response = new ReportesDao().getConcSAPByPosition(bean);
			bean = operationDao.getDocInvBeanDataHeaders(bean, con);
			headerSap.setDocInvId(bean.getDocInvId());
			headerSap.setBukrs(bean.getBukrs());
			headerSap.setBukrsD(bean.getBukrsD());
			headerSap.setWerks(bean.getWerks());
			headerSap.setWerksD(bean.getWerksD());
			headerSap.setRoute(bean.getRoute());
			headerSap.setType(bean.getType());
			headerSap.setSapRecount(bean.isSapRecount());
			headerSap.setConciliationDate(sdf.format(new Date(bean.getModifiedDate())));
			headerSap.setCreationDate(sdf.format(new Date(bean.getCreatedDate())));

			List<PosDocInvBean> docPosition = operationDao.getDocInvPositions(bean, con);
			if (!docPosition.isEmpty()) {
				HashMap<String, String> costByMaterial = operationDao.getCostByMaterial(bean, con);
				HashMap<String, List<PosDocInvBean>> orderWm = new HashMap<>();
				HashMap<String, HashMap<String, List<E_Salida_SapEntity>>> eSalida = operationDao
						.getEsalidaDataDocInv(bean, con);
				HashMap<String, E_Mard_SapEntity> eMard = operationDao.getEmardforDocInv(bean, con);
				HashMap<String, HashMap<String, E_Lqua_SapEntity>> eLqua = operationDao.getElquaforDocInv(bean, con);
				HashMap<String, List<MaterialExplosionBean>> explosionMap = operationDao.getExplotionDetailDocInv(bean,
						con);
				HashMap<String, HashMap<String, List<E_Mseg_SapEntity>>> eMseg = operationDao.getEmsegDataDocInv(bean,
						con);
				List<PosDocInvBean> imPositions = new ArrayList<>();
				HashMap<String, PosDocInvBean> expPosition = new HashMap<>();
				List<PosDocInvBean> wmPositions = new ArrayList<>();
				List<PosDocInvBean> wmExtraPos = new ArrayList<>();
				List<PosDocInvBean> wmExtraPosPiv = new ArrayList<>();
				for (PosDocInvBean docPos : docPosition) {
					if (docPos.getImwmMarker().equalsIgnoreCase("WM")) {
						wmPositions.add(docPos);
					} else {
						imPositions.add(docPos);
					}
				}
				// WM PROC BEGIN
				// Get Data LQUA AND MOVEMENTS
				log.info("[ReporteWorkService getReporteDocInvSAPByLgpla] Get WM LQUA AND MOVEMENTS");
				for (PosDocInvBean wmPos : wmPositions) {
					String lquaKey = wmPos.getLgNum() + "" + wmPos.getLgtyp() + "" + wmPos.getLgpla();
					wmPos.setCountedExpl("0.00");
					if (eLqua.get(lquaKey) != null) {
						if (eLqua.get(lquaKey).get(wmPos.getMatnr()) != null) {
							wmPos.setTheoric(eLqua.get(lquaKey).get(wmPos.getMatnr()).getVerme());
							eLqua.get(lquaKey).get(wmPos.getMatnr()).setMarked(true);
							Date lastCounted = new Date(wmPos.getDateEndCounted());
							BigDecimal theoMovs = new BigDecimal(wmPos.getTheoric());

							if (eSalida.containsKey(lquaKey)) {
								if (eSalida.get(lquaKey).containsKey(wmPos.getMatnr())) {
									for (E_Salida_SapEntity eSalidaBean : eSalida.get(lquaKey).get(wmPos.getMatnr())) {
										if (lastCounted.getTime() >= sdf
												.parse(eSalidaBean.getQdatu() + " " + eSalidaBean.getQzeit())
												.getTime()) {
											theoMovs = theoMovs.add(new BigDecimal(eSalidaBean.getNistm()));
											theoMovs = theoMovs.subtract(new BigDecimal(eSalidaBean.getVistm()));
										}
									}
								}
							}
							if (eMseg.containsKey(wmPos.getLgort() + lquaKey)) {
								if (eMseg.get(wmPos.getLgort() + lquaKey).containsKey(wmPos.getMatnr())) {
									for (E_Mseg_SapEntity eMsegBean : eMseg.get(wmPos.getLgort() + lquaKey)
											.get(wmPos.getMatnr())) {
										if (lastCounted.getTime() >= sdf
												.parse(eMsegBean.getBudat_mkpf() + " " + eMsegBean.getCputm_mkpf())
												.getTime()) {
											if (eMsegBean.getShkzg().equals("S")) {
												theoMovs = theoMovs.add(new BigDecimal(eMsegBean.getMenge()));
											} else {
												theoMovs = theoMovs.subtract(new BigDecimal(eMsegBean.getMenge()));
											}
										}
									}
								}
							}
							if (theoMovs.compareTo(BigDecimal.ZERO) >= 0) {
								wmPos.setTheoric(theoMovs.toString());
							} else {
								wmPos.setTheoric("0.00");
							}

						} else {
							wmPos.setTheoric("0.00");
						}
					}
					// GET EXPLOSION DATA

					if (!bean.isSapRecount()) {
						if (explosionMap.containsKey(wmPos.getMatnr())) {
							for (MaterialExplosionBean posEx : explosionMap.get(wmPos.getMatnr())) {
								PosDocInvBean pBean = new PosDocInvBean();
								String toCount = "0.00";
								if (expPosition.containsKey(posEx.getLgort() + "" + posEx.getIdnrk())) {
									toCount = new BigDecimal(posEx.getBmcal())
											.multiply(new BigDecimal(
													wmPos.getVhilmCounted() != null ? wmPos.getVhilmCounted() : "0.00"))
											.toString();
									pBean = expPosition.get(posEx.getLgort() + "" + posEx.getIdnrk());
									pBean.setDateIniCounted(wmPos.getDateIniCounted());
									pBean.setDateEndCounted(wmPos.getDateEndCounted());
									pBean.setCountedExpl(new BigDecimal(pBean.getCountedExpl())
											.add(new BigDecimal(toCount)).toString());
								} else {
									pBean.setLgort(posEx.getLgort());
									pBean.setLgortD(posEx.getLgortD());
									pBean.setMatnr(posEx.getIdnrk());
									pBean.setMatnrD(posEx.getMaktx());
									pBean.setMeins(posEx.getMeins());
									pBean.setTheoric("0.00");
									pBean.setCountedExpl(new BigDecimal(posEx.getBmcal())
											.multiply(new BigDecimal(
													wmPos.getCounted() != null ? wmPos.getCounted() : "0.00"))
											.toString());
									pBean.setCounted("0.00");
									pBean.setDateIniCounted(wmPos.getDateIniCounted());
									pBean.setDateEndCounted(wmPos.getDateEndCounted());
									pBean.setImwmMarker("IM");
									pBean.setGrouped(true);
									expPosition.put(posEx.getLgort() + "" + posEx.getIdnrk(), pBean);
								}
							}
						}

						// Tarimas

						if (wmPos.getVhilm() != null && !wmPos.getVhilm().isEmpty()
								&& !wmPos.getVhilm().equalsIgnoreCase("-987")) {
							PosDocInvBean pBean = new PosDocInvBean();
							String toCount = "0.00";
							if (expPosition.containsKey(wmPos.getLgort() + "" + wmPos.getVhilm())) {
								toCount = new BigDecimal(
										wmPos.getVhilmCounted() != null ? wmPos.getVhilmCounted() : "0.00").toString();
								pBean = expPosition.get(wmPos.getLgort() + "" + wmPos.getVhilm());
								pBean.setDateIniCounted(wmPos.getDateIniCounted());
								pBean.setDateEndCounted(wmPos.getDateEndCounted());
								pBean.setCountedExpl(
										new BigDecimal(pBean.getCountedExpl()).add(new BigDecimal(toCount)).toString());
							} else {
								pBean.setLgort(wmPos.getLgort());
								pBean.setLgortD(wmPos.getLgortD());
								pBean.setMatnr(wmPos.getVhilm());
								pBean.setMatnrD(operationDao.getNameFromTarima(wmPos.getVhilm(), con));
								pBean.setMeins(wmPos.getMeins());
								pBean.setTheoric("0.00");
								pBean.setCountedExpl(new BigDecimal(
										wmPos.getVhilmCounted() != null ? wmPos.getVhilmCounted() : "0.00").toString());
								pBean.setCounted("0.00");
								pBean.setDateIniCounted(wmPos.getDateIniCounted());
								pBean.setDateEndCounted(wmPos.getDateEndCounted());
								pBean.setImwmMarker("IM");
								pBean.setGrouped(true);
								expPosition.put(wmPos.getLgort() + "" + wmPos.getVhilm(), pBean);
							}
						}
					}

				}
				log.info("[ReporteWorkService getReporteDocInvSAPByLgpla] Get WM LQUA AND MOVEMENTS END");
				log.info("[ReporteWorkService getReporteDocInvSAPByLgpla] Get IM MARD");
				Iterator it = null;

				if (!bean.isSapRecount()) {

					it = eLqua.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry) it.next();
						HashMap<String, E_Lqua_SapEntity> single_lqua = (HashMap<String, E_Lqua_SapEntity>) pair
								.getValue();
						Iterator it2 = single_lqua.entrySet().iterator();
						while (it2.hasNext()) {
							Map.Entry pair2 = (Map.Entry) it2.next();
							E_Lqua_SapEntity lquaExEn = (E_Lqua_SapEntity) pair2.getValue();
							if (!lquaExEn.getMarked()) {
								PosDocInvBean posExLq = new PosDocInvBean();
								posExLq.setLgort(lquaExEn.getLgort());
								posExLq.setLgortD(lquaExEn.getLgortD());
								posExLq.setLgNum(lquaExEn.getLgnum());
								posExLq.setLgtyp(lquaExEn.getLgtyp());
								posExLq.setLgpla(lquaExEn.getLgpla());
								posExLq.setTheoric(lquaExEn.getVerme());
								posExLq.setMeins(lquaExEn.getMeins());
								posExLq.setMatnr(lquaExEn.getMatnr());
								posExLq.setMatnrD(lquaExEn.getMaktx());
								posExLq.setCountedExpl("0.00");
								posExLq.setCounted("0.00");
								posExLq.setImwmMarker("WM");
								posExLq.setGrouped(false);
								wmExtraPos.add(posExLq);
							}
						}
					}

					for (PosDocInvBean wmPos : wmPositions) {
						for (PosDocInvBean wmPosEx : wmExtraPos) {
							if ((wmPos.getLgNum() + "" + wmPos.getLgtyp() + "" + wmPos.getLgpla())
									.equals(wmPosEx.getLgNum() + "" + wmPosEx.getLgtyp() + "" + wmPosEx.getLgpla())) {
								if (!wmExtraPosPiv.contains(wmPosEx)) {
									wmExtraPosPiv.add(wmPosEx);
								}
							}
						}
					}
				}

				log.info("[ReporteWorkService getReporteDocInvSAPByLgpla] Get IM MARD");

				// WM PROC BEGIN

				// Begin Fill IM
				log.info("[ReporteWorkService getReporteDocInvSAPByLgpla] IM MERGES");

				// MErge Im for only use LGORT MATERIAL
				HashMap<String, PosDocInvBean> imMapMerge = new HashMap<>();
				for (PosDocInvBean imPos : imPositions) {
					String lgortMatkey = imPos.getLgort() + imPos.getMatnr();
					if (imMapMerge.containsKey(lgortMatkey)) {
						imMapMerge.get(lgortMatkey).setCounted(new BigDecimal(imMapMerge.get(lgortMatkey).getCounted())
								.add(new BigDecimal(imPos.getCounted())).toString());
						if (imMapMerge.get(lgortMatkey).getDateEndCounted() < imPos.getDateEndCounted()) {
							imMapMerge.get(lgortMatkey).setDateIniCounted(imPos.getDateIniCounted());
							imMapMerge.get(lgortMatkey).setDateEndCounted(imPos.getDateEndCounted());
						}
					} else {
						imPos.setLgpla("");
						imMapMerge.put(lgortMatkey, imPos);
					}
				}

				imPositions = new ArrayList<>();
				it = imMapMerge.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					imPositions.add((PosDocInvBean) pair.getValue());
				}
				// MErge Im for only use LGORT MATERIAL

				for (PosDocInvBean imPos : imPositions) {
					imPos.setLgpla("");
					if (expPosition.containsKey(imPos.getLgort() + "" + imPos.getMatnr())) {
						imPos.setCountedExpl(
								expPosition.get(imPos.getLgort() + "" + imPos.getMatnr()).getCountedExpl());
						expPosition.remove(imPos.getLgort() + "" + imPos.getMatnr());
					} else {
						imPos.setCountedExpl("0.00");
					}
				}
				it = expPosition.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					imPositions.add((PosDocInvBean) pair.getValue());
				}

				for (PosDocInvBean imPos : imPositions) {
					if (eMard.get(imPos.getLgort() + imPos.getMatnr()) != null) {
						imPos.setTheoric(eMard.get(imPos.getLgort() + imPos.getMatnr()).getLabst());
						String lquaKey = imPos.getLgort();
						Date lastCounted = new Date(imPos.getDateEndCounted());
						BigDecimal theoMovs = new BigDecimal(imPos.getTheoric());
						if (eMseg.containsKey(lquaKey)) {
							if (eMseg.get(lquaKey).containsKey(imPos.getMatnr())) {
								for (E_Mseg_SapEntity eMsegBean : eMseg.get(lquaKey).get(imPos.getMatnr())) {
									if (lastCounted.getTime() >= sdf
											.parse(eMsegBean.getBudat_mkpf() + " " + eMsegBean.getCputm_mkpf())
											.getTime()) {
										if (eMsegBean.getShkzg().equals("S")) {
											theoMovs = theoMovs.add(new BigDecimal(eMsegBean.getMenge()));
										} else {
											theoMovs = theoMovs.subtract(new BigDecimal(eMsegBean.getMenge()));
										}
									}
								}
							}
						}
						if (theoMovs.compareTo(BigDecimal.ZERO) >= 0) {
							imPos.setTheoric(theoMovs.toString());
						} else {
							imPos.setTheoric("0.00");
						}

					}
				}

				log.info("[ReporteWorkService getReporteDocInvSAPByLgpla] IM MERGES END");
				// End fill IM

				// Reorganize WM
				log.info("[ReporteWorkService getReporteDocInvSAPByLgpla] Sort Begin");

				for (PosDocInvBean wmPos : wmPositions) {
					String lquaKey = wmPos.getLgNum() + "" + wmPos.getLgtyp() + "" + wmPos.getLgpla();
					if (orderWm.containsKey(lquaKey)) {
						orderWm.get(lquaKey).add(wmPos);
					} else {
						List<PosDocInvBean> listPos = new ArrayList<>();
						listPos.add(wmPos);
						orderWm.put(lquaKey, listPos);
					}
				}
				for (PosDocInvBean wmPos : wmExtraPosPiv) {
					String lquaKey = wmPos.getLgNum() + "" + wmPos.getLgtyp() + "" + wmPos.getLgpla();
					if (orderWm.containsKey(lquaKey)) {
						orderWm.get(lquaKey).add(wmPos);
					} else {
						List<PosDocInvBean> listPos = new ArrayList<>();
						listPos.add(wmPos);
						orderWm.put(lquaKey, listPos);
					}
				}

				log.info("[ReporteWorkService getReporteDocInvSAPByLgpla] Sort END");

				log.info("[ReporteWorkService getReporteDocInvSAPByLgpla] Merg WM AND IM");
				// Merge the two Lists
				it = orderWm.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					List<PosDocInvBean> colList = (List<PosDocInvBean>) pair.getValue();
					docInvBeanList.addAll(colList);
				}
				docInvBeanList.addAll(imPositions);

				log.info("[ReporteWorkService getReporteDocInvSAPByLgpla] Merg WM AND IM END");
				// Finalize with counts

				log.info("[ReporteWorkService getReporteDocInvSAPByLgpla] Operations Begin");

				for (PosDocInvBean docPos : docInvBeanList) {
					if (docPos.getCounted() != null && !docPos.getCounted().equals("")) {
						if (docPos.getCountedExpl() != null && !docPos.getCountedExpl().equals("")) {
							docPos.setCountedTot(new BigDecimal(docPos.getCounted())
									.add(new BigDecimal(docPos.getCountedExpl())).toString());
						} else {
							docPos.setCountedTot(docPos.getCounted());
						}
					} else if (docPos.getCountedExpl() != null && !docPos.getCountedExpl().equals("")) {
						docPos.setCountedTot(docPos.getCountedExpl());
					} else {
						docPos.setCountedTot("0.00");
					}

					if (docPos.getCountedTot() != null && !docPos.getCountedTot().isEmpty()) {
						if (docPos.getTheoric() != null && !docPos.getTheoric().isEmpty()) {
							docPos.setDiff(new BigDecimal(docPos.getCountedTot())
									.subtract(new BigDecimal(docPos.getTheoric())).toString());
						} else {
							docPos.setDiff(docPos.getCountedTot());
						}
					} else {
						if (docPos.getTheoric() != null && !docPos.getTheoric().isEmpty()) {
							docPos.setDiff(docPos.getTheoric());
						} else {
							docPos.setDiff("0.00");
						}
					}

					docPos.setCostByUnit(costByMaterial.get(docPos.getMatnr()) != null
							? costByMaterial.get(docPos.getMatnr()) : "0.00");
					if (docPos.getCountedTot() != null
							&& new BigDecimal(docPos.getCountedTot()).compareTo(BigDecimal.ZERO) > 0) {
						docPos.setCountedCost(new BigDecimal(docPos.getCountedTot())
								.multiply(new BigDecimal(docPos.getCostByUnit())).toString());
					} else {
						docPos.setCountedCost("0.00");
					}
					if (docPos.getTheoric() != null
							&& new BigDecimal(docPos.getTheoric()).compareTo(BigDecimal.ZERO) > 0) {
						docPos.setTheoricCost(new BigDecimal(docPos.getTheoric())
								.multiply(new BigDecimal(docPos.getCostByUnit())).toString());
					} else {
						docPos.setTheoricCost("0.00");
					}
					if (docPos.getDiff() != null) {
						docPos.setDiffCost(new BigDecimal(docPos.getDiff())
								.multiply(new BigDecimal(docPos.getCostByUnit())).toString());
					} else {
						docPos.setDiffCost("0.00");
					}
				}
				log.info("[ReporteWorkService getReporteDocInvSAPByLgpla] Operations End");

				headerSap.setDocInvPosition(docInvBeanList);
				response.setLsObject(headerSap);
				log.info("[ReporteWorkService getReporteDocInvSAPByLgpla] Return:" + response.toString());
			} else {
				result.setResultId(ReturnValues.IERROR);
				result.setResultMsgAbs("¡No se encontro información!");
			}
		} catch (

		Exception e) {
			log.log(Level.SEVERE, "[ReporteWorkService getReporteDocInvSAPByLgpla] catch", e);
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				log.log(Level.SEVERE, "[ReporteWorkService getReporteDocInvSAPByLgpla] catch", e);
			}
		}
		return response;
	}

}
