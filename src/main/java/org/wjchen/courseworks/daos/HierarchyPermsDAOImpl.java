package org.wjchen.courseworks.daos;

import java.util.Arrays;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import org.wjchen.courseworks.models.HierarchyPerms;

@Repository("org.wjchen.courseworks.daos.HierarchyPermsDAO")
public class HierarchyPermsDAOImpl extends GenericDAOImpl<HierarchyPerms, Long> implements HierarchyPermsDAO {

    @Autowired
    @Qualifier("org.wjchen.courseworks.SessionFactory")
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
            super.setSessionFactory(sessionFactory);
    }

    @Override
	public List<HierarchyPerms> searchUserById(String userId, String... permissions) {
		if(permissions.length < 1) {
			return null;
		}
		else {
			Search search = new Search(HierarchyPerms.class);
			Filter userRestric = Filter.equal("user.userId", userId);
			Filter permRestric = Filter.in("permission", Arrays.asList(permissions));
			search.addFilter(userRestric);
			search.addFilter(permRestric);
		
			return this.search(search);
		}
	}
	
    @Override
	public List<HierarchyPerms> searchUserByNm(String userNm, String... permissions) {
		Search search = new Search(HierarchyPerms.class);
		Filter userRestric = Filter.equal("user.userNm", userNm);
		Filter permRestric = Filter.in("permission", Arrays.asList(permissions));
		search.addFilter(userRestric);
		search.addFilter(permRestric);
		
		return this.search(search);		
	}

    @Override
    public List<HierarchyPerms> getNodePerms(String nodeId) {
		Search search = new Search(HierarchyPerms.class);
		Filter nodeRestric = Filter.equal("nodeId", nodeId);
    	search.addFilter(nodeRestric);
    	
    	return this.search(search);
    }
}
