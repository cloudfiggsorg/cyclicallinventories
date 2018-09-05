package com.gmodelo.workservice;

import java.util.ArrayList;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bmore.ume001.beans.Role;
import com.bmore.ume001.beans.User;
import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.LdapUser;
import com.gmodelo.beans.LoginBean;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.UMEDaoE;
import com.gmodelo.utils.ReturnValues;

public class LoginWorkService {

	// private Logger log = Logger.getLogger(LoginWorkService.class.getName());
	private java.util.logging.Logger myLog = java.util.logging.Logger.getLogger(LoginWorkService.class.getName());

	// public String validateLogin(String loginString) {
	// Response<LoginBean> response = new Response<>();
	// Gson gson = new Gson();
	// AbstractResults result = new AbstractResults();
	// ConnectionManager iConnectionManager = new ConnectionManager();
	// LoginBean login = gson.fromJson(loginString, LoginBean.class);
	// LoginDAO loginDAO = new LoginDAO();
	// Connection con = iConnectionManager.createConnectionViaDatasource();
	// response.setAbstractResult(result);
	// try {
	// result = iConnectionManager.ValidateLDAPLogin(login, con);
	// if (result.getResultId().equals(ReturnValues.ISUCCESS)) {
	// if (loginDAO.ValidateToken(login, con) > 0) {
	// RecoveryBean recovery = loginDAO.RecoverStoredToken(login, con);
	// if (recovery.getLogOnCredential().equals(login.getRelationUUID())) {
	// if (new Date().after(recovery.getLogOnValid())) {
	// login.setRelationUUID(new Utilities().generateLoginToken(login));
	// loginDAO.UpdateStoredToken(login, con);
	// } else {
	// loginDAO.ExtendStoredToken(login, con);
	// }
	// } else {
	// login.setRelationUUID(recovery.getLogOnCredential());
	// loginDAO.ExtendStoredToken(login, con);
	// }
	// } else {
	// login.setRelationUUID(new Utilities().generateLoginToken(login));
	// loginDAO.LoginStoreToken(login, con);
	// }
	// } else {
	// response.setAbstractResult(result);
	// }
	// } catch (InvCicException e) {
	// result.setResultId(ReturnValues.IEXCEPTION);
	// result.setResultMsgAbs(e.getMessage());
	// } finally {
	// try {
	// iConnectionManager.CloseConnection(con);
	// } catch (Exception e) {
	// e.printStackTrace(System.out);
	// }
	// }
	// return gson.toJson(response);
	// }

	public Response login(LoginBean loginBean, HttpServletRequest request, HttpSession session) {
		InitialContext ctx;
		// try {
		// ctx = new InitialContext();
		// NamingEnumeration<NameClassPair> list = ctx.list(""); while
		// (list.hasMore()) { System.out.println(list.next().getName()); }
		// } catch (NamingException e2) {
		// log.error("InitialContext error", e2);
		// }

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
			// Check if user exists on UME or LDAP
			UMEDaoE apiUME = new UMEDaoE();
			// apiUME.setConnectionData("35.172.127.238", "1433", "DBEntities",
			// "ume_user", "Bmore2018.");
			// Check if user exists on LDAP
			myLog.info("[login] Check if user exists on LDAP");
			user.getEntity().setIdentyId(loginBean.getLoginId().trim());
			user.getAccInf().setPassword(loginBean.getLoginPass().trim());

			// ArrayList<User> lsUser = new ArrayList<User>();
			// lsUser.add(user);
			try {
				apiUME.setLDAPUser("H0013974");
				apiUME.setLDAPPassword("Agosto2018.");
				apiUME.setLDAPInitialContecxtFactory("com.sun.jndi.ldap.LdapCtxFactory");
				apiUME.setLDAPProviderURL("ldap://10.88.1.209:389");
				apiUME.setLDAPSearchBase("DC=modelo,DC=gmodelo,DC=com,DC=mx");
				apiUME.setLDAPSecurityAuthentication("simple");
				apiUME.setLDAPSecurityPrincipal("Modelo");
				user.getEntity().setIdentyId(loginBean.getLoginId());

				user = apiUME.checkUserLDAP(user);
				abstractResult.setResultId(ReturnValues.ISUCCESS);

				// ConnectionManager iConnectionManager = new
				// ConnectionManager();
				//
				// Connection con =
				// iConnectionManager.createConnection(ConnectionManager.connectionBean);
				// abstractResult =
				// iConnectionManager.ValidateLDAPLogin(loginBean, con);
				// iConnectionManager.CloseConnection(con);
			} catch (NamingException e) {
				user = null;
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				myLog.log(Level.SEVERE, "Error al verificar usuario en LDAP", e);
			}
			// catch (InvCicException e) {
			// myLog.log(Level.SEVERE,"[login] Error while tryng to retrive the
			// user info from LDAP", e);
			// abstractResult.setResultId(ReturnValues.IEXCEPTION);
			// }

			if (user != null && abstractResult.getResultId() == ReturnValues.ISUCCESS) {

				// Check if the account is locked
				if (user.getAccInf().getLockAcc() > 0) {
					abstractResult.setResultId(-1);
					abstractResult.setResultMsgAbs("BLOCKED USER");

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
						resp.setAbstractResult(abstractResult);

						return resp;
					}
					myLog.info(user.getEntity().getIdentyId());
					session = request.getSession(true);
					// user = new User();
					// user.getAccInf().setPassword(null);
					// user.getEntity().setDescription(null);
					// user.getEntity().setCreatedBy(null);
					// user.setAccInf(null);
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
				// abstractResult.setResultId(ReturnValues.IPASSWORDNOTMATCH);
				// abstractResult.setResultMsgAbs("USER OR PASSWORD INCORRECT");
			}
		} else {
			myLog.info("[login] Session already created");
			abstractResult.setResultId(ReturnValues.ISESSIONCREATED);
			abstractResult.setResultMsgAbs("SESSION ALREADY CREATED");
		}

		resp.setAbstractResult(abstractResult);
		return resp;

	}

	public Response<?> logout(HttpServletRequest request) {

		Response<?> resp = new Response();
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
