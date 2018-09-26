package com.gmodelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gmodelo.Exception.InvCicException;
import com.gmodelo.beans.MaterialTarimasBean;
import com.gmodelo.beans.MobileMaterialBean;

public class DownloadDao {

	public static final String GET_MATERIALES_TARIMAS = "SELECT PAT.PACKNR,  PAT.MATNR AS VHILM , MAKT.MAKTX, PAT.TRGQTY, "
			+ "PAT.BASEUNIT, PAT.PACKITEM, PAT.PAITEMTYPE "
			+ "FROM PACKPO PAT left JOIN MAKT MAKT ON PAT.MATNR = MAKT.MATNR WHERE MAKT.MAKTX IS NOT NULL "
			+ "GROUP BY  PAT.PACKNR, PAT.MATNR, MAKT.MAKTX, PAT.TRGQTY, PAT.BASEUNIT, PAT.PACKITEM, PAT.PAITEMTYPE ";

	public static final String GET_ALL_INFO_MATERIAL = "SELECT MR.MATNR, MK.MAKTX, MR.MEINS, MM.MEINH, MM.UMREZ, "
			+ "MM.UMREN, MR.EANNR, MR.EAN11  FROM MARA MR WITH(NOLOCK) INNER JOIN MAKT MK ON MR.MATNR = MK.MATNR "
			+ "INNER JOIN MARM MM ON MR.MATNR = MM.MATNR "
			+ "GROUP BY MR.MATNR, MK.MAKTX, MR.MEINS, MM.MEINH, MM.UMREZ, MM.UMREN, MR.EANNR, MR.EAN11";

	public List<MaterialTarimasBean> getAllMaterialCrossTarimas(Connection con) throws InvCicException {
		List<MaterialTarimasBean> materialTarimasList = new ArrayList<>();
		try {
			PreparedStatement stm = con.prepareStatement(GET_MATERIALES_TARIMAS);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				MaterialTarimasBean bean = new MaterialTarimasBean();
				bean.setPacknr(rs.getString("PACKNR"));
				bean.setVhilm(rs.getString("VHILM"));
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
		return materialTarimasList;
	}

	public List<MobileMaterialBean> getAllMaterialMobile(Connection con) throws InvCicException {
		List<MobileMaterialBean> mobileMaterialList = new ArrayList<>();
		try {
			PreparedStatement stm = con.prepareStatement(GET_ALL_INFO_MATERIAL);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				MobileMaterialBean bean = new MobileMaterialBean();
				bean.setMatnr(rs.getString("MATNR"));
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
		return mobileMaterialList;
	}

}
