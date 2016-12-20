package org.wjchen.courseworks.daos;

import java.util.List;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;

import org.wjchen.courseworks.models.HierarchyPerms;

public interface HierarchyPermsDAO extends GenericDAO<HierarchyPerms, Long> {

	public List<HierarchyPerms> searchUserById(String userId, String... permissions);
	public List<HierarchyPerms> searchUserByNm(String userNm, String... permissions);
	public List<HierarchyPerms> getNodePerms(String nodeId);
	
}
