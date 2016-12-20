package org.wjchen.archivedcourse.apps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * class ExternalDocBaseServlet
 *
 * This servlet is for putting file content into web page due to Tomcat before 7.x does not have the 
 * ability to link external resource into a web application. web.xml setup is the followings:
 * <?xml version="1.0" encoding="ISO-8859-1"?>
 * <web-app xmlns="http://java.sun.com/xml/ns/j2ee"
 *          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 *          xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd"
 *          version="2.4">
 * ...
 * ...
 * <!-- Last servlet in the webapp -->
 * <servlet>
 *      <servlet-name>externalDocBaseServlet</servlet-name>
 *      <servlet-class>org.wjchen.archivepack.apps.ExternalDocBaseServlet</servlet-class>
 *      <init-param>
 *      	<param-name>ExternalDocBases</param-name>
 *      	<param-value>/file/path/dir1:/file/path/dir2</param-value>
 *      </init-param>
 * </servlet>
 * 
 * <servlet-mapping>
 *      <servlet-name>externalDocBaseServlet</servlet-name>
 *      <url-pattern>/*</url-pattern>
 * </servlet-mapping>
 * 
 * </web-app>
 *  
 */
@Slf4j
public class ExternalDocBaseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static String EXTERNAL_DOC_BASES = "ExternalDocBases";
	private static String APP_ROOT = "AppRoot";
	
	private String[] externalDocBases;
	private String appRoot;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		     throws IOException {
		// get the servlet init-param value
		externalDocBases = this.getInitParameter(EXTERNAL_DOC_BASES).split(":");
		if(externalDocBases == null) {
			String message = "Parameter " + EXTERNAL_DOC_BASES + " must set."; 
			log.error(message);
			throw new IOException(message);
		}
		
		appRoot = this.getInitParameter(APP_ROOT);
		if(appRoot == null) {
			String message = "Parameter " + APP_ROOT + " must set."; 
			log.error(message);
			throw new IOException(message);			
		}
		
		// get file name from request
		String uri = URLDecoder.decode(request.getRequestURI(), "UTF-8");
		log.debug("uri: " + uri);
		String fileName = uri.substring(uri.indexOf(appRoot) + appRoot.length());
		
		File file = null;
		// find file name on file system
		for(String docBase : externalDocBases) {	
			File testFile = new File(docBase, fileName);		
			log.debug("Test File: " + testFile.getAbsolutePath());
			if (testFile != null) {
				boolean isFile = testFile.getAbsoluteFile().isFile();
				log.debug("isFile: " + isFile);
				boolean canRead = testFile.getAbsoluteFile().canRead();
				log.debug("canRead: " + canRead);
				
				if(isFile && canRead) {
					file = testFile;
					break;
				}
			}
		}
		
		if(file == null) {
			log.error("Could not find the file, " + fileName);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		log.debug("file = " + file.getAbsolutePath());
		
		// retrieve mime type of the file
		String mimeType = this.getServletContext().getMimeType(file.getAbsolutePath());
		if(mimeType == null) {
			String message = "Could not get the MIME type of [" + file.getAbsolutePath() + "]";
			log.error(message);
			this.getServletContext().log(message);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		// response setting
		response.setContentType(mimeType);
		response.setContentLength((int)file.length());
		
		// copy file content to response output
		FileInputStream in = new FileInputStream(file);
		OutputStream out = response.getOutputStream();
		
		byte[] buf = new byte[1024];
		int count = 0;
	    while ((count = in.read(buf)) >= 0) {
	         out.write(buf, 0, count);
	    }
	    in.close();
	    out.close();		
	}
}
