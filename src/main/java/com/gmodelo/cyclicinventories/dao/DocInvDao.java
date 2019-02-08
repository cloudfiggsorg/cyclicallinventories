package com.gmodelo.cyclicinventories.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.DocInvPositionBean;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;

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
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		final String INV_SP_ADD_DOC_INVENTOY_HEADER = "INV_SP_ADD_DOC_INVENTOY_HEADER ?, ?, ?, ?, ?, ?, ?, ?, ?";

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
			cs.setNull(7, Types.CHAR);

			if (docInvBean.getDocInvId() != null) {
				cs.setInt(8, docInvBean.getDocInvId());
			} else {
				cs.setNull(8, Types.BIGINT);
			}

			if (docInvBean.getDocFatherInvId() != null) {
				cs.setInt(9, docInvBean.getDocFatherInvId());
			} else {
				cs.setNull(9, Types.BIGINT);
			}

			cs.registerOutParameter(6, Types.VARCHAR);
			cs.registerOutParameter(7, Types.VARCHAR);
			cs.registerOutParameter(8, Types.INTEGER);
			log.info("[addDocInv] Executing query...");

			cs.execute();

			user = new User();
			ume = new UMEDaoE();
			user.getEntity().setIdentyId(cs.getString(6));
			ArrayList<User> ls = new ArrayList<>();
			ls.add(user);
			ls = ume.getUsersLDAPByCredentials(ls);

			if (ls.size() > 0) {

				docInvBean.setCreatedBy(cs.getString(6) + " - " + ls.get(0).getGenInf().getName() + " "
						+ ls.get(0).getGenInf().getLastName() + " - " + format.format(new Date()));
			} else {
				docInvBean.setCreatedBy(cs.getString(6) + " - " + format.format(new Date()));
			}

			user.getEntity().setIdentyId(cs.getString(7));
			ls = new ArrayList<>();
			ls.add(user);
			ls = ume.getUsersLDAPByCredentials(ls);

			if (ls.size() > 0) {

				docInvBean.setModifiedBy(cs.getString(7) + " - " + ls.get(0).getGenInf().getName() + " "
						+ ls.get(0).getGenInf().getLastName() + " - " + format.format(new Date()));
			} else {
				docInvBean.setModifiedBy(cs.getString(7) + " - " + format.format(new Date()));
			}

			docInvId = cs.getInt(8);
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
		} catch (SQLException | NamingException e) {
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
		List<DocInvBean> listDocInv = new ArrayList<>();
		int aux;
		String searchFilterNumber = "";
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		try {
			aux = Integer.parseInt(searchFilter);
			searchFilterNumber += aux;
		} catch (Exception e) {
			searchFilterNumber = searchFilter;
			log.info("Is String");
		}

		String INV_VW_DOC_INV = "SELECT DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE, "
				+ "STATUS, JUSTIFICATION, CREATED_BY, MODIFIED_BY, DCREATED, DMODIFIED "
				+ "FROM INV_VW_DOC_INV WITH(NOLOCK) WHERE DOC_FATHER_INV_ID IS NULL ";
		if (searchFilter != null) {
			INV_VW_DOC_INV += " AND TYPE != '3' AND DOC_INV_ID LIKE '%" + searchFilterNumber + "%' OR ROUTE_ID LIKE '%"
					+ searchFilterNumber + "%' OR BDESC LIKE '%" + searchFilterNumber + "%' " + " OR WERKSD LIKE '%"
					+ searchFilterNumber + "%' ";
		} else {
			String condition = buildCondition(docInvBean);
			if (condition != null) {
				INV_VW_DOC_INV += condition;
			}
		}

		log.info(INV_VW_DOC_INV);
		INV_VW_DOC_INV += " GROUP BY DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE, STATUS, "
				+ "JUSTIFICATION, CREATED_BY, MODIFIED_BY, DCREATED, DMODIFIED";
		log.info("[getDocInvDao] Preparing sentence...");

		try {
			stm = con.prepareCall(INV_VW_DOC_INV);
			log.info("[getDocInvDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			ume = new UMEDaoE();
			while (rs.next()) {

				docInvBean = new DocInvBean();
				docInvBean.setRoute(rs.getString("ROUTE_ID"));
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

				if (ls.size() > 0) {

					docInvBean.setCreatedBy(rs.getString("CREATED_BY") + " - " + ls.get(0).getGenInf().getName() + " "
							+ ls.get(0).getGenInf().getLastName() + " - " + format.format(rs.getTimestamp("DCREATED")));
				} else {
					docInvBean.setCreatedBy(rs.getString("CREATED_BY") + " - " + format.format(rs.getTimestamp("DCREATED")));
				}

				user.getEntity().setIdentyId(rs.getString("MODIFIED_BY"));
				ls = new ArrayList<>();
				ls.add(user);
				ls = ume.getUsersLDAPByCredentials(ls);

				if (ls.size() > 0) {

					docInvBean.setModifiedBy(rs.getString("MODIFIED_BY") + " - " + ls.get(0).getGenInf().getName() + " "
							+ ls.get(0).getGenInf().getLastName() + " - " + format.format(rs.getTimestamp("DMODIFIED")));
				} else {
					docInvBean.setModifiedBy(rs.getString("MODIFIED_BY") + " - " + format.format(rs.getTimestamp("DMODIFIED")));
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

	public Response<List<DocInvBean>> getOnlyDocInv(DocInvBean docInvBean, String searchFilter) {
		Response<List<DocInvBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<DocInvBean> listDocInv = new ArrayList<>();
		 
		String INV_VW_DOC_INV = "SELECT DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE, STATUS, JUSTIFICATION FROM INV_VW_DOC_INV WITH(NOLOCK)  WHERE DOC_FATHER_INV_ID IS NULL ";		
		INV_VW_DOC_INV += "AND TYPE != '3' AND (DOC_INV_ID LIKE '%" + searchFilter + "%' OR ROUTE_ID LIKE '%";
		INV_VW_DOC_INV += searchFilter + "%' OR BDESC LIKE '%" + searchFilter + "%' " + " OR WERKSD LIKE '% ";
		INV_VW_DOC_INV += searchFilter + "%') ";
		INV_VW_DOC_INV += docInvBean.getBukrs() != null ? ("AND BUKRS = '" + docInvBean.getBukrs() +"' AND WERKS = '" + docInvBean.getWerks() + "'") : "";
		
		log.info(INV_VW_DOC_INV);
		INV_VW_DOC_INV += " GROUP BY DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE, STATUS, JUSTIFICATION";
		log.info("[getOnlyDocInv] Preparing sentence...");

		try {
			stm = con.prepareCall(INV_VW_DOC_INV);
			log.info("[getOnlyDocInv] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {

				docInvBean = new DocInvBean();
				docInvBean.setRoute(rs.getString("ROUTE_ID"));
				docInvBean.setDocInvId(rs.getInt("DOC_INV_ID"));
				docInvBean.setBukrs(rs.getString("BUKRS"));
				docInvBean.setBukrsD(rs.getString("BDESC"));
				docInvBean.setWerks(rs.getString("WERKS"));
				docInvBean.setWerksD(rs.getString("WERKSD"));
				docInvBean.setType(rs.getString("TYPE"));
				docInvBean.setStatus(rs.getString("STATUS"));

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

			log.info("[getOnlyDocInv] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getOnlyDocInv] Some error occurred while was trying to execute the query: " + INV_VW_DOC_INV, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getOnlyDocInv] Some error occurred while was trying to close the connection.",
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

		log.info(INV_VW_DOC_INV);
		INV_VW_DOC_INV += " GROUP BY DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE, STATUS, JUSTIFICATION";
		log.info("[getDocInvDao] Preparing sentence...");

		try {
			stm = con.prepareCall(INV_VW_DOC_INV);
			log.info("[getDocInvDao] Executing query...");
			ResultSet rs = stm.executeQuery();

			if (rs.next()) {
				docInvBean = new DocInvBean();
				docInvBean.setRoute(rs.getString("ROUTE_ID"));
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

		DOC_INV_ID = (docInvB.getDocInvId() != null ? " AND DOC_INV_ID = '" + docInvB.getDocInvId() + "' " : "");
		condition += DOC_INV_ID;
		ROUTE_ID = (docInvB.getRoute() != null ? " AND ROUTE_ID = '" + docInvB.getRoute() + "' " : "");
		condition += ROUTE_ID;
		bukrs = (docInvB.getBukrs() != null ? " AND BUKRS = '" + docInvB.getBukrs() + "' " : "");
		condition += bukrs;
		werks = (docInvB.getWerks() != null ? " AND WERKS = '" + docInvB.getWerks() + "' " : "");
		condition += werks;
		condition = condition.isEmpty() ? null : condition;
		return condition;
	}

	private static final String INSERT_DOCUMENT_INVENTORY_POSITIONS = "INSERT INTO INV_DOC_INVENTORY_POSITIONS "
			+ " (DIP_DOC_INV_ID,DIP_LGORT,DIP_LGTYP,DIP_LGPLA,DIP_MATNR,DIP_COUNTED,DIP_COUNT_DATE_INI,DIP_COUNT_DATE) VALUES (?,?,?,?,?,?,?,?)";

	public AbstractResultsBean addDocumentPosition(List<DocInvPositionBean> positionBean, Connection con)
			throws SQLException {
		AbstractResultsBean resultBean = new AbstractResultsBean();
		PreparedStatement stm = con.prepareStatement(INSERT_DOCUMENT_INVENTORY_POSITIONS);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		for (DocInvPositionBean singleBean : positionBean) {
			stm.setInt(1, singleBean.getDocInvId());
			stm.setString(2, singleBean.getLgort());
			stm.setString(3, singleBean.getLgtyp());
			stm.setString(4, singleBean.getLgpla());
			stm.setString(5, singleBean.getMatnr());
			stm.setString(6, singleBean.getCounted());
			if(singleBean.getDateIni() == null){
				stm.setNull(7,Types.TIMESTAMP);
			}else{
				
				try {
					stm.setTimestamp(7, new java.sql.Timestamp(sdf.parse(singleBean.getDateIni()).getTime()));
				} catch (ParseException e) {
					stm.setNull(7,Types.TIMESTAMP);
					e.printStackTrace();
				}
			}
			
			if(singleBean.getDateEnd() == null){
				stm.setNull(8,Types.TIMESTAMP);
			}else{
				try {
					stm.setTimestamp(8, new java.sql.Timestamp(sdf.parse(singleBean.getDateEnd()).getTime()));
				} catch (ParseException e) {
					stm.setNull(8,Types.TIMESTAMP);
					e.printStackTrace();
				}
			}
			stm.addBatch();
		}
		stm.executeBatch();
		return resultBean;
	}

	public Response<List<DocInvBean>> getOnlyDocInvNoTask(DocInvBean docInvBean, String searchFilter) {

		Response<List<DocInvBean>> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		List<DocInvBean> listDocInv = new ArrayList<>();
		 
		String INV_VW_DOC_INV = "SELECT DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE, STATUS, JUSTIFICATION FROM INV_VW_DOC_INV WITH(NOLOCK)  WHERE DOC_FATHER_INV_ID IS NULL ";		
		INV_VW_DOC_INV += "AND TYPE != '3' AND DOC_INV_ID NOT IN (SELECT DISTINCT TAS_DOC_INV_ID FROM INV_TASK WITH (NOLOCK))"
				+ " AND (DOC_INV_ID LIKE '%" + searchFilter + "%' OR ROUTE_ID LIKE '%";
		INV_VW_DOC_INV += searchFilter + "%' OR BDESC LIKE '%" + searchFilter + "%' " + " OR WERKSD LIKE '% ";
		INV_VW_DOC_INV += searchFilter + "%') ";
		INV_VW_DOC_INV += docInvBean.getBukrs() != null ? ("AND BUKRS = '" + docInvBean.getBukrs() +"' AND WERKS = '" + docInvBean.getWerks() + "'") : "";
		
		log.info(INV_VW_DOC_INV);
		INV_VW_DOC_INV += " GROUP BY DOC_INV_ID, ROUTE_ID, BUKRS, BDESC, WERKS, WERKSD, TYPE, STATUS, JUSTIFICATION";
		log.info("[getOnlyDocInvNoTaskDao] Preparing sentence...");

		try {
			stm = con.prepareCall(INV_VW_DOC_INV);
			log.info("[getOnlyDocInvNoTaskDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {

				docInvBean = new DocInvBean();
				docInvBean.setRoute(rs.getString("ROUTE_ID"));
				docInvBean.setDocInvId(rs.getInt("DOC_INV_ID"));
				docInvBean.setBukrs(rs.getString("BUKRS"));
				docInvBean.setBukrsD(rs.getString("BDESC"));
				docInvBean.setWerks(rs.getString("WERKS"));
				docInvBean.setWerksD(rs.getString("WERKSD"));
				docInvBean.setType(rs.getString("TYPE"));
				docInvBean.setStatus(rs.getString("STATUS"));

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

			log.info("[getOnlyDocInvNoTaskDao] Sentence successfully executed.");

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getOnlyDocInvNoTaskDao] Some error occurred while was trying to execute the query: " + INV_VW_DOC_INV, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getOnlyDocInvNoTaskDao] Some error occurred while was trying to close the connection.",
						e);
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listDocInv);
		return res;
	
	}

}
