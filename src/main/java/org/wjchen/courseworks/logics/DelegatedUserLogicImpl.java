package org.wjchen.courseworks.logics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.wjchen.courseworks.daos.HierarchyNodeDAO;
import org.wjchen.courseworks.daos.HierarchyPermsDAO;
import org.wjchen.courseworks.models.HierarchyNode;
import org.wjchen.courseworks.models.HierarchyPerms;
import org.wjchen.courseworks.models.SakaiUser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
@Service("org.wjchen.prometheus.logics.DelegatedUserLogic")
@Transactional("courseworks")
public class DelegatedUserLogicImpl implements DelegatedUserLogic, Serializable {

	private static final long serialVersionUID = 1L;

	private static String[] AllPermissions = {
		"role:Auditor",
		"role:EditAllAdmin",
		"role:EditLimitedAdmin",
		"role:ViewOnlyAdmin",
		"role:ViewOnlyLimitedAdmin",
		"role:EditAllAdminNoEval", 
		"role:EditLimitedAdminNoEval", 
		"role:ViewOnlyAdminNoEval"		
	};
	
	// Spring inject beans
	@Autowired
	private HierarchyPermsDAO permsDao;
	
	@Autowired	
	private HierarchyNodeDAO nodeDao;
		
	public void init() {
		log.info("init");
	}	

	@Override
	public boolean hasDelegatedAccess(String uni) {		
		List<HierarchyPerms> perms = permsDao.searchUserByNm(uni, AllPermissions);
		if(perms == null) {
			return false;
		}
		else if(perms.size() == 0) {
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public boolean isGrantAccess(String uni, String courseId, String...allows) {
		boolean isAllow = false;
		List<HierarchyPerms> perms = new ArrayList<HierarchyPerms>();

		HierarchyNode courseNode = nodeDao.findByCourseId(courseId);		
		if(courseNode != null) {
			// this node permission
			perms.addAll(permsDao.getNodePerms(courseNode.getId().toString()));

			//collect parent nodes permission
			List<Long> pids = courseNode.listParentIds();
			for(Long pid : pids) {
				List<HierarchyPerms> parentPerms = permsDao.getNodePerms(pid.toString());
				perms.addAll(parentPerms);
			}
		}
			
		// collect user permissions in all the nodes
		Set<String> userNodePerms = new HashSet<String>();
		for(HierarchyPerms perm : perms) {
			SakaiUser user = perm.getUser();
			if(user != null && uni.equalsIgnoreCase(user.getUserNm())) {
				userNodePerms.add(perm.getPermission());
			}
		}
		
		// find allows permission in user's nodes
		for(String allow : allows) {
			if(userNodePerms.contains(allow)) {
				isAllow = true;
				break;
			}
		}
		
		return isAllow;
	}

	@Override
	public boolean isGrantAccess(String uni, String school, String subject, String...allows) {
		boolean isAllow = false;
		List<HierarchyPerms> perms = new ArrayList<HierarchyPerms>();

		List<Long> nodeIds = nodeDao.nodeIdsFromSchoolSubject(school, subject);
		for(Long nodeId : nodeIds) {
			perms.addAll(permsDao.getNodePerms(nodeId.toString()));
		}

		// collect user permissions in all the nodes
		Set<String> userNodePerms = new HashSet<String>();
		for(HierarchyPerms perm : perms) {
			SakaiUser user = perm.getUser();
			if(user != null && uni.equalsIgnoreCase(user.getUserNm())) {
				userNodePerms.add(perm.getPermission());
			}
		}
		
		// find allows permission in user's nodes
		for(String allow : allows) {
			if(userNodePerms.contains(allow)) {
				isAllow = true;
				break;
			}
		}

		return isAllow;
	}

}
