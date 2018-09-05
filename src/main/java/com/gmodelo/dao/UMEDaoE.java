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
		super.setLDAPUser(UMEDaoE.LDAPUser);
		super.setLDAPPassword(UMEDaoE.LDAPPassword);
		super.setLDAPInitialContecxtFactory(UMEDaoE.LDAPInitialContextFactory);
		super.setLDAPProviderURL(UMEDaoE.LDAPProviderURL);
		super.setLDAPSearchBase(UMEDaoE.LDAPSearchBase);
		super.setLDAPSecurityAuthentication(UMEDaoE.LDAPSecurityAuthentication);
		super.setLDAPSecurityPrincipal(UMEDaoE.LDAPSecurityPrincipal);		
		super.setDataSourceName(UMEDaoE.DS_UME);
	}

}
