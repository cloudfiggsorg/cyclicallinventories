package com.gmodelo.workservice;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.GroupBean;
import com.gmodelo.beans.GroupToRouteBean;
import com.gmodelo.beans.GroupToUserBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.GroupDao;
import com.gmodelo.dao.UMEDaoE;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class GroupWorkService {

	private Logger log = Logger.getLogger(GroupWorkService.class.getName());
	
	public Response<Object> addGroup(Request request, User user){
		
		log.info("[addGroupWS] "+request.toString());
		Gson gson = new Gson();
		GroupBean groupBean;
		Response<Object> res = new Response<Object>();
		
		try {
			groupBean = gson.fromJson(gson.toJson(request.getLsObject()), GroupBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[addGroupWS] Error al pasar de Json a GroupBean");
			groupBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		
		return new GroupDao().addGroup(groupBean, user.getEntity().getIdentyId());
		
	}
	
	public Response<Object> assignGroupToUser(Request request,  User user){
		
		log.info("[assignGroupToUserWS] "+request.toString());
		GroupToUserBean groupToUserBean;
		Response<Object> res = new Response<Object>();
		
		try {
			groupToUserBean = new Gson().fromJson(request.getLsObject().toString(), GroupToUserBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[assignGroupToUserWS] Error al pasar de Json a GroupToUserBean");
			groupToUserBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
	
		return new GroupDao().assignGroupToUser(groupToUserBean , user.getEntity().getIdentyId());
	
	}
	
	public Response<Object> unassignGroupToUser(Request request){
		
		log.info("[unassignGroupToUserWS] "+request.toString());
		GroupToUserBean groupToUserBean;
		Response<Object> res = new Response<Object>();
		
		try {
			groupToUserBean = new Gson().fromJson(request.getLsObject().toString(), GroupToUserBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[unassignGroupToUserWS] Error al pasar de Json a GroupToUserBean");
			groupToUserBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
	
		return new GroupDao().unassignGroupToUser(groupToUserBean);
	
	}

	public Response<Object> unassignGroupToRoute(Request request){
		
		log.info("[unassignGroupToRouteWS] "+request.toString());
		GroupToRouteBean groupToRouteBean;
		Response<Object> res = new Response<Object>();
		
		try {
			groupToRouteBean = new Gson().fromJson(request.getLsObject().toString(), GroupToRouteBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE,"[unassignGroupToRouteWS] Error al pasar de Json a GroupToRouteBean");
			groupToRouteBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
	
		return new GroupDao().unassignGroupToRoute(groupToRouteBean);
	
	}
	
	public Response<Object> deleteGroup(Request request){
		
		log.info("[deleteGroupWS] "+request.toString());
		String arrayIdGroups;
		StringBuilder stringGroups = new StringBuilder();
		Response<Object> res = new Response<Object>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		
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

	public Response<List<GroupBean>> getGroups(Request request){
		log.info("[GroupsWorkService] "+request.toString());
		GroupBean groupsBean = null;
		String searchFilter = null;		
		String req = request.getLsObject().toString().trim();
		
		if(!req.isEmpty()){
			
			try {
				
				groupsBean = new Gson().fromJson(request.getLsObject().toString(), GroupBean.class) ;
				log.info("Fue Objeto: " + request.getLsObject().toString());
				
			}catch (JsonSyntaxException e1){
				
				log.info("Intentando por String");
				searchFilter = request.getLsObject().toString().trim();
			}
			
		}else{
			
			searchFilter = "";
			log.info("Fue cadena vacía ");
		}
		
		return new GroupDao().getGroups(groupsBean, searchFilter);
	}
	
	//Metodo para obtener usuarios de la UME que se usan para GroupWorkService
	public Response<ArrayList<User>> getUMEUsers(Request request){
		log.info(request.toString());
		String req = request.getLsObject().toString().trim();
		UMEDaoE UMEDao = new UMEDaoE();
		Response<ArrayList<User>> res = new Response<ArrayList<User>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ArrayList<User> lista = null;
		User user = new User();
		if(!req.isEmpty()){
			try {
				user.getEntity().setIdentyId(request.getLsObject().toString());
				lista = new ArrayList<>();
				lista.add(user);
				lista = UMEDao.getUsersLDAPByCredentials(lista);
			} catch (NamingException e) {
				log.log(Level.SEVERE,"[getUMEUsersWS] NamingException ", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgGen(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}else{
			log.info("Sin lista");
			try {
				lista = new ArrayList<>();
				user = new User();
				lista.add(user);
				lista = UMEDao.getUsersLDAPByCredentials(lista);
			} catch (NamingException e) {
				log.log(Level.SEVERE,"[getUMEUsersWS] NamingException ", e);
				
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgGen(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
	log.info("Tamanio lista "+lista.size());	
	res.setLsObject(lista);	
	abstractResult.setResultId(ReturnValues.ISUCCESS);
	res.setAbstractResult(abstractResult);
	return 	res;
		
	}

}
