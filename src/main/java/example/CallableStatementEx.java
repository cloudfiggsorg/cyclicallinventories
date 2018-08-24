package example;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class CallableStatementEx {

	static String EMBALAR_HUS = "exec SP_BCPS_WM_GENERATE_NEW_HU_VBELN ?,?,?,?,?,?,?,?,?,?,?,?,?,?";
	
	// public ResultDT embalarHus(PaletizadoraDTO paletizadoraDTO,
	// String keyTimeStamp, String userId) {
	//
	// ResultDT result = new ResultDT();
	// result.setId(0);
	//
	// Connection con = DBConnection.createConnection();
	//
	// CallableStatement callableStatement = null;
	//
	// try {
	//
	// callableStatement = con.prepareCall(EMBALAR_HUS);
	// // AUFNR,ERLKZ,VHILM,MATNR,MEINS,VEMNG,PACKNR,WERKS,CHARG,CHARG2,RETURN
	//
	// callableStatement.setString(1, paletizadoraDTO.getAufnr());
	// callableStatement.setString(2, keyTimeStamp);
	// callableStatement.setString(3, paletizadoraDTO.getTarima());
	// callableStatement.setString(4, paletizadoraDTO
	// .getMaterialPTTarima());
	// callableStatement.setString(5, paletizadoraDTO.getUnidadMedida());
	// callableStatement
	// .setString(6, paletizadoraDTO.getCantidadXTarima());
	// callableStatement.setString(7, paletizadoraDTO.getLetyp());
	// callableStatement.setString(8, paletizadoraDTO.getWerks());
	// callableStatement.setString(9, paletizadoraDTO.getCharg());
	// callableStatement.setString(10, paletizadoraDTO.getCharg2());
	//
	// callableStatement.setString(11, paletizadoraDTO
	// .getCantidadXTarima2());
	// callableStatement.setString(12, paletizadoraDTO.getUnidadMedida2());
	// callableStatement.setString(13, userId);
	//
	// callableStatement.registerOutParameter(14, java.sql.Types.INTEGER);
	//
	// callableStatement.execute();
	//
	// int id = 0;
	// id = callableStatement.getInt(14);
	//
	//
	// result.setId(id);
	//
	// } catch (SQLException e) {
	// result.setId(200);
	// result.setMsg(e.getMessage());
	//
	// } catch (Exception en) {
	// result.setId(200);
	// result.setMsg(en.getMessage());
	//
	// } finally {
	// try {
	// DBConnection.closeConnection(con);
	// } catch (Exception e) {
	// result.setId(200);
	// result.setMsg(e.getMessage());
	// LOCATION.errorT("Exception in generaHusParaImprimirBCPS:"
	// + e.toString());
	//
	// }
	// }
	// return result;
	// }
	
}
