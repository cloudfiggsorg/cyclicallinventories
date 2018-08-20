package com.gmodelo.workservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.GroupBean;
import com.gmodelo.beans.GroupToRouteBean;
import com.gmodelo.beans.GroupToUserBean;
import com.gmodelo.beans.GroupsB;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.GroupDao;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GroupWorkService {

	private Logger log = Logger.getLogger(GroupWorkService.class.getName());
	
	public Response<Object> addGroup(Request<?> request, User user){
		
		log.log(Level.WARNING,"[addGroupWS] "+request.toString());
		GroupBean groupBean;
		Response<Object> res = new Response<Object>();
		
		try {
			groupBean = new Gson().fromJson(request.getLsObject().toString(), GroupBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[addGroupWS] Error al pasar de Json a GroupBean");
			groupBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		return new GroupDao().addGroup(groupBean, user.getEntity().getIdentyId());
		
	}
	
	public Response<Object> assignGroupToUser(Request<?> request,  User user){
		
		log.log(Level.WARNING,"[assignGroupToUserWS] "+request.toString());
		GroupToUserBean groupToUserBean;
		Response<Object> res = new Response<Object>();
		
		try {
			groupToUserBean = new Gson().fromJson(request.getLsObject().toString(), GroupToUserBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[assignGroupToUserWS] Error al pasar de Json a GroupToUserBean");
			groupToUserBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
	
		return new GroupDao().assignGroupToUser(groupToUserBean , user.getEntity().getIdentyId());
	
	}
	
	public Response<Object> unassignGroupToUser(Request<?> request){
		
		log.log(Level.WARNING,"[unassignGroupToUserWS] "+request.toString());
		GroupToUserBean groupToUserBean;
		Response<Object> res = new Response<Object>();
		
		try {
			groupToUserBean = new Gson().fromJson(request.getLsObject().toString(), GroupToUserBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[unassignGroupToUserWS] Error al pasar de Json a GroupToUserBean");
			groupToUserBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
	
		return new GroupDao().unassignGroupToUser(groupToUserBean);
	
	}

	public Response<Object> assignGroupToRoute(Request<?> request){
		
		log.log(Level.WARNING,"[assignGroupToRouteWS] "+request.toString());
		GroupToRouteBean groupToRouteBean;
		Response<Object> res = new Response<Object>();
		
		try {
			groupToRouteBean = new Gson().fromJson(request.getLsObject().toString(), GroupToRouteBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[assignGroupToRouteWS] Error al pasar de Json a GroupToUserBean");
			groupToRouteBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
	
		return new GroupDao().assignGroupToRoute(groupToRouteBean );
	
	}

	public Response<Object> unassignGroupToRoute(Request<?> request){
		
		log.log(Level.WARNING,"[unassignGroupToRouteWS] "+request.toString());
		GroupToRouteBean groupToRouteBean;
		Response<Object> res = new Response<Object>();
		
		try {
			groupToRouteBean = new Gson().fromJson(request.getLsObject().toString(), GroupToRouteBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[unassignGroupToRouteWS] Error al pasar de Json a GroupToRouteBean");
			groupToRouteBean = null;
			AbstractResults abstractResult = new AbstractResults();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
	
		return new GroupDao().unassignGroupToRoute(groupToRouteBean);
	
	}
	
	public Response<Object> deleteGroup(Request<?> request){
		
		log.log(Level.WARNING,"[deleteGroupWS] "+request.toString());
		String arrayIdGroups;
		StringBuilder stringGroups = new StringBuilder();
		Response<Object> res = new Response<Object>();
		AbstractResults abstractResult = new AbstractResults();
		
		try {
			arrayIdGroups = request.getLsObject().toString();
			
			if(arrayIdGroups == null || arrayIdGroups.isEmpty()){
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs("NULL OR EMPTY ARRAY");
				res.setAbstractResult(abstractResult);
				return res;
			}
			
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[deleteGroupWS] Error al pasar de Json a GroupBean");
			
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
	
		return new GroupDao().deleteGroup(stringGroups.toString());
	
	}

	public Response<List<GroupsB>> getGroups(Request request){
		log.log(Level.WARNING,"[GroupsWorkService] "+request.toString());
		GroupsB groupsBean = null;
		String searchFilter = null;		
		String req = request.getLsObject().toString().trim();
		
		if(!req.isEmpty()){
			
			try {
				
				groupsBean = new Gson().fromJson(request.getLsObject().toString(), GroupsB.class) ;
				log.log(Level.WARNING, "Fue Objeto: " + request.getLsObject().toString());
				
			}catch (JsonSyntaxException e1){
				
				log.log(Level.WARNING, "Intentando por String");
				searchFilter = request.getLsObject().toString();
			}
			
		}else{
			
			searchFilter = "";
			log.log(Level.WARNING, "Fue cadena vacía ");
		}
		
		return new GroupDao().getGroups(groupsBean, searchFilter);
	}

}
