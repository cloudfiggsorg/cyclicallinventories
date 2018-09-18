package com.gmodelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class DocInvDao {
	
	private Logger log = Logger.getLogger( DocInvDao.class.getName());
	/*
	public Response<DocInvBean> addDocInv(DocInvBean docInvBean, String createdBy){
		Response<DocInvBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		int idPosition = 0;
		int zoneId = 0;
				
		final String INV_SP_ADD_DOC_INVENTOY_HEADER = "INV_SP_ADD_DOC_INVENTOY_HEADER ?, ?, ?, ?, ?, ?,?";
		final String INV_SP_ADD_DOC_INVENTORY_POSITIONS = "INV_SP_ADD_DOC_INVENTORY_POSITIONS ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";		
		log.info("[addDocInv] Preparing sentence...");
		try {
			con.setAutoCommit(false);
			cs = con.prepareCall(INV_SP_ADD_DOC_INVENTOY_HEADER);
			/*@ROUTE_ID varchar(40),
    @BUKRS varchar(4),
	@WERKS VARCHAR(15),
    @TYPE varchar(15),
    @CREATED_BY varchar(20),
    @JUSTIFICATION varchar(40),
	@DOC_INV_ID INT = NULL OUTPUT*/
	/*
			cs.setString(1,docInvBean.getRoute().getRouteId());
			cs.setString(2,docInvBean.getBukrs());
			cs.setString(3,docInvBean.getWerks());
			cs.setString(4,docInvBean.getType());
			cs.setString(5,createdBy);
			cs.setString(6, createdBy);
			cs.setString(7, docInvBean.getgDesc());
		
			cs.registerOutParameter(1, Types.INTEGER);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.registerOutParameter(8, Types.VARCHAR);
			cs.registerOutParameter(9, Types.VARCHAR);
			log.info("[addZone] Executing query...");
			
			cs.execute();
						
			docInvBean.setZoneId(cs.getString(1));
			docInvBean.setbDesc(cs.getString(7));
			docInvBean.setwDesc(cs.getString(8));
			docInvBean.setgDesc(cs.getString(9));
			
			//Eliminar posiciones
			String ids = "";
			for (int i = 0; i < docInvBean.getPositions().size(); i++) {
				if(docInvBean.getPositions().get(i).getPkAsgId() > 0){
					ids += docInvBean.getPositions().get(i).getPkAsgId() + ",";
				}				
			}
															
			cs = null;
			cs = con.prepareCall(INV_SP_DEL_ZONE_POSITION);
			cs.setInt(1, zoneId);
			cs.setString(2, ids);
			cs.execute();
			
			for (int i = 0; i < docInvBean.getPositions().size(); i++) {
				docInvBean.getPositions().get(i).setZoneId(docInvBean.getZoneId());
				cs = null;
				cs = con.prepareCall(INV_SP_ADD_DOC_INVENTORY_POSITIONS);
				cs.setString(1,docInvBean.getZoneId());
				cs.setString(2,docInvBean.getPositions().get(i).getLgtyp());
				cs.setString(3,docInvBean.getPositions().get(i).getLgpla());
				cs.setString(4,docInvBean.getPositions().get(i).getSecuency());
				cs.setString(5,docInvBean.getPositions().get(i).getImwm());
				cs.setString(6,docInvBean.getPositions().get(i).getLgnum());
				cs.setString(7,docInvBean.getPositions().get(i).getLgtypDesc());
				cs.registerOutParameter(8, Types.INTEGER);
				log.info("[addPositionZone] Executing query...");
				cs.execute();
				idPosition = cs.getInt(8);
				docInvBean.getPositions().get(i).setPkAsgId(idPosition);
				
				//Eliminar materiales de posición
				ids = "";
				for (int j = 0; j < docInvBean.getPositions().get(i).getMaterials().size(); j++) {
									
					ids += docInvBean.getPositions().get(i).getMaterials().get(j).getMatnr() + ",";				
				}
													
				cs = null;
				cs = con.prepareCall(INV_SP_DESASSIGN_MATERIAL_TO_ZONE);
				cs.setInt(1, docInvBean.getPositions().get(i).getPkAsgId());
				cs.setString(2, ids);
				cs.execute();
				
				for(int k = 0; k < docInvBean.getPositions().get(i).getMaterials().size(); k++){
					docInvBean.getPositions().get(i).getMaterials().get(k).setPosMat(idPosition);
					
					cs = null;
					cs = con.prepareCall(INV_SP_ASSIGN_MATERIAL_TO_ZONE);
					
					cs.setInt(1,docInvBean.getPositions().get(i).getMaterials().get(k).getPosMat());
					cs.setString(2,docInvBean.getPositions().get(i).getMaterials().get(k).getMatnr());
					cs.registerOutParameter(3, Types.INTEGER);
					cs.registerOutParameter(4, Types.VARCHAR);
					
					log.info("[assignMaterialToZoneDao] Executing query...");
					cs.execute();
					docInvBean.getPositions().get(i).getMaterials().get(k).setPosMat(cs.getInt(3));
					docInvBean.getPositions().get(i).getMaterials().get(k).setDescM(cs.getString(4));
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
		res.setLsObject(docInvBean);
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

*/
}
