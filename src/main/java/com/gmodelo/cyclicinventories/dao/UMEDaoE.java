package com.gmodelo.cyclicinventories.dao;

import java.sql.Connection;
import java.sql.SQLException;
import org.jboss.logging.Logger;
import com.bmore.ume001.dao.UMEdao;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.exception.InvCicException;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.gmodelo.cyclicinventories.utils.Utilities;

public class UMEDaoE extends UMEdao{
	
	private static String LDAPUser;
	private static String LDAPPassword;
	private static String LDAPInitialContextFactory;
	private static String LDAPProviderURL;
	private static String LDAPSearchBase;
	private static String LDAPSecurityAuthentication;
	private static String LDAPSecurityPrincipal;
	private final static String DS_UME = "USER_MANAGEMENT_DS";
	private final static Logger log =  Logger.getLogger(UMEdao.class);
	
	static{
		
		Utilities iUtils = new Utilities();
		Connection con = new ConnectionManager().createConnection();
		
		try {
			
			LDAPUser = iUtils.getValueRepByKey(con, ReturnValues.REP_LDAP_USER).getStrCom1();
			LDAPPassword  = iUtils.getValueRepByKey(con, ReturnValues.REP_LDAP_PASSWORD).getStrCom1();
			LDAPInitialContextFactory  = "com.sun.jndi.ldap.LdapCtxFactory";
			LDAPProviderURL  = iUtils.getValueRepByKey(con, ReturnValues.REP_LDAP_PROVIDER_URL).getStrCom1();
			LDAPSearchBase  = iUtils.getValueRepByKey(con, ReturnValues.REP_LDAP_BASE).getStrCom1();
			LDAPSecurityAuthentication  = iUtils.getValueRepByKey(con, ReturnValues.REP_LDAP_SECURITY_AUTH).getStrCom1();;
			LDAPSecurityPrincipal  = iUtils.getValueRepByKey(con, ReturnValues.REP_LDAP_DOMAIN).getStrCom1();
			
		} catch (InvCicException e) {
			
			log.error("Error al intentar consultar las propiedades de conexión.");
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				log.error("Error al intentar cerrar la conexión de base datos...");
			}
		}		
	}	 
		
	public UMEDaoE() {
		
		super();
		super.setLDAPUser(UMEDaoE.LDAPUser);
		super.setLDAPPassword(UMEDaoE.LDAPPassword);
		super.setLDAPInitialContecxtFactory(UMEDaoE.LDAPInitialContextFactory);
		super.setLDAPProviderURL(UMEDaoE.LDAPProviderURL);
		super.setLDAPSearchBase(UMEDaoE.LDAPSearchBase);
		super.setLDAPSecurityAuthentication(UMEDaoE.LDAPSecurityAuthentication);
		super.setLDAPSecurityPrincipal(UMEDaoE.LDAPSecurityPrincipal);		
		super.setDataSourceName(UMEDaoE.DS_UME);
	}
	
	public Response<Object> saveUserWerks(String userId, String werks){
		
		
		return null;
	}

}