package com.gmodelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.MaterialToRouteBean;
import com.gmodelo.beans.Response;
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
				log.log(Level.SEVERE,"[addRoute] Some error occurred while was trying to execute the S.P.: INV_SP_ADD_ROUTE ?, ?, ?, ?, ?", e);
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
			log.log(Level.SEVERE,"[addRoutePosition] Some error occurred while was trying to execute the S.P.: INV_SP_ADD_ROUTE_POSITION ?, ?, ?, ?, ?, ?", e);
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
	
public Response<Object> materialToRoute(MaterialToRouteBean materialToRouteBean){
		
		Response<Object> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		String resultSP = null;
		
		final String INV_SP_ASSIGN_MATERIAL_TO_ROUTE = "INV_SP_ASSIGN_MATERIAL_TO_ROUTE ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[materialToRoute] Preparing sentence...");
		
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
			
			log.log(Level.WARNING,"[materialToRoute] Executing query...");
			
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
				log.log(Level.WARNING,"[materialToRoute] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			cs.close();	
			
			log.log(Level.WARNING,"[materialToRoute] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[materialToRoute] Some error occurred while was trying to execute the S.P.: INV_SP_ASSIGN_MATERIAL_TO_ROUTE ?, ?, ?", e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[materialToRoute] Some error occurred while was trying to close the connection.", e);
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
}
