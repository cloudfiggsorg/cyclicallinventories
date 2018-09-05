package com.gmodelo.dao;

import com.bmore.ume001.dao.UMEdao;

public class UMEDaoE extends UMEdao{
	
	private final static String LDAPUser = "H0013974";
	private final static String LDAPPassword  = "Agosto2018.";
	private final static String LDAPInitialContextFactory  = "com.sun.jndi.ldap.LdapCtxFactory";
	private final static String LDAPProviderURL  = "ldap://10.88.1.209:389";
	private final static String LDAPSearchBase  = "DC=modelo,DC=gmodelo,DC=com,DC=mx";
	private final static String LDAPSecurityAuthentication  = "simple";
	private final static String LDAPSecurityPrincipal  = "Modelo";
	private final static String DS_UME = "USER_MANAGEMENT_DS";
	

	
	public UMEDaoE() {
		super();
		super.setLDAPUser(this.LDAPUser);
		super.setLDAPPassword(this.LDAPPassword);
		super.setLDAPInitialContecxtFactory(this.LDAPInitialContextFactory);
		super.setLDAPProviderURL(this.LDAPProviderURL);
		super.setLDAPSearchBase(this.LDAPSearchBase);
		super.setLDAPSecurityAuthentication(this.LDAPSecurityAuthentication);
		super.setLDAPSecurityPrincipal(this.LDAPSecurityPrincipal);
		
		super.setDataSourceName(this.DS_UME);
	}
	
	

}
