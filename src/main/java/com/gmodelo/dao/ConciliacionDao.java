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
import com.gmodelo.beans.DocInvPositionBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteBean;
import com.gmodelo.beans.ConciliacionBean;
import com.gmodelo.beans.ConciliacionBean;
import com.gmodelo.beans.DocInvPositionBean;
import com.gmodelo.beans.ConciliacionBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class ConciliacionDao {
	
	private Logger log = Logger.getLogger( ConciliacionDao.class.getName());
	
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
		log.info(INV_VW_DOC_INV);
		INV_VW_DOC_INV += " GROUP BY DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE,JUSTIFICATION";
		log.info("[getDocInvDao] Preparing sentence...");
		
		try {
			stm = con.prepareCall(INV_VW_DOC_INV);
			log.info("[getDocInvDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
				docInvBean = new ConciliacionBean();
				
				RouteDao route = new RouteDao();
				docInvBean.setRoute(rs.getString("ROUTE_ID"));
				docInvBean.setDocInvId(rs.getInt("DOC_INV_ID"));
				docInvBean.setBukrs(rs.getString("BUKRS"));
				docInvBean.setBukrsD(rs.getString("BDESC"));
				docInvBean.setWerks(rs.getString("WERKS"));
				docInvBean.setWerksD(rs.getString("WERKSD"));
				docInvBean.setJustification(rs.getString("JUSTIFICATION"));
				docInvBean.setPositions(this.getPositions(rs.getString("DOC_INV_ID")));
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
	
	private List<DocInvPositionBean> getPositions(String docInvId){
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<DocInvPositionBean> listPositions = new ArrayList<DocInvPositionBean>();
		String INV_VW_DOC_INVENTORY_POSITIONS = "SELECT LGORT,LGTYP,LGPLA,MATNR,THEORIC,COUNTED,DIFF_COUNTED,DIFF_FLAG,DOC_INV_ID,LTYPT,GDES,MAKTX  FROM INV_VW_DOC_INVENTORY_POSITIONS WITH(NOLOCK) WHERE DOC_INV_ID = ?";
		log.info(INV_VW_DOC_INVENTORY_POSITIONS);
		log.info("getPositionsDocInvDao] Preparing sentence...");
		INV_VW_DOC_INVENTORY_POSITIONS += " GROUP BY LGORT,LGTYP,LGPLA,MATNR,THEORIC,COUNTED,DIFF_COUNTED,DIFF_FLAG,DOC_INV_ID,LTYPT,GDES,MAKTX";
		try {
			stm = con.prepareCall(INV_VW_DOC_INVENTORY_POSITIONS);
			stm.setString(1, docInvId);
			log.info("[getPositionsDocInvDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()){
				DocInvPositionBean position = new DocInvPositionBean();
				position.setDocInvId(Integer.parseInt(docInvId));
				position.setLgort(rs.getInt("LGORT"));
				position.setLgtyp(rs.getString("LGTYP"));
				position.setLgpla(rs.getString("LGPLA"));
				position.setMatnr(rs.getString("MATNR"));
				position.setTheoric(rs.getString("THEORIC"));
				position.setCounted(rs.getString("COUNTED"));
				position.setDiffCounted(rs.getString("DIFF_COUNTED"));
				position.setFlag(rs.getString("DIFF_FLAG"));
				position.setLgtypDes(rs.getString("LTYPT"));
				position.setGdes(rs.getString("GDES"));
				position.setMatnrDes(rs.getString("MAKTX"));
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
			
			log.info("[getPositionsDocInvDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getPositionsDocInvDao] Some error occurred while was trying to execute the query: "+INV_VW_DOC_INVENTORY_POSITIONS, e);
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getPositionsDocInvDao] Some error occurred while was trying to close the connection.", e);
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

}
