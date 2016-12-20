package org.wjchen.courseworks.daos;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import org.wjchen.courseworks.models.HierarchyNode;
import edu.emory.mathcs.backport.java.util.Collections;

@Repository("org.wjchen.courseworks.daos.HierarchyNodeDAO")
public class HierarchyNodeDAOImpl extends GenericDAOImpl<HierarchyNode, Long>implements HierarchyNodeDAO {
	
    @Autowired
    @Qualifier("org.wjchen.courseworks.SessionFactory")
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
            super.setSessionFactory(sessionFactory);
    }

	@Override
	public HierarchyNode findByCourseId(String courseId) {
		Search search = new Search(HierarchyNode.class);
		Filter filter = Filter.equal("meta.title", "/site/" + courseId);
		search.addFilter(filter);
		
		List<HierarchyNode> nodes = this.search(search);
		if(nodes == null) {
			return null;
		}
		else if(nodes.size() == 0) {
			return null;
		}
		else {
			return nodes.get(0);
		}
	}

	@Override
	public List<Long> nodeIdsFromSchoolSubject(String schoolCode, String subjectCode) {
		HierarchyNode schoolNode = null;
		HierarchyNode subjectNode = null;
		
		List<Long> nodes = new ArrayList<Long>();
		
		Search search = new Search(HierarchyNode.class);
		Filter filter = Filter.equal("meta.title", schoolCode);
		search.addFilter(filter);
		
		List<HierarchyNode> results = this.search(search);
		if(results != null) {
			Collections.sort(results);
			for(HierarchyNode node : results) {
				if(node.listParentIds().size() == 1) {
					schoolNode = node;
					break;
				}
			}
		}
		
		if(schoolNode != null) {
			List<Long> childIds = schoolNode.listChildIds();
			Collections.sort(childIds);
			
			search.clearFilters();
			filter = Filter.equal("meta.title", subjectCode);
			search.addFilter(filter);
			results = this.search(search);
			Collections.sort(results);
			for(HierarchyNode node : results) {
				if(childIds.contains(new Long(node.getId()))) {
					// school contains subject
					subjectNode = node;
					// add its parent ids
					nodes.addAll(subjectNode.listParentIds());
					// add itself
					nodes.add(subjectNode.getId());
				}
			}
		}
		
		return nodes;
	}
}
