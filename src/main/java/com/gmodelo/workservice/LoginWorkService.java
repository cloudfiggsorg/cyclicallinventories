package com.gmodelo.workservice;

import java.util.ArrayList;
import java.util.logging.Level;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bmore.ume001.beans.Role;
import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.LoginBean;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.UMEDaoE;
import com.gmodelo.utils.ReturnValues;

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
					for (Role rol : lsRoles) {
						lsRolesAux.add(rol.getRolId().trim());
					}

					if (!lsRolesAux.contains("EXECUTE_INV_CIC_APP")) {
						abstractResult.setResultId(ReturnValues.INOTROLE);
						abstractResult.setResultMsgAbs(ReturnValues.SNOTROLE);
						myLog.log(Level.SEVERE,loginBean.getLoginId().trim()+" : "+ ReturnValues.SNOTROLE);
						resp.setAbstractResult(abstractResult);

						return resp;
					}
					myLog.info(user.getEntity().getIdentyId());
					session = request.getSession(true);
					session.setAttribute("user", user);// Set the idUser on
														// session
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

			myLog.info("[logout] No session to close.");

			abstractResult.setResultId(ReturnValues.IUSERNOSESSION);
			abstractResult.setResultMsgAbs(ReturnValues.SUSERNOSESSION);
		}

		resp.setAbstractResult(abstractResult);
		return resp;
	}

}
