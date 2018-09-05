package com.gmodelo.services;

import javax.json.stream.JsonParsingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.gmodelo.beans.Lgnum;
import com.gmodelo.beans.Request;
import com.google.gson.Gson;

@Path("/STest")
public class STest {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/lgnum")
	public String mTest(Request resquet) {

		Gson gson = new Gson();
		Lgnum lgNum = new Lgnum();
		
		try {
			lgNum = gson.fromJson(gson.toJson(resquet.getLsObject()), Lgnum.class);
		} catch (JsonParsingException e) {
			e.printStackTrace();
		}

		// lgNum = new Gson().fromJson(resquet.getLsObject().toString(),
		// Lgnum.class);
		System.out.println(lgNum.getLgnum());
		return new Gson().toJson(resquet.getLsObject());
	}

}
