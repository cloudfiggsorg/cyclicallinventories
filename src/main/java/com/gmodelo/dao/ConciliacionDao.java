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
		CallableStatement cs = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		List<ConciliationPositionBean> listPositions = new ArrayList<ConciliationPositionBean>();
		
		String DROP_TABLE = "INV_SP_DROP_TABLE_COUNT_POSITIONS ?";
		String INV_VW_DOC_INVENTORY_POSITIONS_TEMP = "SELECT TAS_DOC_INV_ID, ZONE_ID, ZONE_DESC, LGPLA, MATNR,  MATDESC, MEINS, COUNT_NUM, TOTAL INTO "
				+ " DBO.COUNT_POSITIONS FROM INV_VW_COUNT_POSITIONS_BASE WITH(NOLOCK) WHERE TAS_DOC_INV_ID= ?  ORDER BY MATNR,COUNT_NUM ASC";
		
		
		String INV_VW_COUNT_POSITIONS = "SELECT TAS_DOC_INV_ID, ZONE_ID, ZONE_DESC,LGPLA, MATNR, MATDESC, MEINS, [1A],[1B],[2],[3] FROM INV_VW_COUNT_POSITIONS ORDER BY MATNR ASC ";
		try {
			
			//DROP TABLE TEMPORAL CONCILIATION POSITIONS
			cs = con.prepareCall(DROP_TABLE);
			cs.registerOutParameter(1, Types.INTEGER);
			cs.execute();
			if(cs.getInt(1) > 0){
				
				//GENERAR TABLE TEMPORAL
				stm = con.prepareCall(INV_VW_DOC_INVENTORY_POSITIONS_TEMP);
				stm.setInt(1, i);
				System.out.println("id: "+ i);
				log.info("[getPositionsConciliationDao] TABLE_CONCILIATION_POSITIONS Temp, Executing query...");
				int result = stm.executeUpdate();
				System.out.println("result:" +result);
				if(result > 0){
					//EJECUTAR TABLE FINAL TABLE_CONCILIATION_POSITIONS
					stm = null;
					stm = con.prepareCall(INV_VW_COUNT_POSITIONS);
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
							position.setCount1A(rs.getString("1A"));
							position.setCount1B(rs.getString("1B"));
							position.setCount2(rs.getString("2"));
							position.setCount3(rs.getString("3"));
							listPositions.add(position);
						}
					}else{
						log.info("[getPositionsConciliationDao] No data in TABLE_CONCILIATION_POSITIONS Final...");
					}
				}else{
					log.info("[getPositionsConciliationDao] No data in TABLE_CONCILIATION_POSITIONS Temp...");
				}
				
			}else{
				log.info("getPositionsConciliationDao] Not delete table Temp");
			}
			
			//Free resources
			if(cs != null){cs.close();}
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

	/*
	public static void main(String args[]){
		ConciliacionDao dao = new ConciliacionDao();
		ConciliacionBean docInvBean = new ConciliacionBean();
		docInvBean.setDocInvId(3);
		String searchFilter = "";
		Response<List<ConciliacionBean>> x = dao.getConciliacion(docInvBean, searchFilter);
		for(int i=0; i < x.getLsObject().size(); i++){
			System.out.println(x.getLsObject().get(i).toString());
		}
	}
	*/
}
