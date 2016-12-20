package org.wjchen.courseworks.models;

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
@Table(name = "SAKAI_USER_ID_MAP")
public class SakaiUser implements Serializable {

	private static final long serialVersionUID = -4384096556609912236L;
	
	@Id
	@Column(name = "USER_ID")
	private String userId;
		
	@Column(name = "EID")
	private String userNm;	
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	private UserDetail detail;
	
	@OneToMany(mappedBy = "user")
	private Set<HierarchyPerms> permissions = new HashSet<HierarchyPerms>(0);
		
}
