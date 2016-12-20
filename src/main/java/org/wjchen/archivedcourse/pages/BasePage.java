package org.wjchen.archivedcourse.pages;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.core.Bootstrap;
import org.wjchen.archivedcourse.apps.CourseArchiveApplication;
import org.wjchen.archivedcourse.apps.CourseArchiveSession;
import org.wjchen.archivedcourse.logics.CoursePackService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public abstract class BasePage extends WebPage implements IHeaderContributor {

	private static final long serialVersionUID = 6130209537998455547L;
	
	@SpringBean
	protected CoursePackService courseService;

	public BasePage() {
		log.info("init");
		
		CourseArchiveSession session = CourseArchiveSession.get();
		if(session.isSessionInvalidated()) {
			session = (CourseArchiveSession) CourseArchiveApplication.get().newSession(this.getRequest(), this.getResponse());
		}
	}
	
	public void renderHead(IHeaderResponse response) {	
		super.renderHead(response);

        Bootstrap.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings().getJQueryReference()));
	}
	
}
