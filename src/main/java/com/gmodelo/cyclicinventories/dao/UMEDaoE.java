package com.gmodelo.cyclicinventories.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.jboss.logging.Logger;

import com.bmore.ume001.dao.UMEdao;
import com.gmodelo.cyclicinventories.exception.InvCicException;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.gmodelo.cyclicinventories.utils.Utilities;

public class UMEDaoE extends UMEdao{
		
	private final static String DS_UME = "USER_MANAGEMENT_DS";
	private final static Logger log =  Logger.getLogger(UMEdao.class);
			
	public UMEDaoE() {
		
		super();
		
		Connection con = new ConnectionManager().createConnection();
		Utilities iUtils = new Utilities();
		
		try {
			super.setLDAPUser(iUtils.getValueRepByKey(con, ReturnValues.REP_LDAP_USER).getStrCom1());
			super.setLDAPPassword(iUtils.getValueRepByKey(con, ReturnValues.REP_LDAP_PASSWORD).getStrCom1());
			super.setLDAPInitialContecxtFactory("com.sun.jndi.ldap.LdapCtxFactory");
			super.setLDAPProviderURL(iUtils.getValueRepByKey(con, ReturnValues.REP_LDAP_PROVIDER_URL).getStrCom1());
			super.setLDAPSearchBase(iUtils.getValueRepByKey(con, ReturnValues.REP_LDAP_BASE).getStrCom1());
			super.setLDAPSecurityAuthentication(iUtils.getValueRepByKey(con, ReturnValues.REP_LDAP_SECURITY_AUTH).getStrCom1());
			super.setLDAPSecurityPrincipal(iUtils.getValueRepByKey(con, ReturnValues.REP_LDAP_DOMAIN).getStrCom1());		
			super.setDataSourceName(UMEDaoE.DS_UME);
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
	
}
