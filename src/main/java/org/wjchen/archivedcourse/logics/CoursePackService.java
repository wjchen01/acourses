package org.wjchen.archivedcourse.logics;

import java.util.List;

import org.wjchen.archivedcourse.models.CoursePack;
import org.wjchen.prometheus.models.CourseInfo;

public interface CoursePackService {

	public List<CoursePack> getCoursePacks(String courseInput, String instrInput, String requester);
	public boolean isGrantAccess(String courseId, String userName, boolean allowSelf, String[] permissions);
	public void downloadPack(CoursePack pack);
	public CourseInfo getCourseById(Long id);
	public boolean hasDelegatedAccess(String userName);
}
