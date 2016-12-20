package org.wjchen.archivedcourse.apps;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
@Component(value = "userEvaluationAuthorizer")
public class UserEvaluationAuthorizeFilter extends GenericFilterBean {
	
	private static final String RegPath = "(eval|ind_eval|onepage_eval)/(\\S+\\.html)";
	private static final String RegCrsFile = "\\d+_(\\S+)_\\w\\.html";
	private static final String RegIndFile = "(\\d+)?_(\\d+)_(\\d+)_\\S+\\.html";
	private static final String RegIndCoreFile = "(\\d+)_(\\d+)_core_\\S+\\.html";
	private static Pattern PatPath = Pattern.compile(RegPath);
	private static Pattern PatCrsFile = Pattern.compile(RegCrsFile);
	private static Pattern PatIndFile = Pattern.compile(RegIndFile);
	private static Pattern PatIndCoreFile = Pattern.compile(RegIndCoreFile);
	
	private final static String[] Permissions = {
		"role:EditAllAdmin", 
		"role:EditLimitedAdmin", 
		"role:ViewOnlyAdmin"
	};
	
	@Autowired
	private CoursePackService packService;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		CourseArchiveSession session = CourseArchiveSession.get();
		String uni = session.getUser();
		if(uni == null) {
			doError(request, response, "User does not login");
		} else {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String uri = httpRequest.getRequestURI();
			
			String courseId = getCourseId(uri);
			
			if(courseId != null && !packService.isGrantAccess(courseId, uni, true, Permissions)) {
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

	private String getCourseId(String uri) {
		String courseId = null;
		Matcher matPath = PatPath.matcher(uri);
		if(matPath.find()) {
			String evalType = matPath.group(1);
			String fileName = matPath.group(2);
			if(evalType.equals("eval")) {
				Matcher matCrsFile = PatCrsFile.matcher(fileName);
				if(matCrsFile.find()) {
					courseId = matCrsFile.group(1);
				}
			}
			else {
				Matcher matIndFile = PatIndFile.matcher(fileName);
				Matcher matIndCoreFile = PatIndCoreFile.matcher(fileName);
				if(matIndFile.find()) {
					String id = matIndFile.group(2);
					courseId = packService.getCourseById(new Long(id)).getUniqueId();
				}
				else if(matIndCoreFile.find()) {
					String id = matIndCoreFile.group(1);
					courseId = packService.getCourseById(new Long(id)).getUniqueId();
				}
			}
		}
		
		return courseId;
	}
}
