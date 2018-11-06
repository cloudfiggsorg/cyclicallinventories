package com.gmodelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.bmore.ume001.beans.User;
import com.bmore.ume001.beans.UserGenInfo;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.GroupBean;
import com.gmodelo.beans.GroupToRouteBean;
import com.gmodelo.beans.GroupToUserBean;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class GroupDao {
	private Logger log = Logger.getLogger( GroupDao.class.getName());
	
	public Response<Object> addGroup(GroupBean groupBean, String createdBy){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		
		final String INV_SP_ADD_GROUP = "INV_SP_ADD_GROUP ?, ?, ?, ?, ?"; //The Store procedure to call
		
		log.info("[addGroup] Preparing sentence...");

		try {
			cs = con.prepareCall(INV_SP_ADD_GROUP);
			
			cs.setString(1, groupBean.getGroupId());			
			cs.setString(2, groupBean.getGdesc());	
			cs.setString(3, groupBean.getBukrs());
			cs.setString(4, groupBean.getWerks());
			cs.setString(5, createdBy);
			
			cs.registerOutParameter(1, Types.VARCHAR);
			log.info("[addGroup] Executing query...");
			
			cs.execute();
			groupBean.setGroupId(cs.getString(1));
			abstractResult.setResultId(ReturnValues.ISUCCESS);
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[addGroup] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.info("[addGroup] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[addGroup] Some error occurred while was trying to execute the S.P.: " + INV_SP_ADD_GROUP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[addGroup] Some error occurred while was trying to close the connection.", e);
			}
		}
	
		res.setAbstractResult(abstractResult);
		res.setLsObject(groupBean);
		return res ;
	}

	public Response<Object> assignGroupToUser(GroupToUserBean groupToUserBean, String createdBy){
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		String resultSP = null;
		
		final String INV_SP_ASSIGN_GROUP_TO_USER = "INV_SP_ASSIGN_GROUP_TO_USER ?, ?, ?"; //The Store procedure to call
		log.info("[assignGroupToUserDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_ASSIGN_GROUP_TO_USER);
		
			cs.setString(1,groupToUserBean.getGroupId());
			cs.setString(2,groupToUserBean.getUserId());
			cs.setString(3, createdBy);
			
			log.info("[assignGroupToUserDao] Executing query...");
			
			cs.execute();
			
			 abstractResult.setResultId(ReturnValues.ISUCCESS);
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[assignGroupToUserDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.info("[assignGroupToUserDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[assignGroupToUserDao] Some error occurred while was trying to execute the S.P.: "+INV_SP_ASSIGN_GROUP_TO_USER, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[assignGroupToUserDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		abstractResult.setResultMsgAbs(resultSP);
		res.setAbstractResult(abstractResult);
		return res ;
	}

	public Response<Object> unassignGroupToUser(GroupToUserBean groupToUserBean){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		
		final String INV_SP_DESASSIGN_GROUP_TO_USER = "INV_SP_DESASSIGN_GROUP_TO_USER ?, ?"; //The Store procedure to call
		
		log.info("[unassignGroupToUserDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_DESASSIGN_GROUP_TO_USER);
			
			cs.setString(1,groupToUserBean.getUserId());
			
			cs.setString(2,groupToUserBean.getGroupId());
			log.log(Level.WARNING,"[unassignGroupToUserDao] Executing query...");
			
			cs.execute();
			
			abstractResult.setResultId(ReturnValues.ISUCCESS);
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[unassignGroupToUserDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.info("[unassignGroupToUserDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[unassignGroupToUserDao] Some error occurred while was trying to execute the S.P.: "+INV_SP_DESASSIGN_GROUP_TO_USER, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[unassignGroupToUserDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}
	
	public Response<Object> unassignGroupToRoute(GroupToRouteBean groupToRouteBean){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
	//INV_SP_DESASSIGN_GROUP_TO_ROUTE	ROUTE_ID, GROUP_ID, COUNT_NUM	ROUTE_ID OR  MESSAGE ERROR
	
		final String INV_SP_DESASSIGN_GROUP_TO_ROUTE = "INV_SP_DESASSIGN_GROUP_TO_ROUTE ?, ?, ?, ?"; //The Store procedure to call
		
		log.info("[unassignGroupToRouteDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_DESASSIGN_GROUP_TO_ROUTE);
			
			cs.setString(1,groupToRouteBean.getRouteId());
			cs.setString(2,groupToRouteBean.getGroupId());
			cs.setString(3,groupToRouteBean.getCountNum());
			cs.registerOutParameter(4, Types.INTEGER);
			log.info("[unassignGroupToRouteDao] Executing query...");
			
			cs.execute();
			abstractResult.setResultId(cs.getInt(4));
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[unassignGroupToRouteDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.info("[unassignGroupToRouteDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[unassignGroupToRouteDao] Some error occurred while was trying to execute the S.P.: "+INV_SP_DESASSIGN_GROUP_TO_ROUTE, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[unassignGroupToRouteDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}
	
	public Response<Object> deleteGroup(String arrayIdGroups){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		
		final String INV_SP_DEL_GROUP = "INV_SP_DEL_GROUP ?"; //The Store procedure to call
		
		log.info("[deleteGroupDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_DEL_GROUP);
			
			cs.setString(1,arrayIdGroups);
			
			log.info("[deleteGroupDao] Executing query...");
			
			cs.execute();
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[deleteGroupDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.info("[deleteGroupDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[deleteGroupDao] Some error occurred while was trying to execute the S.P.: "+INV_SP_DEL_GROUP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[deleteGroupDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}

	public Response<List<GroupBean>> getGroupsWithUsers(GroupBean groupB, String searchFilter){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		
		Response<List<GroupBean>> res = new Response<List<GroupBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<GroupBean> listGroupsBean = new ArrayList<GroupBean>(); 
		String INV_VW_GET_GROUPS = null;
		
		int aux;		
		String searchFilterNumber = "";
		
		try {
			aux = Integer.parseInt(searchFilter); 
			searchFilterNumber += aux;
		} catch (Exception e) {
			searchFilterNumber = searchFilter;
			log.info("Trying to convert String to Int");
		}		

		INV_VW_GET_GROUPS = "SELECT IP_GROUP, GDESC FROM [INV_CIC_DB].[dbo].[INV_VW_GET_GROUPS] WITH(NOLOCK) ";
		
		if (searchFilter != null) {
			INV_VW_GET_GROUPS += "WHERE IP_GROUP LIKE '%" + searchFilterNumber + "%' OR GDESC LIKE '%" + searchFilter + "%'";
		} else {
			String condition = buildCondition(groupB);
			if (condition != null) {
				INV_VW_GET_GROUPS += condition;
			}
		}
		
		log.info(INV_VW_GET_GROUPS);
		log.info("[getGroupsDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_GET_GROUPS);		
			log.info("[getGroupsDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
				groupB = new GroupBean();				
				groupB.setGroupId(rs.getString("IP_GROUP"));
				groupB.setGdesc(rs.getString("GDESC"));
				groupB.setUsers(this.groupUsers(rs.getString("IP_GROUP"), null));
				listGroupsBean.add(groupB);
			}
			
			//Retrive the warnings if there're
			SQLWarning warning = stm.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			stm.close();
			log.info("[getGroupsDao] Sentence successfully executed.");
		} catch (SQLException | NamingException e) {
			log.log(Level.SEVERE,"[getGroupsDao] Some error occurred while was trying to execute the query: "+INV_VW_GET_GROUPS, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getMantrDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		
		res.setAbstractResult(abstractResult);
		res.setLsObject(listGroupsBean);
		return res;
	}
	
	public Response<List<GroupBean>> getOnlyGroup(GroupBean groupBean, String searchFilter){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		
		Response<List<GroupBean>> res = new Response<List<GroupBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<GroupBean> listGroupsBean = new ArrayList<GroupBean>(); 
		GroupBean groupB = null;		
		String INV_VW_GET_GROUPS = null;						

		INV_VW_GET_GROUPS = "SELECT IP_GROUP, GDESC, IVWBB.BUKRS, BUTXT, IVWBB.WERKS, NAME1 FROM [INV_CIC_DB].[dbo].[INV_VW_GET_GROUPS] AS IVGG WITH(NOLOCK) ";
		INV_VW_GET_GROUPS += "INNER JOIN INV_VW_WERKS_BY_BUKRS AS IVWBB ON (IVWBB.BUKRS = IVGG.BUKRS AND IVWBB.WERKS = IVGG.WERKS) ";		
		INV_VW_GET_GROUPS += "WHERE IP_GROUP LIKE '%" + searchFilter + "%' OR GDESC LIKE '%" + searchFilter + "%' ";
		INV_VW_GET_GROUPS += "AND IVWBB.BUKRS LIKE '%" + (groupBean.getBukrs() == null? "" : groupBean.getBukrs()) + "%' ";
		INV_VW_GET_GROUPS += "AND IVWBB.WERKS LIKE '%" + (groupBean.getWerks() == null? "" : groupBean.getWerks()) + "%' ";		
		
		log.info(INV_VW_GET_GROUPS);
		log.info("[getGroupsDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_GET_GROUPS);		
			log.info("[getGroupsDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
				groupB = new GroupBean();				
				groupB.setGroupId(rs.getString("IP_GROUP"));
				groupB.setGdesc(rs.getString("GDESC"));
				groupB.setBukrs(rs.getString("BUKRS"));
				groupB.setbDesc(rs.getString("BUTXT"));
				groupB.setWerks(rs.getString("WERKS"));
				groupB.setwDesc(rs.getString("NAME1"));
				listGroupsBean.add(groupB);
			}
			
			//Retrive the warnings if there're
			SQLWarning warning = stm.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			stm.close();
			log.info("[getGroupsDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getGroupsDao] Some error occurred while was trying to execute the query: "+INV_VW_GET_GROUPS, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getMantrDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		
		res.setAbstractResult(abstractResult);
		res.setLsObject(listGroupsBean);
		return res;
	}

	public List<User> groupUsers(String groupId, String userId) throws SQLException, NamingException{
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<User> listUser = new ArrayList<User>();
		
		String INV_VW_GROUPS_USER = "SELECT PK_GROUP_USER, GRU_USER_ID FROM INV_GROUPS_USER WHERE GRU_GROUP_ID = ? ";
		INV_VW_GROUPS_USER = (userId != null) ?  INV_VW_GROUPS_USER+= ("AND GRU_USER_ID LIKE '%"+userId+"%'") : INV_VW_GROUPS_USER;
		log.info(INV_VW_GROUPS_USER);
		log.info("[groupUsersDao] Preparing sentence...");
		INV_VW_GROUPS_USER += " GROUP BY PK_GROUP_USER, GRU_USER_ID";
		
		stm = con.prepareCall(INV_VW_GROUPS_USER);
		stm.setString(1, groupId);
		log.info("[groupUsersDao] Executing query...");
		ResultSet rs = stm.executeQuery();
		
		UMEDaoE ume = new UMEDaoE();
		while (rs.next()){
			log.info(rs.getString("GRU_USER_ID"));
			User userB = new User();
			
			userB.getEntity().setIdentyId(rs.getString("GRU_USER_ID"));
			ArrayList<User> lista = new ArrayList<>();
			lista.add(userB);
			ArrayList<User> lsUser = ume.getUsersLDAPByCredentials(lista);
			if(lsUser.size() > 0)
			listUser.add(ume.getUsersLDAPByCredentials(lista).get(0));
		}
	
		//Retrive the warnings if there're
		SQLWarning warning = stm.getWarnings();
		while (warning != null) {
			log.log(Level.WARNING,warning.getMessage());
			warning = warning.getNextWarning();
		}
		
		//Free resources
		rs.close();
		stm.close();	
		con.close();	
		log.info("[groupUsersDao] Sentence successfully executed.");
		return listUser;
	}

	public UserGenInfo getUsers(String idUser){
		UMEDaoE ume = new UMEDaoE();
		UserGenInfo user = ume.getUserInfo(idUser);
		return user;
	}
	
	private String buildCondition(GroupBean groupB){
		String groupId = "";
		String gdes = "";
		
		String condition = "";
		
		groupId = (groupB.getGroupId() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" IP_GROUP LIKE '%"+ groupB.getGroupId() + "%' "  : "";
		condition+=groupId;
		gdes = (groupB.getGdesc() != null) 		? (condition.contains("WHERE") ? " AND " : " WHERE ") + " GDESC LIKE '%" + groupB.getGdesc() +"%' " : "";
		condition+=gdes;		
		condition = condition.isEmpty() ? null : condition;
		
		return condition;
	}
}
