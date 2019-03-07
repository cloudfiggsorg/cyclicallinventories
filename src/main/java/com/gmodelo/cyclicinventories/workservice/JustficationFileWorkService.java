package com.gmodelo.cyclicinventories.workservice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;

import com.gmodelo.cyclicinventories.beans.AbstractResultsBean;
import com.gmodelo.cyclicinventories.beans.JustificationFile;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.dao.SapConciliationDao;
import com.gmodelo.cyclicinventories.dao.SapOperationDao;
import com.gmodelo.cyclicinventories.utils.ConnectionManager;
import com.gmodelo.cyclicinventories.utils.ReturnValues;
import com.gmodelo.cyclicinventories.utils.Utilities;
import com.google.gson.Gson;

public class JustficationFileWorkService {
	
	private static Logger log = Logger.getLogger(JustficationFileWorkService.class.getName());
	private Gson gson = new Gson();
	private static String PATH_TO_SAVE_FILES = "";

	static {

		Utilities iUtils = new Utilities();
		Connection con = new ConnectionManager().createConnection();

		try {

			PATH_TO_SAVE_FILES = iUtils.getValueRepByKey(con, ReturnValues.PATH_TO_SAVE_FILES).getStrCom1();
		} catch (Exception e) {

			log.info("Some error occurred whiles was trying to get the path...");
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.info("Some error occurred while was trying to close the DB.");
			}
		}
	}
	
	public Response<JustificationFile> uploadFile(Request request) {
		
		//log.info("[uploadFile] " + request.toString());
		JustificationFile jsFile = null;
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		Response<JustificationFile> res = new Response<>();
		File file;
		byte[] bytes;
		
		try {
			
			jsFile = gson.fromJson(gson.toJson(request.getLsObject()), JustificationFile.class);
			
			file = new File(PATH_TO_SAVE_FILES + File.separator + jsFile.getDocInvId() + File.separator
					+ jsFile.getJsId() + File.separator + jsFile.getFileName());
			bytes = Base64.getDecoder().decode(jsFile.getBase64());
			FileUtils.writeByteArrayToFile(file, bytes);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			// Delete directory if was created
			File directory = new File(PATH_TO_SAVE_FILES + File.separator + "Files" + File.separator + jsFile.getDocInvId());

			if (directory.exists()) {
				directory.delete();
			}
			
			//Delete the records inserted
			Response<Object> respAux = new SapOperationDao().deleteConciliationSAP(jsFile.getDocInvId());
			if(respAux.getAbstractResult().getResultId() != 1){
				abstractResult.setResultId(respAux.getAbstractResult().getResultId());
				abstractResult.setResultMsgAbs(respAux.getAbstractResult().getResultMsgAbs());
			}
			
			log.info("[uploadFile] Error al tratar de grabar el archivo.");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);			
		}
		
		res.setAbstractResult(abstractResult);

		return res;
	}
	
	public Response<String> getjsFileBase64(Request<ArrayList<String>> request) {

		log.info("[getjsFileBase64] " + request.toString());
		Response<String> res = new Response<>();
		AbstractResultsBean abstractResult = new AbstractResultsBean();
		int docInvId = 0;
		int jsId = 0;
		String fileName = "";

		try {
			
			docInvId = Integer.parseInt(request.getLsObject().get(0));
			jsId = Integer.parseInt(request.getLsObject().get(1));
			fileName = (String) request.getLsObject().get(2);
						
			String PATH = PATH_TO_SAVE_FILES + File.separator;
			PATH += docInvId + File.separator;
			PATH += jsId + File.separator;
			File folder;
			
			folder = new File(PATH);

			if (!folder.exists()) {
				log.severe("File not found");
				abstractResult.setResultId(ReturnValues.FILE_NOT_FOUND);
				abstractResult.setResultMsgAbs("File not found...");
				res.setAbstractResult(abstractResult);
				return res;
			}

			if (new File(PATH + fileName).isFile()) {

				String base64 = "";
				try {
					base64 = DatatypeConverter.printBase64Binary(Files.readAllBytes(Paths.get(PATH + fileName)));
				} catch (IOException e) {
					log.severe("Some error occurred while was trying to get the file " + e.getMessage());
					abstractResult.setResultId(ReturnValues.FILE_EXCEPTION);
					abstractResult.setResultMsgAbs(e.getMessage());
					res.setAbstractResult(abstractResult);
				}

				res.setLsObject(base64);
				res.setAbstractResult(abstractResult);

			} else {

				log.severe("File not found");
				abstractResult.setResultId(ReturnValues.FILE_NOT_FOUND);
				abstractResult.setResultMsgAbs("File not found...");
				res.setAbstractResult(abstractResult);
			}
			
		} catch (JSONException e) {
			log.log(Level.SEVERE, "[getjsFileBase64] Objeto no válido.");
			abstractResult.setResultId(ReturnValues.IEXCEPTION);
			abstractResult.setResultMsgAbs(e.getMessage());
			res.setAbstractResult(abstractResult);
		}

		return res;
	}
	
}
