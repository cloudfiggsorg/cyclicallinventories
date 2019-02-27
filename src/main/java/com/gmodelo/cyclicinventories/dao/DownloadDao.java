package com.gmodelo.cyclicinventories.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.cyclicinventories.beans.MaterialTarimasBean;
import com.gmodelo.cyclicinventories.beans.MobileMaterialBean;
import com.gmodelo.cyclicinventories.beans.ZIACST_I360_OBJECTDATA_SapEntity;
import com.gmodelo.cyclicinventories.exception.InvCicException;
import com.gmodelo.cyclicinventories.structure.ZIACMF_I360_EXT_SIS_CLAS;

public class DownloadDao {

	Logger log = Logger.getLogger(getClass().getName());

	public static final String GET_MATERIALES_TARIMAS = "SELECT PAT.PACKNR,  PAT.MATNR AS VHILM , MAKT.MAKTX, PAT.TRGQTY, "
			+ "PAT.BASEUNIT, PAT.PACKITEM, PAT.PAITEMTYPE "
			+ "FROM PACKPO PAT left JOIN MAKT MAKT ON PAT.MATNR = MAKT.MATNR WHERE MAKT.MAKTX IS NOT NULL "
			+ "GROUP BY  PAT.PACKNR, PAT.MATNR, MAKT.MAKTX, PAT.TRGQTY, PAT.BASEUNIT, PAT.PACKITEM, PAT.PAITEMTYPE ";

	public static final String GET_ALL_INFO_MATERIAL = "SELECT MR.MATNR, MK.MAKTX, TA.MSEH3 MEINS, '' MEINH, '' UMREZ, '' UMREN, MR.EANNR, MR.EAN11 "
			+ " FROM MARA MR WITH(NOLOCK) INNER JOIN MAKT MK ON MR.MATNR = MK.MATNR "
			+ " INNER JOIN MARM MM ON MR.MATNR = MM.MATNR  " + " INNER JOIN T006A TA ON MR.MEINS = TA.MSEHI "
			+ " GROUP BY MR.MATNR, MK.MAKTX, TA.MSEH3, MR.EANNR, MR.EAN11";

	public static final String GET_CLASS_SYSTEM = "SELECT SUBSTRING(MATNR, PATINDEX('%[^0 ]%', MATNR + ' '), LEN(MATNR)) MATNR, "
			+ "SMBEZ, CONVERT(INT, CONVERT(NUMERIC(23,3),ATFLV)) ATFLV, ATNAM FROM E_CLASS WITH(NOLOCK)";

