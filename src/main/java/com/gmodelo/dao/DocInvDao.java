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
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.DocInvPositionBean;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.RouteBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.DocInvPositionBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class DocInvDao {
	
	private Logger log = Logger.getLogger( DocInvDao.class.getName());
	
	public Response<DocInvBean> addDocInv(DocInvBean docInvBean, String createdBy){
		Response<DocInvBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		int idPosition = 0;
		int docInvId = 0;
				
		final String INV_SP_ADD_DOC_INVENTOY_HEADER = "INV_SP_ADD_DOC_INVENTOY_HEADER ?, ?, ?, ?, ?, ?,?";
		final String INV_SP_ADD_DOC_INVENTORY_POSITIONS = "INV_SP_ADD_DOC_INVENTORY_POSITIONS ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
		final String INV_SP_DEL_DOC_INV_POSITION = "INV_SP_DEL_DOC_INV_POSITION ?, ?";
		
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
	
			cs.setString(1,docInvBean.getRoute().getRouteId());
			cs.setString(2,docInvBean.getBukrs());
			cs.setString(3,docInvBean.getWerks());
			cs.setString(4,docInvBean.getType());
			cs.setString(5,createdBy);
			cs.setString(6,docInvBean.getJustification());
			
			cs.registerOutParameter(7, Types.INTEGER);
			log.info("[addDocInv] Executing query...");
			
			cs.execute();
			docInvId = cs.getInt(7);			
			docInvBean.setDocInvId(docInvId);
			
			//Eliminar posiciones
			String ids = "";
			for (int i = 0; i < docInvBean.getPositions().size(); i++) {
				if(docInvBean.getPositions().get(i).getPositionId() > 0){
					ids += docInvBean.getPositions().get(i).getPositionId() + ",";
				}				
			}
															
			cs = null;
			cs = con.prepareCall(INV_SP_DEL_DOC_INV_POSITION);
			cs.setInt(1, docInvId);
			cs.setString(2, ids);
			cs.execute();

			//INSERTAR POSICIONES
			
			for (int i = 0; i < docInvBean.getPositions().size(); i++) {
				docInvBean.getPositions().get(i).setDocInvId(docInvId);
				
				cs = null;
				cs = con.prepareCall(INV_SP_ADD_DOC_INVENTORY_POSITIONS);
	
				cs.setInt(2,docInvBean.getDocInvId());
				cs.setInt(3,docInvBean.getPositions().get(i).getLgort());
				cs.setString(4,docInvBean.getPositions().get(i).getLgtyp());
				cs.setString(5,docInvBean.getPositions().get(i).getLgpla());
				cs.setString(6,docInvBean.getPositions().get(i).getMatnr());
				cs.setString(7,docInvBean.getPositions().get(i).getTheoric());
				cs.setString(8,docInvBean.getPositions().get(i).getCounted());
				cs.setString(9,docInvBean.getPositions().get(i).getDiffCounted());
				cs.setString(10,docInvBean.getPositions().get(i).getFlag());
				cs.setString(11,docInvBean.getPositions().get(i).getMeins());
				cs.setString(12,docInvBean.getPositions().get(i).getExplosion());
				cs.setString(13,docInvBean.getPositions().get(i).getGdes());
				cs.setString(14,docInvBean.getPositions().get(i).getLgtypDes());
				cs.registerOutParameter(1, Types.INTEGER);
				log.info("[addPositionDocInv] Executing query...");
				cs.execute();
				idPosition = cs.getInt(1);
				docInvBean.getPositions().get(i).setPositionId(idPosition);
			}
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,warning.getMessage());
				warning = warning.getNextWarning();
			}
			con.commit();
			cs.close();	
			log.info("[addDocInv] Sentence successfully executed.");
		} catch (SQLException e) {
			try {
				log.log(Level.WARNING,"[addDocInv] Execute rollback");
				con.rollback();
			} catch (SQLException e1) {
				log.log(Level.SEVERE,"[addDocInv] Not rollback .", e);
			}
			log.log(Level.SEVERE,"[addDocInv] Some error occurred while was trying to execute the S.P.: "+ INV_SP_ADD_DOC_INVENTOY_HEADER, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[addDocInv] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(docInvBean);
		return res ;
	}
	
	public Response<Object> deleteDocInvId(String arrayIdDocInv){
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		final String INV_SP_DEL_DOC_INV = "INV_SP_DEL_DOC_INV ?";
		
		log.info("[deleteDocInvId] Preparing sentence...");
		try {
			cs = con.prepareCall(INV_SP_DEL_DOC_INV);
			cs.setString(1,arrayIdDocInv);
			log.info("[deleteDocInvId] Executing query...");
			cs.execute();
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[deleteDocInvId] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			cs.close();	
			log.info("[deleteDocInvId] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[deleteDocInvId] Some error occurred while was trying to execute the S.P.: "+INV_SP_DEL_DOC_INV, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[deleteDocInvId] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}

	public Response<List<DocInvBean>> getDocInv(DocInvBean docInvBean, String searchFilter){
		Response<List<DocInvBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<DocInvBean> listDocInv = new ArrayList<DocInvBean>();
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
				docInvBean = new DocInvBean();
				
				RouteDao route = new RouteDao();
				docInvBean.setRoute(route.getRoute(rs.getInt("ROUTE_ID")));
				
				docInvBean.setDocInvId(rs.getInt("DOC_INV_ID"));
				docInvBean.setBukrs(rs.getString("BUKRS"));
				docInvBean.setBukrsD(rs.getString("BDESC"));
				docInvBean.setWerks(rs.getString("WERKS"));
				docInvBean.setWerksD(rs.getString("WERKSD"));
				docInvBean.setType(rs.getString("TYPE"));
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
	
	private String buildCondition(DocInvBean docInvB){
		String condition ="";
		String DOC_INV_ID ="";
		String ROUTE_ID ="";
		String bukrs ="";
		String werks ="";
		String TYPE ="";
		String JUSTIFICATION = "";
		
		DOC_INV_ID = (docInvB.getDocInvId() != 0 ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "DOC_INV_ID LIKE '%" 	+ docInvB.getDocInvId() + "%' " : "");
		condition+=DOC_INV_ID;
		ROUTE_ID = (docInvB.getRoute() 	!= null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "ROUTE_ID LIKE '%" 	+ docInvB.getRoute() + "%' ": "");
		condition+=ROUTE_ID;
		bukrs = (docInvB.getBukrs() 	!= null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "BUKRS LIKE '%" 	+ docInvB.getBukrs() + "%' ": "");
		condition+=bukrs;
		werks = (docInvB.getWerks() 	!= null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "WERKS LIKE '%"		+ docInvB.getWerks() + "%' ": "");
		condition+=werks;
		TYPE = (docInvB.getType() 	!= null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "TYPE LIKE '%"		+ docInvB.getType() + "%' ": "");
		condition+=TYPE;
		JUSTIFICATION = (docInvB.getJustification() != null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "JUSTIFICATION LIKE '%"	+ docInvB.getJustification() + "%' ": "");
		condition+=JUSTIFICATION;

		condition = condition.isEmpty() ? null : condition;
		return condition;
	}


}
