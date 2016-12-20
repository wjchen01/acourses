package org.wjchen.prometheus.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(of={"control"})
@Entity
@Table(name="STUDENTS")
public class Faculty implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CONTROL")
	private Long control;
	
	@Column(name="FIRSTNAME")
	private String firstName;
		
	@Column(name="LASTNAME")
	private String lastName;
	
	@Column(name="USERN")
	private String userName;
	
	@OneToMany(mappedBy = "faculty")
	private Set<Permission> permissions = new HashSet<Permission>(0);
	
	@OneToMany(mappedBy = "faculty")
	private Set<EvalIndReport> reports = new HashSet<EvalIndReport>(0);
		
}
