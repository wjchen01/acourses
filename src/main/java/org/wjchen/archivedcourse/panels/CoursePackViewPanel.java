package org.wjchen.archivedcourse.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import org.wjchen.archivedcourse.models.AnnualPack;
import org.wjchen.archivedcourse.models.CoursePackViewList;

public class CoursePackViewPanel extends Panel {

	private static final long serialVersionUID = -343790583184998642L;

	public CoursePackViewPanel(String id, CompoundPropertyModel<CoursePackViewList> model) {
		super(id, model);

		setOutputMarkupId(true);
		
		final CoursePackViewList viewList = model.getObject();
		
		// Header
		Label l_course_number = new Label("item-header-course-number-label", 
				new StringResourceModel("table.header.course.number.${hasCourses}", this, 
						new Model<CoursePackViewList>(viewList)));
		add(l_course_number);
		
		Label l_course_title = new Label("item-header-course-title-label", 
				new StringResourceModel("table.header.course.title.${hasCourses}", this, 
						new Model<CoursePackViewList>(viewList)));				  
		add(l_course_title);

		Label l_archive_content = new Label("item-header-archive-content-label", 
				new StringResourceModel("table.header.arch.content.${hasCourses}", this, 
						new Model<CoursePackViewList>(viewList)));				  
		add(l_archive_content);

		Label l_archive_eval = new Label("item-header-archive-eval-label", 
				new StringResourceModel("table.header.arch.eval.${hasCourses}", this, 
						new Model<CoursePackViewList>(viewList)));				  
		add(l_archive_eval);

//		Label l_action = new Label("item-header-action-label", 
//				new StringResourceModel("table.header.user.action.${hasCourses}", this, 
//						new Model<CoursePackViewList>(viewList)));				  
//		add(l_action);

		// Rows
		final DataView<AnnualPack> dataView = new DataView<AnnualPack>("table-archived-courses", 
																		new ListDataProvider<AnnualPack>(viewList.getAnnualPacks())) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<AnnualPack> item) {
				//
				final AnnualPack pack = (AnnualPack) item.getModelObject();
				CompoundPropertyModel<AnnualPack> packModel = new  CompoundPropertyModel<AnnualPack>(pack);
				
				item.add(new Label("annual-pack-view-title", pack.getYear()));
				item.add(new AnnualPackViewPanel("annual-pack-course-panel", packModel));				
			}
		};
		
		add(dataView);		
	}

}

