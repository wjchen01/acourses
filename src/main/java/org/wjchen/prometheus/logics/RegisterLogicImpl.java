package org.wjchen.prometheus.logics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import org.wjchen.prometheus.daos.PermissionDAO;
import org.wjchen.prometheus.models.CourseInfo;
import org.wjchen.prometheus.models.Faculty;
import org.wjchen.prometheus.models.Permission;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
@Service("org.wjchen.prometheus.logics.RegisterLogic")
@Transactional("prometheus")
public class RegisterLogicImpl implements RegisterLogic {

	private static Long FacultyLevel = new Long(6);
	
	@Autowired
	private PermissionDAO daoPerm;
	
	public void init() {
		log.info("init");
	}
	
	@Override
	public Set<Faculty> getFacultyByLastName(String lastName) {
		Set<Faculty> faculties = new HashSet<Faculty>(0);
		
		List<Permission> permissions = this.getByLastName(lastName);
		for(Permission permission : permissions) {
			faculties.add(permission.getFaculty());
		}
		
		return faculties;
	}

	@Override
	public Set<Faculty> getFacultyByUserName(String userName) {
		Set<Faculty> faculties = new HashSet<Faculty>(0);
		
		List<Permission> permissions = this.getByUserName(userName);
		for(Permission permission : permissions) {
			faculties.add(permission.getFaculty());
		}

		return faculties;
	}

	@Override
	public Set<Faculty> getFacultyByCourseId(String uniqueId) {
		Set<Faculty> faculties = new HashSet<Faculty>(0);

		List<Permission> permissions = this.getByCourseId(uniqueId);
		for(Permission permission : permissions) {
			faculties.add(permission.getFaculty());
		}
		
		return faculties;
	}

	@Override
	public Set<Faculty> getFacultyByCourseTt(String title) {
		Set<Faculty> faculties = new HashSet<Faculty>(0);

		List<Permission> permissions = this.getByCourseTitle(title);
		for(Permission permission : permissions) {
			faculties.add(permission.getFaculty());
		}
		
		return faculties;
	}

	@Override
	public Set<CourseInfo> getCourseByUserName(String userName) {
		Set<CourseInfo> courses = new HashSet<CourseInfo>(0);
		
		List<Permission> permissions = this.getByUserName(userName);
		for(Permission permission : permissions) {
			CourseInfo course = permission.getCourse(); 
			
			courses.add(course);
		}

		return courses;
	}

	@Override
	public Set<CourseInfo> getCourseByLastName(String lastName) {
		Set<CourseInfo> courses = new HashSet<CourseInfo>(0);
		
		List<Permission> permissions = this.getByLastName(lastName);
		for(Permission permission : permissions) {
			CourseInfo course = permission.getCourse(); 

			courses.add(course);
		}

		return courses;
	}
	
	@Override
	public Set<CourseInfo> searchCourseByCourseId(String uniqueId) {
		Set<CourseInfo> courses = new HashSet<CourseInfo>(0);
				
		List<Permission> permissions = this.getByCourseId(uniqueId);
		for(Permission permission : permissions) {
			CourseInfo course = permission.getCourse(); 

			courses.add(course);
		}

		return courses;
	}

	@Override
	public Set<CourseInfo> searchCourseByCourseTt(String title) {
		Set<CourseInfo> courses = new HashSet<CourseInfo>(0);

		List<Permission> permissions = this.getByCourseTitle(title);
		for(Permission permission : permissions) {
			CourseInfo course = permission.getCourse(); 

			courses.add(course);
		}
		
		return courses;
	}

	@Override
	public CourseInfo getCourseByCourseId(String uniqueId) {
		List<Permission> permissions = this.getByCourseId(uniqueId);
		if(permissions.isEmpty()) {
			return null;
		}
		else {
			Iterator<Permission> iter = permissions.iterator();
			Permission permission = iter.next();
			
			return permission.getCourse();
		}
	}

	@Override
	public CourseInfo getCourseById(Long id) {
		Search search = new Search(Permission.class);
		
		Filter facultyFilter = Filter.equal("securityLevel", FacultyLevel);
		search.addFilter(facultyFilter);
		Filter nameFilter = Filter.equal("course.id", id);
		search.addFilter(nameFilter);
		
		List<Permission> permissions = daoPerm.search(search);
		if(permissions.size() > 0) {
			return permissions.get(0).getCourse();
		}
		else {			
			return null;
		}
	}

	private List<Permission> getByLastName(String lastName) {
		if(lastName == null) {
			return new ArrayList<Permission>();
		}
		else {
			Search search = new Search(Permission.class);
			
			Filter facultyFilter = Filter.equal("securityLevel", FacultyLevel);
			search.addFilter(facultyFilter);
			Filter nameFilter = Filter.equal("faculty.lastName", lastName);
			search.addFilter(nameFilter);
			
			return daoPerm.search(search);
		}
	}
	
	private List<Permission> getByUserName(String userName) {
		if(userName == null) {
			return new ArrayList<Permission>();
		}
		else {
			Search search = new Search(Permission.class);
			
			Filter facultyFilter = Filter.equal("securityLevel", FacultyLevel);
			search.addFilter(facultyFilter);
			Filter nameFilter = Filter.equal("faculty.userName", userName);
			search.addFilter(nameFilter);
			
			return daoPerm.search(search);
		}
	}
	
	private List<Permission> getByCourseId(String uniqueId) {
		if(uniqueId == null) {
			return new ArrayList<Permission>();
		}
		else {
			Search search = new Search(Permission.class);
			
			Filter facultyFilter = Filter.equal("securityLevel", FacultyLevel);
			search.addFilter(facultyFilter);
			Filter courseFilter = Filter.like("course.uniqueId", '%' + uniqueId + '%');
			search.addFilter(courseFilter);
			
			return daoPerm.search(search);
		}
	}
	
	private List<Permission> getByCourseTitle(String title) {
		if(title == null) {
			return new ArrayList<Permission>();
		}
		else {
			Search search = new Search(Permission.class);
			
			Filter facultyFilter = Filter.equal("securityLevel", FacultyLevel);
			search.addFilter(facultyFilter);
			Filter courseFilter = Filter.like("course.title", '%' + title + '%');
			search.addFilter(courseFilter);
			
			return daoPerm.search(search);
		}
	}

	@Override
	public boolean isOwner(String courseId, String userName) {
		boolean owned = false;
		
		Set<Faculty> faculties = this.getFacultyByCourseId(courseId);
		for (Faculty faculty : faculties) {
			if (faculty.getUserName().equals(userName)) {
				owned = true;
				break;
			}
		}
		
		return owned;
	}
	
}
