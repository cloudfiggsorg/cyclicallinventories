package com.gmodelo.cyclicinventories.workservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.ApegosBean;
import com.gmodelo.cyclicinventories.beans.ConciAccntReportBean;
import com.gmodelo.cyclicinventories.beans.ConciliationsIDsBean;
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.DocInvBeanHeaderSAP;
import com.gmodelo.cyclicinventories.beans.ProductivityBean;
import com.gmodelo.cyclicinventories.beans.ReporteConteosBean;
import com.gmodelo.cyclicinventories.beans.ReporteDocInvBeanHeader;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.ReportesDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;

public class ReportesWorkService {

	private Logger log = Logger.getLogger(ReportesWorkService.class.getName());
	private Gson gson = new Gson();

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
	
	public Response<DocInvBeanHeaderSAP> getReporteDocInvSAP(Request request) {
		log.info("[ReporteWorkService getReporteDocInv] " + request.toString());
		Response<DocInvBeanHeaderSAP> response = new Response<>();		
		DocInvBean bean = null;
		try {
			log.info("[ReporteWorkService getReporteDocInv] try");
			bean = gson.fromJson(gson.toJson(request.getLsObject()), DocInvBean.class);
			response = new ReportesDao().getConsDocInv(bean);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[ReporteWorkService getReporteDocInv] catch", e);
			AbstractResultsBean result = new AbstractResultsBean();
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
			response.setAbstractResult(result);
		}
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
			log.log(Level.SEVERE, "getCountedProductivityTareasWorkService] catch", e );
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
			Response<List<ProductivityBean>> badResponse = new Response<>();
			badResponse.setAbstractResult(result);
			return badResponse ;
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
		} catch (Exception e) {
			log.log(Level.SEVERE, "[getUserProductivityWorkService] catch", e );
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
			Response<List<ProductivityBean>> badResponse = new Response<>();
			badResponse.setAbstractResult(result);
			return badResponse ;
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
		ConciAccntReportBean conciAccntReportBean = gson.fromJson(gson.toJson(request.getLsObject()), ConciAccntReportBean.class);
		response = new ReportesDao().getReporteConciAccntReport(conciAccntReportBean);
		if(response.getAbstractResult().getResultId() != 1){
			return response; 
		}else{
			list = response.getLsObject();
			
			List<String> listDocInv = new ArrayList<>();
			
			HashMap<String, List<ConciAccntReportBean>> map = new HashMap<>(); 
			
			
			for(ConciAccntReportBean item : list){
//				item.setAccountant(80000D);
				item.setType(item.getType() == "1" ? "Diario" : "Mensual");
//				Obteniendo los DocId unicos y generando listas para cada doc inv existente
				if(listDocInv.size() == 0){
					listDocInv.add(item.getDocInvId());
					List<ConciAccntReportBean> listBean = new ArrayList<>();
					
					map.put(item.getDocInvId(), listBean);
				}else{
					boolean existDocId = false;
					if(listDocInv.contains(item.getDocInvId())){
						existDocId = true;
					}
					if(!existDocId){
						listDocInv.add(item.getDocInvId());
						List<ConciAccntReportBean> listBean = new ArrayList<>();
						map.put(item.getDocInvId(), listBean);
					}
				}

			}
			
//			Agregando cada item en su lista por doc inv correspondiente
			for(String id : listDocInv){
				for(ConciAccntReportBean item : list){
					if(id.equalsIgnoreCase(item.getDocInvId())){
						map.get(id).add(item);
					}
				}
			}
			
			List<ConciAccntReportBean> finalList = new ArrayList<>();
			
			for(String id : listDocInv){
				List<Integer> catIdList = new ArrayList<>();
//				obteniendo todas las categorias de un docInv y creando los beans final
				for(ConciAccntReportBean carb :map.get(id)){
					if(catIdList.size() == 0){
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
					}else{
						boolean existCatId = false;
						if(catIdList.contains(carb.getCatId())){
							existCatId = true;
						}
						if(!existCatId){
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
				
//				usando listaFinal para sumar accountant, justification y difference en los beans finales
				for(ConciAccntReportBean finalBean : finalList){
					for(ConciAccntReportBean carb :map.get(id)){
						
						if(finalBean.getDocInvId().equalsIgnoreCase(id) && finalBean.getCatId() == carb.getCatId()){
							finalBean.setAccountant(finalBean.getAccountant()+carb.getAccountant());
							finalBean.setJustification(finalBean.getJustification()+carb.getJustification());
							finalBean.setAccDiff(finalBean.getAccDiff()+carb.getAccDiff());
						}
					}
				}
				
			}
			
			for(ConciAccntReportBean item : finalList){
				item.setPercAccDiff(String.valueOf((item.getAccDiff()*100)/item.getAccountant()));
				item.setPercJustification(String.valueOf((item.getJustification()*100)/item.getAccountant()));
			}
			response.setLsObject(finalList);
		}
		return response;
	}
}
