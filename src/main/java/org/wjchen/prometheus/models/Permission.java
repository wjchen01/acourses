package org.wjchen.prometheus.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="PERMISSIONS")
public class Permission implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PK")	
	private Long pk;
	
	@Column(name="SECURITY_LEVEL")		
	private Long securityLevel;
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID")
	private CourseInfo course;
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CONTROL")
	private Faculty faculty;
	
}
