package org.wjchen.prometheus.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="CMS_EVAL_CONTROL")
public class EvaluationControl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EVAL_ID")
	private Long evalId;
	
	@Column(name = "TITLE")
	private String title;
	
	@OneToMany(mappedBy = "control")
	private Set<EvaluationReport> reports = new HashSet<EvaluationReport>(0);
	
}
