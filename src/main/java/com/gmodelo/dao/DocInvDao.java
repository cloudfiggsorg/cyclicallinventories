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

import javax.naming.NamingException;

import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.DocInvBean;
import com.gmodelo.beans.DocInvPositionBean;
import com.gmodelo.beans.Response;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class DocInvDao {

	private Logger log = Logger.getLogger(DocInvDao.class.getName());
	private UMEDaoE ume;  
	private User user; 

	public Response<DocInvBean> addDocInv(DocInvBean docInvBean, String createdBy) {
		Response<DocInvBean> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		int docInvId = 0;

		final String INV_SP_ADD_DOC_INVENTOY_HEADER = "INV_SP_ADD_DOC_INVENTOY_HEADER ?, ?, ?, ?, ?, ?, ?, ?";

		log.info("[addDocInv] Preparing sentence...");
		try {
			con.setAutoCommit(false);
			cs = con.prepareCall(INV_SP_ADD_DOC_INVENTOY_HEADER);

			cs.setString(1, docInvBean.getRoute());
			cs.setString(2, docInvBean.getBukrs());
			cs.setString(3, docInvBean.getType());
			cs.setString(4, docInvBean.getWerks());
			if (docInvBean.getStatus() == null) {
				cs.setNull(5, Types.CHAR);
			} else {
				cs.setString(5, docInvBean.getStatus());
			}
			cs.setString(6, createdBy);

			if (docInvBean.getDocInvId() != null) {
				cs.setInt(7, docInvBean.getDocInvId());
			} else {
				cs.setNull(7, Types.BIGINT);
			}

			if (docInvBean.getDocFatherInvId() != null) {
				cs.setInt(8, docInvBean.getDocFatherInvId());
			} else {
				cs.setNull(8, Types.BIGINT);
			}

			cs.registerOutParameter(7, Types.INTEGER);
			log.info("[addDocInv] Executing query...");

			cs.execute();
			docInvId = cs.getInt(7);
			docInvBean.setDocInvId(docInvId);

			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, warning.getMessage());
				warning = warning.getNextWarning();
			}
			con.commit();
			cs.close();
			log.info("[addDocInv] Sentence successfully executed.");
		} catch (SQLException e) {
			try {
				log.log(Level.WARNING, "[addDocInv] Execute rollback");
				con.rollback();
			} catch (SQLException e1) {
				log.log(Level.SEVERE, "[addDocInv] Not rollback .", e);
			}
			log.log(Level.SEVERE, "[addDocInv] Some error occurred while was trying to execute the S.P.: "
					+ INV_SP_ADD_DOC_INVENTOY_HEADER, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[addDocInv] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(docInvBean);
		return res;
	}
	
	
	
	public Response<Object> deleteDocInvId(String arrayIdDocInv) {
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		final String INV_SP_DEL_DOC_INV = "INV_SP_DEL_DOC_INV ?";

		log.info("[deleteDocInvId] Preparing sentence...");
		try {
			cs = con.prepareCall(INV_SP_DEL_DOC_INV);
			cs.setString(1, arrayIdDocInv);
			log.info("[deleteDocInvId] Executing query...");
			cs.execute();
			// Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, "[deleteDocInvId] " + warning.getMessage());
				warning = warning.getNextWarning();
			}

			cs.close();
			log.info("[deleteDocInvId] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[deleteDocInvId] Some error occurred while was trying to execute the S.P.: " + INV_SP_DEL_DOC_INV,
					e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[deleteDocInvId] Some error occurred while was trying to close the connection.",
						e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res;
	}

	public Response<List<DocInvBean>> getDocInv(DocInvBean docInvBean, String searchFilter) {
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
			log.info("Is String");
		}

		String INV_VW_DOC_INV = "SELECT DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE, STATUS, JUSTIFICATION, CREATED_BY, MODIFIED_BY FROM INV_VW_DOC_INV WITH(NOLOCK)";
		if (searchFilter != null) {
			INV_VW_DOC_INV += " WHERE TYPE != '3' AND DOC_INV_ID LIKE '%" + searchFilterNumber
					+ "%' OR ROUTE_ID LIKE '%" + searchFilterNumber + "%' OR BDESC LIKE '%" + searchFilterNumber + "%' "
					+ " OR WERKSD LIKE '%" + searchFilterNumber + "%' ";
		} else {
			String condition = buildCondition(docInvBean);
			if (condition != null) {
				INV_VW_DOC_INV += condition;
			}
		}

		log.info(INV_VW_DOC_INV);
		INV_VW_DOC_INV += " GROUP BY DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE, STATUS, JUSTIFICATION, CREATED_BY, MODIFIED_BY";
		log.info("[getDocInvDao] Preparing sentence...");

		try {
			stm = con.prepareCall(INV_VW_DOC_INV);
			log.info("[getDocInvDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			ume = new UMEDaoE();
			while (rs.next()) {
				
				docInvBean = new DocInvBean();
				docInvBean.setRoute(String.format("%08d", rs.getInt("ROUTE_ID")));
				docInvBean.setDocInvId(rs.getInt("DOC_INV_ID"));
				docInvBean.setBukrs(rs.getString("BUKRS"));
				docInvBean.setBukrsD(rs.getString("BDESC"));
				docInvBean.setWerks(rs.getString("WERKS"));
				docInvBean.setWerksD(rs.getString("WERKSD"));
				docInvBean.setType(rs.getString("TYPE"));
				docInvBean.setStatus(rs.getString("STATUS"));
				
				user = new User();	
				
				user.getEntity().setIdentyId(rs.getString("CREATED_BY"));
				ArrayList<User> ls = new ArrayList<>();
				ls.add(user);
				ls = ume.getUsersLDAPByCredentials(ls);
				
				if(ls.size() > 0){
					
					docInvBean.setCreatedBy(rs.getString("CREATED_BY") + " - " + ls.get(0).getGenInf().getName() + " " + ls.get(0).getGenInf().getLastName());
				}else{
					docInvBean.setCreatedBy(rs.getString("CREATED_BY"));
				}
				
				user.getEntity().setIdentyId(rs.getString("MODIFIED_BY"));
				ls = new ArrayList<>();
				ls.add(user);
				ls = ume.getUsersLDAPByCredentials(ls);
				
				if(ls.size() > 0){
					
					docInvBean.setModifiedBy(rs.getString("MODIFIED_BY") + " - " + ls.get(0).getGenInf().getName() + " " + ls.get(0).getGenInf().getLastName());
				}else{
					docInvBean.setModifiedBy(rs.getString("MODIFIED_BY"));
				}
				listDocInv.add(docInvBean);
			}

			// Retrive the warnings if there're
			SQLWarning warning = stm.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			rs.close();
			stm.close();

			log.info("[getDocInvDao] Sentence successfully executed.");

		} catch (SQLException | NamingException e) {
			log.log(Level.SEVERE,
					"[getDocInvDao] Some error occurred while was trying to execute the query: " + INV_VW_DOC_INV, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getDocInvDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listDocInv);
		return res;
	}

	public DocInvBean getDocInvById(int docInvId) throws SQLException {
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		DocInvBean docInvBean = null;

		String INV_VW_DOC_INV = "SELECT DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE, STATUS, JUSTIFICATION FROM INV_VW_DOC_INV WITH(NOLOCK)";
		INV_VW_DOC_INV += "WHERE DOC_INV_ID = " + docInvId + " ";
		// INV_VW_DOC_INV += "AND (STATUS IS NULL OR STATUS = '0')";

		log.info(INV_VW_DOC_INV);
		INV_VW_DOC_INV += " GROUP BY DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE, STATUS, JUSTIFICATION";
		log.info("[getDocInvDao] Preparing sentence...");

		try {
			stm = con.prepareCall(INV_VW_DOC_INV);
			log.info("[getDocInvDao] Executing query...");
			ResultSet rs = stm.executeQuery();

			if (rs.next()) {
				docInvBean = new DocInvBean();
				docInvBean.setRoute(String.format("%08d", rs.getInt("ROUTE_ID")));
				docInvBean.setDocInvId(rs.getInt("DOC_INV_ID"));
				docInvBean.setBukrs(rs.getString("BUKRS"));
				docInvBean.setBukrsD(rs.getString("BDESC"));
				docInvBean.setWerks(rs.getString("WERKS"));
				docInvBean.setWerksD(rs.getString("WERKSD"));
				docInvBean.setType(rs.getString("TYPE"));
				docInvBean.setStatus(rs.getString("STATUS"));
			}

			// Retrive the warnings if there're
			SQLWarning warning = stm.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			rs.close();
			stm.close();

			log.info("[getDocInvDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getDocInvDao] Some error occurred while was trying to execute the query: " + INV_VW_DOC_INV, e);
			throw e;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getDocInvDao] Some error occurred while was trying to close the connection.",
						e);
				throw e;
			}
		}
		return docInvBean;
	}

	public int updateStatusDocInv(int i) {
		ConnectionManager iConnectionManager = new ConnectionManager();
		;
		Connection con = iConnectionManager.createConnection();
		String UPDATE = "UPDATE INV_DOC_INVENTORY_HEADER SET DIH_STATUS='1' WHERE DOC_INV_ID=?";
		try {
			PreparedStatement stm = con.prepareStatement(UPDATE);
			stm.setInt(1, i);
		} catch (SQLException e) {
			try {
				con.close();
			} catch (SQLException e1) {
				log.log(Level.SEVERE,
						"[updateStatusDocInv] Some error occurred while was trying to close the connection.", e1);
			}
			log.log(Level.SEVERE, "[updateStatusDocInv] Some error occurred while was trying to execute: " + UPDATE
					+ "Exeption: " + e);
		}
		return 0;
	}

	private String buildCondition(DocInvBean docInvB) {
		String condition = "";
		String DOC_INV_ID = "";
		String ROUTE_ID = "";
		String bukrs = "";
		String werks = "";
		String status = "";

		DOC_INV_ID = (docInvB.getDocInvId() != null
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + "DOC_INV_ID = '" + docInvB.getDocInvId() + "' "
				: "");
		condition += DOC_INV_ID;
		ROUTE_ID = (docInvB.getRoute() != null
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + "ROUTE_ID = '" + docInvB.getRoute() + "' "
				: "");
		condition += ROUTE_ID;
		bukrs = (docInvB.getBukrs() != null
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + "BUKRS = '" + docInvB.getBukrs() + "' " : "");
		condition += bukrs;
		werks = (docInvB.getWerks() != null
				? (condition.contains("WHERE") ? " AND " : " WHERE ") + "WERKS = '" + docInvB.getWerks() + "' " : "");
		condition += werks;
		status = (docInvB.getStatus() == null ? (condition.contains("WHERE") ? " AND " : " WHERE ") + "STATUS IS NULL "
				: (condition.contains("WHERE") ? " AND " : " WHERE ") + " STATUS = '" + docInvB.getStatus() + "'");
		condition += status;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

	private static final String INSERT_DOCUMENT_INVENTORY_POSITIONS = "INSERT INTO INV_DOC_INVENTORY_POSITIONS "
			+ " (DIP_DOC_INV_ID,DIP_LGORT,DIP_LGTYP,DIP_LGPLA,DIP_MATNR,DIP_COUNTED) VALUES (?,?,?,?,?,?)";

	public AbstractResultsBean addDocumentPosition(List<DocInvPositionBean> positionBean, Connection con)
			throws SQLException {
		AbstractResultsBean resultBean = new AbstractResultsBean();
		PreparedStatement stm = con.prepareStatement(INSERT_DOCUMENT_INVENTORY_POSITIONS);
		for (DocInvPositionBean singleBean : positionBean) {
			stm.setInt(1, singleBean.getDocInvId());
			stm.setString(2, singleBean.getLgort());
			stm.setString(3, singleBean.getLgtyp());
			stm.setString(4, singleBean.getLgpla());
			stm.setString(5, singleBean.getMatnr());
			stm.setString(6, singleBean.getCounted());
			stm.addBatch();
		}
		stm.executeBatch();
		return resultBean;
	}

}
