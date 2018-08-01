package com.gmodelo.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import com.gmodelo.Exception.InvCicException;
import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.LoginBean;

public class Utilities {

	public static final String GETLANGBYKEY = "SELECT LANG_VALUE FROM INV_CIC_LNG_VAL WITH(NOLOCK) WHERE LANG_KEY = ? and LANG = ?";
	public static final String GETREPVALUEBYKEY = "SELECT STORED_VALUE, STORED_ENCODED from  INV_CIC_REPOSITORY WITH(NOLOCK) WHERE STORED_KEY  = ?";

	public AbstractResults GetLangByKey(Connection con, String key, String lang) throws InvCicException {
		AbstractResults result = new AbstractResults();
		try {
			PreparedStatement stm = con.prepareStatement(GETLANGBYKEY);
			stm.setString(1, key);
			stm.setString(2, lang);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				result.setResultId(ReturnValues.ISUCCESS);
				result.setStrCom1(rs.getString(ReturnValues.SLANGVALUE));
			} else {
				throw new InvCicException("GetLangByKey Not Found!");
			}
		} catch (SQLException e) {
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		}
		return result;
	}

	public AbstractResults GetValueRepByKey(Connection con, String key) throws InvCicException {
		AbstractResults result = new AbstractResults();
		try {
			PreparedStatement stm = con.prepareStatement(GETREPVALUEBYKEY);
			stm.setString(1, key);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				result.setResultId(ReturnValues.ISUCCESS);
				result.setStrCom1(rs.getString(ReturnValues.SREPVALUE));
				result.setBooleanResult(rs.getBoolean(ReturnValues.SREPSECURED));
			} else {
				throw new InvCicException("GetValueRepByKey Not Found!");
			}
		} catch (SQLException e) {
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		}
		return result;
	}

	public String EncodeB64(String toEncode) {
		return Base64.getEncoder().encodeToString(toEncode.getBytes());
	}

	public String DecodeB64(String toDecode) {
		return new String(Base64.getDecoder().decode(toDecode.getBytes()));
	}

	public String generateLoginToken(LoginBean login) {
		return this.EncodeB64(login.getLoginId()) + UUID.randomUUID().toString() + new Date().toString();
	}

}
