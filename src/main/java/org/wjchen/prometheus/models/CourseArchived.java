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
@Table(name = "CMS_CP_ARCHIVE")
public class CourseArchived implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="STATUS")
	private int status;
	
	@Column(name="HAS_CONTENT")
	private int hasContent;

	@Column(name="CP_LOCATION")
	private String location;
		
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private CourseInfo course;
	
	public boolean isArchived() {
		boolean archived = false;
		
		if(status > 0 && hasContent > 0) {
			archived = true;
		}
		
		return archived;
	}
	
}
