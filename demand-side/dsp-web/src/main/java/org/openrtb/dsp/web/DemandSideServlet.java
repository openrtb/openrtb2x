package org.openrtb.dsp.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// import org.openrtb.common.api.BlocklistAPI;
import org.openrtb.common.api.OpenRTBAPI;
import org.openrtb.dsp.core.DemandSideServer;
import org.openrtb.dsp.intf.model.DemandSideDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemandSideServlet extends HttpServlet {
	private static final long serialVersionUID = 2L;
	private static final Logger logger = LoggerFactory.getLogger(DemandSideServlet.class);
	private DemandSideServer dsp = null;
	// private BlocklistAPI blockListRequestor = null;
	private DemandSideDAO daoObject = null;

	public void init() throws javax.servlet.ServletException {
		try {
			OpenRTBAPI bidder = null;
			String classname = getServletConfig().getInitParameter("DAOClassName");
			String daoDBlocation = getServletConfig().getInitParameter("DBLocation");
			daoObject = (DemandSideDAO)  Class.forName(classname).newInstance();
			daoObject.loadData(daoDBlocation);	

			classname = getServletConfig().getInitParameter("BidderClassName");
			bidder = (OpenRTBAPI) Class.forName(classname).newInstance();
			/*	
			classname = getServletConfig().getInitParameter("BatchImplClassName");
			if ((classname != null) || (classname != "")) {
				blockListRequestor = (BlocklistAPI)  Class.forName(classname).newInstance();
			} */
			dsp = new DemandSideServer(bidder, daoObject);

		} catch (Exception e) {
			throw new ServletException(e.getMessage());
		} 
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String sspOrgName = request.getParameter("ssp_name");
			if ((sspOrgName == null) || (sspOrgName == "")) {
				String errMsg = "400 Bad Request: Required parameter ssp_name is missing or empty in Request URI";
				logger.error(errMsg);
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, errMsg );
				return;
			}
			if (!dsp.authorizeRemoteService(sspOrgName)) {
				logger.error("401 Unauthorized");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unknown Sender");
			} else {
				String requestContentType = request.getContentType();
				if (!dsp.verifyContentType(sspOrgName, requestContentType)) {
					String errMsg = "415 Unsupported media type: Unexpected Content type in Servlet Request: " 
										+ requestContentType;
					logger.error(errMsg);
					response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, errMsg);
				} else {
					response.setContentType(requestContentType);

					byte[] responseBuff = dsp.respond(sspOrgName, request.getInputStream(), 
							requestContentType);
					if (responseBuff == null) {
						logger.error("400 Bad Request: Format error in request");
						response.sendError(HttpServletResponse.SC_BAD_REQUEST);
					}
					else {
						if (responseBuff.length == 0) {
							logger.info("204 No content: Sending empty response");
							response.setStatus(HttpServletResponse.SC_NO_CONTENT);
						} else {
							logger.info("200 OK: Sent Response");
							response.setContentLength(responseBuff.length);
							response.getOutputStream().write(responseBuff);
						}
					}
				}
			}
			response.flushBuffer();
		} catch (IOException ie) { 
			throw new ServletException(ie);
		} catch (Exception e) {
			logger.error("400 Bad Request: Error in processing request: ", e.getMessage());
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} 
	}
}
