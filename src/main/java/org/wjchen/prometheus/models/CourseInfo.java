package org.wjchen.prometheus.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="ABSTRACT")
public class CourseInfo implements Serializable, Comparable<CourseInfo> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="CRSTITLE")
	private String title;
	
	@Column(name="UNIQUEID")	
	private String uniqueId;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.ALL)
	private CourseDetail detail;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.ALL)
	private CourseArchived archived;
	
	@OneToMany(mappedBy = "course")
	private Set<Permission> permissions = new HashSet<Permission>(0);
	
	@OneToMany(mappedBy = "course")
	private Set<EvaluationReport> reports = new HashSet<EvaluationReport>(0);
	
	public boolean isArchived() {
		boolean archived = false;
		
		CourseArchived courseArchived = this.getArchived();
		if(courseArchived != null && courseArchived.isArchived()) {
			archived = true;
		}
		
		return archived;
	}

	@Override
	public int compareTo(CourseInfo other) {
		return this.getUniqueId().compareTo(other.getUniqueId());
	}
		
}
