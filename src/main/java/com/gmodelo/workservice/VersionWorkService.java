package com.gmodelo.workservice;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.gmodelo.beans.AbstractResultsBean;
import com.gmodelo.beans.Response;
import com.gmodelo.dao.VersionDao;
import com.gmodelo.utils.ReturnValues;

public class VersionWorkService {
	
	private Logger log = Logger.getLogger(VersionWorkService.class.getName());
	
	public Response<Object> getVersion(HttpServletRequest request){
		log.log(Level.WARNING,"[getVersionService] "+ request.toString());
		Response<Object> res = null;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		 
		if (request.getSession().getAttribute("user") != null) {
			res = new VersionDao().getVersion();
		}else{
			log.info("[logout] No session to close.");
			abstractResult.setResultId(ReturnValues.IUSERNOSESSION);
			abstractResult.setResultMsgAbs(ReturnValues.SUSERNOSESSION);
			res.setAbstractResult(abstractResult);
		}
		return res;
	}
}
