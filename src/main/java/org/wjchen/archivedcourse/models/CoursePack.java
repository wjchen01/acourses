package org.wjchen.archivedcourse.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.wjchen.prometheus.models.CourseInfo;
import org.wjchen.prometheus.models.EvaluationReport;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(of={"uniqueId", "requester"})
public class CoursePack implements Serializable {

	private static final long serialVersionUID = 5904550097566749645L;

	private String uniqueId;
	private String requester;
	private String courseTitle;
	private String semester;
	private String year;
	private CourseInfo course = null;
	private List<EvaluationReport> reports = new ArrayList<EvaluationReport>(0); 
	
	public CoursePack(String uniqueId, String requester, String courseTitle, String semester, String year) {
		this.uniqueId = uniqueId;
		this.requester = requester;
		this.courseTitle = courseTitle;
		this.semester = semester;
		this.year = year;
	}
	
	public CoursePack(CourseInfo course, String requester) {
		this.uniqueId = course.getUniqueId();
		this.requester = requester;
		this.year = course.getDetail().getYear();
		this.semester = course.getDetail().getSemester();
		this.setCourse(course);		
	}
		
}
