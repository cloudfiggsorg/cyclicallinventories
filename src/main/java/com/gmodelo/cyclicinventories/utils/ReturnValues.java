package com.gmodelo.cyclicinventories.utils;

public class ReturnValues {

	public static final int ISUCCESS = 1;
	public static final int ISESSIONCREATED = 2;
	public static final int IERROR = -999;

	public static final int IEMPTY = 3;
	public static final String SEMPTY = "EMPTY";

	public static final String SLOGINSUCCESS = "INV_CIC_USER_LOGIN";
	public static final int IPASSWORDNOTMATCH = -101;
	public static final String SPASSWORDNOTMATCH = "INV_CIC_PASS_NOT_MATCH";

	public static final int IUSERNOTEXISTS = -102;
	public static final String SUSERNOTEXISTS = "INV_CIC_USER_NOT_FOUND";

	public static final int INOTROLE = -104;
	public static final String SNOTROLE = "INV_CIC_ROLE_NOT_FOUND";

	public static final int ILDAPTIMEOUT = -103;
	public static final String SLDAPTIMEOUT = "INV_CIC_LDAP_CON_TIMEOUT";
	public static final int IEXCEPTION = -105;

	public static final int IUSERNOSESSION = -106;
	public static final String SUSERNOSESSION = "INV_CIC_USER_NOT_SESSION";

	public static final int IINVALIDTOKEN = -108;
	public static final String SINVALIDTOKEN = "INVALID TOKEN";

	public static final String SINVALIDUSER = "USUARIO Y/O PASSWORD INVALIDO";

	public static final int IUSERNOTTASK = -107;
	
	public static final int IUSERNOTVALIDATEDTASK = -108;
	public static final int IUSERTASKFINISHED = -109;
	public static final int IUSERTASKNOUPDATED = -110;
	public static final int ILOCKEDUSER = -111;
	public static final String SLOCKEDUSER = "LOCKED USER";
	public static final int IMISSING_BUKRS_OR_WERKS = -112;
	public static final int FILE_EXCEPTION = -113;
	public static final int FILE_NOT_FOUND = -114;
	public static final int IUSERNOTTASKNOTREADY = -128;
	
	public static final String SMISSING_BUKRS_OR_WERKS = "BUKRS OR WERKS NOT FOUND";

	public static final String SEXCEPTION = "";
	public static final int ILANGKEYNOTFOUND = -404;
	public static final String SLANGVALUE = "LANG_VALUE";
	public static final String SLANGKEYNOTFOUND = "INV_CIC_KEY_NOT_FOUND";
	public static final int IREPKEYNOTFOUND = -405;
	public static final String SREPVALUE = "STORED_VALUE";
	public static final String SREPSECURED = "STORED_ENCODED";
	public static final String REP_LDAP_BASE = "LDAP_SEARCHBASE";
	public static final String REP_LDAP_DOMAIN = "LDAP_DOMAIN";// LDAP_DOMAIN
	public static final String REP_LDAP_PROVIDER_URL = "LDAP_PROVIDER_URL";// LDAP_PROVIDER_URL
	public static final String REP_LDAP_USER = "LDAP_USER";// LDAP_USER
	public static final String REP_LDAP_PASSWORD = "LDAP_PASSWORD";// LDAP_PASSWORD
	public static final String REP_LDAP_SECURITY_AUTH = "LDAP_SECURITY_AUTH";// LDAP_SECURITY_AUTH
	public static final int ACTIVE_INTERVAL = 3600;// ACTIVE_INTERVAL

	public static final String REP_DESTINATION_VALUE = "INV_CIC_CONCILIATION_DESTINATION";

	public static final String REP_CLASS_SYSTEM = "INV_CIC_CLASSIFICATION_STATUS";
	public static int REP_CLASS_UPDATED = 0;

	public static final int CONTEO_DIARIO = 1;
	public static final int CONTEO_MENSUAL = 2;
	public static final int CONTEO_ESPECIAL = 3;

	public static final String ROLE_MASK = "ROLE_MASK";
	public static final String PATH_TO_SAVE_FILES = "PATH_TO_SAVE_FILES";
	public static final String E_CLASS_LAST_UPDATED = "E_CLASS_LAST_UPDATED";

}
