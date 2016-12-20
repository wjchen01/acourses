package org.wjchen.archivedcourse.apps;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import org.wjchen.archivedcourse.logics.CoursePackService;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Component(value = "userContentAuthorizer")
public class UserContentAuthorizeFilter extends GenericFilterBean {
	
	@Autowired
	private CoursePackService packService;

	private final static String[] Permissions = {
		"role:EditAllAdmin", 
		"role:ViewOnlyAdmin"
	};

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		CourseArchiveSession session = CourseArchiveSession.get();
		String uni = session.getUser();
		if(uni == null) {
			doError(request, response, "User does not login");
		} else {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String uri = httpRequest.getRequestURI();
			String coursePath = uri.substring(uri.indexOf("course_packs/")).replaceFirst("course_packs/", "");
			String[] pathElments = coursePath.split("/");
			String courseId = pathElments[0];
			
			if(!packService.isGrantAccess(courseId, uni, true, Permissions)) {
				doError(request, response, "You have no permission to view this content");
			}
			else {
				chain.doFilter(request, response);				
			}
		}
	}

	private void doError(ServletRequest request, ServletResponse response, String message) throws IOException {
		PrintWriter writer = response.getWriter();
		writer.print("<h1>Deny Access</h1>");
		writer.print("<h2>" + message + "</h2>");
	}

	
}
