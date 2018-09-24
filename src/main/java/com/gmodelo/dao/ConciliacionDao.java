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
import com.gmodelo.beans.ConciliacionBean;
import com.gmodelo.beans.ConciliationsIDsBean;
import com.gmodelo.beans.ConciliationPositionBean;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class ConciliacionDao {
	
	private Logger log = Logger.getLogger( ConciliacionDao.class.getName());
	
	public Response<List<ConciliationsIDsBean>> getConciliationIDs(){
		Response<List<ConciliationsIDsBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ConciliationsIDsBean> listConIds = new ArrayList<ConciliationsIDsBean>();
		ConciliationsIDsBean conciliationIDsBean;
		final String GENERATE_IDDESC_CONCILIATION = "select DOC_INV_ID as DOC_INV, (CONVERT(VARCHAR, DOC_INV_ID) + ' - ' + CONVERT(VARCHAR,inr.ROU_DESC)) as DESCRIPCION from INV_DOC_INVENTORY_HEADER idih WITH(NOLOCK)  Inner join INV_ROUTE inr WITH(NOLOCK) on idih.DIH_ROUTE_ID = inr.routE_ID WHERE idih.DIH_STATUS = '1'";
		
		try {
			stm = con.prepareCall(GENERATE_IDDESC_CONCILIATION);
			log.info("[getConciliationIDsDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
				 conciliationIDsBean = new ConciliationsIDsBean();
				
				 conciliationIDsBean.setId(rs.getString("DOC_INV"));
				 conciliationIDsBean.setDesc(rs.getString("DESCRIPCION"));
				
				 listConIds.add(conciliationIDsBean);
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
			
			log.info("[getConciliationIDsDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getConciliationIDsDao] Some error occurred while was trying to execute the query: "+GENERATE_IDDESC_CONCILIATION, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getConciliationIDsDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listConIds);
		return res ;
	}
	
	public Response<List<ConciliacionBean>> getConciliacion(ConciliacionBean docInvBean, String searchFilter){
		Response<List<ConciliacionBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<ConciliacionBean> listDocInv = new ArrayList<ConciliacionBean>();
		int aux;		
		String searchFilterNumber = "";
		try {
			aux = Integer.parseInt(searchFilter); 
			searchFilterNumber += aux;
		} catch (Exception e) {
			searchFilterNumber = searchFilter;
			log.info("Trying to convert String to Int");
		}
		String INV_VW_DOC_INV = "SELECT DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE,JUSTIFICATION FROM INV_VW_DOC_INV WITH(NOLOCK)";
		if(searchFilter != null){
			INV_VW_DOC_INV += " WHERE DOC_INV_ID LIKE '%" + searchFilterNumber + "%' OR ROUTE_ID LIKE '%"+searchFilter+"%' ";
		}else{
			String condition = buildCondition(docInvBean);
			if(condition != null){
				INV_VW_DOC_INV += condition;
			}
		}
		
		INV_VW_DOC_INV += " GROUP BY DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE,JUSTIFICATION";
		log.info(INV_VW_DOC_INV);
		log.info("[getDocInvDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_DOC_INV);
			log.info("[getDocInvDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
				docInvBean = new ConciliacionBean();
				
				docInvBean.setRoute(rs.getString("ROUTE_ID"));
				docInvBean.setDocInvId(rs.getInt("DOC_INV_ID"));
				docInvBean.setBukrs(rs.getString("BUKRS"));
				docInvBean.setBukrsD(rs.getString("BDESC"));
				docInvBean.setWerks(rs.getString("WERKS"));
				docInvBean.setWerksD(rs.getString("WERKSD"));
				docInvBean.setJustification(rs.getString("JUSTIFICATION"));
				docInvBean.setPositions(getPositions(rs.getInt("DOC_INV_ID")));
				listDocInv.add(docInvBean);
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
			
			log.info("[getDocInvDao] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getDocInvDao] Some error occurred while was trying to execute the query: "+INV_VW_DOC_INV, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getDocInvDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listDocInv);
		return res ;
	}
	
	private List<ConciliationPositionBean> getPositions(int i){
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		ResultSet rs = null;
		List<ConciliationPositionBean> listPositions = new ArrayList<ConciliationPositionBean>();
		
		String INV_VW_DOC_INVENTORY_POSITIONS_TEMP = "SELECT TAS_DOC_INV_ID, ZONE_ID, ZONE_DESC, LGPLA, MATNR,  MATDESC, MEINS FROM INV_VW_COUNT_POSITIONS_BASE WITH(NOLOCK) WHERE TAS_DOC_INV_ID= ? "
				+ "GROUP BY TAS_DOC_INV_ID, ZONE_ID, ZONE_DESC, LGPLA, MATNR,  MATDESC, MEINS ORDER BY MATNR ASC";
		
		try {
				stm = con.prepareCall(INV_VW_DOC_INVENTORY_POSITIONS_TEMP);
				stm.setInt(1, i);
				log.info("[getPositionsConciliationDao] TABLE_CONCILIATION_POSITIONS Temp, Executing query...");
				rs = stm.executeQuery();
				
				if(rs != null){
					while (rs.next()){
						ConciliationPositionBean position = new ConciliationPositionBean();
						position.setZoneId(rs.getString("ZONE_ID"));   
						position.setZoneD(rs.getString("ZONE_DESC"));
						position.setLgpla(rs.getString("LGPLA"));
						
						try {
							position.setMatnr(String.valueOf(rs.getInt("MATNR")));
						} catch (Exception e) {
							position.setMatnr(rs.getString("MATNR"));
						}
						position.setMatnrD(rs.getString("MATDESC"));
						position.setMeasureUnit(rs.getString("MEINS"));
						
						position.setCount1A(this.getRowCount(i, "1A",rs.getString("ZONE_ID"),rs.getString("LGPLA"), rs.getString("MATNR"), rs.getString("MEINS"), con, stm));
						position.setCount1B(this.getRowCount(i, "1B",rs.getString("ZONE_ID"),rs.getString("LGPLA"), rs.getString("MATNR"), rs.getString("MEINS"), con, stm));
						position.setCount2(this.getRowCount(i, "2",rs.getString("ZONE_ID"),rs.getString("LGPLA"), rs.getString("MATNR"), rs.getString("MEINS"), con, stm));
						position.setCount3(this.getRowCount(i, "3",rs.getString("ZONE_ID"),rs.getString("LGPLA"), rs.getString("MATNR"), rs.getString("MEINS"), con, stm));
						listPositions.add(position);
					}
				}else{
					log.info("[getPositionsConciliationDao] No data in TABLE_CONCILIATION_POSITIONS Final...");
				}
				
			//Free resources
			if(rs != null){rs.close();}
			if(stm != null){stm.close();}	
			
			log.info("[getPositionsConciliationDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getPositionsConciliationDao] Some error occurred while was trying to execute the query: "+INV_VW_DOC_INVENTORY_POSITIONS_TEMP, e);
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getPositionsConciliationDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		return listPositions;
	}
	
	public String getRowCount(int docInvId, String conteo,String idZone,String lgpla, String matnr, String meins,Connection con, PreparedStatement stm){
		String result="";
		stm = null;
		String INV_VW_COUNT_POSITIONS = "SELECT TOTAL FROM INV_VW_COUNT_POSITIONS_BASE WITH(NOLOCK) WHERE TAS_DOC_INV_ID= ? AND ZONE_ID=? AND LGPLA = ? "
				+ " AND MATNR=? AND COUNT_NUM=?";
		try {
			stm = con.prepareStatement(INV_VW_COUNT_POSITIONS);
			stm.setInt(1, docInvId);
			stm.setString(2, idZone);
			stm.setString(3, lgpla);
			stm.setString(4, matnr);
			stm.setString(5, conteo);
			ResultSet res = stm.executeQuery();
			while(res.next()){
				result = res.getString("TOTAL");
			}
		} catch (SQLException e1) {
			log.log(Level.SEVERE,"[getRowCountConciliationDao] Some error occurred while was execute: "+ INV_VW_COUNT_POSITIONS + "Exception: "+ e1.getMessage());
		}
		return result;
	}
	
	private String buildCondition(ConciliacionBean docInvB){
		String condition ="";
		String DOC_INV_ID ="";
		String ROUTE_ID ="";
		String bukrs ="";
		String werks ="";
		String JUSTIFICATION = "";
		
		DOC_INV_ID = (docInvB.getDocInvId() != 0 ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "DOC_INV_ID = '" 	+ docInvB.getDocInvId() + "' " : "");
		condition+=DOC_INV_ID;
		ROUTE_ID = (docInvB.getRoute() 	!= null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "ROUTE_ID = '" 	+ docInvB.getRoute() + "' ": "");
		condition+=ROUTE_ID;
		bukrs = (docInvB.getBukrs() 	!= null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "BUKRS = '" 	+ docInvB.getBukrs() + "' ": "");
		condition+=bukrs;
		werks = (docInvB.getWerks() 	!= null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "WERKS = '"		+ docInvB.getWerks() + "' ": "");
		condition+=werks;
		JUSTIFICATION = (docInvB.getJustification() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "JUSTIFICATION = '="	+ docInvB.getJustification() + "' ": "");
		condition+=JUSTIFICATION;

		condition = condition.isEmpty() ? null : condition;
		return condition;
	}	
}
