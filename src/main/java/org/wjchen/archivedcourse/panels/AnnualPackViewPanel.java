package org.wjchen.archivedcourse.panels;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

import org.wjchen.archivedcourse.apps.CourseArchiveSession;
import org.wjchen.archivedcourse.logics.CoursePackService;
import org.wjchen.archivedcourse.models.AnnualPack;
import org.wjchen.archivedcourse.models.CoursePack;
import org.wjchen.archivedcourse.pages.ArchiveContentPage;
import org.wjchen.archivedcourse.pages.ArchiveEvaluationPage;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
public class AnnualPackViewPanel extends Panel {

	private static final long serialVersionUID = -343790583184998642L;
	
	private final static String[] Pack_Permissions = {
			"role:EditAllAdmin", 
			"role:ViewOnlyAdmin",
	};

	@SpringBean
	protected CoursePackService courseService;

	public AnnualPackViewPanel(String id, CompoundPropertyModel<AnnualPack> model) {
		super(id, model);

		setOutputMarkupId(true);
		
		final AnnualPack pack = model.getObject();
		final String packBase = CourseArchiveSession.get().getPackBase();
		final String packRoot = CourseArchiveSession.get().getPackRoot();
		final String userNm = CourseArchiveSession.get().getUser();
		
		// Rows
		final DataView<CoursePack> dataView = 
				new DataView<CoursePack>("table-archived-courses",	new ListDataProvider<CoursePack>(pack.getCoursePacks())) {

			private static final long serialVersionUID = 1L;
		    
			@Override
			protected void populateItem(final Item<CoursePack> item) {
				final CoursePack pack = (CoursePack) item.getModelObject();

				final boolean hasContent = (pack.getCourse() != null) && (pack.getCourse().getArchived() != null) && pack.getCourse().getArchived().isArchived();
				final boolean hasEvaluation = (pack.getCourse() != null) && (pack.getReports().size() > 0);
				
				// Course Id 
				final String uniqueId = pack.getUniqueId();						
				item.add(new Label("item-course-site-id", uniqueId));
				
				// Course title
				String title = pack.getCourseTitle();
				item.add(new Label("item-course-site-title", title));
				
				// Content Link
				Link<CoursePack> contentUrl = new Link<CoursePack>("item-archive-content-url", item.getModel()) {

					private static final long serialVersionUID = 1L;

					public void onClick() {
						CoursePack thisPack = (CoursePack) getModelObject();
						WebRequest request = (WebRequest) this.getRequestCycle().getRequest();
						String app_name = request.getContextPath();
						String thisUrl = app_name + "/course_packs/" + 
								thisPack.getUniqueId() + 
								"/course_pack/index.html";
						ArchiveContentPage thisPage = new ArchiveContentPage(thisUrl);
						setResponsePage(thisPage);	
					}
				};
				contentUrl.add(new Label("item-archive-content-label", "View Content"));
				contentUrl.setVisible(hasContent);
				item.add(contentUrl);
				
				// Eval Link
				Link<CoursePack> evalUrl = new Link<CoursePack>("item-archive-eval-url", item.getModel()) {

					private static final long serialVersionUID = 1L;

					public void onClick() {
						CoursePack thisPack = (CoursePack) getModelObject();
						ArchiveEvaluationPage thisPage = new ArchiveEvaluationPage(thisPack);
						setResponsePage(thisPage);	
					}
				};
				evalUrl.add(new Label("item-archive-eval-label", "View Reports"));
				evalUrl.setVisible(hasEvaluation);					
				PopupSettings popupSettings = new PopupSettings(PopupSettings.RESIZABLE |PopupSettings.SCROLLBARS).setHeight(600).setWidth(800);
				contentUrl.setPopupSettings(popupSettings);
				evalUrl.setPopupSettings(popupSettings);
				item.add(evalUrl);

				// Download Link
				LoadableDetachableModel<File> fileModel = new LoadableDetachableModel<File>(){

					private static final long serialVersionUID = 1L;

			        @Override
			        protected File load() {
						String zipFile = "/tmp/" + userNm + "-" + pack.getUniqueId() + ".zip";
						try {							
							FileOutputStream fileOutStream = new FileOutputStream(zipFile);
							ZipOutputStream zipOutStream = new ZipOutputStream(fileOutStream);
													
							// Generate content file list for the zip file
							if(hasContent && courseService.isGrantAccess(uniqueId, userNm, true, Pack_Permissions)) {
								List<File> zipArchives = new ArrayList<File>();
								String packName = pack.getUniqueId();
								
								getAllFiles(new File(packBase, packName), zipArchives);
								for (File file : zipArchives) {
									if (!file.isDirectory()) { // we only zip files, not directories
										addToZip(new File(packBase), new File(packRoot), file, zipOutStream);
									}
								}
								
								String fileBase = packBase + "/../";
								String zipRoot = packRoot + "/../"; 
								log.debug("fileBase = " + fileBase);
								addToZip(new File(fileBase), new File(zipRoot), new File(fileBase, "README.txt"), zipOutStream);
							}
							else {
								String fileBase = packBase + "/../";
								String zipRoot = packRoot + "/../"; 
								addToZip(new File(fileBase), new File(zipRoot), new File(fileBase, "not_authorized.html"), zipOutStream);								
							}
														
							zipOutStream.close();
							fileOutStream.close();
						} catch (FileNotFoundException e) {
							log.error(e.getMessage());
						} catch (IOException e) {
							log.error(e.getMessage());
						} 
						
						return new File(zipFile);
				    }		
					
			        @Override
			        protected void onDetach() {
			        	File zipFile = this.getObject();
			        	zipFile.delete();
			        }
			        
					private void getAllFiles(File dir, List<File> fileList) throws IOException {
						File[] files = dir.listFiles();
						
						if(files != null) {
							for (File file : files) {
								fileList.add(file);
								if (file.isDirectory()) {
									getAllFiles(file, fileList);
								}
							}
						}
					}

					private void addToZip(File fileBase, File zipRoot, File file, ZipOutputStream zos) 
							throws FileNotFoundException, IOException {

						FileInputStream fis = new FileInputStream(file);
						String filePath = file.getCanonicalPath();
						String fileName = filePath.substring(fileBase.getCanonicalPath().length());
						String zipFilePath = "." + zipRoot.getCanonicalPath() + fileName;
						ZipEntry zipEntry = new ZipEntry(zipFilePath);
						zos.putNextEntry(zipEntry);
						byte[] bytes = new byte[1024];
						int length;
						while ((length = fis.read(bytes)) >= 0) {
							zos.write(bytes, 0, length);
						}
						zos.closeEntry();
						fis.close();
						log.debug("Add " + filePath + " to the zip file.");
					}
				};
				
				DownloadLink downloadLink = new DownloadLink("item-download-link", fileModel) {
					 
					private static final long serialVersionUID = 1L;

					@Override
				    public void onClick() {
				 
				        File file = (File)getModelObject();
				        IResourceStream resourceStream = new FileResourceStream(new org.apache.wicket.util.file.File(file));
				 
				        getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(resourceStream, file.getName()));
				        log.info("File downloaded: " + file.getName());
				    }
				};				 
				downloadLink.setVisible(hasContent);
				downloadLink.setDeleteAfterDownload(true);
				downloadLink.add(new Label("item-download-label", "Download"));
				item.add(downloadLink);
			}
		};
		
		add(dataView);		
	}

}

