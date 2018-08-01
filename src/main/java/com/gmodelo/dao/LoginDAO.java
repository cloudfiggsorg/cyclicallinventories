package com.gmodelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.gmodelo.Exception.InvCicException;
import com.gmodelo.beans.LoginBean;
import com.gmodelo.beans.RecoveryBean;
import com.gmodelo.utils.Utilities;

public class LoginDAO {

	public static final String STORE_TOKEN_ID = "insert into INV_CIC_KEY_EXCHANGE VALUES (?,?,getdate(),dateadd(mi, 60,getdate()))";
	public static final String UPDATE_STORED_TOKEN = "update INV_CIC_KEY_EXCHANGE set logInCredential = ?, logOnDate = GETDATE(), logOnValid =dateadd(mi, 60,getdate()) where logOnValue = ?";
	public static final String EXTEND_STORED_TOKEN = "update INV_CIC_KEY_EXCHANGE logOnDate = GETDATE(), logOnValid =dateadd(mi, 60,getdate()) where logInCredential = ?";
	public static final String GET_VALID_TOKEN_ID = "SELECT logOnValue, logInCredential, logOnDate, logOnValid  FROM INV_CIC_KEY_EXCHANGE WITH(NOLOCK) WHERE logInCredential = ?";
	public static final String GET_INVALID_TOKEN = "SELECT COUNT(*) as TOTAL FROM INV_CIC_KEY_EXCHANGE WITH(NOLOCK) WHERE logOnValue = ?";

	public void LoginStoreToken(LoginBean login, Connection con) throws InvCicException {
		try {
			PreparedStatement stm = con.prepareStatement(STORE_TOKEN_ID);
			stm.setString(1, new Utilities().EncodeB64(login.getLoginId()));
			stm.setString(2, login.getRelationUUID());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new InvCicException(e);
		}
	}

	public void UpdateStoredToken(LoginBean login, Connection con) throws InvCicException {
		try {
			PreparedStatement stm = con.prepareStatement(UPDATE_STORED_TOKEN);
			stm.setString(1, login.getRelationUUID());
			stm.setString(2, new Utilities().EncodeB64(login.getLoginId()));
		} catch (SQLException e) {
			throw new InvCicException(e);
		}
	}

	public void ExtendStoredToken(LoginBean login, Connection con) throws InvCicException {
		try {
			PreparedStatement stm = con.prepareStatement(EXTEND_STORED_TOKEN);
			stm.setString(1, login.getRelationUUID());
		} catch (SQLException e) {
			throw new InvCicException(e);
		}
	}

	public RecoveryBean RecoverStoredToken(LoginBean login, Connection con) throws InvCicException {
		RecoveryBean recovery = null;
		try {
			PreparedStatement stm = con.prepareStatement(GET_VALID_TOKEN_ID);
			stm.setString(1, login.getRelationUUID());
			ResultSet rs = stm.executeQuery();
			recovery = new RecoveryBean(rs);
		} catch (SQLException e) {
			throw new InvCicException(e);
		}
		return recovery;
	}

	public Integer ValidateToken(LoginBean login, Connection con) throws InvCicException {
		try {
			PreparedStatement stm = con.prepareStatement(GET_INVALID_TOKEN);
			stm.setString(1, new Utilities().EncodeB64(login.getLoginId()));
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				return rs.getInt("TOTAL");
			}
		} catch (SQLException e) {
			throw new InvCicException(e);
		}
		return 0;
	}

}
