package org.wjchen.archivedcourse.apps;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CASLogOutFilter implements Filter {
	
	private static Log log = LogFactory.getLog(CASLogOutFilter.class);

	private String logoutUrl = null;
	
	public static String LOGOUT_URL = "logout-url";
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hsq = (HttpServletRequest) request;
		String contextPath = hsq.getContextPath();
		
		String returnUrl = request.getScheme() + "://" + request.getServerName() 
						+ ":" + request.getServerPort() 
						+ contextPath;
		
		HttpSession session = hsq.getSession();
		String remoteUser = (String) session.getAttribute("uni");
		log.info("User, " + remoteUser + " logout.");

		HttpServletResponse hsp = (HttpServletResponse) response;		
		log.debug("returnUrl = " + returnUrl);
		hsp.sendRedirect(hsp.encodeRedirectURL(logoutUrl + "?service=" + returnUrl));
		
		// remove session information
		hsq.getSession().invalidate();
		
		return;
	}

	@Override
	public void destroy() {		
		log.info("shutdown logout filter");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.logoutUrl = filterConfig.getInitParameter(LOGOUT_URL);
		if((this.logoutUrl == null) || this.logoutUrl.equals("")) {
			String message = "Parameter " + LOGOUT_URL + " is required.";
			log.error(message);
			throw new ServletException(message);
		}
	}

}
