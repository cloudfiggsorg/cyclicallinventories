package com.gmodelo.cyclicinventories.workservice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.GroupBean;
import com.gmodelo.cyclicinventories.beans.GroupToRouteBean;
import com.gmodelo.cyclicinventories.beans.GroupToUserBean;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.GroupDao;
import com.gmodelo.cyclicinventories.dao.UMEDaoE;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GroupWorkService {

	private Logger log = Logger.getLogger(GroupWorkService.class.getName());
	private Gson gson = new Gson();

	public Response<Object> addGroup(Request request, User user) {

		log.info("[addGroupWS] " + request.toString());

		GroupBean groupBean;
		Response<Object> res = new Response<>();

		try {
			groupBean = gson.fromJson(gson.toJson(request.getLsObject()), GroupBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[addGroupWS] Error al pasar de Json a GroupBean");
			groupBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new GroupDao().addGroup(groupBean, user.getEntity().getIdentyId());

	}

	public Response<Object> assignGroupToUser(Request request, User user) {

		log.info("[assignGroupToUserWS] " + request.toString());
		GroupToUserBean groupToUserBean;
		Response<Object> res = new Response<>();

		try {
			groupToUserBean = gson.fromJson(gson.toJson(request.getLsObject()), GroupToUserBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[assignGroupToUserWS] Error al pasar de Json a GroupToUserBean");
			groupToUserBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new GroupDao().assignGroupToUser(groupToUserBean, user.getEntity().getIdentyId());

	}

	public Response<Object> unassignGroupToUser(Request request) {

		log.info("[unassignGroupToUserWS] " + request.toString());
		GroupToUserBean groupToUserBean;
		Response<Object> res = new Response<>();

		try {
			groupToUserBean = gson.fromJson(gson.toJson(request.getLsObject()), GroupToUserBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[unassignGroupToUserWS] Error al pasar de Json a GroupToUserBean");
			groupToUserBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new GroupDao().unassignGroupToUser(groupToUserBean);

	}

	public Response<Object> unassignGroupToRoute(Request request) {

		log.info("[unassignGroupToRouteWS] " + request.toString());
		GroupToRouteBean groupToRouteBean;
		Response<Object> res = new Response<>();

		try {
			groupToRouteBean = gson.fromJson(gson.toJson(request.getLsObject()), GroupToRouteBean.class);
		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[unassignGroupToRouteWS] Error al pasar de Json a GroupToRouteBean");
			groupToRouteBean = null;
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new GroupDao().unassignGroupToRoute(groupToRouteBean);

	}

	public Response<Object> deleteGroup(Request request) {

		log.info("[deleteGroupWS] " + request.toString());
		String arrayIdGroups;
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		try {
			arrayIdGroups = request.getLsObject().toString();

			if (arrayIdGroups == null || arrayIdGroups.isEmpty()) {
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs("NULL OR EMPTY ARRAY");
				res.setAbstractResult(abstractResult);
				return res;
			}

		} catch (JsonSyntaxException e) {
			log.log(Level.SEVERE, "[deleteGroupWS] Error al pasar de Json a GroupBean");

			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new GroupDao().deleteGroup(arrayIdGroups);

	}

	public Response<List<GroupBean>> getGroups(Request request) {
		log.info("[GroupsWorkService] " + request.toString());
		GroupBean groupsBean = null;
		String searchFilter = null;
		String req = request.getLsObject().toString().trim();

		if (!req.isEmpty()) {

			try {

				groupsBean = gson.fromJson(gson.toJson(request.getLsObject()), GroupBean.class);
				log.info("Fue Objeto: " + request.getLsObject().toString());

			} catch (JsonSyntaxException e1) {

				log.info("Intentando por String");
				searchFilter = request.getLsObject().toString().trim();
			}

		} else {

			searchFilter = "";
			log.info("Fue cadena vacía ");
		}

		return new GroupDao().getGroupsWithUsers(groupsBean, searchFilter);
	}

	// Metodo para obtener usuarios de la UME que se usan para GroupWorkService
	public Response<ArrayList<User>> getUMEUsers(Request request) {
		log.info(request.toString());
		String req = request.getLsObject().toString().trim();
		UMEDaoE UMEDao = new UMEDaoE();
		Response<ArrayList<User>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ArrayList<User> lista = null;
		User user = new User();
		if (!req.isEmpty()) {
			try {
				
				user.getEntity().setIdentyId(request.getLsObject().toString().trim());
				user.getGenInf().setName(request.getLsObject().toString().trim());
				lista = new ArrayList<>();
				lista.add(user);
				lista = UMEDao.getUsersLDAPByCredentials(lista);
			} catch (NamingException e) {
				log.log(Level.SEVERE, "[getUMEUsersWS] NamingException ", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgGen(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		} else {
			log.info("Sin lista");
			try {
				lista = new ArrayList<>();
				user = new User();
				lista.add(user);
				lista = UMEDao.getUsersLDAPByCredentials(lista);
			} catch (NamingException e) {
				log.log(Level.SEVERE, "[getUMEUsersWS] NamingException ", e);

				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgGen(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		log.info("Tamanio lista " + lista.size());
		res.setLsObject(lista);
		abstractResult.setResultId(ReturnValues.ISUCCESS);
		res.setAbstractResult(abstractResult);
		return res;

	}

	public Response<List<GroupBean>> getOnlyGroups(Request<List<Object>> request) {
		log.info("[GroupsWorkService] " + request.toString());
		GroupBean groupsBean = null;
		String searchFilter = null;
		String req = request.getLsObject().toString();
		Response<List<GroupBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();	

		if (!req.isEmpty()) {

			try {
				
				groupsBean = gson.fromJson(gson.toJson(request.getLsObject().get(0)), GroupBean.class);
				searchFilter = (String)request.getLsObject().get(1);

			} catch (JsonSyntaxException e1) {

				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs("Error. Getting data...");
				res.setAbstractResult(abstractResult);
				return res;
			}

		} else {

			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs("Error. Missing data...");
			res.setAbstractResult(abstractResult);
			return res;
		}

		return new GroupDao().getOnlyGroup(groupsBean, searchFilter);
	}

	public Response<List<User>> getUsersGroup(Request request) {
		log.info("[getUsersGroupWorkService] "+request);
		GroupToUserBean groupUser = new GroupToUserBean();
		groupUser = gson.fromJson(gson.toJson(request.getLsObject()), GroupToUserBean.class);
		Response<List<User>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		res.setAbstractResult(abstractResult);
		try {
			res.setLsObject(new GroupDao().groupUsers(groupUser.getGroupId(),groupUser.getUserId()));
			abstractResult.setResultId(ReturnValues.ISUCCESS);
		} catch (SQLException | NamingException e) {
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}
		return res;
	}

}
