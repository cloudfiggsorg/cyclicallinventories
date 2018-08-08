package com.gmodelo.workservice;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import com.bmore.ume001.beans.Role;
import com.bmore.ume001.beans.User;
import com.gmodelo.Exception.InvCicException;
import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.LdapUser;
import com.gmodelo.beans.LoginBean;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.UMEDaoE;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;
import com.google.gson.Gson;

public class LoginWorkService {

	HttpSession session;

	private Logger log = Logger.getLogger(LoginWorkService.class.getName());
	private java.util.logging.Logger myLog = java.util.logging.Logger.getLogger( LoginWorkService.class.getName());

//	public String validateLogin(String loginString) {
//		Response<LoginBean> response = new Response<>();
//		Gson gson = new Gson();
//		AbstractResults result = new AbstractResults();
//		ConnectionManager iConnectionManager = new ConnectionManager();
//		LoginBean login = gson.fromJson(loginString, LoginBean.class);
//		LoginDAO loginDAO = new LoginDAO();
//		Connection con = iConnectionManager.createConnectionViaDatasource();
//		response.setAbstractResult(result);
//		try {
//			result = iConnectionManager.ValidateLDAPLogin(login, con);
//			if (result.getResultId().equals(ReturnValues.ISUCCESS)) {
//				if (loginDAO.ValidateToken(login, con) > 0) {
//					RecoveryBean recovery = loginDAO.RecoverStoredToken(login, con);
//					if (recovery.getLogOnCredential().equals(login.getRelationUUID())) {
//						if (new Date().after(recovery.getLogOnValid())) {
//							login.setRelationUUID(new Utilities().generateLoginToken(login));
//							loginDAO.UpdateStoredToken(login, con);
//						} else {
//							loginDAO.ExtendStoredToken(login, con);
//						}
//					} else {
//						login.setRelationUUID(recovery.getLogOnCredential());
//						loginDAO.ExtendStoredToken(login, con);
//					}
//				} else {
//					login.setRelationUUID(new Utilities().generateLoginToken(login));
//					loginDAO.LoginStoreToken(login, con);
//				}
//			} else {
//				response.setAbstractResult(result);
//			}
//		} catch (InvCicException e) {
//			result.setResultId(ReturnValues.IEXCEPTION);
//			result.setResultMsgAbs(e.getMessage());
//		} finally {
//			try {
//				iConnectionManager.CloseConnection(con);
//			} catch (Exception e) {
//				e.printStackTrace(System.out);
//			}
//		}
//		return gson.toJson(response);
//	}

