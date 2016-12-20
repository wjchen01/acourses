package org.wjchen.prometheus.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class EvaluationReport implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PK")
	protected Long pk;
	
	@Column(name = "TYPE")
	protected String formatType;
	
	@Column(name = "PATH")
	protected String path;
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EVAL_ID")
	protected EvaluationControl control;

	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID")
	protected CourseInfo course;	

	public String getTitle() {
		return this.getControl().getTitle();
	}

	public abstract String getEvalType();
	public abstract String formatName();
	public abstract String filePath(); 
	public abstract String getUserName();

}