	public List<MaterialTarimasBean> getAllMaterialCrossTarimas(Connection con) throws InvCicException {
		List<MaterialTarimasBean> materialTarimasList = new ArrayList<>();
		try {
			log.log(Level.WARNING, "getAllMaterialCrossTarimas");
			PreparedStatement stm = con.prepareStatement(GET_MATERIALES_TARIMAS);
			log.log(Level.WARNING, "executing: " + GET_MATERIALES_TARIMAS);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				MaterialTarimasBean bean = new MaterialTarimasBean();
				bean.setPacknr(rs.getString("PACKNR"));
				try {
					bean.setVhilm(String.valueOf(rs.getInt("VHILM")));
				} catch (Exception e) {
					bean.setVhilm(rs.getString("VHILM"));
				}
				bean.setMaktx(rs.getString("MAKTX"));
				bean.setTrgqty(rs.getString("TRGQTY"));
				bean.setBaseunit(rs.getString("BASEUNIT"));
				bean.setPackitem(rs.getString("PACKITEM"));
				bean.setPaitemtype(rs.getString("PAITEMTYPE"));
				materialTarimasList.add(bean);
			}
		} catch (SQLException e) {
			throw new InvCicException(e);
		} catch (Exception e) {
			throw new InvCicException(e);
		}
		log.log(Level.WARNING, "Finish: " + materialTarimasList.size());
		return materialTarimasList;
	}

	public List<MobileMaterialBean> getAllMaterialMobile(Connection con) throws InvCicException {
		List<MobileMaterialBean> mobileMaterialList = new ArrayList<>();
		log.log(Level.WARNING, "getAllMaterialMobile");
		try {
			PreparedStatement stm = con.prepareStatement(GET_ALL_INFO_MATERIAL);
			log.log(Level.WARNING, "executing: " + GET_ALL_INFO_MATERIAL);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				MobileMaterialBean bean = new MobileMaterialBean();
				try {
					bean.setMatnr(String.valueOf(rs.getInt("MATNR")));
				} catch (Exception e) {
					bean.setMatnr(rs.getString("MATNR"));
				}
				bean.setMaktx(rs.getString("MAKTX"));
				bean.setMeins(rs.getString("MEINS"));
				bean.setMeinh(rs.getString("MEINH"));
				bean.setUmrez(rs.getString("UMREZ"));
				bean.setUmren(rs.getString("UMREN"));
				bean.setEannr(rs.getString("EANNR"));
				bean.setEan11(rs.getString("EAN11"));
				mobileMaterialList.add(bean);
			}
		} catch (SQLException e) {
			throw new InvCicException(e);
		} catch (Exception e) {
			throw new InvCicException(e);
		}
		log.log(Level.WARNING, "Finish: " + mobileMaterialList.size());
		return mobileMaterialList;
	}

	public ZIACMF_I360_EXT_SIS_CLAS getClassSystemMobile(Connection con) throws InvCicException {
		ZIACMF_I360_EXT_SIS_CLAS i360_EXT_SIS_CLAS = new ZIACMF_I360_EXT_SIS_CLAS();
		log.log(Level.WARNING, "getClassSystemMobile");
		try {
			PreparedStatement stm = con.prepareStatement(GET_CLASS_SYSTEM);
			log.log(Level.WARNING, "executing: " + GET_CLASS_SYSTEM);
			List<ZIACST_I360_OBJECTDATA_SapEntity> entities = new ArrayList<>();
			i360_EXT_SIS_CLAS.setObjectData(entities);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				ZIACST_I360_OBJECTDATA_SapEntity entity = new ZIACST_I360_OBJECTDATA_SapEntity();
				try {
					entity.setObject(String.valueOf(rs.getInt("MATNR")));
				} catch (Exception e) {
					entity.setObject(rs.getString("MATNR"));
				}
				entity.setSmbez(rs.getString("SMBEZ"));
				entity.setAtflv(rs.getString("ATFLV"));
				entity.setAtnam(rs.getString("ATNAM"));
				entities.add(entity);
			}
		} catch (SQLException e) {
			throw new InvCicException(e);
		} catch (Exception e) {
			throw new InvCicException(e);
		}
		log.log(Level.WARNING, "Finish: " + i360_EXT_SIS_CLAS.getObjectData().size());
		return i360_EXT_SIS_CLAS;
	}

	public static final String GET_DELTA_MATERIALES_TARIMAS = "SELECT PAT.PACKNR,  PAT.MATNR AS VHILM , MAKT.MAKTX, PAT.TRGQTY, "
			+ "PAT.BASEUNIT, PAT.PACKITEM, PAT.PAITEMTYPE "
			+ "FROM PACKPO PAT left JOIN MAKT MAKT ON PAT.MATNR = MAKT.MATNR WHERE MAKT.MAKTX IS NOT NULL AND MAKT.LASTMODIFY >= ? "
			+ "GROUP BY  PAT.PACKNR, PAT.MATNR, MAKT.MAKTX, PAT.TRGQTY, PAT.BASEUNIT, PAT.PACKITEM, PAT.PAITEMTYPE ";

	public static final String GET_DELTA_ALL_INFO_MATERIAL = "SELECT MR.MATNR, MK.MAKTX, TA.MSEH3 MEINS, '' MEINH, '' UMREZ, '' UMREN, MR.EANNR, MR.EAN11 "
			+ " FROM MARA MR WITH(NOLOCK) INNER JOIN MAKT MK ON MR.MATNR = MK.MATNR "
			+ " INNER JOIN MARM MM ON MR.MATNR = MM.MATNR  INNER JOIN T006A TA ON MR.MEINS = TA.MSEHI "
			+ " WHERE MR.LASTMODIFY >= ? GROUP BY MR.MATNR, MK.MAKTX, TA.MSEH3, MR.EANNR, MR.EAN11";

	public List<MaterialTarimasBean> getDeltaAllMaterialCrossTarimas(Date lastRequest, Connection con)
			throws InvCicException {
		List<MaterialTarimasBean> materialTarimasList = new ArrayList<>();
		try {
			log.log(Level.WARNING, "getAllMaterialCrossTarimas");
			PreparedStatement stm = con.prepareStatement(GET_DELTA_MATERIALES_TARIMAS);
			log.log(Level.WARNING, "executing: " + GET_DELTA_MATERIALES_TARIMAS);
			stm.setDate(1, new java.sql.Date(lastRequest.getTime()));
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				MaterialTarimasBean bean = new MaterialTarimasBean();
				bean.setPacknr(rs.getString("PACKNR"));
				try {
					bean.setVhilm(String.valueOf(rs.getInt("VHILM")));
				} catch (Exception e) {
					bean.setVhilm(rs.getString("VHILM"));
				}
				bean.setMaktx(rs.getString("MAKTX"));
				bean.setTrgqty(rs.getString("TRGQTY"));
				bean.setBaseunit(rs.getString("BASEUNIT"));
				bean.setPackitem(rs.getString("PACKITEM"));
				bean.setPaitemtype(rs.getString("PAITEMTYPE"));
				materialTarimasList.add(bean);
			}
		} catch (SQLException e) {
			throw new InvCicException(e);
		}
		log.log(Level.WARNING, "Finish: " + materialTarimasList.size());
		return materialTarimasList;
	}

	public List<MobileMaterialBean> getDeltaAllMaterialMobile(Date lastRequest, Connection con) throws InvCicException {
		List<MobileMaterialBean> mobileMaterialList = new ArrayList<>();
		log.log(Level.WARNING, "getAllMaterialMobile");
		try {
			PreparedStatement stm = con.prepareStatement(GET_DELTA_ALL_INFO_MATERIAL);
			stm.setDate(1, new java.sql.Date(lastRequest.getTime()));
			log.log(Level.WARNING, "executing: " + GET_DELTA_ALL_INFO_MATERIAL);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				MobileMaterialBean bean = new MobileMaterialBean();
				try {
					bean.setMatnr(String.valueOf(rs.getInt("MATNR")));
				} catch (Exception e) {
					bean.setMatnr(rs.getString("MATNR"));
				}
				bean.setMaktx(rs.getString("MAKTX"));
				bean.setMeins(rs.getString("MEINS"));
				bean.setMeinh(rs.getString("MEINH"));
				bean.setUmrez(rs.getString("UMREZ"));
				bean.setUmren(rs.getString("UMREN"));
				bean.setEannr(rs.getString("EANNR"));
				bean.setEan11(rs.getString("EAN11"));
				mobileMaterialList.add(bean);
			}
		} catch (SQLException e) {
			throw new InvCicException(e);
		}
		log.log(Level.WARNING, "Finish: " + mobileMaterialList.size());
		return mobileMaterialList;
	}

}