	public Response login(LoginBean loginBean, HttpServletRequest request) {
		InitialContext ctx;
//		try {
//			ctx = new InitialContext();
//			NamingEnumeration<NameClassPair> list = ctx.list(""); while (list.hasMore()) { System.out.println(list.next().getName()); }
//		} catch (NamingException e2) {
//			log.error("InitialContext error", e2);
//		} 

		Response<LoginBean> resp = new Response<LoginBean>();
		AbstractResults abstractResult = new AbstractResults();

		// LoginBean loginBean = gson.fromJson(login, LoginBean.class);

		if (loginBean.getLoginId() == null || loginBean.getLoginId().isEmpty() || loginBean.getLoginPass().isEmpty()
				|| loginBean.getLoginPass() == null) {

			log.error("[login] Missing data");

			abstractResult.setResultId(-1);
			abstractResult.setResultMsgAbs("SOME ERROR OCCURED...");
			resp.setAbstractResult(abstractResult);
			return resp;
		}

		// Check credentials to create a session
		if (request.getSession().getAttribute("user") == null) {

			User user = new User();
			// Check if user exists on UME or LDAP
			UMEDaoE apiUME = new UMEDaoE();
//			apiUME.setConnectionData("35.172.127.238", "1433", "DBEntities", "ume_user", "Bmore2018.");
			user.getEntity().setIdentyId(loginBean.getLoginId().trim());
			user.getAccInf().setPassword(loginBean.getLoginPass().trim());
			// Check if user exists on UME
			try {
				user = apiUME.checkUserUME(user);
				
//				myLog.log(Level.WARNING,"Si funcione UME DS");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				user = null;
				myLog.log(Level.SEVERE,"Ya valió UME DS",e1);
			}
			if(user != null){
				abstractResult.setResultId(ReturnValues.ISUCCESS); 
			}
			else{// Check if user exists on LDAP
				myLog.log(Level.WARNING,"[login] Check if user exists on LDAP");
				user = new User();
				user.getEntity().setIdentyId(loginBean.getLoginId().trim());
				user.getAccInf().setPassword(loginBean.getLoginPass().trim());

//				ArrayList<User> lsUser = new ArrayList<User>();
//				lsUser.add(user);
				try {
					 //lsUser = apiUME.getUsersLDAP(lsUser);
					ConnectionManager iConnectionManager = new ConnectionManager();
					
					Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
					abstractResult = iConnectionManager.ValidateLDAPLogin(loginBean, con);
					iConnectionManager.CloseConnection(con);
				} catch (InvCicException e) {
					myLog.log(Level.SEVERE,"[login] Error while tryng to retrive the user info from LDAP", e);
					abstractResult.setResultId(ReturnValues.IEXCEPTION);
				}
//				if (!lsUser.isEmpty()) {
//					user = lsUser.get(0);
//				} else {
//					user = null;
//				}
			}

			if (user != null && abstractResult.getResultId() == ReturnValues.ISUCCESS) {

				// Check if the account is locked
				if (user.getAccInf().getLockAcc() > 0) {
					abstractResult.setResultId(-1);
					abstractResult.setResultMsgAbs("BLOCKED USER");

				} else {
					
					//verify role to execute application
					ArrayList<Role> lsRoles = new ArrayList<Role>();
					lsRoles = apiUME.getUserRoles(loginBean.getLoginId().trim());
					ArrayList<String> lsRolesAux = new ArrayList<String>();
					for(Role rol : lsRoles){
						lsRolesAux.add(rol.getRolId().trim());
					}
					
					if(!lsRolesAux.contains("EXECUTE_INV_CIC_APP")){
						abstractResult.setResultId(ReturnValues.INOTROLE);
						abstractResult.setResultMsgAbs(ReturnValues.SNOTROLE);
						resp.setAbstractResult(abstractResult);
						
						return resp;
					}
					
					session = request.getSession(true);
//					user = new User();
//					user.getAccInf().setPassword(null);
//					user.getEntity().setDescription(null);
//					user.getEntity().setCreatedBy(null);
//					user.setAccInf(null);
					session.setAttribute("user", user);// Set the idUser on
														// session
					session.setAttribute("roles", lsRolesAux);
					
					myLog.log(Level.WARNING,"[login] Loggin success for: " + user.getEntity().getIdentyId());
					session.setMaxInactiveInterval(ReturnValues.ACTIVE_INTERVAL);
//					abstractResult.setIntCom1(1*60);
					loginBean.setActiveInterval(ReturnValues.ACTIVE_INTERVAL);
					loginBean.setLoginId(null);
					loginBean.setLoginPass(null);
					loginBean.setRelationUUID(session.getId());
					loginBean.setLsObjectLB(new LdapUser<>());
					resp.setLsObject(loginBean);
				}

			} else {
				log.warn(abstractResult.getResultMsgAbs());
				//abstractResult.setResultId(ReturnValues.IPASSWORDNOTMATCH);
				//abstractResult.setResultMsgAbs("USER OR PASSWORD INCORRECT");
			}
		} else {
			log.warn("[login] Session already created");
			abstractResult.setResultId(ReturnValues.ISESSIONCREATED);
			abstractResult.setResultMsgAbs("SESSION ALREADY CREATED");
		}

		resp.setAbstractResult(abstractResult);
		return resp;

	}

}
