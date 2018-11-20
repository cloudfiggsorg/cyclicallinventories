package com.gmodelo.workservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bmore.ume001.beans.Role;
import com.bmore.ume001.beans.User;
import com.gmodelo.Exception.InvCicException;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.LoginBean;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.UMEDaoE;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;
import com.gmodelo.utils.Utilities;

public class LoginWorkService {

	// private Logger log = Logger.getLogger(LoginWorkService.class.getName());
	private java.util.logging.Logger myLog = java.util.logging.Logger.getLogger(LoginWorkService.class.getName());

	public Response login(LoginBean loginBean, HttpServletRequest request, HttpSession session) {
		
		Response<LoginBean> resp = new Response<LoginBean>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();

		// LoginBean loginBean = gson.fromJson(login, LoginBean.class);

		if (loginBean.getLoginId() == null || loginBean.getLoginId().isEmpty() || loginBean.getLoginPass().isEmpty()
				|| loginBean.getLoginPass() == null) {

			myLog.log(Level.SEVERE, "[login] Missing data");

			abstractResult.setResultId(-1);
			abstractResult.setResultMsgAbs("SOME ERROR OCCURED...");
			resp.setAbstractResult(abstractResult);
			return resp;
		}

		// Check credentials to create a session
		if (request.getSession().getAttribute("user") == null) {

			User user = new User();
			UMEDaoE apiUME = new UMEDaoE();
//			
			user.getEntity().setIdentyId(loginBean.getLoginId().trim());
			user.getAccInf().setPassword(loginBean.getLoginPass().trim());
			
			myLog.info("[login] Check if user exists in UME");
			// Check if user exists on UME or LDAP
			try {
				user = apiUME.checkUserUME(user);
				abstractResult.setResultId(ReturnValues.ISUCCESS);
			} catch (Exception e1) {
				user = null;
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				myLog.log(Level.SEVERE, "Error al verificar usuario en UME", e1);
			}
			if(user == null){ // si descomentan  UME (arriba) if va -> if(user == null) si no va en true
				user = new User();
				try {
					myLog.info("[login] Check if user exists on LDAP");
					
					user.getEntity().setIdentyId(loginBean.getLoginId().trim());
					user.getAccInf().setPassword(loginBean.getLoginPass().trim());
					user = apiUME.checkUserLDAP(user);
					abstractResult.setResultId(ReturnValues.ISUCCESS);
				} catch (NamingException e) {
					user = null;
					abstractResult.setResultId(ReturnValues.IEXCEPTION);
					if(e.toString().contains("Connection timed out")){
						abstractResult.setResultId(ReturnValues.ILDAPTIMEOUT);
						abstractResult.setResultMsgAbs(ReturnValues.SLDAPTIMEOUT);
					}
					if(e.getMessage().contains("AcceptSecurityContext")){
						abstractResult.setResultId(ReturnValues.IPASSWORDNOTMATCH);
						abstractResult.setResultMsgAbs(ReturnValues.SINVALIDUSER);
					}
					
					myLog.log(Level.SEVERE, "Error al verificar usuario en LDAP", e);
					myLog.log(Level.SEVERE, abstractResult.getResultMsgAbs(), e);
					resp.setAbstractResult(abstractResult);
					return resp;
				}
			}
			

			if (user != null && abstractResult.getResultId() == ReturnValues.ISUCCESS) {

				// Check if the account is locked
				if (user.getAccInf().getLockAcc() > 0) {
					abstractResult.setResultId(ReturnValues.ILOCKEDUSER);
					abstractResult.setResultMsgAbs(ReturnValues.SLOCKEDUSER);

				} else {

					// verify role to execute application
					ArrayList<Role> lsRoles = new ArrayList<Role>();
					lsRoles = apiUME.getUserRoles(loginBean.getLoginId().trim());
					ArrayList<String> lsRolesAux = new ArrayList<String>();
					Utilities iUtils = new Utilities();
					Connection con = new ConnectionManager().createConnection();
					String regEx = null;
					Pattern pattern = null;
					Matcher m; 
					boolean found = false;
					
					try {	
						
						regEx = "^" + iUtils.GetValueRepByKey(con, ReturnValues.ROLE_MASK).getStrCom1() + "$";		
						pattern = Pattern.compile(regEx);
						
					} catch (InvCicException e) {
						
						myLog.info("Error al intentar obtener la mascara para verificar bukrs y werks.");
					}finally{
						try {
							con.close();
						} catch (SQLException e) {
							myLog.info("Error al intentar cerrar la conexión de base datos...");
						}
					}						
					
					for (Role rol : lsRoles) {
						
						lsRolesAux.add(rol.getRolId().trim());						
						m = pattern.matcher(rol.getRolId().trim());
						
						//Get the bukrs and werks by role
						if(m.matches() && !found){
							
							user.setBukrs(m.group(1));
							user.setWerks(m.group(2));
							found = true;
						}
					}
					
											
					if (!lsRolesAux.contains("EXECUTE_INV_CIC_APP")) {
						abstractResult.setResultId(ReturnValues.INOTROLE);
						abstractResult.setResultMsgAbs(ReturnValues.SNOTROLE);
						resp.setAbstractResult(abstractResult);
							return resp;
					}
						
					
					if(!lsRolesAux.contains("INV_CIC_ADMIN")){
					
						if(!found){
						
							abstractResult.setResultId(ReturnValues.IMISSING_BUKRS_OR_WERKS);
							abstractResult.setResultMsgAbs(ReturnValues.SMISSING_BUKRS_OR_WERKS);
							resp.setAbstractResult(abstractResult);

							return resp;
						}	
					}	
					
					user.getEntity().setIdentyId(user.getEntity().getIdentyId().toUpperCase());
					
					myLog.info(user.getEntity().getIdentyId().toUpperCase());
					myLog.info(user.getBukrs());
					myLog.info(user.getWerks());
					
					session = request.getSession(true);
					session.setAttribute("user", user);// Set the idUser on														// session
					session.setAttribute("roles", lsRolesAux);

					myLog.info("[login] Loggin success for: " + user.getEntity().getIdentyId());
					// session.setMaxInactiveInterval(ReturnValues.ACTIVE_INTERVAL);
					abstractResult.setIntCom1(1 * 60);
					loginBean.setActiveInterval(ReturnValues.ACTIVE_INTERVAL);
					loginBean.setLoginId(null);
					loginBean.setLoginPass(null);
					loginBean.setRelationUUID(session.getId());
					myLog.info(loginBean.getRelationUUID());
					loginBean.setLsObjectLB(user);
					resp.setLsObject(loginBean);
				}

			} else {
				myLog.info(abstractResult.getResultMsgAbs());
			}
		} else {
			myLog.info("[login] Session already created");
			abstractResult.setResultId(ReturnValues.ISESSIONCREATED);
			abstractResult.setResultMsgAbs("SESSION ALREADY CREATED");
		}

		resp.setAbstractResult(abstractResult);
		return resp;

	}

	public Response<Object> logout(HttpServletRequest request) {

		Response<Object> resp = new Response();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		if (request.getSession().getAttribute("user") != null) {

			request.getSession(false).invalidate();

			myLog.info("[logout] Session closed!");
		} else {

			myLog.info("[logout] No session found to close.");

			abstractResult.setResultId(ReturnValues.IUSERNOSESSION);
			abstractResult.setResultMsgAbs(ReturnValues.SUSERNOSESSION);
		}

		resp.setAbstractResult(abstractResult);
		return resp;
	}

}
