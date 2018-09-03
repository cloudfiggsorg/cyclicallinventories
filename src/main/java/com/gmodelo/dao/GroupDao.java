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
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		int resultSP = -1;
		
		final String INV_SP_ADD_GROUP = "INV_SP_ADD_GROUP ?, ?, ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[addGroup] Preparing sentence...");

		try {
			cs = con.prepareCall(INV_SP_ADD_GROUP);
			
			cs.setString(1,groupBean.getGroupId());
			
			cs.setString(2,groupBean.getGroupDesc());
			
			cs.setString(4, createdBy);
			
			cs.registerOutParameter(5, Types.INTEGER);
			log.log(Level.WARNING,"[addGroup] Executing query...");
			
			 cs.execute();
			
			 abstractResult.setResultId(cs.getInt(5));
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[addGroup] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.log(Level.WARNING,"[addGroup] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[addGroup] Some error occurred while was trying to execute the S.P.: "+INV_SP_ADD_GROUP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[addGroup] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
	
		res.setAbstractResult(abstractResult);
		return res ;
	}

	public Response<Object> assignGroupToUser(GroupToUserBean groupToUserBean, String createdBy){
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		String resultSP = null;
		
		final String INV_SP_ASSIGN_GROUP_TO_USER = "INV_SP_ASSIGN_GROUP_TO_USER ?, ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[assignGroupToUserDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_ASSIGN_GROUP_TO_USER);
		
			cs.setString(1,groupToUserBean.getGroupId());
			cs.setString(2,groupToUserBean.getUserId());
			cs.setString(3, createdBy);
			cs.registerOutParameter(4, Types.INTEGER);
			
			log.log(Level.WARNING,"[assignGroupToUserDao] Executing query...");
			
			cs.execute();
			abstractResult.setResultId(cs.getInt(4));
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[assignGroupToUserDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.log(Level.WARNING,"[assignGroupToUserDao] Sentence successfully executed.");
			
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
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				return res;
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
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		
		final String INV_SP_DESASSIGN_GROUP_TO_USER = "INV_SP_DESASSIGN_GROUP_TO_USER ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[unassignGroupToUserDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_DESASSIGN_GROUP_TO_USER);
			
			cs.setString(1,groupToUserBean.getUserId());
			
			cs.setString(2,groupToUserBean.getGroupId());
			
			cs.registerOutParameter(3, Types.INTEGER);
			log.log(Level.WARNING,"[unassignGroupToUserDao] Executing query...");
			
			cs.execute();
			
			abstractResult.setResultId(cs.getInt(3));
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[unassignGroupToUserDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.log(Level.WARNING,"[unassignGroupToUserDao] Sentence successfully executed.");
			
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
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}
	
	public Response<Object> unassignGroupToRoute(GroupToRouteBean groupToRouteBean){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
	//INV_SP_DESASSIGN_GROUP_TO_ROUTE	ROUTE_ID, GROUP_ID, COUNT_NUM	ROUTE_ID OR  MESSAGE ERROR
	
		final String INV_SP_DESASSIGN_GROUP_TO_ROUTE = "INV_SP_DESASSIGN_GROUP_TO_ROUTE ?, ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[unassignGroupToRouteDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_DESASSIGN_GROUP_TO_ROUTE);
			
			cs.setString(1,groupToRouteBean.getRouteId());
			cs.setString(2,groupToRouteBean.getGroupId());
			cs.setString(3,groupToRouteBean.getCountNum());
			cs.registerOutParameter(4, Types.INTEGER);
			log.log(Level.WARNING,"[unassignGroupToRouteDao] Executing query...");
			
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
			
			log.log(Level.WARNING,"[unassignGroupToRouteDao] Sentence successfully executed.");
			
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
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}
	
	public Response<Object> deleteGroup(String arrayIdGroups){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		
		final String INV_SP_DEL_GROUP = "INV_SP_DEL_GROUP ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[deleteGroupDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_DEL_GROUP);
			
			cs.setString(1,arrayIdGroups);
			
			log.log(Level.WARNING,"[deleteGroupDao] Executing query...");
			
			cs.execute();
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[deleteGroupDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.log(Level.WARNING,"[deleteGroupDao] Sentence successfully executed.");
			
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
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}

public Response<List<GroupBean>> getGroups(GroupBean groupB, String searchFilter){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
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
			log.warning("Trying to convert String to Int");
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
		
		log.warning(INV_VW_GET_GROUPS);
		log.log(Level.WARNING,"[getGroupsDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_GET_GROUPS);		
			log.log(Level.WARNING,"[getGroupsDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
				groupB = new GroupBean();				
				groupB.setGroupId(rs.getString("IP_GROUP"));
				groupB.setGroupDesc(rs.getString("GDESC"));
				groupB.setUsers(this.groupUsers(rs.getString("IP_GROUP")));
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
			log.log(Level.WARNING,"[getGroupsDao] Sentence successfully executed.");
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
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		
		res.setAbstractResult(abstractResult);
		res.setLsObject(listGroupsBean);
		return res;
	}

	public List<User> groupUsers(String groupId) throws SQLException{
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<User> listUser = new ArrayList<User>();
		
		String INV_VW_GROUPS_USER = "SELECT PK_GROUP_USER, GRU_USER_ID FROM INV_GROUPS_USER WHERE GRU_GROUP_ID = ? ";
		log.warning(INV_VW_GROUPS_USER);
		log.log(Level.WARNING,"[groupUsersDao] Preparing sentence...");
		INV_VW_GROUPS_USER += " GROUP BY PK_GROUP_USER, GRU_USER_ID";
		
		stm = con.prepareCall(INV_VW_GROUPS_USER);
		stm.setString(1, groupId);
		log.log(Level.WARNING,"[groupUsersDao] Executing query...");
		ResultSet rs = stm.executeQuery();
		
		UMEDaoE ume = new UMEDaoE();
		while (rs.next()){
			System.out.println(rs.getString("GRU_USER_ID"));
			User userB = new User();
			userB.getEntity().setIdentyId(rs.getString("GRU_USER_ID"));
			UserGenInfo userInfo = ume.getUserInfo(rs.getString("GRU_USER_ID"));
			userB.setGenInf(userInfo);
			listUser.add(userB);
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
		log.log(Level.WARNING,"[groupUsersDao] Sentence successfully executed.");
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
		gdes = (groupB.getGroupDesc() != null) 		? (condition.contains("WHERE") ? " AND " : " WHERE ") + " GDESC LIKE '%" + groupB.getGroupDesc() +"%' " : "";
		condition+=gdes;		
		condition = condition.isEmpty() ? null : condition;
		
		return condition;
	}
	
	
	public static void main(String[] args) {
		GroupDao dao = new GroupDao();
		String searchFilter = "";
		GroupBean groupB = new GroupBean();
		Response<List<GroupBean>> x = dao.getGroups(groupB, searchFilter);
		
		for(int i=0; i< x.getLsObject().size(); i++){
			System.out.println(x.getLsObject().get(i).toString());
		}
	}
}
