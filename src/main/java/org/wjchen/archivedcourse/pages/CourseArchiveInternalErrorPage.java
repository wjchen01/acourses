package org.wjchen.archivedcourse.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.pages.InternalErrorPage;
import org.apache.wicket.model.ResourceModel;

import org.wjchen.archivedcourse.apps.CourseArchiveSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CourseArchiveInternalErrorPage extends InternalErrorPage {

	private static final long serialVersionUID = 1L;

	public CourseArchiveInternalErrorPage() {
		super();
		
		CourseArchiveSession session = CourseArchiveSession.get();
		String sid = session.getId();
		session.invalidateNow();
		
		log.debug("Logout internal error: {}", sid);
		
		add(new Label("coursearchive-internal-error", new ResourceModel("coursearchive.app.internalerror")));
	}

}
