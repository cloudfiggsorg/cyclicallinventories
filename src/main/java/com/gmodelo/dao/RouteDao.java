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

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.MaterialToRouteBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteBean;
import com.gmodelo.beans.RouteGroupB;
import com.gmodelo.beans.RoutePositionB;
import com.gmodelo.beans.RoutePositionBean;
import com.gmodelo.beans.UserB;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class RouteDao {

	private Logger log = Logger.getLogger( RouteDao.class.getName());
	
	public Response<Object> addRoute(RouteBean routeBean, String createdBy){
			
			Response<Object> res = new Response<>();
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			ConnectionManager iConnectionManager = new ConnectionManager();
			Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
			CallableStatement cs = null;
			String routeId = null;
			final String INV_SP_ADD_ROUTE = "INV_SP_ADD_ROUTE ?, ?, ?, ?, ?, ?"; //The Store procedure to call
			
			log.log(Level.WARNING,"[addRoute] Preparing sentence...");
			
			try {
				cs = con.prepareCall(INV_SP_ADD_ROUTE);
				
				cs.setString(1,routeBean.getRouteId());
				cs.setString(2,routeBean.getRdesc());
				cs.setString(3,routeBean.getBukrs());
				cs.setString(4,routeBean.getWerks());
				cs.setString(5, createdBy);
				cs.setString(6, routeBean.getType());
				cs.registerOutParameter(1, Types.VARCHAR);
				
				log.log(Level.WARNING,"[addRoute] Executing query...");
				
				cs.execute();
				
				routeId = cs.getString(1);
				routeBean.setRouteId(routeId);
				
				System.out.println(routeBean.toString());
				
				abstractResult.setResultMsgAbs(cs.getString(1));
				
				//Retrive the warnings if there're
				SQLWarning warning = cs.getWarnings();
				while (warning != null) {
					log.log(Level.WARNING,"[addRoute] "+warning.getMessage());
					warning = warning.getNextWarning();
				}
				
				//Free resources
				cs.close();	
				
				log.log(Level.WARNING,"[addRoute] Sentence successfully executed.");
				String idPosition="";
				//INSERTAR POSICIONES
				for(int i=0; i < routeBean.getPositions().size();i++){
					routeBean.getPositions().get(i).setRouteId(routeId);
					idPosition = addRoutePosition(routeBean.getPositions().get(i));
						
					routeBean.getPositions().get(i).setPositionId(idPosition);
					
					System.out.println("Position del bean: "+ routeBean.getPositions().get(i).toString());
				}
				
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[addRoute] Some error occurred while was trying to execute the S.P.: "+INV_SP_ADD_ROUTE, e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}finally {
				try {
					con.close();
				} catch (SQLException e) {
					log.log(Level.SEVERE,"[addRoute] Some error occurred while was trying to close the connection.", e);
					abstractResult.setResultId(ReturnValues.IEXCEPTION);
					abstractResult.setResultMsgAbs(e.getMessage());
					res.setAbstractResult(abstractResult);
					return res;
				}
			}
			res.setAbstractResult(abstractResult);
			return res ;
		}

	public String addRoutePosition(RoutePositionB routePositionBean) throws SQLException {
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		//EXEC INV_SP_ADD_ROUTE_POSITION @ROUTE_ID='000002',@LGTYP='001',@LGORT='0088', @SECUENCY='' ,@ZONE_ID='1'
		final String INV_SP_ADD_ROUTE_POSITION = "INV_SP_ADD_ROUTE_POSITION ?, ?, ?, ?, ?, ?"; //The Store procedure to call
		String idPosition = null;
		log.log(Level.WARNING,"[addRoutePosition] Preparing sentence...");
		
			cs = con.prepareCall(INV_SP_ADD_ROUTE_POSITION);
			
			cs.setString(1,routePositionBean.getRouteId());
			cs.setString(2,routePositionBean.getPositionId());
			cs.setString(3,routePositionBean.getLgort());
			cs.setString(4,routePositionBean.getLgtyp());
			cs.setString(5,routePositionBean.getZoneId());
			cs.setString(6,routePositionBean.getSecuency());
			
			cs.registerOutParameter(2, Types.VARCHAR);
			
			log.log(Level.WARNING,"[addRoutePosition] Executing query...");
			
			cs.execute();
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[addRoutePosition] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			idPosition = cs.getString(2); 
			//Free resources
			cs.close();	
			
			log.log(Level.WARNING,"[addRoutePosition] Sentence successfully executed.");
			return(idPosition);	
	}
	
	public Response<Object> assignMaterialToRoute(MaterialToRouteBean materialToRouteBean){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		
		final String INV_SP_ASSIGN_MATERIAL_TO_ROUTE = "INV_SP_ASSIGN_MATERIAL_TO_ROUTE ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[assignMaterialToRouteDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_ASSIGN_MATERIAL_TO_ROUTE);
			
			cs.setString(1,materialToRouteBean.getRouteId());
			cs.setString(2,materialToRouteBean.getPosition());
			cs.setString(3,materialToRouteBean.getMatnr());
			cs.registerOutParameter(1, Types.VARCHAR);
			
			log.log(Level.WARNING,"[assignMaterialToRouteDao] Executing query...");
			
			cs.execute();
			abstractResult.setResultMsgAbs(cs.getString(1));
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[assignMaterialToRouteDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.log(Level.WARNING,"[assignMaterialToRouteDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[assignMaterialToRouteDao] Some error occurred while was trying to execute the S.P.: "+INV_SP_ASSIGN_MATERIAL_TO_ROUTE, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[assignMaterialToRouteDao] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}

	public Response<Object> unassignMaterialToRoute(MaterialToRouteBean materialToRouteBean){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		
		final String INV_SP_DESASSIGN_MATERIAL_TO_ROUTE = "INV_SP_DESASSIGN_MATERIAL_TO_ROUTE ?, ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[unassignMaterialToRouteDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_DESASSIGN_MATERIAL_TO_ROUTE);
			
			cs.setString(1,materialToRouteBean.getRouteId());
			cs.setString(2,materialToRouteBean.getPosition());
			cs.setString(3,materialToRouteBean.getMatnr());
			cs.registerOutParameter(4, Types.INTEGER);
			
			log.log(Level.WARNING,"[unassignMaterialToRouteDao] Executing query...");
			
			cs.execute();
			abstractResult.setResultId(cs.getInt(4));
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[unassignMaterialToRouteDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.log(Level.WARNING,"[unassignMaterialToRouteDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[unassignMaterialToRouteDao] Some error occurred while was trying to execute the S.P.: "+INV_SP_DESASSIGN_MATERIAL_TO_ROUTE, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[unassignMaterialToRouteDao] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}
	
	public Response<Object> deleteRoute(String arrayIdRoutes){
			
			Response<Object> res = new Response<>();
			AbstractResultsBean abstractResult = new AbstractResultsBean();
			ConnectionManager iConnectionManager = new ConnectionManager();
			Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
			CallableStatement cs = null;
			
			final String INV_SP_DEL_ROUTES = "INV_SP_DEL_ROUTES ?, ?"; //The Store procedure to call
			
			log.log(Level.WARNING,"[deleteRouteDao] Preparing sentence...");
			
			try {
				cs = con.prepareCall(INV_SP_DEL_ROUTES);
				
				cs.setString(1,arrayIdRoutes);
				cs.registerOutParameter(2, Types.INTEGER);
				log.log(Level.WARNING,"[deleteRouteDao] Executing query...");
				
				cs.execute();
				
				abstractResult.setResultId(cs.getInt(2));
				
				//Retrive the warnings if there're
				SQLWarning warning = cs.getWarnings();
				while (warning != null) {
					log.log(Level.WARNING,"[deleteRouteDao] "+warning.getMessage());
					warning = warning.getNextWarning();
				}
				
				//Free resources
				cs.close();	
				
				log.log(Level.WARNING,"[deleteRouteDao] Sentence successfully executed.");
				
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[deleteRouteDao] Some error occurred while was trying to execute the S.P.: "+INV_SP_DEL_ROUTES, e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}finally {
				try {
					con.close();
				} catch (SQLException e) {
					log.log(Level.SEVERE,"[deleteRouteDao] Some error occurred while was trying to close the connection.", e);
					abstractResult.setResultId(ReturnValues.IEXCEPTION);
					abstractResult.setResultMsgAbs(e.getMessage());
					res.setAbstractResult(abstractResult);
					return res;
				}
			}
			res.setAbstractResult(abstractResult);
			return res ;
		}
	
	public Response<List<RouteBean>> getRoutesByUser(UserB userB){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		
		Response<List<RouteBean>> res = new Response<List<RouteBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<RouteBean> listRoutesBean = new ArrayList<RouteBean>(); 
		String INV_VW_ROUTES = null;
		
		INV_VW_ROUTES = "SELECT ROUTE_ID, BUKRS, WERKS, RDESC, RTYPE FROM dbo.INV_VW_ROUTES_USER WITH(NOLOCK) WHERE USER_ID = ?";
		
		log.warning(INV_VW_ROUTES);
		log.log(Level.WARNING,"[getRoutesDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_ROUTES);		
			
			log.log(Level.WARNING,"[getRoutesDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				RouteBean routeBean = new RouteBean();
				
				routeBean.setRouteId(rs.getString(1));
				routeBean.setBukrs(rs.getString(2));
				routeBean.setWerks(rs.getString(3));
				routeBean.setRdesc(rs.getString(4));
				routeBean.setType(rs.getString(5));
				routeBean.setPositions(this.getPositions(rs.getString(1)));
				routeBean.setGroups(this.getGroups(rs.getString(1)));
				
				listRoutesBean.add(routeBean);
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
			log.log(Level.WARNING,"[getRoutesDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getRoutesDao] Some error occurred while was trying to execute the query: "+INV_VW_ROUTES, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getRoutesDao] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		
		res.setAbstractResult(abstractResult);
		res.setLsObject(listRoutesBean);
		return res;
	}

	
	public Response<List<RouteBean>> getRoutes(RouteBean routeBean, String searchFilter){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		
		Response<List<RouteBean>> res = new Response<List<RouteBean>>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<RouteBean> listRoutesBean = new ArrayList<RouteBean>(); 
		String INV_VW_ROUTES = null;
		
		INV_VW_ROUTES = "SELECT ROUTE_ID, BUKRS, WERKS, RDESC, RTYPE FROM dbo.INV_VW_ROUTES WITH(NOLOCK) ";
		
		if(searchFilter != null){
			INV_VW_ROUTES += "WHERE ROUTE_ID LIKE '%"+searchFilter+"%' OR BUKRS LIKE '%"+searchFilter+"%' OR WERKS LIKE '%"+searchFilter+"%' OR RDESC LIKE '%"+ searchFilter +"%' OR RTYPE LIKE '%"+ searchFilter +"%' ";
		}else{
			String condition = buildCondition(routeBean);
			if(condition != null){
				INV_VW_ROUTES += condition;
			}
		}
		log.warning(INV_VW_ROUTES);
		log.log(Level.WARNING,"[getRoutesDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_ROUTES);		
			
			log.log(Level.WARNING,"[getRoutesDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				routeBean = new RouteBean();
				
				routeBean.setRouteId(rs.getString(1));
				routeBean.setBukrs(rs.getString(2));
				routeBean.setWerks(rs.getString(3));
				routeBean.setRdesc(rs.getString(4));
				routeBean.setType(rs.getString(5));
				routeBean.setPositions(this.getPositions(rs.getString(1)));
				routeBean.setGroups(this.getGroups(rs.getString(1)));
				
				listRoutesBean.add(routeBean);
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
			log.log(Level.WARNING,"[getRoutesDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getRoutesDao] Some error occurred while was trying to execute the query: "+INV_VW_ROUTES, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getRoutesDao] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		
		res.setAbstractResult(abstractResult);
		res.setLsObject(listRoutesBean);
		return res;
	}
	
	public List<RoutePositionB> getPositions(String idRoute){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		
		List<RoutePositionB> listPositions = new ArrayList<RoutePositionB>(); 
		 
		String INV_VW_ROUTES_WITH_POSITIONS = "SELECT POSITION_ID ,LGORT ,LGTYP ,ZONE_ID ,SECUENCY ,ZDESC, ROUTE_ID FROM dbo.INV_VW_ROUTES_WITH_POSITIONS WITH(NOLOCK) WHERE ROUTE_ID = ?";
		
		log.warning(INV_VW_ROUTES_WITH_POSITIONS);
		log.log(Level.WARNING,"[getRoutesDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_ROUTES_WITH_POSITIONS);		
			stm.setString(1, idRoute);
			log.log(Level.WARNING,"[getRoutesDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				RoutePositionB position = new RoutePositionB();
				position.setPositionId(rs.getString(1));
				position.setLgort(rs.getString(2));
				position.setLgtyp(rs.getString(3));
				position.setZoneId(rs.getString(4));
				position.setSecuency(rs.getString(5));
				position.setZdesc(rs.getString(6));
				position.setRouteId(rs.getString(7));
				listPositions.add(position);
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
			log.log(Level.WARNING,"[getRoutesDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getRoutesDao] Some error occurred while was trying to execute the query: "+INV_VW_ROUTES_WITH_POSITIONS, e);
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getRoutesDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		
		return listPositions;
	}

	public List<RouteGroupB> getGroups(String idRoute){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		
		List<RouteGroupB> listGroups = new ArrayList<RouteGroupB>(); 
		 
		String INV_VW_ROUTE_GROUPS = "SELECT PK_ASG_ID, GROUP_ID ,GDESC ,COUNT_NUM FROM dbo.INV_VW_ROUTE_GROUPS WITH(NOLOCK) WHERE ROUTE_ID = ? ";
		
		log.warning(INV_VW_ROUTE_GROUPS);
		log.log(Level.WARNING,"[getRoutesDao] Preparing sentence...");
		try {
			stm = con.prepareStatement(INV_VW_ROUTE_GROUPS);		
			stm.setString(1, idRoute);
			log.log(Level.WARNING,"[getRoutesDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				RouteGroupB group = new RouteGroupB();
				group.setPkRouteGroup(rs.getString(1));
				group.setGroupId(rs.getString(2));
				group.setGdesc(rs.getString(3));
				group.setCountNum(rs.getString(4));
				
				listGroups.add(group);
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
			log.log(Level.WARNING,"[getRoutesDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getRoutesDao] Some error occurred while was trying to execute the query: "+ INV_VW_ROUTE_GROUPS, e);
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getRoutesDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		
		return listGroups;
	}
	
	private String buildCondition(RouteBean routeB){
		String routeId="";
		String bukrs="";
		String werks="";
		String rdesc="";
//		String status="";
		String type = "";
		String condition = "";
		
		routeId = (routeB.getRouteId() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" ROUTE_ID = '"+ routeB.getRouteId() + "' "  : "";
		condition+=routeId;
		bukrs = (routeB.getBukrs() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" BUKRS = '"+ routeB.getBukrs() + "' "  : "";
		condition+=bukrs;
		werks = (routeB.getWerks() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" WERKS = '"+ routeB.getWerks() + "' "  : "";
		condition+=werks;
		rdesc = (routeB.getRdesc() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" RDESC = '"+ routeB.getRdesc() + "' "  : "";
		condition+=rdesc;
		type = (routeB.getType() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " TYPE = '" + routeB.getType() +"' " : "";
		condition+=type;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

	public static void main(String[] args) {
		System.out.println("Hello, World!");
		//@ROUTE_ID='000001',@LGTYP='001',@LGORT='0088', @SECUENCY='' ,@ZONE_ID='1'
		RoutePositionB p = new RoutePositionB("001","0088","1","1");
		RoutePositionB p1 = new RoutePositionB("002","0088","1","1");
		List<RoutePositionB> positions = new ArrayList<RoutePositionB>();
		positions.add(p);
		positions.add(p1);
		//EXEC INV_SP_ADD_ROUTE @DESC = 'RUTA 14.1', @BUKRS = '0001', @WERKS ='0001',  @CREATED_BY ='0001', @TYPE='1'
		RouteBean r = new RouteBean();
		r.setBukrs("0001");
		r.setRdesc("RUTA 16.1");
		r.setWerks("0001");
		r.setType("1");
		r.setPositions(positions);
		RouteDao dao = new RouteDao();
		dao.addRoute(r, "001");
		
	}
	
}
