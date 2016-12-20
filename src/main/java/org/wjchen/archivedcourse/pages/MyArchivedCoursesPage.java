package org.wjchen.archivedcourse.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.resource.PackageResourceReference;

import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel;
import org.wjchen.archivedcourse.apps.CourseArchiveSession;
import org.wjchen.archivedcourse.models.CoursePack;
import org.wjchen.archivedcourse.models.CoursePackViewList;
import org.wjchen.archivedcourse.panels.CoursePackViewPanel;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
@RequireHttps
@AuthorizeInstantiation("SIGNED_IN")
public class MyArchivedCoursesPage extends BasePage {

	// private transient CoursePack pack;

	private static final long serialVersionUID = -6461650845514212496L;

	public class NavBarContainer extends WebMarkupContainer {

		private static final long serialVersionUID = 1L;
		
		public NavBarContainer(String id) {
			super(id);
			
			setOutputMarkupId(true);
			
			add(new Label("project-title-label", new ResourceModel("project.title")));

			Link<Void> logoutLink = new Link<Void>("project-logout") {
				private static final long serialVersionUID = 1L;

				public void onClick() {
					throw new RedirectToUrlException("/logout");
				}
			};
			add(logoutLink);
		}

	}
	
	public class SearchContainer extends WebMarkupContainer {

		private static final long serialVersionUID = 1L;

		private final class SearchForm extends Form<CoursePackViewList> {
			private static final long serialVersionUID = 1L;

			public SearchForm(String id, CompoundPropertyModel<CoursePackViewList> model) {
				super(id);

				setOutputMarkupId(true);

				final CoursePackViewList viewList = model.getObject();

				// Instructor Field
				final TextField<String> instructorField = new TextField<String>(
						"search-instructor-textfield", new Model<String>(""));
				instructorField.setOutputMarkupId(true);
				add(instructorField);
				
				add(new Label("search-instructor-label", new ResourceModel(
						"search.instructor")));

				// Course Id Field
				final TextField<String> courseIdField = new TextField<String>(
						"search-course-id-textfield", new Model<String>(""));
				courseIdField.setOutputMarkupId(true);
				add(courseIdField);
				
				add(new Label("search-course-id-label", new ResourceModel(
						"search.course.id")));

				// Submit Button
//				IndicatingAjaxButton submitButton = new IndicatingAjaxButton("search-submit", this) {
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					protected void onError(AjaxRequestTarget target, Form<?> form) {
//						target.add(dataViewContainer);
//					}
//
//					@Override
//					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//						target.add(courseIdField);
//						target.add(instructorField);
//						target.add(dataViewContainer);
//
//						String courseInput = courseIdField.getModelObject();
//						String instrInput = instructorField.getModelObject();
//
//						List<CoursePack> formResults = getCourseService().getCoursePacks(courseInput, instrInput, user);
//						viewList.addCourses(formResults);
//					}
//				};
				Button submitButton = new Button("search-submit") {

					private static final long serialVersionUID = 1L;
					
					@Override
					public void onSubmit() {
						String courseInput = courseIdField.getModelObject();
						String instrInput = instructorField.getModelObject();
						viewList.clear();
						List<CoursePack> formResults = getCourseService().getCoursePacks(courseInput, instrInput, user);
						viewList.addCourses(formResults);						
					}
				};
				add(submitButton);
			}
		}

		public SearchContainer(String id,
				CompoundPropertyModel<CoursePackViewList> myModel,
				CompoundPropertyModel<CoursePackViewList> daModel) {
			super(id);

			setOutputMarkupId(true);

			// Search Instruction
			add(new Label("search-instruction",	new ResourceModel("search.instruction")));
			
			SearchForm searchForm = new SearchForm("search-course-form", daModel);
			if (user == null || ((user != null) && ! getCourseService().hasDelegatedAccess(user))) {
				searchForm.setVisible(false);
			}
			add(searchForm);

			CoursePackViewList myViewList = myModel.getObject();
			String courseInput = null;
			if ((user != null) && (getCourseService() != null)) {
				List<CoursePack> userResults = getCourseService().getCoursePacks(courseInput, user, user);
				myViewList.addCourses(userResults);
			}
		}

	}

	public class ViewDataContainer extends WebMarkupContainer {

		private static final long serialVersionUID = 1L;

		public ViewDataContainer(String id,
				final CompoundPropertyModel<CoursePackViewList> myModel,
				final CompoundPropertyModel<CoursePackViewList> daModel) {
			super(id);

			setOutputMarkupId(true);

			List<ITab> tabs = new ArrayList<ITab>();

			tabs.add(new AbstractTab(new Model<String>("My Course Archives")) {
				private static final long serialVersionUID = 1L;

				@Override
				public Panel getPanel(String panelId) {
					return new CoursePackViewPanel(panelId, myModel);
				}
			});			

			if(getCourseService().hasDelegatedAccess(user)) {
				tabs.add(new AbstractTab(new Model<String>("DA Course Archives")) {
					private static final long serialVersionUID = 1L;

					@Override
					public Panel getPanel(String panelId) {
						return new CoursePackViewPanel(panelId, daModel);
					}
				});
			}
			
			BootstrapTabbedPanel<ITab> dataPanel = new BootstrapTabbedPanel<ITab>("data-view-tabs", tabs);
			if(getCourseService().hasDelegatedAccess(user)) {
				dataPanel.setSelectedTab(1);
			}
			else {
				dataPanel.setSelectedTab(0);
			}
			add(dataPanel);
			
		}
	}

	private CoursePackViewList myList;
	private CoursePackViewList daList;
	private NavBarContainer navBarContainer;
	private SearchContainer searchContainer;
	private ViewDataContainer dataViewContainer;
	private String user = null;

	public MyArchivedCoursesPage() {
		super();

		log.info("init");

		myList = new CoursePackViewList();
		daList = new CoursePackViewList();

		CourseArchiveSession session = CourseArchiveSession.get();
		user = session.getUser();
		
		CompoundPropertyModel<CoursePackViewList> myModel = new CompoundPropertyModel<CoursePackViewList>(
				myList);
		CompoundPropertyModel<CoursePackViewList> daModel = new CompoundPropertyModel<CoursePackViewList>(
				daList);

		navBarContainer = new NavBarContainer("navbar-container");
		add(navBarContainer);
		navBarContainer.setVisible(!session.isLTI());
		
		FeedbackPanel feedback = new FeedbackPanel("feedback-panel");
		feedback.setMaxMessages(1);
		add(feedback);
		
		searchContainer = new SearchContainer("search-container", myModel, daModel);
		add(searchContainer);

		dataViewContainer = new ViewDataContainer("data-container", myModel, daModel);
		add(dataViewContainer);

	}

	@Override
	public void renderHead(IHeaderResponse response) {	
		super.renderHead(response);
		
	    response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(this.getClass(), "MyArchivedCoursesPage.js")));
	    response.render(CssHeaderItem.forReference(new PackageResourceReference(this.getClass(), "MyArchivedCoursesPage.css")));
	}

}
