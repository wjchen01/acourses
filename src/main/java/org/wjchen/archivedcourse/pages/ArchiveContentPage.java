package org.wjchen.archivedcourse.pages;

import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.InlineFrame;
import org.apache.wicket.markup.html.pages.RedirectPage;

public class ArchiveContentPage extends WebPage implements IHeaderContributor
{
	
	private static final long serialVersionUID = -251549639602203407L;

	public ArchiveContentPage(final String url) {		
		RedirectPage page = new RedirectPage(url);
		
		InlineFrame contentFrame = new InlineFrame("frame.content", page);
		add(contentFrame);
	}

}
