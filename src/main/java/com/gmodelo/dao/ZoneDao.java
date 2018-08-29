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
import com.gmodelo.beans.MaterialToZoneBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.ZoneBean;
import com.gmodelo.beans.ZonePositionMaterialsBean;
import com.gmodelo.beans.ZonePositionsBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class ZoneDao {
	
	private Logger log = Logger.getLogger( ZoneDao.class.getName());
	
	public Response<ZoneBean> addZone(ZoneBean zoneBean, String createdBy){
		
		Response<ZoneBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		
		String zoneId = zoneBean.getZoneId() == null ? null : zoneBean.getZoneId().replaceFirst ("^0*", "");
		
		final String INV_SP_ADD_ZONE = "INV_SP_ADD_ZONE ?, ?, ?, ?, ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[addZone] Preparing sentence...");
		
		try {
			con.setAutoCommit(false);
			
			cs = con.prepareCall(INV_SP_ADD_ZONE);
			
			cs.setString(1,zoneBean.getZoneId());
			cs.setString(2,zoneBean.getZdesc());
			cs.setString(3,zoneBean.getBukrs());
			cs.setString(4,zoneBean.getWerks());
			cs.setString(5,zoneBean.getLgort());
			cs.setString(6, createdBy);
		
			cs.registerOutParameter(7, Types.INTEGER);
			
			log.log(Level.WARNING,"[addZone] Executing query...");
			
			cs.execute();
			
			zoneId = cs.getString(1);
			zoneBean.setZoneId(String.format("%08d",Integer.parseInt(zoneId))); //addZeros
			
			

			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,warning.getMessage());
				warning = warning.getNextWarning();
			}
			con.commit();
			//Free resources
			cs.close();	
			
			log.log(Level.WARNING,"[addZone] Sentence successfully executed.");
			
		} catch (SQLException e) {
			try {
				//deshace todos los cambios realizados en los datos
				log.log(Level.WARNING,"[addZone] Execute rollback");
				con.rollback();
			} catch (SQLException e1) {
				log.log(Level.SEVERE,"[addZone] Not rollback .", e);
			}
			
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
		res.setAbstractResult(abstractResult);
		return res ;
	}
	
	public Response<Object> addPositionZone(ZonePositionsBean zonePositionsBean){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		
		final String INV_SP_ADD_POSITION_ZONE = "INV_SP_ADD_POSITION_ZONE ?, ?, ?, ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[addPositionZone] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_ADD_POSITION_ZONE);
			
			cs.setString(1,zonePositionsBean.getZoneId());
			cs.setString(2,zonePositionsBean.getLgtyp());
			cs.setString(3,zonePositionsBean.getLgpla());
			cs.setString(4,zonePositionsBean.getSecuency());
			cs.setString(5,zonePositionsBean.getImwm());
			cs.registerOutParameter(6, Types.INTEGER);
			
			log.log(Level.WARNING,"[addPositionZone] Executing query...");
			
			cs.execute();
			abstractResult.setResultId(cs.getInt(6));
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[addPositionZone] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
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
		res.setAbstractResult(abstractResult);
		return res ;
	}

	public Response<Object> deleteZone(String arrayIdZones){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		
		final String INV_SP_DEL_ZONE = "INV_SP_DEL_ZONE ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[deleteZone] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_DEL_ZONE);
			
			cs.setString(1,arrayIdZones);
			
			log.log(Level.WARNING,"[deleteZone] Executing query...");
			
			cs.execute();
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[deleteZone] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
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
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<ZoneBean> listZone = new ArrayList<ZoneBean>();
		
		String INV_VW_ZONE_BY_LGORT = "SELECT [LGORT], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] FROM [INV_CIC_DB].[dbo].[INV_VW_ZONE_BY_LGORT] "
				+ "WHERE BUKRS = '"+zoneBean.getBukrs()+"' AND WERKS = '"+zoneBean.getWerks()+"' ";
		
			String OR =	"AND ( ZONE_ID LIKE '%"+zoneBean.getZoneId()+"%' OR ZON_DESC LIKE '%"+zoneBean.getZdesc()+"%' )";
		 //query
		
		String condition = buildCondition(zoneBean);
		if(condition != null){
			INV_VW_ZONE_BY_LGORT += condition;
		}
		
		if(zoneBean.getZoneId() != null){
			INV_VW_ZONE_BY_LGORT += OR;
		}
		 
		INV_VW_ZONE_BY_LGORT += " GROUP BY [LGORT], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] ";
		INV_VW_ZONE_BY_LGORT += "ORDER BY [ZONE_ID]";
		
		log.warning(INV_VW_ZONE_BY_LGORT);
		
		log.log(Level.WARNING,"[getZoneByLgortDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_ZONE_BY_LGORT);
			
			log.log(Level.WARNING,"[getZoneByLgortDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				
				zoneBean = new ZoneBean();
				
				zoneBean.setLgort(rs.getString("LGORT"));
				zoneBean.setZoneId(rs.getString("ZONE_ID"));
				zoneBean.setZdesc(rs.getString("ZON_DESC"));
				zoneBean.setBukrs(rs.getString("BUKRS"));
				zoneBean.setWerks(rs.getString("WERKS"));
				
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
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<ZoneBean> listZone = new ArrayList<ZoneBean>();
		
		String INV_VW_ZONE_BY_LGORT = "SELECT [LGORT], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] FROM [INV_CIC_DB].[dbo].[INV_VW_ZONE_BY_LGORT] "
				+ "WHERE BUKRS = '"+zoneBean.getBukrs()+"' AND WERKS = '"+zoneBean.getWerks()+"' ";
		
			String OR =	"AND ( ZONE_ID LIKE '%"+zoneBean.getZoneId()+"%' AND ZON_DESC LIKE '%"+zoneBean.getZdesc()+"%' )";
		 //query
		
		String condition = buildCondition(zoneBean);
		if(condition != null){
			INV_VW_ZONE_BY_LGORT += condition;
		}
		
		if(zoneBean.getZoneId() != null){
			INV_VW_ZONE_BY_LGORT += OR;
		}
		 
		INV_VW_ZONE_BY_LGORT += " GROUP BY [LGORT], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] ";
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
				zoneBean.setZoneId(rs.getString(2));
				zoneBean.setZdesc(rs.getString(3));
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
		String condition = "";
	
		lgort = (zoneBean.getLgort() != null ? "AND " + zoneBean.getLgort()+"%'" :"");
		condition += lgort;
		condition = condition.isEmpty() ? null : condition;
		
		return condition;
	}
	
	public Response<List<ZoneBean>> getZones(ZoneBean zoneBean, String searchFilter){
		
		Response<List<ZoneBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<ZoneBean> listZone = new ArrayList<ZoneBean>();
		
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
					
				zoneBean = new ZoneBean();
				
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
	
	private List<ZonePositionsBean> getPositionsZone(String zoneId){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<ZonePositionsBean> listPositions = new ArrayList<ZonePositionsBean>();
		
		String INV_VW_ZONE_WITH_POSITIONS = "SELECT PK_ASG_ID, LGTYP ,LGPLA ,SECUENCY ,IMWM FROM dbo.INV_VW_ZONE_WITH_POSITIONS WHERE ZONE_ID = ?";
		
		log.warning(INV_VW_ZONE_WITH_POSITIONS);
		log.log(Level.WARNING,"[getZonesDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_ZONE_WITH_POSITIONS);
			stm.setString(1, zoneId);
			log.log(Level.WARNING,"[getZonesDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
				
				ZonePositionsBean position = new ZonePositionsBean();
				position.setPkAsgId(rs.getString(1));
				position.setLgtyp(rs.getString(2));
				position.setLgpla(rs.getString(3));
				position.setSecuency(rs.getString(4));
				position.setImwm(rs.getString(5));
				position.setPositionMaterial(this.getPositionMaterials(rs.getString(1)));
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
	
	private List<ZonePositionMaterialsBean> getPositionMaterials(String pkAsgId){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		PreparedStatement stm = null;
		List<ZonePositionMaterialsBean> listMaterials = new ArrayList<ZonePositionMaterialsBean>();
		
		String INV_VW_ZONE_POSITIONS_MATERIALS = "SELECT PK_POS_MAT, MATNR ,TYP_MAT ,DEN_TYP_MAT FROM dbo.INV_VW_ZONE_POSITIONS_MATERIALS WHERE PK_ZONPOS_MAT = ?";
		
		log.warning(INV_VW_ZONE_POSITIONS_MATERIALS);
		log.log(Level.WARNING,"[getZonesDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_ZONE_POSITIONS_MATERIALS);
			stm.setString(1,pkAsgId );
			log.log(Level.WARNING,"[getZonesDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
				
				ZonePositionMaterialsBean material = new ZonePositionMaterialsBean();
				
				material.setPkPosMat(rs.getString(1));
				material.setMatnr(rs.getString(2));
				material.setTypMat(rs.getString(3));
				material.setDescTM(rs.getString(4));
				
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

	private String buildConditionZones(ZoneBean zoneB){
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

	public Response<Object> assignMaterialToZone(MaterialToZoneBean materialToZoneBean){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		String resultSP = null;
		
		final String INV_SP_ASSIGN_MATERIAL_TO_ZONE = "INV_SP_ASSIGN_MATERIAL_TO_ZONE ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[assignMaterialToZoneDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_ASSIGN_MATERIAL_TO_ZONE);
			
			if(materialToZoneBean.getZoneId() != null){
				cs.setString(1,materialToZoneBean.getZoneId());
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			if(materialToZoneBean.getPosition() != null){
				cs.setString(2,materialToZoneBean.getPosition());
			}else{
				cs.setNull(2, Types.INTEGER);
			}
			if(materialToZoneBean.getMatnr() != null){
				cs.setString(3,materialToZoneBean.getMatnr());
			}else{
				cs.setNull(3, Types.INTEGER);
			}
			
			log.log(Level.WARNING,"[assignMaterialToZoneDao] Executing query...");
			
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
				log.log(Level.WARNING,"[assignMaterialToZoneDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			cs.close();	
			
			log.log(Level.WARNING,"[assignMaterialToZoneDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[assignMaterialToZoneDao] Some error occurred while was trying to execute the S.P.: "+INV_SP_ASSIGN_MATERIAL_TO_ZONE, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[assignMaterialToZoneDao] Some error occurred while was trying to close the connection.", e);
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

	public Response<Object> unassignMaterialToZone(MaterialToZoneBean materialToZoneBean){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		String resultSP = null;
		
		final String INV_SP_DESASSIGN_MATERIAL_TO_ZONE = "INV_SP_DESASSIGN_MATERIAL_TO_ZONE ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[unassignMaterialToZoneDao] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_DESASSIGN_MATERIAL_TO_ZONE);
			
			if(materialToZoneBean.getZoneId() != null){
				cs.setString(1,materialToZoneBean.getZoneId());
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			if(materialToZoneBean.getPosition() != null){
				cs.setString(2,materialToZoneBean.getPosition());
			}else{
				cs.setNull(2, Types.INTEGER);
			}
			if(materialToZoneBean.getMatnr() != null){
				cs.setString(3,materialToZoneBean.getMatnr());
			}else{
				cs.setNull(3, Types.INTEGER);
			}
			
			log.log(Level.WARNING,"[unassignMaterialToZoneDao] Executing query...");
			
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
				log.log(Level.WARNING,"[unassignMaterialToZoneDao] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			cs.close();	
			
			log.log(Level.WARNING,"[unassignMaterialToZoneDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[unassignMaterialToZoneDao] Some error occurred while was trying to execute the S.P.: "+INV_SP_DESASSIGN_MATERIAL_TO_ZONE, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[unassignMaterialToZoneDao] Some error occurred while was trying to close the connection.", e);
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
