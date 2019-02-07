package com.gmodelo.cyclicinventories.dao;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.DocInvBeanHeaderSAP;
import com.gmodelo.cyclicinventories.beans.E_Error_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Lqua_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mard_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mbew_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Mseg_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Msku_SapEntity;
import com.gmodelo.cyclicinventories.beans.E_Xtab6_SapEntity;
import com.gmodelo.cyclicinventories.beans.Justification;
import com.gmodelo.cyclicinventories.beans.PosDocInvBean;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.beans.ZIACST_I360_OBJECTDATA_SapEntity;
import com.gmodelo.cyclicinventories.exception.InvCicException;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_EXT_SIS_CLAS;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_INV_MOV_1;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_INV_MOV_2;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_INV_MOV_3;
import com.gmodelo.cyclicinventories.structure.ZIACMF_MBEW;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class SapConciliationDao {

	private static final String ZIACMF_I360_INV_MOV_1 = "ZIACMF_I360_INV_MOV_1";
	private static final String ZIACMF_I360_INV_MOV_2 = "ZIACMF_I360_INV_MOV_2";
	private static final String ZIACMF_I360_INV_MOV_3 = "ZIACMF_I360_INV_MOV_3";
	private static final String ZIACMF_I360_EXT_SIS_CLAS = "ZIACMF_I360_EXT_SIS_CLAS";
	private static final String ZIACMF_I360_MBEW = "ZIACMF_MBEW";
	private Logger log = Logger.getLogger(SapConciliationDao.class.getName());
	private final SapOperationDao operationDao = new SapOperationDao();

	public Response<DocInvBeanHeaderSAP> saveConciliationSAP(DocInvBeanHeaderSAP dibhSAP, String userId) {

		Response<DocInvBeanHeaderSAP> resp = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		List<PosDocInvBean> lsPositionBean = dibhSAP.getDocInvPosition();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		CallableStatement csBatch = null;

		String CURRENTSP = "";
		final String INV_SP_ADD_CON_POS_SAP = "INV_SP_ADD_CON_POS_SAP ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
		final String INV_SP_ADD_JUSTIFY = "INV_SP_ADD_JUSTIFY ?, ?, ?, ?, ?";
		final String INV_CLS_SAP_DOC_INV = "INV_CLS_SAP_DOC_INV ?, ?";
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		try {
			con.setAutoCommit(false);
			csBatch = con.prepareCall(INV_SP_ADD_CON_POS_SAP);

			for (PosDocInvBean dipb : lsPositionBean) {

				CURRENTSP = INV_SP_ADD_CON_POS_SAP;

				if (!dipb.getLsJustification().isEmpty()) {

					cs = null;
					cs = con.prepareCall(INV_SP_ADD_CON_POS_SAP);

					cs.setInt(1, dipb.getDoncInvId());
					cs.setString(2, dipb.getLgort());
					cs.setString(3, dipb.getImwmMarker());
					cs.setString(4, dipb.getLgtyp());
					cs.setString(5, dipb.getLgpla());
					cs.setString(6, dipb.getMatnr());
					cs.setString(7, dipb.getCostByUnit());
					cs.setString(8, dipb.getMeins());
					cs.setString(9, dipb.getCounted());
					cs.setString(10, dipb.getCountedExpl());
					cs.setString(11, dipb.getTheoric());
					cs.setString(12, dipb.getDiff());
					cs.setString(13, dipb.getConsignation());
					cs.setString(14, dipb.getTransit());
					cs.setByte(15, (byte)(dipb.isExplosion()?1:0));
					cs.setString(16, userId);
					cs.registerOutParameter(17, Types.BIGINT);

					cs.execute();

					log.info("[saveConciliationSAP] Sentence successfully executed. " + CURRENTSP);

					dipb.setPosId(cs.getInt(17));
					CURRENTSP = INV_SP_ADD_JUSTIFY;

					ArrayList<Justification> lsJustification = dipb.getLsJustification();

					// Insert the justification per position
					for (Justification js : lsJustification) {

						cs = null;
						cs = con.prepareCall(INV_SP_ADD_JUSTIFY);
						cs.setLong(1, dipb.getPosId());
						cs.setString(2, js.getQuantity());
						cs.setInt(3, js.getJsId());
						cs.setString(4, js.getFileName());
						cs.registerOutParameter(5, Types.BIGINT);
						cs.execute();

						js.setJsId(cs.getInt(5));
						
						log.info("[saveConciliationSAP] Sentence successfully executed. " + CURRENTSP);
					}

				} else {

					csBatch.setInt(1, dipb.getDoncInvId());
					csBatch.setString(2, dipb.getLgort());
					csBatch.setString(3, dipb.getImwmMarker());
					csBatch.setString(4, dipb.getLgtyp());
					csBatch.setString(5, dipb.getLgpla());
					csBatch.setString(6, dipb.getMatnr());
					csBatch.setString(7, dipb.getCostByUnit());
					csBatch.setString(8, dipb.getMeins());
					csBatch.setString(9, dipb.getCounted());
					csBatch.setString(10, dipb.getCountedExpl());
					csBatch.setString(11, dipb.getTheoric());
					csBatch.setString(12, dipb.getDiff());
					csBatch.setString(13, dipb.getConsignation());
					csBatch.setString(14, dipb.getTransit());
					csBatch.setByte(15, (byte)(dipb.isExplosion()?1:0));
					csBatch.setString(16, userId);
					csBatch.setNull(17, Types.NULL);
					csBatch.addBatch();
				}
			}
			
			log.info("[saveConciliationSAP] Sentence successfully executed.");
			csBatch.executeBatch();

			cs = null;
			cs = con.prepareCall(INV_CLS_SAP_DOC_INV);
			cs.setInt(1, dibhSAP.getDocInvId());
			cs.setString(2, userId);
			cs.execute();
			
			UMEDaoE ume = new UMEDaoE();
			User user = new User();
			user.getEntity().setIdentyId(userId);
			ArrayList<User> ls = new ArrayList<>();
			ls.add(user);
			ls = ume.getUsersLDAPByCredentials(ls);

			if (ls.size() > 0) {

				dibhSAP.setCreatedBy(userId + " - " + ls.get(0).getGenInf().getName() + " "
						+ ls.get(0).getGenInf().getLastName() + " - " + format.format(new Date()));
			} else {
				dibhSAP.setCreatedBy(userId + " - " + format.format(new Date()));
			}
			
			con.commit();
			con.setAutoCommit(true);
			cs.close();

			log.info("[saveConciliationSAP] Executing query...");

		} catch (Exception e) {

			try {
				log.log(Level.WARNING, "[saveConciliationSAP] Execute rollback");
				con.rollback();
			} catch (SQLException e1) {
				log.log(Level.SEVERE, "[saveConciliationSAP] Not rollback .", e);
			}

			log.log(Level.SEVERE,
					"[saveConciliationSAP] Some error occurred while was trying to execute the S.P.: " + CURRENTSP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			resp.setAbstractResult(abstractResult);
			return resp;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[saveConciliationSAP] Some error occurred while was trying to close the connection.", e);
			}
		}

		resp.setAbstractResult(abstractResult);
		resp.setLsObject(dibhSAP);
		return resp;

	}
	
	public Response<Object> deleteConciliationSAP(int docInvId){
		
		Response<Object> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		CallableStatement cs = null;
		
		final String SP = "INV_SP_DEL_CONS_SAP ?"; //The Store procedure to call
		
		log.info("[deleteConciliationSAP] Preparing sentence...");
		
		try {
			
			cs = con.prepareCall(SP);			
			cs.setInt(1, docInvId);
			
			log.log(Level.WARNING,"[deleteConciliationSAP] Executing query...");
			
			cs.execute();
			
			abstractResult.setResultId(ReturnValues.ISUCCESS);
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,"[deleteConciliationSAP] "+warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			cs.close();	
			
			log.info("[deleteConciliationSAP] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[deleteConciliationSAP] Some error occurred while was trying to execute the S.P.: "+ SP, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[deleteConciliationSAP] Some error occurred while was trying to close the connection.", e);
			}
		}
		res.setAbstractResult(abstractResult);
		return res ;
	}

	private static final String INV_VW_REP_HEADER = "SELECT DOC_INV_ID, DIH_BUKRS, BUTXT, DIH_WERKS, NAME1, DIH_TYPE, "
			+ "DIH_CLSD_SAP_BY, DIH_CLSD_SAP_DATE, DIH_ROUTE_ID, ROU_DESC, DIH_CREATED_DATE, DIH_MODIFIED_DATE "
			+ "FROM INV_VW_DOC_INV_REP_HEADER WHERE DOC_INV_ID = ?";

	public Response<DocInvBeanHeaderSAP> getClosedConsSapReport(DocInvBean docInvBean) {
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		DocInvBeanHeaderSAP bean = new DocInvBeanHeaderSAP();
		Response<DocInvBeanHeaderSAP> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		log.info(INV_VW_REP_HEADER);
		log.info("[getReporteDocInvDao] Preparing sentence...");

		try {
			stm = con.prepareStatement(INV_VW_REP_HEADER);
			stm.setInt(1, docInvBean.getDocInvId());
			log.info("[getReporteDocInvDao] Executing query...");
			ResultSet rs = stm.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss");

			if (rs.next()) {

				bean.setDocInvId(docInvBean.getDocInvId());
				bean.setBukrs(rs.getString("DIH_BUKRS"));
				bean.setBukrsD(rs.getString("BUTXT"));
				bean.setRoute(rs.getString("ROU_DESC"));
				bean.setWerks(rs.getString("DIH_WERKS"));
				bean.setWerksD(rs.getString("NAME1"));
				bean.setType(rs.getString("DIH_TYPE"));
				bean.setCreationDate(sdf.format(new Date(rs.getTimestamp("DIH_CREATED_DATE").getTime())));
				bean.setConciliationDate(sdf.format(new Date(rs.getTimestamp("DIH_MODIFIED_DATE").getTime())));
				bean.setDocInvPosition(getConciliationSAPPositions(bean.getDocInvId(), con));
				bean.setCreatedBy(rs.getString("DIH_CLSD_SAP_BY"));
				bean.setConcSAPDate(sdf.format(new Date(rs.getTimestamp("DIH_CLSD_SAP_DATE").getTime())));

				User user = new User();
				UMEDaoE ume = new UMEDaoE();
				user.getEntity().setIdentyId(bean.getCreatedBy());
				ArrayList<User> ls = new ArrayList<>();
				ls.add(user);
				ls = ume.getUsersLDAPByCredentials(ls);
				bean.setCreatedBy(bean.getCreatedBy() + " - " + ls.get(0).getGenInf().getName() + " "
						+ ls.get(0).getGenInf().getLastName() + " - " + bean.getConcSAPDate());

			}
		} catch (SQLException | NamingException e) {
			log.log(Level.SEVERE, "[getClosedConsSap] Some error occurred while was trying to execute the query: "
					+ INV_VW_REP_HEADER, e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,
						"[getClosedConsSap] Some error occurred while was trying to close the connection.", e);
			}
		}

		res.setAbstractResult(abstractResult);
		res.setLsObject(bean);

		return res;
	}

	private static final String GET_POS_CONS_SAP = "SELECT CS_CON_SAP, CS_MATNR, ISNULL(CATEGORY, '') CATEGORY, MAKTX, MEINS, CS_COST_BY_UNIT, CS_THEORIC, " 
			+ "CS_COUNTED, CS_COUNTED_EXPL, CS_DIFFERENCE, CS_TRANSIT, CS_CONSIGNATION, CS_IS_EXPL " 
			+ "FROM INV_VW_CONC_SAP AS A "
			+ "LEFT JOIN INV_REL_CAT_MAT AS B ON (A.CS_MATNR = B.REL_MATNR) "
			+ "LEFT JOIN INV_CAT_CATEGORY AS C ON (B.REL_CAT_ID = C.CAT_ID) "
			+ "WHERE DOC_INV_ID = ? "
			+ "GROUP BY CS_CON_SAP, CS_MATNR, CATEGORY, MAKTX, MEINS, CS_COST_BY_UNIT, CS_THEORIC, " 
			+ "CS_COUNTED, CS_COUNTED_EXPL, CS_DIFFERENCE, CS_TRANSIT, CS_CONSIGNATION, CS_IS_EXPL";

	public ArrayList<PosDocInvBean> getConciliationSAPPositions(int docInvId, Connection con) throws SQLException {

		PreparedStatement ps = null;
		ResultSet rs;
		PosDocInvBean pdib;
		String lsPosIds = "";
		ArrayList<PosDocInvBean> lsPdib = new ArrayList<>();
		try {
			
			ps = con.prepareStatement(GET_POS_CONS_SAP);
			ps.setInt(1, docInvId);
			rs = ps.executeQuery();

			while (rs.next()) {

				pdib = new PosDocInvBean();
				pdib.setPosId(rs.getInt("CS_CON_SAP"));
				pdib.setMatnr(rs.getString("CS_MATNR"));
				pdib.setMatnrD(rs.getString("MAKTX"));
				pdib.setCategory(rs.getString("CATEGORY"));
				pdib.setMeins(rs.getString("MEINS"));
				pdib.setCostByUnit(rs.getString("CS_COST_BY_UNIT"));
				pdib.setTheoric(rs.getString("CS_THEORIC"));
				pdib.setCounted(rs.getString("CS_COUNTED"));
				pdib.setCountedExpl(rs.getString("CS_COUNTED_EXPL"));
				pdib.setDiff(rs.getString("CS_DIFFERENCE"));
				pdib.setTransit(rs.getString("CS_TRANSIT"));
				pdib.setConsignation(rs.getString("CS_CONSIGNATION"));
				pdib.setExplosion(rs.getBoolean("CS_IS_EXPL"));
				lsPosIds += pdib.getPosId() + ",";
				lsPdib.add(pdib);
			}

			// Get the justifications
			ArrayList<Justification> lsJustify = getJustification(lsPosIds, con);

			// Add the justification to positions
			for (PosDocInvBean obj : lsPdib) {

				for (Justification js : lsJustify) {

					if (js.getConsPosSAPId() == obj.getPosId()) {
						obj.getLsJustification().add(js);
					}
				}
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getConciliationSAPPositions] Some error occurred while was trying to retrive the positions.", e);
			throw e;
		}

		return lsPdib;
	}

	private static final String GET_JUSTIFICATION = "SELECT A.JS_ID, JS_CON_SAP, JS_QUANTITY, "
				+ "(CAST(A.JS_JUSTIFY AS varchar(200)) + ' - ' + B.JUSTIFICATION) JUSTIFICATION, JS_FILE_NAME " 
				+ "FROM INV_JUSTIFY AS A " 
				+ "INNER JOIN INV_CAT_JUSTIFY AS B ON (A.JS_JUSTIFY = B.JS_ID) "
				+ "WHERE JS_CON_SAP IN (SELECT * FROM STRING_SPLIT(?, ',')) "; 

	private ArrayList<Justification> getJustification(String ids, Connection con) throws SQLException {

		ArrayList<Justification> lsJustification = new ArrayList<>();
		Justification js;
		PreparedStatement stm = null;
		ResultSet rs;
		try {

			stm = con.prepareStatement(GET_JUSTIFICATION);
			stm.setString(1, ids);
			rs = stm.executeQuery();

			while (rs.next()) {

				js = new Justification();
				js.setJsId(rs.getInt("JS_ID"));
				js.setConsPosSAPId(rs.getInt("JS_CON_SAP"));
				js.setQuantity(rs.getString("JS_QUANTITY"));
				js.setJustify(rs.getString("JUSTIFICATION"));
				js.setFileName(rs.getString("JS_FILE_NAME"));
				lsJustification.add(js);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE,
					"[getJustification] Some error occurred while was trying to retrive the justifications.", e);
			throw e;
		}
		return lsJustification;
	}

	public ZIACMF_I360_INV_MOV_1 inventoryMovementsDao(DocInvBean docInvBean, Connection con,
			JCoDestination destination) throws JCoException, SQLException, RuntimeException, InvCicException {
		ZIACMF_I360_INV_MOV_1 ziacmf_I360_INV_MOV_1 = new ZIACMF_I360_INV_MOV_1();
		List<E_Mseg_SapEntity> msegList = new ArrayList<>();
		try {
			
			DocInvBean requestBean = operationDao.getDocInvBeanData(docInvBean, con);
			log.info(requestBean.toString());
			JCoFunction jcoFunction = destination.getRepository().getFunction(ZIACMF_I360_INV_MOV_1);
			jcoFunction.getImportParameterList().setValue("I_BUKRS", requestBean.getBukrs());
			jcoFunction.getImportParameterList().setValue("I_WERKS", requestBean.getWerks());
			jcoFunction.getImportParameterList().setValue("I_BUDAT_I", new Date(requestBean.getCreatedDate()));
			jcoFunction.getImportParameterList().setValue("I_BUDAT_F", new Date(requestBean.getModifiedDate()));
			jcoFunction.getImportParameterList().setValue("I_CPUTM_I", new Date(requestBean.getCreatedDate()));
			jcoFunction.getImportParameterList().setValue("I_CPUTM_F", new Date(requestBean.getModifiedDate()));
			// Form the Current Structure
			JCoTable lgortTable = jcoFunction.getImportParameterList().getTable("I_R_LGORT");
			for (String lgort : operationDao.getDocInvLgort(requestBean, con)) {
				lgortTable.appendRow();
				lgortTable.setValue("SIGN", "I");
				lgortTable.setValue("OPTION", "EQ");
				lgortTable.setValue("LOW", lgort);
			}
			jcoFunction.execute(destination);
			JCoTable jcoE_MsegTable = jcoFunction.getExportParameterList().getTable("E_MSEG");
			JCoTable jcoE_Error = jcoFunction.getExportParameterList().getTable("E_ERROR");
			E_Error_SapEntity eError = new E_Error_SapEntity();
			try {
				eError = new E_Error_SapEntity(jcoE_Error);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				log.warning("SapConciliationDao: [ZIACMF_I360_INV_MOV_1]:" + e1.getMessage());
			}
			if (eError.getType().equals("S")) {
				do {

					try {
						msegList.add(new E_Mseg_SapEntity(jcoE_MsegTable));
					} catch (JCoException | RuntimeException e) {
						log.warning("SapConciliationDao: [ZIACMF_I360_INV_MOV_1]:" + e.getMessage());
						// Not Readable Row or EOF
					}
				} while (jcoE_MsegTable.nextRow());
			} else {
				log.severe("Regresó con E_ERROR " + eError.toString());
			}
			ziacmf_I360_INV_MOV_1.seteError_SapEntities(eError);
			ziacmf_I360_INV_MOV_1.seteMseg_SapEntities(msegList);
		} catch (JCoException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		}
		return ziacmf_I360_INV_MOV_1;
	}

	public ZIACMF_I360_INV_MOV_2 getSystemSnapshot(DocInvBean docInvBean, Connection con, JCoDestination destination)
			throws JCoException, SQLException, RuntimeException, InvCicException {
		ZIACMF_I360_INV_MOV_2 ziacmf_I360_INV_MOV_2 = new ZIACMF_I360_INV_MOV_2();
		try {
			List<E_Mard_SapEntity> eMard_SapEntities = new ArrayList<>();
			List<E_Msku_SapEntity> eMsku_SapEntities = new ArrayList<>();
			List<E_Lqua_SapEntity> eLqua_SapEntities = new ArrayList<>();
			DocInvBean requestBean = operationDao.getDocInvBeanData(docInvBean, con);
			JCoFunction jcoFunction = destination.getRepository().getFunction(ZIACMF_I360_INV_MOV_2);
			jcoFunction.getImportParameterList().setValue("I_WERKS", requestBean.getWerks());
			JCoTable lgortTable = jcoFunction.getImportParameterList().getTable("I_R_LGORT");
			for (String lgort : operationDao.getDocInvLgort(requestBean, con)) {
				lgortTable.appendRow();
				lgortTable.setValue("SIGN", "I");
				lgortTable.setValue("OPTION", "EQ");
				lgortTable.setValue("LOW", lgort);
			}
			JCoTable lgnumTable = jcoFunction.getImportParameterList().getTable("I_R_LGNUM");
			HashMap<String, List<String>> lgnumLgtypMap = operationDao.getDocInvLgnumLgtyp(requestBean, con);
			if (lgnumLgtypMap.get("LGNUM") != null && !lgnumLgtypMap.get("LGNUM").isEmpty()) {
				for (String lgnum : lgnumLgtypMap.get("LGNUM")) {
					lgnumTable.appendRow();
					lgnumTable.setValue("SIGN", "I");
					lgnumTable.setValue("OPTION", "EQ");
					lgnumTable.setValue("LOW", lgnum);
					// lgnumTable.setValue("High", lgnum);
				}
			}
			JCoTable lgtypTable = jcoFunction.getImportParameterList().getTable("I_R_LGTYP");
			if (lgnumLgtypMap.get("LGTYP") != null && !lgnumLgtypMap.get("LGTYP").isEmpty()) {
				for (String lgtyp : lgnumLgtypMap.get("LGTYP")) {
					lgtypTable.appendRow();
					lgtypTable.setValue("SIGN", "I");
					lgtypTable.setValue("OPTION", "EQ");
					lgtypTable.setValue("LOW", lgtyp);
				}
			}

			jcoFunction.execute(destination);
			JCoTable E_MARD = jcoFunction.getExportParameterList().getTable("E_MARD");
			JCoTable E_MSKU = jcoFunction.getExportParameterList().getTable("E_MSKU");
			JCoTable E_LQUA = jcoFunction.getExportParameterList().getTable("E_LQUA");
			JCoTable E_ERROR = jcoFunction.getExportParameterList().getTable("E_ERROR");
			E_Error_SapEntity eError = new E_Error_SapEntity();
			try {
				eError = new E_Error_SapEntity(E_ERROR);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				log.warning("SapConciliationDao: [ZIACMF_I360_INV_MOV_2]: " + e1.getMessage());
			}
			if (eError.getType().equals("S")) {
				// Cycle of the E_MARD Export Table
				do {
					try {
						eMard_SapEntities.add(new E_Mard_SapEntity(E_MARD));
					} catch (JCoException | RuntimeException e) {
						// Not Readable Row or EOF
						log.warning("SapConciliationDao: [ZIACMF_I360_INV_MOV_2]: " + e.getMessage());
					}
				} while (E_MARD.nextRow());

				// Cycle of the E_MSKU Export Table
				do {
					try {
						eMsku_SapEntities.add(new E_Msku_SapEntity(E_MSKU));
					} catch (JCoException | RuntimeException e) {
						// Not Readable Row or EOF
						log.warning("SapConciliationDao: [ZIACMF_I360_INV_MOV_2]: " + e.getMessage());
					}
				} while (E_MSKU.nextRow());

				// Cycle of the E_LQUA Export Table
				do {
					try {
						eLqua_SapEntities.add(new E_Lqua_SapEntity(E_LQUA));
					} catch (JCoException | RuntimeException e) {
						// Not Readable Row or EOF
						log.warning("SapConciliationDao: [ZIACMF_I360_INV_MOV_2]: " + e.getMessage());
					}
				} while (E_LQUA.nextRow());
			}
			ziacmf_I360_INV_MOV_2.seteMard_SapEntities(eMard_SapEntities);
			ziacmf_I360_INV_MOV_2.seteMsku_SapEntities(eMsku_SapEntities);
			ziacmf_I360_INV_MOV_2.seteLqua_SapEntities(eLqua_SapEntities);
			ziacmf_I360_INV_MOV_2.seteError_SapEntities(eError);
		} catch (JCoException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		}
		return ziacmf_I360_INV_MOV_2;
	}

	public ZIACMF_I360_INV_MOV_3 getTransitMovements(DocInvBean docInvBean, Connection con, JCoDestination destination)
			throws JCoException, SQLException, RuntimeException, InvCicException {
		ZIACMF_I360_INV_MOV_3 ziacmf_I360_INV_MOV_3 = new ZIACMF_I360_INV_MOV_3();
		try {
			List<E_Xtab6_SapEntity> xtab6_SapEntities = new ArrayList<>();
			JCoFunction jcoFunction = destination.getRepository().getFunction(ZIACMF_I360_INV_MOV_3);
			DocInvBean requestBean = operationDao.getDocInvBeanData(docInvBean, con);
			JCoTable materialTable = jcoFunction.getImportParameterList().getTable("I_R_MATNR");
			for (String material : operationDao.getmaterialForDocInv(requestBean, con)) {
				materialTable.appendRow();
				materialTable.setValue("SIGN", "I");
				materialTable.setValue("OPTION", "EQ");
				try {
					materialTable.setValue("LOW", String.format("%018d", Integer.parseInt(material)));
				} catch (Exception e) {
					materialTable.setValue("LOW", material);
				}
			}
			JCoTable werksTable = jcoFunction.getImportParameterList().getTable("I_R_WERKS");
			werksTable.appendRow();
			werksTable.setValue("SIGN", "I");
			werksTable.setValue("OPTION", "EQ");
			werksTable.setValue("LOW", requestBean.getWerks());

			jcoFunction.execute(destination);
			JCoTable E_XTAB6 = jcoFunction.getExportParameterList().getTable("E_XTAB6");
			JCoTable E_ERROR = jcoFunction.getExportParameterList().getTable("E_ERROR");
			E_Error_SapEntity eError = new E_Error_SapEntity();
			try {
				eError = new E_Error_SapEntity(E_ERROR);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				log.warning("SapConciliationDao: [ZIACMF_I360_INV_MOV_3]: " + e1.getMessage());
			}
			if (eError.getType().equals("S")) {
				do {
					try {
						xtab6_SapEntities.add(new E_Xtab6_SapEntity(E_XTAB6));
					} catch (JCoException | RuntimeException e) {
						log.warning("SapConciliationDao: [ZIACMF_I360_INV_MOV_3]: " + e.getMessage());
						// Not Readable Row or EOF
					}
				} while (E_XTAB6.nextRow());
			}
			ziacmf_I360_INV_MOV_3.setXtab6_SapEntities(xtab6_SapEntities);
			ziacmf_I360_INV_MOV_3.seteError_SapEntities(eError);
		} catch (JCoException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		}
		return ziacmf_I360_INV_MOV_3;
	}

	public ZIACMF_MBEW getCostByMaterial(Connection con, JCoDestination destination, List<String> materialList)
			throws JCoException, RuntimeException, InvCicException {
		ZIACMF_MBEW ziacmf_MBEW = new ZIACMF_MBEW();
		try {
			List<E_Mbew_SapEntity> mbewData = new ArrayList<>();
			JCoFunction jcoFunction = destination.getRepository().getFunction(ZIACMF_I360_MBEW);
			JCoTable i_matnr = jcoFunction.getImportParameterList().getTable("I_MATNR");
			if (materialList != null && !materialList.isEmpty()) {

				for (String materialS : materialList) {
					i_matnr.appendRow();
					i_matnr.setValue("SIGN", "I");
					i_matnr.setValue("OPTION", "EQ");
					try {
						i_matnr.setValue("LOW", String.format("%018d", Integer.parseInt(materialS)));
					} catch (Exception e) {
						i_matnr.setValue("LOW", materialS);
					}

				}
			}
			jcoFunction.execute(destination);
			JCoTable E_MBEW = jcoFunction.getExportParameterList().getTable("E_MBEW");
			JCoTable E_ERROR = jcoFunction.getExportParameterList().getTable("E_ERROR");
			E_Error_SapEntity eError = new E_Error_SapEntity();
			try {
				eError = new E_Error_SapEntity(E_ERROR);
			} catch (Exception e1) {
				log.warning("SapConciliationDao: [getCostByMaterial]: " + e1.getMessage());
			}
			if (eError.getType().equals("S")) {
				do {
					try {
						mbewData.add(new E_Mbew_SapEntity(E_MBEW));
					} catch (JCoException | RuntimeException e) {
						log.warning("SapConciliationDao: [getCostByMaterial]: " + e.getMessage());
					}
				} while (E_MBEW.nextRow());
			}
			ziacmf_MBEW.seteError_SapEntities(eError);
			ziacmf_MBEW.seteMbewSapEntities(mbewData);
		} catch (JCoException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		}
		return ziacmf_MBEW;

	}

	public ZIACMF_I360_EXT_SIS_CLAS getClassSystem(Connection con, JCoDestination destination,
			List<String> materialList, String booleanDelta, String dateDelta)
			throws JCoException, RuntimeException, InvCicException {
		ZIACMF_I360_EXT_SIS_CLAS ziacmf_I360_EXT_SIS_CLAS = new ZIACMF_I360_EXT_SIS_CLAS();
		try {
			List<ZIACST_I360_OBJECTDATA_SapEntity> objectData = new ArrayList<>();
			JCoFunction jcoFunction = destination.getRepository().getFunction(ZIACMF_I360_EXT_SIS_CLAS);
			if (materialList != null && !materialList.isEmpty()) {
				JCoTable i_object = jcoFunction.getImportParameterList().getTable("I_OBJECT");
				for (String materialS : materialList) {
					i_object.appendRow();
					i_object.setValue("OBJEK", materialS);
				}
			} else {
				jcoFunction.getImportParameterList().setValue("I_DELTA", booleanDelta);
				if (dateDelta != null && !dateDelta.isEmpty()) {
					jcoFunction.getImportParameterList().setValue("I_F_DELTA", dateDelta);
				}
			}
			jcoFunction.execute(destination);
			JCoTable objectTable = jcoFunction.getExportParameterList().getTable("E_OBJECTDATA");
			do {
				objectData.add(new ZIACST_I360_OBJECTDATA_SapEntity(objectTable));
			} while (objectTable.nextRow());
			ziacmf_I360_EXT_SIS_CLAS.setObjectData(objectData);
		} catch (JCoException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		}
		return ziacmf_I360_EXT_SIS_CLAS;
	}

}
