package org.wjchen.prometheus.logics;

import java.util.Set;

import org.wjchen.prometheus.models.CourseInfo;
import org.wjchen.prometheus.models.Faculty;

public interface RegisterLogic {

	public Set<Faculty> getFacultyByLastName(String lastName);
	public Set<Faculty> getFacultyByUserName(String userName);
	public Set<Faculty> getFacultyByCourseId(String uniqueId);
	public Set<Faculty> getFacultyByCourseTt(String title);
	public Set<CourseInfo> getCourseByUserName(String userName);
	public Set<CourseInfo> getCourseByLastName(String lastName);
	public Set<CourseInfo> searchCourseByCourseId(String uniqueId);
	public Set<CourseInfo> searchCourseByCourseTt(String title);
	public CourseInfo getCourseByCourseId(String uniqueId);
	public CourseInfo getCourseById(Long id);
	public boolean isOwner(String courseId, String userName);
//	public boolean isCourseArchived(String uniqueId);
	
}
