package com.gmodelo.cyclicinventories.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.LoginBean;
import com.gmodelo.cyclicinventories.exception.InvCicException;

public class Utilities {

	public static final String GETLANGBYKEY = "SELECT LANG_VALUE FROM INV_CIC_LNG_VAL WITH(NOLOCK) WHERE LANG_KEY = ? and LANG = ?";
	public static final String GETREPVALUEBYKEY = "SELECT STORED_VALUE, STORED_ENCODED from  INV_CIC_REPOSITORY WITH(NOLOCK) WHERE STORED_KEY  = ?";
	public static final String INSERTVALUEBYKEY = "INSERT INTO INV_CIC_REPOSITORY (STORED_KEY, STORED_VALUE, STORED_ENCODED) VALUES (?,?,?)";
	public static final String UPDATEVALUEBYKEY = "UPDATE INV_CIC_REPOSITORY SET STORED_VALUE = ? WHERE STORED_KEY = ?";

	public AbstractResultsBean getLangByKey(Connection con, String key, String lang) throws InvCicException {
		AbstractResultsBean result = new AbstractResultsBean();
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

	public AbstractResultsBean getValueRepByKey(Connection con, String key) throws InvCicException {
		AbstractResultsBean result = new AbstractResultsBean();
		try {
			PreparedStatement stm = con.prepareStatement(GETREPVALUEBYKEY);
			stm.setString(1, key);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				result.setResultId(ReturnValues.ISUCCESS);
				if (rs.getBoolean(ReturnValues.SREPSECURED)) {
					result.setStrCom1(decodeB64(rs.getString(ReturnValues.SREPVALUE)));
				} else {
					result.setStrCom1(rs.getString(ReturnValues.SREPVALUE));
				}
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

	// INV_CIC_CLASSIFICATION_STATUS
	public AbstractResultsBean updateValueRepByKey(Connection con, String key, String value) throws InvCicException {
		AbstractResultsBean result = new AbstractResultsBean();
		try {
			PreparedStatement stm = con.prepareStatement(UPDATEVALUEBYKEY);
			stm.setString(1, value);
			stm.setString(2, key);
			stm.executeUpdate();
		} catch (SQLException e) {
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		}
		return result;
	}

	public AbstractResultsBean insertValueRepKey(Connection con, String key, String value, boolean encoded)
			throws InvCicException {
		AbstractResultsBean result = new AbstractResultsBean();
		try {
			PreparedStatement stm = con.prepareStatement(INSERTVALUEBYKEY);
			stm.setString(1, key);
			if (encoded) {
				stm.setString(2, encodeB64(value));
			} else {
				stm.setString(2, value);
			}
			stm.setBoolean(3, encoded);
			stm.executeUpdate();
		} catch (SQLException e) {
			result.setResultId(ReturnValues.IEXCEPTION);
			result.setResultMsgAbs(e.getMessage());
		}
		return result;
	}

	public String encodeB64(String toEncode) {
		return Base64.getEncoder().encodeToString(toEncode.getBytes());
	}

	public String decodeB64(String toDecode) {
		return new String(Base64.getDecoder().decode(toDecode.getBytes()));
	}

}
