package com.gmodelo.dao;

public class BulksDao {

	/*
	public ResultDT bulk(String tabla, String werks) {

		ResultDT resultDT = new ResultDT();
		Connection con = DBConnection.createConnection();
		PreparedStatement st2 = null;
		PreparedStatement upd = null;

		String tablaSQL = "";
		String txtTabla = "";
		String campo = "";

		CallableStatement stm = null;
		boolean integrity_constraint = false;
		try {

			

			if (tabla.equals("ZPAITT_CONT_HU")) {

				integrity_constraint = true;
				tablaSQL = "TB_BCPS_NEW_HU";
				txtTabla = tabla;

			} else if (tabla.equals("ZPAITT_FACTURA")) {

				integrity_constraint = Boolean.TRUE;
				tablaSQL = "TB_BCPS_ZFACT";
				txtTabla = tabla;

			} else if (tabla.equals("ZPAITT_ENTREGA")) {

				integrity_constraint = Boolean.TRUE;
				tablaSQL = "TB_BCPS_NEW_VBELN";
				txtTabla = tabla;

			} else if (tabla.equals("ZPAITT_HU")) {

				tablaSQL = "ZPAITT_HU_EXT";
				txtTabla = tabla;

			} else if (tabla.equals("ZPAITT_PALL_EYT")) {

				tablaSQL = "ZPAITT_PALLETOBR";
				txtTabla = tabla;

			} else {

				tablaSQL = tabla;
				txtTabla = tablaSQL;

			}

			stm = con.prepareCall(GET_ULTIMO_CAMPO);

			stm.setString(1, tablaSQL);
			stm.registerOutParameter(2, java.sql.Types.VARCHAR);
			stm.registerOutParameter(3, java.sql.Types.INTEGER);

			stm.execute();

			if (stm.getInt(3) == 1) {

				campo = stm.getString(2);

				try {

					st2 = con
							.prepareStatement("bulk insert HCMDB.dbo."
									+ tablaSQL
									+ " from  'C:/DATAUPLOAD/_"
									+ txtTabla
									+ "_"
									+ werks
									+ ".txt' "
									+ "with (FIELDTERMINATOR = '|-|', KEEPIDENTITY, KEEPNULLS, ROWTERMINATOR ='\n', FIRSTROW=2, CODEPAGE = 'ACP')");

					st2.executeUpdate();

					if (campo.equals("SKZUA") || campo.equals("HU_LGORT")) {

						upd = con.prepareStatement("update HCMDB.dbo."
								+ tablaSQL + " set " + campo
								+ " = case when len(" + campo
								+ ") > 1 then replace(" + campo
								+ ",char(13),'') else replace(" + campo
								+ ",char(13),null) end  where " + campo
								+ " NOT IN ('A','X') and " + campo
								+ " is not null");
						upd.executeUpdate();

					} else {

						upd = con.prepareStatement("update HCMDB.dbo."
								+ tablaSQL + " set " + campo
								+ " = case when len(" + campo + ") > 1"
								+ " then replace(" + campo
								+ ",char(13),'') else replace(" + campo
								+ ",char(13),null) end");
						upd.executeUpdate();

					}
					LOCATION.errorT("Tabla SQL: " + tablaSQL + "   TxtTabla: "
							+ txtTabla + "  Centro:" + werks);
					resultDT.setId(1);

				} catch (SQLException e) {

					if (integrity_constraint) {

						resultDT.setId(1);

					} else {

						LOCATION.errorT("ERROR BULK ->SQLException: "
								+ e.toString());
						resultDT.setId(2);
						resultDT.setMsg(e.getMessage());
					}
				} catch (Exception en) {
					LOCATION.errorT("ERROR BULK ->Exception: " + en.toString());
					resultDT.setId(2);
					resultDT.setMsg(en.getMessage());
				} finally {
					try {
						DBConnection.closeConnection(con);
					} catch (Exception e) {
						LOCATION.errorT("ERROR BULK ->Exception: "
								+ e.toString());

					}
				}
			} else {
				resultDT.setId(2);
				resultDT.setMsg("No se recupero el ultimo campo de la tabla:"
						+ tabla);
			}

		} catch (SQLException e) {
			LOCATION.errorT("SQLException : " + e.toString());
			resultDT.setId(2);
			resultDT.setMsg("No fue posible recuperar las tablas: "
					+ e.toString());
		} catch (Exception e) {
			LOCATION.errorT("Exception1 : " + e.toString());
			resultDT.setId(2);
			resultDT.setMsg("No fue posible recuperar las tablas: "
					+ e.toString());
		} finally {
			try {
				DBConnection.closeConnection(con);
			} catch (Exception e) {
				LOCATION.errorT("Exception2 : " + e.toString());

			}
		}
		return resultDT;

	}*/

}
