package com.gmodelo.cyclicinventories.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gmodelo.cyclicinventories.beans.JustificationFile;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.JustficationFileWorkService;

@Path("/services/JustficationFileService")
public class JustficationFileService {
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/uploadFile")
	public Response<JustificationFile> uploadFile(Request request){
		return new JustficationFileWorkService().uploadFile(request);
	}

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getjsFileBase64")
	public Response<String> getjsFileBase64(Request request){
		
		return new JustficationFileWorkService().getjsFileBase64(request);
	}
}
