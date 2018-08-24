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

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.GroupBean;
import com.gmodelo.beans.GroupToRouteBean;
import com.gmodelo.beans.GroupToUserBean;
import com.gmodelo.beans.GroupsB;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class GroupDao {
	private Logger log = Logger.getLogger( GroupDao.class.getName());
	
	public Response<Object> addGroup(GroupBean groupBean, String createdBy){
		
		Response<Object> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
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

			cs.setString(3,groupBean.getGroupType());
			
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
		AbstractResults abstractResult = new AbstractResults();
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
		AbstractResults abstractResult = new AbstractResults();
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
	
	public Response<Object> assignGroupToRoute(GroupToRouteBean groupToRouteBean){
		Response<Object> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		String resultSP = null;

		final String INV_SP_ASSIGN_GROUP_TO_ROUTE = "INV_SP_ASSIGN_GROUP_TO_ROUTE ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[assignGroupToRouteDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_ASSIGN_GROUP_TO_ROUTE);
			
			cs.setString(1,groupToRouteBean.getRouteId());
			cs.setString(2,groupToRouteBean.getGroupId());
			cs.setString(3,groupToRouteBean.getCountNum());
			cs.registerOutParameter(4, Types.INTEGER);
			
			log.log(Level.WARNING,"[assignGroupToRouteDao] Executing query...");
			
			cs.execute();
			
			abstractResult.setResultId(cs.getInt(4));
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[assignGroupToRouteDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.log(Level.WARNING,"[assignGroupToRouteDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[assignGroupToRouteDao] Some error occurred while was trying to execute the S.P.: "+INV_SP_ASSIGN_GROUP_TO_ROUTE, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[assignGroupToRouteDao] Some error occurred while was trying to close the connection.", e);
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
		AbstractResults abstractResult = new AbstractResults();
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
		AbstractResults abstractResult = new AbstractResults();
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

	public Response<List<GroupsB>> getGroups(GroupsB groupB, String searchFilter){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		
		Response<List<GroupsB>> res = new Response<List<GroupsB>>();
		AbstractResults abstractResult = new AbstractResults();
		List<GroupsB> listGroupsBean = new ArrayList<GroupsB>(); 
		 
		String INV_VW_GET_GROUPS = "SELECT IP_GROUP, GDESC, GTYPE, CREATE_BY, CREATED_DATE  FROM [INV_CIC_DB].[dbo].[INV_VW_GET_GROUPS] WITH(NOLOCK) ";
		
		if(searchFilter ==  null){
			
			String condition = buildCondition(groupB);
			if(condition != null){
				
				INV_VW_GET_GROUPS += condition;
			}
			
		}else{
			
			INV_VW_GET_GROUPS += "WHERE IP_GROUP LIKE '%"+searchFilter+"%' OR GDESC LIKE '%"+searchFilter+"%'";
		}
		
		log.warning(INV_VW_GET_GROUPS);
						
		log.log(Level.WARNING,"[getGroupsDao] Preparing sentence...");
		try {
			
			stm = con.prepareStatement(INV_VW_GET_GROUPS);		
			
			log.log(Level.WARNING,"[getGroupsDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				groupB = new GroupsB();
				
				groupB.setGroupId(rs.getString(1));
				groupB.setGdes(rs.getString(2));
				groupB.setGtype(rs.getString(3));
				groupB.setCreateBy(rs.getString(4));
				groupB.setCreatedDate(rs.getString(5));
				
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
	
	private String buildCondition(GroupsB groupB){
		String groupId = "";
		String gdes = "";
		String gtype = "";
		String createBy = "";
		String createdDate = "";
		
		String condition = "";
		//IP_GROUP, GDESC, GTYPE, CREATE_BY, CREATED_DATE
		groupId = (groupB.getGroupId() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" IP_GROUP LIKE '%"+ groupB.getGroupId() + "%' "  : "";
		condition+=groupId;
		gdes = (groupB.getGdes() != null) 		? (condition.contains("WHERE") ? " AND " : " WHERE ") + " GDESC LIKE '%" + groupB.getGdes() +"%' " : "";
		condition+=gdes;
		gtype = (groupB.getGtype() != null) 	? (condition.contains("WHERE") ? " AND " : " WHERE ") + " GTYPE LIKE '%" + groupB.getGtype() +"%' " : "";
		condition+=gtype;
		createBy = (groupB.getCreateBy() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " CREATE_BY = '" + groupB.getCreateBy() +"' " : "";
		condition+=createBy;
		createdDate = (groupB.getCreatedDate() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " CREATED_DATE = '" + groupB.getCreatedDate() +"' " : "";
		condition+=createdDate;
		condition = condition.isEmpty() ? null : condition;
		
		return condition;
	}
}
