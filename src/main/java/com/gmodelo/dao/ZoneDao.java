package com.gmodelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.ZoneBean;
import com.gmodelo.utils.ConnectionManager;
import com.gmodelo.utils.ReturnValues;

public class ZoneDao {
	
	private Logger log = Logger.getLogger( ZoneDao.class.getName());
	
public Response<List<ZoneBean>> getZoneByLgort(ZoneBean zoneBean){
		
		Response<List<ZoneBean>> res = new Response<>();
		AbstractResults abstractResult = new AbstractResults();
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection(ConnectionManager.connectionBean);
		CallableStatement cs = null;
		List<ZoneBean> listZone = new ArrayList<ZoneBean>();
		
		final String INV_SP_ZONE_BY_GORT = "INV_SP_ZONE_BY_GORT ?, ?, ?, ?, ?"; //The Store procedure to call
		
		log.log(Level.WARNING,"[getZoneByLgort] Preparing sentence...");
		
		try {
			cs = con.prepareCall(INV_SP_ZONE_BY_GORT);
			
			if(zoneBean.getLgort() != null){
				cs.setString(1,zoneBean.getLgort());
			}else{
				cs.setNull(1, Types.INTEGER);
			}
			if(zoneBean.getIdZone() != null){
				cs.setInt(2,zoneBean.getIdZone());
			}else{
				cs.setNull(2, Types.INTEGER);
			}
			if(zoneBean.getZoneDesc() != null){
				cs.setString(3,zoneBean.getZoneDesc());
			}else{
				cs.setNull(3, Types.INTEGER);
			}
			if(zoneBean.getBukrs() != null){
				cs.setString(4,zoneBean.getBukrs());
			}else{
				cs.setNull(4, Types.INTEGER);
			}
			if(zoneBean.getWerks() != null){
				cs.setString(5,zoneBean.getWerks());
			}else{
				cs.setNull(5, Types.INTEGER);
			}
			
			log.log(Level.WARNING,"[getZoneByLgort] Executing query...");
			
			ResultSet rs = cs.executeQuery();
			
			while (rs.next()){
				
				zoneBean = new ZoneBean();
				
				zoneBean.setLgort(rs.getString(1));
				zoneBean.setIdZone(rs.getInt(2));
				zoneBean.setZoneDesc(rs.getString(3));
				zoneBean.setBukrs(rs.getString(4));
				zoneBean.setWerks(rs.getString(5));
				
				listZone.add(zoneBean);
				
			}
			
			//Retrive the warnings if there're
			SQLWarning warning = cs.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING,warning.getMessage());
				warning = warning.getNextWarning();
			}
			
			//Free resources
			rs.close();
			cs.close();	
			
			log.log(Level.WARNING,"[getZoneByLgort] Sentence successfully executed.");
			
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getZoneByLgort] Some error occurred while was trying to execute the S.P.: INV_SP_ZONE_BY_GORT ?, ?, ?, ?, ?", e);
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			res.setAbstractResult(abstractResult);
			return res;
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE,"[getZoneByLgort] Some error occurred while was trying to close the connection.", e);
				abstractResult.setResultId(ReturnValues.IEXCEPTION);
				abstractResult.setResultMsgAbs(e.getMessage());
				res.setAbstractResult(abstractResult);
				return res;
			}
		}
		res.setAbstractResult(abstractResult);
		res.setLsObject(listZone);
		return res ;
	}


}
