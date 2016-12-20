package org.wjchen.archivedcourse.pages;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.resource.PackageResourceReference;

import org.wjchen.archivedcourse.models.CoursePack;
import org.wjchen.prometheus.models.EvaluationReport;

public class ArchiveEvaluationPage extends WebPage {

	private static final long serialVersionUID = 1L;

	public ArchiveEvaluationPage(final CoursePack pack) {
		
		List<EvaluationReport> evals = pack.getReports();
		
		// Header Labels
		Label l_eval_title = new Label("header-eval-title-label",
				new StringResourceModel("table.header.eval.title", this, null));
		add(l_eval_title);
		Label l_eval_format = new Label("header-eval-format-label",
				new StringResourceModel("table.header.eval.format", this, null));
		add(l_eval_format);
		Label l_eval_type = new Label("header-eval-type-label",
				new StringResourceModel("table.header.eval.type", this, null));
		add(l_eval_type);
		Label l_eval_uni = new Label("header-eval-uni-label",
				new StringResourceModel("table.header.eval.uni", this, null));
		add(l_eval_uni);
		Label l_eval_link = new Label("header-eval-link-label",
				new StringResourceModel("table.header.eval.link", this, null));
		add(l_eval_link);
		
		// Rows
		final DataView<EvaluationReport> reportView = new DataView<EvaluationReport>("table-archived-reports",
					new ListDataProvider<EvaluationReport>(evals)) {

			private static final long serialVersionUID = 1L;
			private String currentTitle = null;
			
			@Override
			protected void populateItem(final Item<EvaluationReport> item) {
				final EvaluationReport report = item.getModelObject();
				
				// Eval title
				String title = report.getTitle();
				if(currentTitle == null || !title.equals(currentTitle)) {
					item.add(new Label("item-eval-report-title", title));
					currentTitle = title;
				}
				else {
					item.add(new Label("item-eval-report-title", ""));					
				}
				
				// Eval format
				String format = report.formatName();
				item.add(new Label("item-eval-report-format", format));
				
				// Eval type
				String type = report.getEvalType();
				item.add(new Label("item-eval-report-type", type));
				
				// Eval uni
				String uni = report.getUserName();
				item.add(new Label("item-eval-report-uni", uni));
				
				// Eval Link
				WebRequest request = (WebRequest) this.getRequestCycle().getRequest();
				String appName = request.getContextPath();
				String evalPath = report.filePath();
				String thisUrl = appName + "/" + "evals" + "/" + evalPath;		
				ExternalLink evalUrl = new ExternalLink("item-eval-report-url", thisUrl);					
				PopupSettings settings = new PopupSettings(PopupSettings.SCROLLBARS);
				evalUrl.setPopupSettings(settings);
				evalUrl.add(new Label("item-eval-report-link", "View Report"));
				item.add(evalUrl);	
				
				item.setOutputMarkupId(true);
			}
		};
		add(reportView);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {	
		super.renderHead(response);
		
	    response.render(CssHeaderItem.forReference(new PackageResourceReference(this.getClass(), "ArchiveEvaluationPage.css")));
	}

}
