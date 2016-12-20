package org.wjchen.prometheus.daos;

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

import org.wjchen.prometheus.models.Permission;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:prometheus-components.xml")
@TransactionConfiguration(defaultRollback=true)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
		 DirtiesContextTestExecutionListener.class, 
		 TransactionalTestExecutionListener.class})
@Transactional("prometheus")
public class PermissionDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private PermissionDAO dao;

	@Test
	public void testCount() {
		Search search = new Search(Permission.class);

		String userName = "amo1";
		Filter facultyFilter = Filter.equal("securityLevel", new Long(6));
		search.addFilter(facultyFilter);
		Filter nameFilter = Filter.equal("faculty.userName", userName);
		search.addFilter(nameFilter);

		List<Permission> perms = dao.search(search);
		Assert.assertEquals("Report count: ", 142, perms.size());
	}
	
}
