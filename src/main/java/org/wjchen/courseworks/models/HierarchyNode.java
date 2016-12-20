package org.wjchen.courseworks.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "HIERARCHY_NODE")
public class HierarchyNode implements Serializable, Comparable<HierarchyNode> {

	private static final long serialVersionUID = 5840303013421563825L;

	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "PARENTIDS")
	private String parentIds;
	
	@Column(name = "CHILDIDS")
	private String childIds;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "node", cascade = CascadeType.ALL)
	private HierarchyMeta meta;

	public List<Long> listParentIds() {
		List<Long> pids = new ArrayList<Long>();
		
		for(String pid : parentIds.split(":")) {
			if(pid == null) {
				continue;
			}
			else if(pid.trim().isEmpty()) {
				continue;
			}
			else {
				pids.add(new Long(pid.trim()));
			}
		}
		
		return pids;
	}
	
	public List<Long> listChildIds() {
		List<Long> cids = new ArrayList<Long>();
		
		for(String cid : childIds.split(":")) {
			if(cid == null) {
				continue;
			}
			else if(cid.trim().isEmpty()) {
				continue;
			}
			else {
				cids.add(new Long(cid.trim()));
			}
		}
		
		return cids;
	}

	@Override
	public int compareTo(HierarchyNode other) {		
		return this.getId().compareTo(other.getId());
	}

}
