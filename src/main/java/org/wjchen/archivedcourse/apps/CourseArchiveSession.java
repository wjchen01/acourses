package org.wjchen.archivedcourse.apps;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.imsglobal.basiclti.provider.api.BasicLtiContext;
import org.imsglobal.basiclti.provider.servlet.util.BasicLTIContextWebUtil;
import org.jasig.cas.client.authentication.AttributePrincipal;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
public class CourseArchiveSession extends AuthenticatedWebSession {

	private static final long serialVersionUID = -6472022331276302618L;
	private static final String PACK_BASE_PARAM = "packBase";
	private static final String EVAL_BASE_PARAM = "evalBase";
	private static final String PACK_ROOT_PARAM = "packRoot";
	private static final String EVAL_ROOT_PARAM = "evalRoot";
	
	private boolean LTI = false;
	private String user;
	private String packBase;
	private String packRoot;
	private String evalBase;
	private String evalRoot;
	private List<String> roleNms = new ArrayList<String>();
	
	CourseArchiveSession(Request request) {
		super(request);
		
		packBase = CourseArchiveApplication.get().getInitParameter(PACK_BASE_PARAM);
		evalBase = CourseArchiveApplication.get().getInitParameter(EVAL_BASE_PARAM);
		packRoot = CourseArchiveApplication.get().getInitParameter(PACK_ROOT_PARAM);
		evalRoot = CourseArchiveApplication.get().getInitParameter(EVAL_ROOT_PARAM);
		
		HttpServletRequest hsq = (HttpServletRequest) request.getContainerRequest();

		String loginUser = null;
		HttpSession session = hsq.getSession();		
		BasicLtiContext ltiContext = BasicLTIContextWebUtil.getBasicLtiContext(session);
		if(ltiContext != null) {
			String ltiUser = ltiContext.getLisPerson().getSourcedId();
			if(ltiUser != null) {
				this.setLTI(true);
				loginUser = ltiUser;
			}
		}

		AttributePrincipal principal = (AttributePrincipal)hsq.getUserPrincipal();
		String casUser = principal.getName();
		
		log.debug("CAS Login User = " + casUser);

		if(casUser != null) {
			if(loginUser == null) {
				loginUser = casUser;
			}			
		}
		
		signIn(loginUser, null);
		log.info("Login - authenticated user: " + casUser + " as user: " + user);
	}
	
	@Override
	public final boolean authenticate(String username, String password) {
		boolean authenticated = false;
		
		if(username != null) {
			String existUser = this.getUser();
			if(existUser == null) {
				this.setUser(username);
				authenticated = true;
			}
			else {
				authenticated = existUser.equals(username);
			}
		}
		log.debug(username + ": " + authenticated);
		return authenticated;
	}
	
	public static CourseArchiveSession get() {
		if(AuthenticatedWebSession.exists()) {
			CourseArchiveSession existed = (CourseArchiveSession) AuthenticatedWebSession.get();			
			RequestCycle requestCycle = RequestCycle.get();
			if(requestCycle != null) {
				Request request = requestCycle.getRequest();
				HttpServletRequest hsq = (HttpServletRequest) request.getContainerRequest();
				HttpSession session = hsq.getSession();		
				BasicLtiContext ltiContext = BasicLTIContextWebUtil.getBasicLtiContext(session);
				if(ltiContext != null) {
					String ltiUser = ltiContext.getLisPerson().getSourcedId();
					if(! existed.getUser().equals(ltiUser)) {
						log.debug("Previous user is: " + existed.getUser());
						existed.setUser(ltiUser);
						existed.replaceSession();
						log.debug("Current user is: " + existed.getUser());
					}
				}
			}
			
			return existed;
		}
		else {
			return (CourseArchiveSession) AuthenticatedWebSession.get();
		}
	}

	@Override
	public Roles getRoles() {
		Roles roles = new Roles();
		
		if(isSignedIn()) {
			roles.add("SIGNED_IN");
		}		

		return roles;
	}

}
