package org.wjchen.archivedcourse.apps;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.stereotype.Component;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.less.BootstrapLess;
import org.wjchen.archivedcourse.pages.CourseArchiveInternalErrorPage;
import org.wjchen.archivedcourse.pages.MyArchivedCoursesPage;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
@Component(value = "courseArchiveApplication")
public class CourseArchiveApplication extends AuthenticatedWebApplication {

	private static String JQUERY_JS = "js/jquery-1.11.3.min.js";

	protected void init() {
		log.info("init");
		super.init();
		
		// Runtime error handle
		getRequestCycleListeners().add(new AbstractRequestCycleListener() { 
            @Override 
            public IRequestHandler onException(RequestCycle cycle, Exception ex) {             	
            	if (ex instanceof WicketRuntimeException) {
            		log.error(ex.getMessage());
            		// print stack trace
                    StringWriter sw = new StringWriter(1024);
                    PrintWriter pw = new PrintWriter(sw);
            		ex.printStackTrace(pw);
            		pw.flush();
            		log.error(sw.toString());
            		pw.close();
            		try {
						sw.close();
					} catch (IOException e) {
						log.error(e.getMessage());
					}
            		return new RenderPageRequestHandler(new PageProvider(CourseArchiveInternalErrorPage.class)); 
            	}
            	
				return super.onException(cycle, ex);
            } 
		}); 
		
		// Authorization Annotation
		getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy(this));
		
		// Bootstrap
		configureBootstrap();
		
		// Javascript
		JavaScriptResourceReference jquery_js_ref = new JavaScriptResourceReference(this.getClass(), JQUERY_JS);
		getResourceBundles().addJavaScriptBundle(this.getClass(), "javascript", jquery_js_ref);

		// Spring Injection
		getComponentInstantiationListeners().add(new SpringComponentInjector(this));

		// Resource Settings
		getResourceSettings().setThrowExceptionOnMissingResource(true);
		
		// Markup Settings
		getMarkupSettings().setStripWicketTags(true);
		
		// Mount Web Pages
		mountPage("myCourseArchive", MyArchivedCoursesPage.class);
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return MyArchivedCoursesPage.class; 
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new CourseArchiveSession(request);
	}
	
	// Expose Application itself
	public static CourseArchiveApplication get() {
		return (CourseArchiveApplication) Application.get();
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return CourseArchiveSession.class;
	}

    private void configureBootstrap() {
        
        final IBootstrapSettings settings = new BootstrapSettings();
        settings.useCdnResources(true);
        
        Bootstrap.install(this, settings);
        BootstrapLess.install(this);
    }

}
