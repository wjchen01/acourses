package org.wjchen.courseworks.models;

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
@Table(name = "HIERARCHY_PERMS")
public class HierarchyPerms implements Serializable {

	private static final long serialVersionUID = -1075342235066363872L;

	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NODEID")
	private String nodeId;
	
	@Column(name = "PERMISSION")
	private String permission;
		
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="USERID", referencedColumnName="USER_ID")
	private SakaiUser user;
	
}
