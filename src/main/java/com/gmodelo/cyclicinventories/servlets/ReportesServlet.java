package com.gmodelo.cyclicinventories.servlets;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bmore.ume001.beans.User;
import com.gmodelo.cyclicinventories.beans.DocInvBean;
import com.gmodelo.cyclicinventories.beans.ReporteDocInvBean;
import com.gmodelo.cyclicinventories.beans.ReporteDocInvBeanHeader;
import com.gmodelo.cyclicinventories.beans.Request;
import com.gmodelo.cyclicinventories.beans.Response;
import com.gmodelo.cyclicinventories.workservice.ReportesWorkService;
import com.gmodelo.cyclicinventories.workservice.RouteWorkService;

/**
 * Servlet implementation class ReportesServlet
 */
@WebServlet("/ReportesServlet")
public class ReportesServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(RouteWorkService.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReportesServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override 
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override 
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doIdentifyRequest(request, response);
	}

	protected void doIdentifyRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			String doRequest = request.getParameter("REQUEST");
			try {
				DocInvBean docInvBean = new DocInvBean();
				docInvBean.setDocInvId(Integer.parseInt(doRequest));
				Request<DocInvBean> requestDoc =  new Request<>();
				requestDoc.setLsObject(docInvBean);
				requestDoc.setTokenObject(null);	
				Response<ReporteDocInvBeanHeader> reportInv = new ReportesWorkService().getReporteDocInv(requestDoc);
				generateDocInvReporte(request, response, reportInv.getLsObject());
			} catch (Exception e) {
				// Fill With Another Object Report
			}
		} else {
			response.getWriter().write("Usuario no logueado");
		}

	}

	private static final int BUFSIZE = 4096;

	private void generateDocInvReporte(HttpServletRequest request, HttpServletResponse response,
			ReporteDocInvBeanHeader reportInv) throws ServletException, IOException {
		PrintWriter writer = null;
		String filePath = "DocumentoInventario" + reportInv.getDocInvId() + ".csv";
		try {
			writer = new PrintWriter(
					new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(filePath)), "ISO-8859-1"));
			writer.append("Documento de Inventario:," + reportInv.getDocInvId()).println();
			writer.append("Sociedad:, " + reportInv.getBukrs() + "-" + reportInv.getBukrsD()).println();
			writer.append("Centro:," + reportInv.getWerks() + "-" + reportInv.getWerksD()).println();
			writer.append("Fecha de Creacion:, " + reportInv.getCreationDate()).println();
			writer.append("Fecha de Conciliacion:," + reportInv.getConciliationDate()).println();
			writer.append("Ruta:," + reportInv.getRoute()).println();
			writer.append("Tipo de Documento:," + (reportInv.getType().equals("1") ? "Diario" : "Mensual")).println();
			writer.println();
			writer.println();
			writer.append(new ReporteDocInvBean().supHeadString()).println();
			for (ReporteDocInvBean singleBean : reportInv.getDocInvPosition()) {
				writer.append(singleBean.supString()).println();
			}
			writer.flush();
			writer.close();
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				log.log(Level.WARNING, "[ReportesServlet generateDocInvReporte]", e);
			}
			File file = new File(filePath);
			ServletOutputStream outputStream = response.getOutputStream();
			ServletContext context = getServletConfig().getServletContext();
			String mimetype = context.getMimeType(filePath);
			mimetype = "application/octet-stream";
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			response.setContentType(mimetype);
			response.setContentLength((int) file.length());
			int length = 0;
			byte[] byteBuffer = new byte[BUFSIZE];
			DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
			while ((dataInputStream != null) && ((length = dataInputStream.read(byteBuffer)) != -1)) {
				outputStream.write(byteBuffer, 0, length);
			}
			dataInputStream.close();
			outputStream.close();
			// FIN Mostrar en pantalla PDF concatenado
			try {
				file.delete();
			} catch (Exception e) {
				log.log(Level.WARNING, "[ReportesServlet generateDocInvReporte]", e);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "[ReportesServlet generateDocInvReporte]", e);
			response.getWriter().write("Ocurrio un error al generar el archivo:" + e);
		}

	}

}
