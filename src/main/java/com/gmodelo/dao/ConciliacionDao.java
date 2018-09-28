package com.gmodelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.ConciliacionBean;
import com.gmodelo.beans.ConciliationPositionBean;
import com.gmodelo.beans.ConciliationsIDsBean;
import com.gmodelo.beans.GroupBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.TaskBean;
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
				docInvBean.setType(rs.getString("TYPE"));
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
		String INV_VW_COUNT_POSITIONS = "SELECT SUM(TOTAL) TOTAL FROM INV_VW_COUNT_POSITIONS_BASE WITH(NOLOCK) WHERE TAS_DOC_INV_ID= ? AND ZONE_ID=? AND LGPLA = ? "
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
	
	public Response<List<GroupBean>> getAvailableGroups(int docInvId){
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		ResultSet rs = null;
		List<GroupBean> listGroups = new ArrayList<GroupBean>();
		Response<List<GroupBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		GroupBean gb = new GroupBean();
		GroupDao gpDao= new GroupDao();
		
		String INV_VW_AVAILABLE_GROUPS = "SELECT GRPS.GROUP_ID, GRPS.GRP_DESC "   
				+ "FROM INV_ROUTE_GROUPS AS IRG "
				+ "INNER JOIN INV_DOC_INVENTORY_HEADER AS IDIH ON (IRG.RGR_ROUTE_ID = IDIH.DIH_ROUTE_ID) "
				+ "INNER JOIN INV_GROUPS AS GRPS ON(GRPS.GROUP_ID = IRG.RGR_GROUP_ID) "
				+ "AND RGR_GROUP_ID NOT IN(SELECT TAS_GROUP_ID "
				+ "FROM INV_TASK "
				+ "WHERE TAS_DOC_INV_ID = ?) "
				+ "WHERE IDIH.DOC_INV_ID = ? "
				+ "ORDER BY IRG.RGR_COUNT_NUM ASC";
		
		try {
				stm = con.prepareCall(INV_VW_AVAILABLE_GROUPS);
				stm.setInt(1, docInvId);
				stm.setInt(2, docInvId);
				log.info(INV_VW_AVAILABLE_GROUPS);
				rs = stm.executeQuery();
				
				if(rs.next()){
					
					gb = new GroupBean();				
					gb.setGroupId(rs.getString(1));
					gb.setGdesc(rs.getString(2));
					gb.setUsers(gpDao.groupUsers(gb.getGroupId(), null));
					listGroups.add(gb);
				}
				
			//Free resources
			rs.close();	
			stm.close();
							
			log.info("[getAvailableGroups] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getAvailableGroups] Some error occurred while was trying to execute the query: " + INV_VW_AVAILABLE_GROUPS, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getAvailableGroups] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;

			}
		}
		
		res.setAbstractResult(abstractResult);
		res.setLsObject(listGroups);
		return res;
	}
	
	public Response<TaskBean> getFatherTaskByDocId(int docInvId){
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		ResultSet rs = null;
		TaskBean tb = null;
		Response<TaskBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		
		String SQL_GET_PARENT_TASK = "SELECT TASK_ID, TAS_GROUP_ID, TAS_CREATED_DATE, "
				+ "TAS_DOWLOAD_DATE, TAS_UPLOAD_DATE, TASK_ID_PARENT "
				+ "FROM INV_TASK " 
				+ "INNER JOIN INV_DOC_INVENTORY_HEADER ON INV_TASK.TAS_DOC_INV_ID = INV_DOC_INVENTORY_HEADER.DOC_INV_ID "
				+ "WHERE TAS_DOC_INV_ID = ? AND TAS_STATUS = '1' " 
				+ "AND INV_DOC_INVENTORY_HEADER.DIH_STATUS = '1' "
				+ "AND TASK_ID_PARENT IS NULL";
		
		try {
				stm = con.prepareCall(SQL_GET_PARENT_TASK);
				stm.setInt(1, docInvId);
				log.info(SQL_GET_PARENT_TASK);
				rs = stm.executeQuery();
				
				if(rs.next()){
					
					tb = new TaskBean();
					tb.setTaskId(rs.getString(1));
					tb.setGroupId(rs.getString(2));				
					
					try {
						tb.setdCreated(rs.getDate(3).getTime());
					} catch (NullPointerException e) {
						tb.setdCreated(0);
					}
					
					try {
						tb.setdDownlad(rs.getDate(4).getTime());
					} catch (NullPointerException e) {
						tb.setdDownlad(0);
					}
					
					try {
						tb.setdUpload(rs.getDate(5).getTime());
					} catch (Exception e) {
						tb.setdUpload(0);
					}
														
					try {
						tb.setTaskIdFather(rs.getString(6));
					} catch (Exception e) {
						tb.setTaskIdFather(null);
					}
				}
				
			//Free resources
			rs.close();	
			stm.close();
							
			log.info("[getFatherTaskByDocId] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getFatherTaskByDocId] Some error occurred while was trying to execute the query: " + SQL_GET_PARENT_TASK, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getFatherTaskByDocId] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;

			}
		}
		
		res.setAbstractResult(abstractResult);
		res.setLsObject(tb);
		return res;
	}
	
	public Response<String> getZonePosition(int zoneId, String lgpla){
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		ResultSet rs = null;
		String posId = null;
		Response<String> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		
		String SQL_GET_ZONE_POSITION = "SELECT ZPO_PK_ASG_ID "
				+ "FROM INV_ZONE_POSITION " 
				+ "WHERE ZPO_ZONE_ID = ? AND ZPO_LGPLA = ? "; 
		
		try {
				stm = con.prepareCall(SQL_GET_ZONE_POSITION);
				stm.setInt(1, zoneId);
				stm.setString(2, lgpla);
				log.info(SQL_GET_ZONE_POSITION);
				rs = stm.executeQuery();
				
				while(rs.next()){
					
					posId = rs.getString(1);
				}
				
			//Free resources
			rs.close();	
			stm.close();
							
			log.info("[getZonePosition] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getZonePosition] Some error occurred while was trying to execute the query: " + SQL_GET_ZONE_POSITION, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getZonePosition] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;

			}
		}
		
		res.setAbstractResult(abstractResult);
		res.setLsObject(posId);
		return res;
	}
	
	/*
	public static void main(String args[]){
		ConciliacionDao dao = new ConciliacionDao();
		ConciliacionBean docInvBean = new ConciliacionBean();
	//	docInvBean.setDocInvId(3);
		String searchFilter = "";
		Response<List<ConciliacionBean>> x = dao.getConciliacion(docInvBean, searchFilter);
		for(int i=0; i < x.getLsObject().size(); i++){
			System.out.println(x.getLsObject().get(i).toString());
		}
	}
	*/
}
