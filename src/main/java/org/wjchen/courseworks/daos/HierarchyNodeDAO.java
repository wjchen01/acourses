package org.wjchen.courseworks.daos;

import java.util.List;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;

import org.wjchen.courseworks.models.HierarchyNode;

public interface HierarchyNodeDAO extends GenericDAO<HierarchyNode, Long> {

	public HierarchyNode findByCourseId(String courseId);
	public List<Long> nodeIdsFromSchoolSubject(String schoolCode, String subjectCode);
	
}
