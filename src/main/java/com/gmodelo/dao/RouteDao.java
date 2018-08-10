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
import com.gmodelo.beans.MaterialToRouteBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteB;
import com.gmodelo.beans.RouteBean;
import com.gmodelo.beans.RoutePositionBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class RouteDao {

	private Logger log = Logger.getLogger( RouteDao.class.getName());
	
	public Response<Object> addRoute(RouteBean routeBean, String createdBy){
			
			Response<Object> res = new Response<>();
			AbstractResults abstractResult = new AbstractResults();
			ConnectionManager iConnectionManager = new ConnectionManager();
			Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
			CallableStatement cs = null;
			String resultSP = null;
			
			final String INV_SP_ADD_ROUTE = "INV_SP_ADD_ROUTE ?, ?, ?, ?, ?"; //The Store procedure to call
			
			log.log(Level.WARNING,"[addRoute] Preparing sentence...");
			
			try {
				cs = con.prepareCall(INV_SP_ADD_ROUTE);
				
				if(routeBean.getRouteId() != null){
					cs.setString(1,routeBean.getRouteId());
				}else{
					cs.setNull(1, Types.INTEGER);
				}
				if(routeBean.getRouteDesc() != null){
					cs.setString(2,routeBean.getRouteDesc());
				}else{
					cs.setNull(2, Types.INTEGER);
				}
				if(routeBean.getBukrs() != null){
					cs.setString(3,routeBean.getBukrs());
				}else{
					cs.setNull(3, Types.INTEGER);
				}
				if(routeBean.getWerks() != null){
					cs.setString(4,routeBean.getWerks());
				}else{
					cs.setNull(4, Types.INTEGER);
				}
				
				cs.setString(5, createdBy);
				
				log.log(Level.WARNING,"[addRoute] Executing query...");
				
				ResultSet rs = cs.executeQuery();
				
				while (rs.next()){
					
					resultSP = rs.getString(1);
					
				}
				
				if(resultSP != null){
					try {
						
						Integer.parseInt(resultSP);
						
					} catch (NumberFormatException e) {
						
						abstractResult.setResultId(ReturnValues.IEXCEPTION);
						abstractResult.setResultMsgAbs(resultSP);
						
						res.setAbstractResult(abstractResult);
						return res;
					}
				}
				//Retrive the warnings if there're
				SQLWarning warning = cs.getWarnings();
				while (warning != null) {
					log.log(Level.WARNING,"[addRoute] "+warning.getMessage());
					warning = warning.getNextWarning();
				}
				
				//Free resources
				rs.close();
				cs.close();	
				
				log.log(Level.WARNING,"[addRoute] Sentence successfully executed.");
				
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
			abstractResult.setResultMsgAbs(resultSP);
			res.setAbstractResult(abstractResult);
			return res ;
		}

	public Response<Object> addRoutePosition(RoutePositionBean routePositionBean){
		
		Response<Object> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		String resultSP = null;
		
		final String INV_SP_ADD_ROUTE_POSITION = "INV_SP_ADD_ROUTE_POSITION ?, ?, ?, ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[addRoutePosition] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_ADD_ROUTE_POSITION);
			
			if(routePositionBean.getRouteId() != null){
				cs.setString(1,routePositionBean.getRouteId());
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			if(routePositionBean.getPositionRouteId() != null){
				cs.setString(2,routePositionBean.getPositionRouteId());
			}else{
				cs.setNull(2, Types.INTEGER);
			}
			if(routePositionBean.getLgort() != null){
				cs.setString(3,routePositionBean.getLgort());
			}else{
				cs.setNull(3, Types.INTEGER);
			}
			if(routePositionBean.getLgtyp() != null){
				cs.setString(4,routePositionBean.getLgtyp());
			}else{
				cs.setNull(4, Types.INTEGER);
			}
			if(routePositionBean.getZoneId() != null){
				cs.setInt(5,routePositionBean.getZoneId());
			}else{
				cs.setNull(5, Types.INTEGER);
			}
			if(routePositionBean.getSecuency() != null){
				cs.setString(6,routePositionBean.getSecuency());
			}else{
				cs.setNull(6, Types.INTEGER);
			}
			
			log.log(Level.WARNING,"[addRoutePosition] Executing query...");
			
			ResultSet rs = cs.executeQuery();
			
			while (rs.next()){
				
				resultSP = rs.getString(1);
				
			}
			
			if(resultSP != null){
				try {
					
					Integer.parseInt(resultSP);
					
				} catch (NumberFormatException e) {
					
					abstractResult.setResultId(ReturnValues.IEXCEPTION);
					abstractResult.setResultMsgAbs(resultSP);
					
					res.setAbstractResult(abstractResult);
					return res;
				}
			}
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[addRoutePosition] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			cs.close();	
			
			log.log(Level.WARNING,"[addRoutePosition] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[addRoutePosition] Some error occurred while was trying to execute the S.P.: "+INV_SP_ADD_ROUTE_POSITION, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[addRoutePosition] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		abstractResult.setResultMsgAbs(resultSP);
		res.setAbstractResult(abstractResult);
		return res ;
	}
	
	public Response<Object> assignMaterialToRoute(MaterialToRouteBean materialToRouteBean){
		
		Response<Object> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		String resultSP = null;
		
		final String INV_SP_ASSIGN_MATERIAL_TO_ROUTE = "INV_SP_ASSIGN_MATERIAL_TO_ROUTE ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[assignMaterialToRouteDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_ASSIGN_MATERIAL_TO_ROUTE);
			
			if(materialToRouteBean.getRouteId() != null){
				cs.setString(1,materialToRouteBean.getRouteId());
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			if(materialToRouteBean.getPosition() != null){
				cs.setString(2,materialToRouteBean.getPosition());
			}else{
				cs.setNull(2, Types.INTEGER);
			}
			if(materialToRouteBean.getMatnr() != null){
				cs.setString(3,materialToRouteBean.getMatnr());
			}else{
				cs.setNull(3, Types.INTEGER);
			}
			
			log.log(Level.WARNING,"[assignMaterialToRouteDao] Executing query...");
			
			ResultSet rs = cs.executeQuery();
			
			while (rs.next()){
				
				resultSP = rs.getString(1);
				
			}
			
			if(resultSP != null){
				try {
					
					Integer.parseInt(resultSP);
					
				} catch (NumberFormatException e) {
					
					abstractResult.setResultId(ReturnValues.IEXCEPTION);
					abstractResult.setResultMsgAbs(resultSP);
					
					res.setAbstractResult(abstractResult);
					return res;
				}
			}
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[assignMaterialToRouteDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
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
		abstractResult.setResultMsgAbs(resultSP);
		res.setAbstractResult(abstractResult);
		return res ;
	}

	public Response<Object> unassignMaterialToRoute(MaterialToRouteBean materialToRouteBean){
		
		Response<Object> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		String resultSP = null;
		
		final String INV_SP_DESASSIGN_MATERIAL_TO_ROUTE = "INV_SP_DESASSIGN_MATERIAL_TO_ROUTE ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[unassignMaterialToRouteDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_DESASSIGN_MATERIAL_TO_ROUTE);
			
			if(materialToRouteBean.getRouteId() != null){
				cs.setString(1,materialToRouteBean.getRouteId());
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			if(materialToRouteBean.getPosition() != null){
				cs.setString(2,materialToRouteBean.getPosition());
			}else{
				cs.setNull(2, Types.INTEGER);
			}
			if(materialToRouteBean.getMatnr() != null){
				cs.setString(3,materialToRouteBean.getMatnr());
			}else{
				cs.setNull(3, Types.INTEGER);
			}
			
			log.log(Level.WARNING,"[unassignMaterialToRouteDao] Executing query...");
			
			ResultSet rs = cs.executeQuery();
			
			while (rs.next()){
				
				resultSP = rs.getString(1);
				
			}
			
			if(resultSP != null){
				try {
					
					Integer.parseInt(resultSP);
					
				} catch (NumberFormatException e) {
					
					abstractResult.setResultId(ReturnValues.IEXCEPTION);
					abstractResult.setResultMsgAbs(resultSP);
					
					res.setAbstractResult(abstractResult);
					return res;
				}
			}
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[unassignMaterialToRouteDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
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
		abstractResult.setResultMsgAbs(resultSP);
		res.setAbstractResult(abstractResult);
		return res ;
	}
	
	public Response<Object> deleteRoute(String arrayIdRoutes){
			
			Response<Object> res = new Response<>();
			AbstractResults abstractResult = new AbstractResults();
			ConnectionManager iConnectionManager = new ConnectionManager();
			Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
			CallableStatement cs = null;
			
			final String INV_SP_DEL_ROUTES = "INV_SP_DEL_ROUTES ?"; //The Store procedure to call
			
			log.log(Level.WARNING,"[deleteRouteDao] Preparing sentence...");
			
			try {
				cs = con.prepareCall(INV_SP_DEL_ROUTES);
				
				if(arrayIdRoutes != null && !arrayIdRoutes.isEmpty()){
					cs.setString(1,arrayIdRoutes);
				}else{
					cs.setNull(1, Types.INTEGER);
				}
				
				log.log(Level.WARNING,"[deleteRouteDao] Executing query...");
				
				ResultSet rs = cs.executeQuery();
				
				//Retrive the warnings if there're
				SQLWarning warning = cs.getWarnings();
				while (warning != null) {
					log.log(Level.WARNING,"[deleteRouteDao] "+warning.getMessage());
					warning = warning.getNextWarning();
				}
				
				//Free resources
				rs.close();
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
	
	public Response<List<RouteB>> getRoutes(RouteB routeBean){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		
		Response<List<RouteB>> res = new Response<List<RouteB>>();
		AbstractResults abstractResult = new AbstractResults();
		List<RouteB> listRoutesBean = new ArrayList<RouteB>(); 
		 
		String INV_VW_ROUTES_WITH_POSITIONS = "SELECT ROUTE_ID, BUKRS, WERKS,  RDESC, STATUS,MODIFIED_BY, MODIFIED_DATE, CREATED_BY, CREATED_DATE, POSITION_ID, LGORT, LGTYP, ZONE_ID, SECUENCY  FROM [INV_CIC_DB].[dbo].[INV_VW_ROUTES_WITH_POSITIONS] WITH(NOLOCK) ";
		
		String condition = buildCondition(routeBean);
		if(condition != null){
			INV_VW_ROUTES_WITH_POSITIONS += condition;
			log.warning(INV_VW_ROUTES_WITH_POSITIONS);
		}
		log.log(Level.WARNING,"[getRoutesDao] Preparing sentence...");
		try {
			
			stm = con.prepareStatement(INV_VW_ROUTES_WITH_POSITIONS);		
			
			log.log(Level.WARNING,"[getRoutesDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				routeBean = new RouteB();
				
				routeBean.setRouteId(rs.getString(1));
				routeBean.setBukrs(rs.getString(2));
				routeBean.setWerks(rs.getString(3));
				routeBean.setRdesc(rs.getString(4));
				routeBean.setStatus(rs.getString(5));
				routeBean.setModifiedBy(rs.getString(6));
				routeBean.setModifiedDate(rs.getString(7));
				routeBean.setCreatedBy(rs.getString(8));
				routeBean.setCreatedDate(rs.getString(9));
				routeBean.setPositionId(rs.getString(10));
				routeBean.setLgort(rs.getString(11));
				routeBean.setLgtyp(rs.getString(12));
				routeBean.setZoneId(rs.getString(13));
				routeBean.setSecuency(rs.getString(13));
				
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
			log.log(Level.SEVERE,"[getRoutesDao] Some error occurred while was trying to execute the query: "+INV_VW_ROUTES_WITH_POSITIONS, e);
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
	
	private String buildCondition(RouteB routeB){

		String routeId="";
		String bukrs="";
		String werks="";
		String rdesc="";
		String status="";
		String modifiedBy="";
		String modifiedDate="";
		String createdBy="";
		String createdDate="";
		String positionId="";
		String lgort="";
		String lgtyp="";
		String zoneId="";
		String secuency="";

		String condition = null;
		
		routeId = (routeB.getRouteId() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" ROUTE_ID = '"+ routeB.getRouteId() + "' "  : "";
		condition+=routeId;
		bukrs = (routeB.getBukrs() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" BUKRS = '"+ routeB.getBukrs() + "' "  : "";
		condition+=bukrs;
		werks = (routeB.getWerks() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" WERKS = '"+ routeB.getWerks() + "' "  : "";
		condition+=werks;
		rdesc = (routeB.getRdesc() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" RDESC = '"+ routeB.getRdesc() + "' "  : "";
		condition+=rdesc;
		status = (routeB.getStatus() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" STATUS = '"+ routeB.getStatus() + "' "  : "";
		condition+=status;
		modifiedBy = (routeB.getModifiedBy() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" MODIFIED_BY = '"+ routeB.getModifiedBy() + "' "  : "";
		condition+=modifiedBy;
		modifiedDate = (routeB.getModifiedDate() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" MODIFIED_DATE = '"+ routeB.getModifiedDate() + "' "  : "";
		condition+=modifiedDate;
		createdBy = (routeB.getCreatedBy() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" CREATED_BY = '"+ routeB.getCreatedBy() + "' "  : "";
		condition+=createdBy;
		createdDate = (routeB.getCreatedDate() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" CREATED_DATE = '"+ routeB.getCreatedDate() + "' "  : "";
		condition+=createdDate;
		positionId = (routeB.getPositionId() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ")+" POSITION_ID = '"+ routeB.getPositionId() + "' "  : "";
		condition+=positionId;		
		lgort = (routeB.getLgort() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGORT = '" + routeB.getLgort() +"' " : "";
		condition+=lgort;
		lgtyp = (routeB.getLgtyp() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " LGTYP = '" + routeB.getLgtyp() +"' " : "";
		condition+=lgtyp;
		zoneId = (routeB.getZoneId() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " ZONE_ID = '" + routeB.getZoneId() +"' " : "";
		condition+=zoneId;
		secuency = (routeB.getSecuency() != null) ? (condition.contains("WHERE") ? " AND " : " WHERE ") + " SECUENCY = '" + routeB.getSecuency() +"' " : "";
		condition+=secuency;
		
		return condition;
	}

}