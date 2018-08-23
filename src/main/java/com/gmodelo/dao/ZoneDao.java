package com.gmodelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.PositionZoneBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.ZoneB;
import com.gmodelo.beans.ZoneBean;
import com.gmodelo.beans.ZonePositionMaterialsB;
import com.gmodelo.beans.ZonePositionsB;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class ZoneDao {
	
	private Logger log = Logger.getLogger( ZoneDao.class.getName());
	
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
		
		final String INV_SP_ADD_POSITION_ZONE = "INV_SP_ADD_POSITION_ZONE ?, ?, ?, ?, ?, ?"; //The Store procedure to call
		
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
			if(positionZoneBean.getImwm() != null){
				cs.setString(6,positionZoneBean.getImwm());
			}else{
				cs.setNull(6, Types.INTEGER);
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

	public Response<Object> deleteZone(String arrayIdZones){
		
		Response<Object> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		
		final String INV_SP_DEL_ZONE = "INV_SP_DEL_ZONE ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[deleteZone] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_DEL_ZONE);
			
			if(arrayIdZones != null && !arrayIdZones.isEmpty()){
				cs.setString(1,arrayIdZones);
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			
			log.log(Level.WARNING,"[deleteZone] Executing query...");
			
			ResultSet rs = cs.executeQuery();
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[deleteZone] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			cs.close();	
			
			log.log(Level.WARNING,"[deleteZone] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[deleteZone] Some error occurred while was trying to execute the S.P.: "+INV_SP_DEL_ZONE, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[deleteZone] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}

	//vista se agrega LGOBE  y query se modifica para que siempre incluya AND de bukrs y werks y un OR de idZone y ZoneDesc
	public Response<List<ZoneBean>> getLgortByZone(ZoneBean zoneBean){
		
		Response<List<ZoneBean>> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<ZoneBean> listZone = new ArrayList<ZoneBean>();
		
		String INV_VW_ZONE_BY_LGORT = "SELECT [LGORT], [LGOBE], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] FROM [INV_CIC_DB].[dbo].[INV_VW_ZONE_BY_LGORT] "
				+ "WHERE BUKRS = '"+zoneBean.getBukrs()+"' AND WERKS = '"+zoneBean.getWerks()+"' ";
		
			String OR =	"AND ( ZONE_ID LIKE '%"+zoneBean.getIdZone()+"%' OR ZON_DESC LIKE '%"+zoneBean.getZoneDesc()+"%' )";
		 //query
		
		String condition = buildCondition(zoneBean);
		if(condition != null){
			INV_VW_ZONE_BY_LGORT += condition;
		}
		
		if(zoneBean.getIdZone() != null){
			INV_VW_ZONE_BY_LGORT += OR;
		}
		 
		INV_VW_ZONE_BY_LGORT += " GROUP BY [LGORT], [LGOBE], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] ";
		INV_VW_ZONE_BY_LGORT += "ORDER BY [ZONE_ID]";
		
		log.warning(INV_VW_ZONE_BY_LGORT);
		
		log.log(Level.WARNING,"[getZoneByLgortDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_ZONE_BY_LGORT);
			
			log.log(Level.WARNING,"[getZoneByLgortDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				
				zoneBean = new ZoneBean();
				
				zoneBean.setLgort(rs.getString(1));
				zoneBean.setLgobe(rs.getString(2));
				zoneBean.setIdZone(rs.getInt(3));
				zoneBean.setZoneDesc(rs.getString(4));
				zoneBean.setBukrs(rs.getString(5));
				zoneBean.setWerks(rs.getString(6));
				
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
			
			log.log(Level.WARNING,"[getZoneByLgortDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getZoneByLgortDao] Some error occurred while was trying to execute the query: "+INV_VW_ZONE_BY_LGORT, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getZoneByLgortDao] Some error occurred while was trying to close the connection.", e);
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
	
	public Response<List<ZoneBean>> validateZone(ZoneBean zoneBean){
		
		Response<List<ZoneBean>> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<ZoneBean> listZone = new ArrayList<ZoneBean>();
		
		String INV_VW_ZONE_BY_LGORT = "SELECT [LGORT], [LGOBE], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] FROM [INV_CIC_DB].[dbo].[INV_VW_ZONE_BY_LGORT] "
				+ "WHERE BUKRS = '"+zoneBean.getBukrs()+"' AND WERKS = '"+zoneBean.getWerks()+"' ";
		
			String OR =	"AND ( ZONE_ID LIKE '%"+zoneBean.getIdZone()+"%' AND ZON_DESC LIKE '%"+zoneBean.getZoneDesc()+"%' )";
		 //query
		
		String condition = buildCondition(zoneBean);
		if(condition != null){
			INV_VW_ZONE_BY_LGORT += condition;
		}
		
		if(zoneBean.getIdZone() != null){
			INV_VW_ZONE_BY_LGORT += OR;
		}
		 
		INV_VW_ZONE_BY_LGORT += " GROUP BY [LGORT], [LGOBE], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] ";
		INV_VW_ZONE_BY_LGORT += "ORDER BY [ZONE_ID]";
		
		log.warning(INV_VW_ZONE_BY_LGORT);
		
		log.log(Level.WARNING,"[validateZoneDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_ZONE_BY_LGORT);
			
			log.log(Level.WARNING,"[validateZoneDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				
				zoneBean = new ZoneBean();
				
				zoneBean.setLgort(rs.getString(1));
				zoneBean.setLgobe(rs.getString(2));
				zoneBean.setIdZone(rs.getInt(3));
				zoneBean.setZoneDesc(rs.getString(4));
				zoneBean.setBukrs(rs.getString(5));
				zoneBean.setWerks(rs.getString(6));
				
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
			
			log.log(Level.WARNING,"[validateZoneDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[validateZoneDao] Some error occurred while was trying to execute the query: "+INV_VW_ZONE_BY_LGORT, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[validateZoneDao] Some error occurred while was trying to close the connection.", e);
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
	
	private String buildCondition(ZoneBean zoneBean){
		String lgort = "";
		String lgobe = "";
		String condition = "";
	
		lgort = (zoneBean.getLgort() != null ? "AND " + zoneBean.getLgort()+"%'" :"");
		condition += lgort;
		lgobe = (zoneBean.getLgobe() != null ? "AND " +"ZON_DESC LIKE '%" + zoneBean.getLgobe()+"%'" : "");
		condition += lgobe;
		condition = condition.isEmpty() ? null : condition;
		
		return condition;
	}
	
	public Response<List<ZoneB>> getZones(ZoneB zoneBean, String searchFilter){
		
		Response<List<ZoneB>> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<ZoneB> listZone = new ArrayList<ZoneB>();
		
		String INV_VW_ZONES = "SELECT ZONE_ID,ZDESC,BUKRS,WERKS,LGORT FROM dbo.INV_VW_ZONES";
		if(searchFilter != null){
			INV_VW_ZONES += " WHERE ZONE_ID LIKE '%"+searchFilter+"%' OR ZDESC LIKE '%"+searchFilter+"%' OR BUKRS LIKE '%"+searchFilter+"%' OR WERKS LIKE '%"+searchFilter+ "%' OR LGORT LIKE '%"+ searchFilter+"%'";
		}else{
			String condition = buildConditionZones(zoneBean);
			if(condition != null){
				INV_VW_ZONES += condition;
			}
		}
		log.warning(INV_VW_ZONES);
		log.log(Level.WARNING,"[getZonesDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_ZONES);
			log.log(Level.WARNING,"[getZonesDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
					
				zoneBean = new ZoneB();
				
				zoneBean.setZoneId(rs.getString(1));
				zoneBean.setZdesc(rs.getString(2));
				zoneBean.setBukrs(rs.getString(3));
				zoneBean.setWerks(rs.getString(4));
				zoneBean.setLgort(rs.getString(5));
				zoneBean.setPositionsB(this.getPositionsZone(rs.getString(1)));
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
			
			log.log(Level.WARNING,"[getZonesDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getZonesDao] Some error occurred while was trying to execute the query: "+INV_VW_ZONES, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getZonesDao] Some error occurred while was trying to close the connection.", e);
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
	
	private List<ZonePositionsB> getPositionsZone(String zoneId){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<ZonePositionsB> listPositions = new ArrayList<ZonePositionsB>();
		
		String INV_VW_ZONE_WITH_POSITIONS = "SELECT POSITION_ID ,LGTYP ,LGPLA ,SECUENCY ,IMWM FROM dbo.INV_VW_ZONE_WITH_POSITIONS WHERE ZONE_ID = '" + zoneId + "'";
		
		log.warning(INV_VW_ZONE_WITH_POSITIONS);
		log.log(Level.WARNING,"[getZonesDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_ZONE_WITH_POSITIONS);
			log.log(Level.WARNING,"[getZonesDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
				
				ZonePositionsB position = new ZonePositionsB();
				
				position.setPositionId(rs.getString(1));
				position.setLgtyp(rs.getString(2));
				position.setLgpla(rs.getString(3));
				position.setSecuency(rs.getString(4));
				position.setImwm(rs.getString(5));
				position.setPositionMaterial(this.getPositionMaterials(zoneId, rs.getString(1)));
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
			
			log.log(Level.WARNING,"[getZonesDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getZonesDao] Some error occurred while was trying to execute the query: "+INV_VW_ZONE_WITH_POSITIONS, e);
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getZonesDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		return listPositions ;
	}
	
	private List<ZonePositionMaterialsB> getPositionMaterials(String zoneId, String position){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<ZonePositionMaterialsB> listMaterials = new ArrayList<ZonePositionMaterialsB>();
		
		String INV_VW_ZONE_POSITIONS_MATERIALS = "SELECT MATNR ,TYP_MAT ,DEN_TYP_MAT FROM dbo.INV_VW_ZONE_POSITIONS_MATERIALS WHERE ZONE_ID ='"+zoneId+"' AND POSITION_ID ='"+ position +"'";
		
		log.warning(INV_VW_ZONE_POSITIONS_MATERIALS);
		log.log(Level.WARNING,"[getZonesDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_ZONE_POSITIONS_MATERIALS);
			log.log(Level.WARNING,"[getZonesDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
				
				ZonePositionMaterialsB material = new ZonePositionMaterialsB();
				
				material.setMatnr(rs.getString(1));
				material.setTypMat(rs.getString(2));
				material.setDescTM(rs.getString(3));
				
				listMaterials.add(material);
				
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
			
			log.log(Level.WARNING,"[getZonesDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getZonesDao] Some error occurred while was trying to execute the query: "+INV_VW_ZONE_POSITIONS_MATERIALS, e);
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getZonesDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		return listMaterials ;
	}

	private String buildConditionZones(ZoneB zoneB){
		String condition ="";
		String zoneId ="";
		String zdesc ="";
		String bukrs ="";
		String werks ="";
		String lgort ="";
		
		zoneId = (zoneB.getZoneId() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "ZONE_ID LIKE '%" 	+ zoneB.getZoneId() + "%' " : "");
		condition+=zoneId;
		zdesc = (zoneB.getZdesc() 	!= null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "ZDESC LIKE '%" 	+ zoneB.getZdesc() + "%' ": "");
		condition+=zdesc;
		bukrs = (zoneB.getBukrs() 	!= null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "BUKRS LIKE '%" 	+ zoneB.getBukrs() + "%' ": "");
		condition+=bukrs;
		werks = (zoneB.getWerks() 	!= null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "WERKS LIKE '%"		+ zoneB.getWerks() + "%' ": "");
		condition+=werks;
		lgort = (zoneB.getLgort() 	!= null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "LGORT LIKE '%"		+ zoneB.getLgort() + "%' ": "");
		condition+=lgort;

		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

}
