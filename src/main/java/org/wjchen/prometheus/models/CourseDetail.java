package org.wjchen.prometheus.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "CMS_ABSTRACT")
public class CourseDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;	
	
	@Column(name = "YEAR")
	private String year;

	@Column(name = "SEMESTER")
	private String semester;
	
	@Column(name = "SCHOOL_CODE")
	private String schoolCode;

	@Column(name = "DEPARTMENT_CODE")
	private String departmentCode;

	@Column(name = "SUBJECT_CODE")
	private String subjectCode;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private CourseInfo course;
}
