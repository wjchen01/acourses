package org.wjchen.prometheus.daos;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
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

import org.wjchen.prometheus.models.EvalCrsReport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:prometheus-components.xml")
@TransactionConfiguration(defaultRollback=true)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
		 DirtiesContextTestExecutionListener.class, 
		 TransactionalTestExecutionListener.class})
@Transactional("prometheus")
public class EvalCrsReportDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private EvalCrsReportDAO dao;
	private Search search = new Search(EvalCrsReport.class);

	@Before
	public void setUp() {
		Filter restrict = Filter.equal("course.id", 53392L);
		search.addFilter(restrict);
		restrict = Filter.equal("control.evalId", 2714L);
		search.addFilter(restrict);		
	}
	
	@Test
	public void testCount() {
		List<EvalCrsReport> reports = dao.search(search);
		Assert.assertEquals("Report count: ", 4, reports.size());
	}
	
	@Test
	public void testCourse() {
		List<EvalCrsReport> reports = dao.search(search);
		Assert.assertEquals("Course ID: ", "COMSW4231_001_2004_3", reports.get(0).getCourse().getUniqueId());
	}
	
	@Test
	public void testTitle() {
		List<EvalCrsReport> reports = dao.search(search);
		Assert.assertEquals("Evaluation Title: ", "SEAS FINAL NO ABET", reports.get(0).getControl().getTitle());
	}
	
}
