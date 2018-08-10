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
import com.gmodelo.beans.PositionZoneBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.ZoneBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class ZoneDao {
	
	private Logger log = Logger.getLogger( ZoneDao.class.getName());
	
public Response<List<ZoneBean>> getZoneByLgort(ZoneBean zoneBean){
		
		Response<List<ZoneBean>> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<ZoneBean> listZone = new ArrayList<ZoneBean>();
		
		String INV_VW_ZONE_BY_LGORT = "SELECT [LGORT], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] FROM [INV_CIC_DB].[dbo].[INV_VW_ZONE_BY_LGORT] "; //query
		
		String condition = buildCondition(zoneBean);
		if(condition != null){
			INV_VW_ZONE_BY_LGORT += condition;
			log.warning(INV_VW_ZONE_BY_LGORT);
		}
		log.log(Level.WARNING,"[getZoneByLgort] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_ZONE_BY_LGORT);
			
			log.log(Level.WARNING,"[getZoneByLgort] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				
				zoneBean = new ZoneBean();
				
				zoneBean.setLgort(rs.getString(1));
				zoneBean.setIdZone(rs.getInt(2));
				zoneBean.setZoneDesc(rs.getString(3));
				zoneBean.setBukrs(rs.getString(4));
				zoneBean.setWerks(rs.getString(5));
				
				listZone.add(zoneBean);
				
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
			
			log.log(Level.WARNING,"[getZoneByLgort] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getZoneByLgort] Some error occurred while was trying to execute the query: "+INV_VW_ZONE_BY_LGORT, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getZoneByLgort] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listZone);
		return res ;
	}

	public Response<Object> addZone(ZoneBean zoneBean, String createdBy){
		
		Response<Object> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		String resultSP = null;
		
		final String INV_SP_ADD_ZONE = "INV_SP_ADD_ZONE ?, ?, ?, ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[addZone] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_ADD_ZONE);
			
			if(zoneBean.getIdZone() != null){
				cs.setInt(1,zoneBean.getIdZone());
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			if(zoneBean.getZoneDesc() != null){
				cs.setString(2,zoneBean.getZoneDesc());
			}else{
				cs.setNull(2, Types.INTEGER);
			}
			if(zoneBean.getBukrs() != null){
				cs.setString(3,zoneBean.getBukrs());
			}else{
				cs.setNull(3, Types.INTEGER);
			}
			if(zoneBean.getWerks() != null){
				cs.setString(4,zoneBean.getWerks());
			}else{
				cs.setNull(4, Types.INTEGER);
			}
			if(zoneBean.getLgort() != null){
				cs.setString(5,zoneBean.getLgort());
			}else{
				cs.setNull(5, Types.INTEGER);
			}
			
			cs.setString(6, createdBy);
			
			
			
			log.log(Level.WARNING,"[addZone] Executing query...");
			
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
				log.log(Level.WARNING,warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			cs.close();	
			
			log.log(Level.WARNING,"[addZone] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[addZone] Some error occurred while was trying to execute the S.P.: INV_SP_ADD_ZONE ?, ?, ?, ?, ?, ?", e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[addZone] Some error occurred while was trying to close the connection.", e);
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
	
	public Response<Object> addPositionZone(PositionZoneBean positionZoneBean){
		
		Response<Object> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		String resultSP = null;
		
		final String INV_SP_ADD_POSITION_ZONE = "INV_SP_ADD_POSITION_ZONE ?, ?, ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[addPositionZone] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_ADD_POSITION_ZONE);
			
			if(positionZoneBean.getZoneId() != null){
				cs.setInt(1,positionZoneBean.getZoneId());
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			if(positionZoneBean.getPositionId() != null){
				cs.setString(2,positionZoneBean.getPositionId());
			}else{
				cs.setNull(2, Types.INTEGER);
			}
			if(positionZoneBean.getLgtyp() != null){
				cs.setString(3,positionZoneBean.getLgtyp());
			}else{
				cs.setNull(3, Types.INTEGER);
			}
			if(positionZoneBean.getLgpla() != null){
				cs.setString(4,positionZoneBean.getLgpla());
			}else{
				cs.setNull(4, Types.INTEGER);
			}
			if(positionZoneBean.getSecuency() != null){
				cs.setString(5,positionZoneBean.getSecuency());
			}else{
				cs.setNull(5, Types.INTEGER);
			}
			
			log.log(Level.WARNING,"[addPositionZone] Executing query...");
			
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
				log.log(Level.WARNING,"[addPositionZone] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			cs.close();	
			
			log.log(Level.WARNING,"[addPositionZone] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[addPositionZone] Some error occurred while was trying to execute the S.P.: INV_SP_ADD_POSITION_ZONE ?, ?, ?, ?, ?", e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[addPositionZone] Some error occurred while was trying to close the connection.", e);
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

	private String buildCondition(ZoneBean zoneBean){
			
			String lgort = null;
			String zoneId = null;
			String zoneDesc = null;
			String bukrs = null;
			String werks = null;
			Boolean clause = false;
			String condition = null;
			
			if(zoneBean.getLgort() != null){
				lgort = "LGORT = '" + zoneBean.getLgort()+"'";
				clause = true;
			}
			if(zoneBean.getIdZone() != null){
				zoneId = "ZONE_ID = '" + zoneBean.getIdZone()+"'";
				clause = true;
			}
			if(zoneBean.getZoneDesc() != null){
				zoneDesc = "ZON_DESC = '" + zoneBean.getZoneDesc()+"'";
				clause = true;
			}
			if(zoneBean.getBukrs() != null){
				bukrs = "BUKRS = '" + zoneBean.getBukrs()+"'";
				clause = true;
			}
			if(zoneBean.getWerks() != null){
				werks = "WERKS = '" + zoneBean.getWerks()+"'";
				clause = true;
			}
			
			if(clause){
				condition = "WHERE ";
				if(lgort != null){
					condition += lgort;
					if(zoneId != null){
						condition += " AND "+ zoneId;
						if(zoneDesc != null){
							condition += " AND "+ zoneDesc;
							if(bukrs != null){
								condition += " AND "+ bukrs;
								if(werks != null){
									condition += " AND "+ werks;
								}
							}
						}
					}
				} else if(zoneId != null){
							condition += zoneId;
							if(zoneDesc != null){
								condition += " AND "+ zoneDesc;
								if(bukrs != null){
									condition += " AND "+ bukrs;
									if(werks != null){
										condition += " AND "+ werks;
									}
								}
							}
				}else if(zoneDesc != null){
							condition += zoneDesc;
							if(bukrs != null){
								condition += " AND "+ bukrs;
								if(werks != null){
									condition += " AND "+ werks;
								}
							}
					}else if(bukrs != null){
								condition += bukrs;
								if(werks != null){
									condition += " AND "+ werks;
								}
						}else if(werks != null){
									condition += werks;
							}
			}
			
			return condition;
		}

}
