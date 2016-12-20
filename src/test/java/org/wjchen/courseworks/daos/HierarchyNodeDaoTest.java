package org.wjchen.courseworks.daos;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import org.wjchen.courseworks.models.HierarchyNode;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:courseworks-components.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
									 DirtiesContextTestExecutionListener.class, 
									 TransactionalTestExecutionListener.class})
@TransactionConfiguration(defaultRollback=true)
@Transactional("courseworks")
public class HierarchyNodeDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private HierarchyNodeDAO dao;

	@Test
	public void testDao() {
		String courseId = "LAW_L8678_001_2016_1";
		Search search = new Search(HierarchyNode.class);
		Filter filter = Filter.equal("meta.title", "/site/" + courseId);
		search.addFilter(filter);
		List<HierarchyNode> nodes = dao.search(search);	
		Assert.assertEquals("Course find: ", 1, nodes.size());
		if(nodes.size() > 0) {
			HierarchyNode node = nodes.get(0);
			Assert.assertEquals("Course node: ", new Long(939505), node.getId());
		}
	}
	
	@Test
	public void testFindByCourseId() {
		String courseId = "LAW_L8678_001_2016_1";
		HierarchyNode node = dao.findByCourseId(courseId);
		Assert.assertNotNull("Course find: ", node);
		if(node != null) {
			Assert.assertEquals("Course node: ", new Long(939505), node.getId());			
		}
	}
	
}
