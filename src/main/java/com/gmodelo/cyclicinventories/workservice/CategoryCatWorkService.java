package com.gmodelo.cyclicinventories.workservice;

import java.util.List;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.Category;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.CategoryCatDao;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;

public class CategoryCatWorkService {
	
	private Logger log = Logger.getLogger(ZoneWorkService.class.getName());
	private Gson gson = new Gson();

	public Response<Category> saveCategory(Request<?> request, String userId) {
		log.info("[saveCategoryWS] " + request.toString());
		Category category = null;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<Category> res = new Response<>();
		try {
			category = gson.fromJson(gson.toJson(request.getLsObject()), Category.class);
			res = new CategoryCatDao().saveCategory(category, userId);
		} catch (Exception e) {
			log.info("[saveCategoryWS] Error al convertir a objeto.");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);			
		}

		return res;
	}

	public Response<Object> deleteCategories(Request<?> request) {
		log.info("[deleteCategoriesWS] " + request.toString());
		String catIds = null;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<Object> res = new Response<>();
		try {
			catIds = request.getLsObject().toString();
			res = new CategoryCatDao().deleteCategories(catIds);
		} catch (Exception e) {
			log.info("[deleteCategoriesWS] Error al obtener los Ids");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);			
		}

		return res;
	}
	
	public Response<List<Category>> getCategories() {
		
		log.info("[getCategoriesWS]... "); 
		return new CategoryCatDao().getCategories();
	}

}
