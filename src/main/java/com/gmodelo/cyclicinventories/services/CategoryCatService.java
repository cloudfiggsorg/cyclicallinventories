package com.gmodelo.cyclicinventories.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.Category;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.CategoryCatWorkService;

@Path("/services/CategoryCatService")
public class CategoryCatService {

	@Context
	private HttpServletRequest httpRequest;	
	
	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveCategory")
	public Response<Category> saveCategory(Request request) {
		User user = (User) httpRequest.getSession().getAttribute("user");
		return new CategoryCatWorkService().saveCategory(request, user.getEntity().getIdentyId());
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteCategories")
	public Response<Object> deleteCategories(Request request) {
		return new CategoryCatWorkService().deleteCategories(request);
	}

	@SuppressWarnings("rawtypes")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCategories")
	public Response<List<Category>> getCategories(Request request) {
		return new CategoryCatWorkService().getCategories();
	}
}
