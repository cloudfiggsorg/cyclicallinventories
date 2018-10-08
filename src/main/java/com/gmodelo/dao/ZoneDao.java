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
		
		System.out.println("CALL...");
		
		Response<ZoneBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		int idPosition = 0;
		int zoneId = 0;
		try {
			zoneId = Integer.parseInt(zoneBean.getZoneId());
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}		

		final String INV_SP_ADD_ZONE = "INV_SP_ADD_ZONE ?, ?, ?, ?, ?, ?";
		final String INV_SP_DEL_ZONE_POSITION = "INV_SP_DEL_ZONE_POSITION ?";
		final String INV_SP_DESASSIGN_MATERIAL_TO_ZONE = "INV_SP_DESASSIGN_MATERIAL_TO_ZONE ?";
		final String INV_SP_ADD_POSITION_ZONE = "INV_SP_ADD_POSITION_ZONE ?, ?, ?, ?, ?, ?, ?";		
		final String INV_SP_ASSIGN_MATERIAL_TO_ZONE = "INV_SP_ASSIGN_MATERIAL_TO_ZONE ?, ?, ?";
		log.info("[addZone] Preparing sentence...");
		try {
			con.setAutoCommit(false);
			cs = con.prepareCall(INV_SP_ADD_ZONE);
			cs.setString(1,zoneBean.getZoneId());
			cs.setString(2,zoneBean.getZdesc());
			cs.setString(3,zoneBean.getBukrs());
			cs.setString(4,zoneBean.getWerks());
			cs.setString(5,zoneBean.getLgort());
			cs.setString(6, createdBy);
		
			cs.registerOutParameter(1, Types.INTEGER);
			log.info("[addZone] Executing query...");
			
			cs.execute();
						
			zoneBean.setZoneId(String.format("%08d", cs.getInt(1))); // addZeroscs
						
			//Eliminar posiciones						
			cs = null;
			cs = con.prepareCall(INV_SP_DEL_ZONE_POSITION);
			cs.setInt(1, zoneId);
			cs.execute();
			
			for (int i = 0; i < zoneBean.getPositions().size(); i++) {
				
				System.out.println("Here " + zoneBean.getPositions().size());
				
				zoneBean.getPositions().get(i).setZoneId(zoneBean.getZoneId());
				cs = null;
				cs = con.prepareCall(INV_SP_ADD_POSITION_ZONE);
				cs.setString(1,zoneBean.getZoneId());
				cs.setString(2,zoneBean.getPositions().get(i).getLgtyp());
				cs.setString(3,zoneBean.getPositions().get(i).getLgpla());
				cs.setString(4,zoneBean.getPositions().get(i).getSecuency());
				cs.setString(5,zoneBean.getPositions().get(i).getImwm());
				cs.setString(6,zoneBean.getPositions().get(i).getLgnum());
				cs.registerOutParameter(7, Types.INTEGER);
				log.info("[addPositionZone] Executing query...");
				cs.execute();
				idPosition = cs.getInt(7);
				zoneBean.getPositions().get(i).setPkAsgId(idPosition);
				
				//Eliminar materiales de posición													
				cs = null;
				cs = con.prepareCall(INV_SP_DESASSIGN_MATERIAL_TO_ZONE);
				cs.setInt(1, zoneBean.getPositions().get(i).getPkAsgId());
				cs.execute();
								
				for(int k = 0; k < zoneBean.getPositions().get(i).getMaterials().size(); k++){
					zoneBean.getPositions().get(i).getMaterials().get(k).setPosMat(idPosition);
					
					cs = null;
					cs = con.prepareCall(INV_SP_ASSIGN_MATERIAL_TO_ZONE);
					
					cs.setInt(1,zoneBean.getPositions().get(i).getMaterials().get(k).getPosMat());
					cs.setString(2,zoneBean.getPositions().get(i).getMaterials().get(k).getMatnr());
					cs.registerOutParameter(3, Types.INTEGER);
					
					log.info("[assignMaterialToZoneDao] Executing query...");
					cs.execute();
					zoneBean.getPositions().get(i).getMaterials().get(k).setPosMat(cs.getInt(3));
				}
				
			}
						
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			con.commit();
			//Free resources
			cs.close();	
			
			log.info("[addZone] Sentence successfully executed.");
	
		} catch (SQLException e) {
			try {
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
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(zoneBean);
		return res ;
	}
	
	public Response<Object> deleteZone(String arrayIdZones){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		
		final String INV_SP_DEL_ZONE = "INV_SP_DEL_ZONE ?"; //The Store procedure to call
		
		log.info("[deleteZone] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_DEL_ZONE);
			
			cs.setString(1,arrayIdZones);
			
			log.info("[deleteZone] Executing query...");
			
			cs.execute();
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[deleteZone] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.info("[deleteZone] Sentence successfully executed.");
			
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
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}

	//vista se agrega LGOBE  y query se modifica para que siempre incluya AND de bukrs y werks y un OR de idZone y ZoneDesc
	public Response<List<ZoneBean>> getLgortByZone(ZoneBean zoneBean, String searchFilter){
		
		Response<List<ZoneBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ZoneBean> listZone = new ArrayList<ZoneBean>();
		
		String INV_VW_ZONE_BY_LGORT = "SELECT [LGORT], [LGOBE], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] FROM [INV_CIC_DB].[dbo].[INV_VW_ZONE_BY_LGORT] ";
				
		if(searchFilter == null){

			String condition = buildCondition(zoneBean);
			if(condition != null){
				INV_VW_ZONE_BY_LGORT += condition;
			}
		}else{
			String  searchFilterNumber = "";
			try {
				int aux = Integer.parseInt(searchFilter); 
				searchFilterNumber  += aux;
			} catch (Exception e) {
				searchFilterNumber = searchFilter;
				log.info("Trying to convert String to Int");
			}
			INV_VW_ZONE_BY_LGORT += "WHERE  ZONE_ID LIKE '%"+searchFilterNumber+"%' OR ZON_DESC LIKE '%"+searchFilter+"%' ";
		}
		 
		INV_VW_ZONE_BY_LGORT += " GROUP BY [LGORT], [LGOBE], [ZONE_ID], [ZON_DESC], [BUKRS], [WERKS] ";
		INV_VW_ZONE_BY_LGORT += "ORDER BY [ZONE_ID]";
		
		log.info(INV_VW_ZONE_BY_LGORT);
		
		log.info("[getZoneByLgortDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_ZONE_BY_LGORT);
			
			log.info("[getZoneByLgortDao] Executing query...");
			
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()){
				
				zoneBean = new ZoneBean();
				
				zoneBean.setLgort(rs.getString("LGORT"));
				zoneBean.setgDesc(rs.getString("LGOBE"));
				zoneBean.setZoneId(String.format("%08d",Integer.parseInt(rs.getString("ZONE_ID"))));
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
			
			log.info("[getZoneByLgortDao] Sentence successfully executed.");
			
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
		Connection con = iConnectionManager.createConnection();
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
		
		log.info(INV_VW_ZONE_BY_LGORT);
		
		log.info("[validateZoneDao] Preparing sentence...");
		
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
			
			log.info("[validateZoneDao] Sentence successfully executed.");
			
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
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listZone);
		return res ;
	}
	
	private String buildCondition(ZoneBean zoneBean){
		String zoneId = "";
		String zdesc = "";
		String bukrs = "";
		String werks = "";
		String lgort = "";
		String condition = "WHERE ";
		
		bukrs = (zoneBean.getBukrs() != null ? " BUKRS LIKE '%"+zoneBean.getBukrs()+"%'" : "");
		condition += bukrs;
		werks = (zoneBean.getWerks() != null ? " AND WERKS LIKE '%"+zoneBean.getWerks()+"%'" : "");
		condition += werks;
		zoneId = (zoneBean.getZoneId() != null ? " AND ZONE_ID = "+Integer.parseInt(zoneBean.getZoneId()) : "");
		condition += zoneId;
		zdesc = (zoneBean.getZdesc() != null ? " AND ZON_DESC LIKE '%"+zoneBean.getZdesc()+"%'" : "");
		condition += zdesc;		
		lgort = (zoneBean.getLgort() != null ? "AND LGORT LIKE '%" + zoneBean.getLgort()+"%'" :"");
		condition += lgort;
		condition = condition.isEmpty() ? null : condition;
		
		return condition;
	}
	
	public Response<List<ZoneBean>> getZones(ZoneBean zoneBean, String searchFilter){
		Response<List<ZoneBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ZoneBean> listZone = new ArrayList<ZoneBean>();
		int aux;		
		String searchFilterNumber = "";
		try {
			aux = Integer.parseInt(searchFilter); 
			searchFilterNumber += aux;
		} catch (Exception e) {
			searchFilterNumber = searchFilter;
			log.info("Trying to convert String to Int");
		}
		String INV_VW_ZONES = "SELECT ZONE_ID, ZDESC, BUKRS, WERKS, LGORT, BDESC, WDESC, GDES FROM dbo.INV_VW_ZONES";
		if(searchFilter != null){
			
			INV_VW_ZONES += " WHERE ZONE_ID LIKE '%" + searchFilterNumber + "%' OR ZDESC LIKE '%"+searchFilter+"%' ";
		}else{
			
			String condition = buildConditionZones(zoneBean);
			if(condition != null){
				INV_VW_ZONES += condition;
			}
		}		
		
		log.info(INV_VW_ZONES);
		//INV_VW_ZONES += " GROUP BY ZONE_ID, ZDESC, BUKRS, WERKS, LGORT, BDESC, WDESC, GDES";
		log.info("[getZonesDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_ZONES);
			log.info("[getZonesDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
					
				zoneBean = new ZoneBean();
				
				zoneBean.setZoneId(String.format("%08d", rs.getInt("ZONE_ID")));
				zoneBean.setZdesc(rs.getString("ZDESC"));
				zoneBean.setBukrs(rs.getString("BUKRS"));
				zoneBean.setWerks(rs.getString("WERKS"));
				zoneBean.setLgort(rs.getString("LGORT"));
				zoneBean.setbDesc(rs.getString("BDESC"));
				zoneBean.setwDesc(rs.getString("WDESC"));
				zoneBean.setgDesc(rs.getString("GDES"));
				zoneBean.setPositions(this.getPositionsZone(rs.getString("ZONE_ID")));
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
			
			log.info("[getZonesDao] Sentence successfully executed.");
			
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
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listZone);
		return res ;
	}
	
	public Response<List<ZoneBean>> getZonesOnly(String searchFilter){
		
		Response<List<ZoneBean>> res = new Response<>();
		List<ZoneBean> listZone = new ArrayList<ZoneBean>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		int aux;		
		ZoneBean zoneBean = null;
		String searchFilterNumber = "";
		
		try {
			aux = Integer.parseInt(searchFilter); 
			searchFilterNumber += aux;
		} catch (Exception e) {
			searchFilterNumber = searchFilter;
			log.info("Trying to convert String to Int");
		}
		
		String INV_VW_ZONES = "SELECT ZONE_ID, ZDESC FROM dbo.INV_VW_ZONES";
		INV_VW_ZONES += " WHERE ZONE_ID LIKE '%" + searchFilterNumber + "%' OR ZDESC LIKE '%"+searchFilter+"%' ";
		INV_VW_ZONES += " GROUP BY ZONE_ID, ZDESC, BUKRS, WERKS, LGORT, BDESC, WDESC, GDES";
		log.info(INV_VW_ZONES);
		log.info("[getZonesOnlyDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_ZONES);
			log.info("[getZonesOnlyDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
					
				zoneBean = new ZoneBean();				
				zoneBean.setZoneId(String.format("%08d", rs.getInt("ZONE_ID")));
				zoneBean.setZdesc(rs.getString("ZDESC"));
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
			
			log.info("[getZonesOnlyDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getZonesOnlyDao] Some error occurred while was trying to execute the query: "+INV_VW_ZONES, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getZonesOnlyDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		
		res.setAbstractResult(abstractResult);
		res.setLsObject(listZone);
		return res ;
	}
	
	private List<ZonePositionsBean> getPositionsZone(String zoneId){
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ZonePositionsBean> listPositions = new ArrayList<ZonePositionsBean>();
		String INV_VW_ZONE_WITH_POSITIONS = "SELECT PK_ASG_ID, LGTYP ,LGPLA ,SECUENCY ,IMWM, ZPO_LGNUM, LTYPT FROM dbo.INV_VW_ZONE_WITH_POSITIONS WHERE ZONE_ID = ?";		
		INV_VW_ZONE_WITH_POSITIONS += " GROUP BY PK_ASG_ID, LGTYP ,LGPLA ,SECUENCY ,IMWM, ZPO_LGNUM, LTYPT";
		try {
			stm = con.prepareCall(INV_VW_ZONE_WITH_POSITIONS);
			stm.setString(1, zoneId);
			log.info(INV_VW_ZONE_WITH_POSITIONS);
			log.info("[getZonesDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
				ZonePositionsBean position = new ZonePositionsBean();
				position.setZoneId(zoneId);
				position.setPkAsgId(rs.getInt("PK_ASG_ID"));
				position.setLgtyp(rs.getString("LGTYP"));
				position.setLgpla(rs.getString("LGPLA"));
				position.setSecuency(rs.getString("SECUENCY"));
				position.setImwm(rs.getString("IMWM"));
				position.setLgtypDesc(rs.getString("LTYPT"));
				position.setLgnum(rs.getString("ZPO_LGNUM"));
				position.setMaterials(this.getPositionMaterials(rs.getString(1)));
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
			
			log.info("[getZonesDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getZonesDao] Some error occurred while was trying to execute the query: " + INV_VW_ZONE_WITH_POSITIONS, e);
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
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ZonePositionMaterialsBean> listMaterials = new ArrayList<ZonePositionMaterialsBean>();
		
		String INV_VW_ZONE_POSITIONS_MATERIALS = "SELECT PK_POS_MAT, MATNR ,MAKTX FROM dbo.INV_VW_ZONE_POSITIONS_MATERIALS WHERE POSITION_ID = ?";
		
		log.info(INV_VW_ZONE_POSITIONS_MATERIALS);
		log.info("[getZonesDao] Preparing sentence...");
		INV_VW_ZONE_POSITIONS_MATERIALS += " GROUP BY PK_POS_MAT, MATNR ,MAKTX";
		
		try {
			stm = con.prepareCall(INV_VW_ZONE_POSITIONS_MATERIALS);
			stm.setString(1,pkAsgId );
			log.info("[getZonesDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
				
				ZonePositionMaterialsBean material = new ZonePositionMaterialsBean();
				
				material.setPosMat(rs.getInt("PK_POS_MAT"));
				material.setMatnr(rs.getString("MATNR"));
				material.setDescM(rs.getString("MAKTX"));
				
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
			
			log.info("[getZonesDao] Sentence successfully executed.");
			
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
		
		zoneId = (zoneB.getZoneId() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "ZONE_ID LIKE '%" 	+ zoneB.getZoneId().replaceFirst("^0*", "") + "%' " : "");
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

	public Response<Object> unassignMaterialToZone(MaterialToZoneBean materialToZoneBean){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		String resultSP = null;
		
		final String INV_SP_DESASSIGN_MATERIAL_TO_ZONE = "INV_SP_DESASSIGN_MATERIAL_TO_ZONE ?, ?, ?"; //The Store procedure to call
		
		log.info("[unassignMaterialToZoneDao] Preparing sentence...");
		
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
			
			log.info("[unassignMaterialToZoneDao] Executing query...");
			
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
			
			log.info("[unassignMaterialToZoneDao] Sentence successfully executed.");
			
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
			}
		}
		abstractResult.setResultMsgAbs(resultSP);
		res.setAbstractResult(abstractResult);
		return res ;
	}

}
