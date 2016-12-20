package org.wjchen.archivedcourse.pages;

import org.apache.wicket.markup.html.WebPage;

import org.wjchen.archivedcourse.apps.CourseArchiveSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppSignInPage extends WebPage {

	private static final long serialVersionUID = 1L;

	public AppSignInPage() {
		super();
		
		CourseArchiveSession session = CourseArchiveSession.get();
		String sid = session.getId();
		if(sid != null) {
			session.invalidateNow();
			log.debug("Logout expired session: {}", sid);			
		}
	}

}
