package org.wjchen.archivedcourse.logics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import org.wjchen.archivedcourse.models.CoursePack;
import org.wjchen.courseworks.logics.DelegatedUserLogic;
import org.wjchen.prometheus.logics.EvalReportsLogic;
import org.wjchen.prometheus.logics.RegisterLogic;
import org.wjchen.prometheus.models.CourseInfo;
import org.wjchen.prometheus.models.EvaluationReport;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
@Service("org.wjchen.archivedcourse.logics.CoursePackService")
public class CoursePackServiceImpl implements CoursePackService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private DelegatedUserLogic userDALogic;
	
	@Autowired
	private RegisterLogic registerLogic;
	
	@Autowired
	private EvalReportsLogic reportLogic;

	public void init() {
		log.info("init");
	}

	@Override
	public List<CoursePack> getCoursePacks(String courseInput, String instrInput, String requester) {
		Set<CourseInfo> courseInfos = new HashSet<CourseInfo>();
		if (courseInput != null) {
			courseInfos.addAll(registerLogic.searchCourseByCourseTt(courseInput));
			courseInfos.addAll(registerLogic.searchCourseByCourseId(courseInput));
		}
		
		Set<CourseInfo> instrInfos = new HashSet<CourseInfo>();
		if(instrInput != null) {
			instrInfos.addAll(registerLogic.getCourseByUserName(instrInput));
			instrInfos.addAll(registerLogic.getCourseByLastName(instrInput));
		}
		
		List<CourseInfo> searchInfos = new ArrayList<CourseInfo>();
		// AND condition for instructor and courses
		if ((courseInput != null) && (instrInput != null)) {
			searchInfos.addAll(Sets.intersection(courseInfos, instrInfos));
		} else {
			if(courseInfos != null && courseInfos.size() > 0) {
				searchInfos.addAll(courseInfos);
			}
			
			if(instrInfos != null && instrInfos.size() > 0) {
				searchInfos.addAll(instrInfos);
			}
		}

		searchInfos.removeAll(Collections.singleton(null));
		List<CourseInfo> deletes = new ArrayList<CourseInfo>();
		for(CourseInfo info : searchInfos) {
			if(info.getUniqueId() == null) {
				log.info(info.getId().toString());
				deletes.add(info);
			}
		}
		searchInfos.removeAll(deletes);
		Collections.sort(searchInfos);
		
		List<CoursePack> archivedPacks = new ArrayList<CoursePack>();
		for (CourseInfo info : searchInfos) {
			if(info == null) {
				continue;
			}
			String uniqueId = info.getUniqueId();
			if((uniqueId != null) && (info.getDetail() != null)) {
				CoursePack archivedPack = new CoursePack(info, requester);
				Long id = info.getId();
				List<EvaluationReport> reports = reportLogic.getById(id);
				if(reports != null && reports.size() > 0) {
					archivedPack.setReports(reports);
				}
				archivedPacks.add(archivedPack);
			}
		}
		
		return archivedPacks;
	}

	public CoursePack getUniqueId(String uniqueId) {
		return null;
	}

	public void downloadPack(CoursePack pack) {

	}

	@Override
	public boolean isGrantAccess(String courseId, String userName, boolean allowSelf, String[] permissions) {
		boolean grant = false;

		CourseInfo courseInfo = registerLogic.getCourseByCourseId(courseId);
		if(courseInfo == null) {
			log.debug("Course Id is null: " + grant);
			return grant;
		}
		
		// self grant
		if(allowSelf && !grant) {
			grant = registerLogic.isOwner(courseId, userName);
			log.debug("Self grant the user, " + userName + " for the course, "+ courseId + " : " + grant);
		}

		// DA grant
		if (!grant) {
			grant = userDALogic.isGrantAccess(userName, courseId, permissions);
			if(!grant) {
				String school = courseInfo.getDetail().getSchoolCode();
				String subject = courseInfo.getDetail().getSubjectCode();
				grant = userDALogic.isGrantAccess(userName, school, subject, permissions);
				log.debug("School/Subject level - the user, " + userName + 
							" for the school, "+ school  + ", or for the subject, " + subject + " : " + grant);
			}
			else {
				log.debug("Course level - the user, " + userName + " for the course, "+ courseId + " : " + grant);
			}
		}
		log.debug("The user, " + userName + " access the course, " + courseId + " : " + grant);
		
		return grant;
	}

	@Override
	public CourseInfo getCourseById(Long id) {
		return registerLogic.getCourseById(id);
	}

	@Override
	public boolean hasDelegatedAccess(String userName) {
		return userDALogic.hasDelegatedAccess(userName);
	}

}
