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

import org.wjchen.courseworks.models.HierarchyPerms;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:courseworks-components.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
									 DirtiesContextTestExecutionListener.class, 
									 TransactionalTestExecutionListener.class})
@TransactionConfiguration(defaultRollback=true)
@Transactional("courseworks")
public class HierarchyPermDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private HierarchyPermsDAO dao;
	
	@Test
	public void test() {
		Search search = new Search(HierarchyPerms.class);		
		Filter restrict = Filter.equal("user.userId", "77bed6ec-60ec-4535-9f53-37eba6898a45");
		search.addFilter(restrict);

		List<HierarchyPerms> perms = dao.search(search);
		Assert.assertEquals("Find Course Id: ", 30, perms.size());		
	}

	@Test
	public void testSearchUserById() {
		String userId = "77bed6ec-60ec-4535-9f53-37eba6898a45";
		String permission = "role:ViewOnlyAdmin";
		List<HierarchyPerms> perms = dao.searchUserById(userId, permission);
		Assert.assertEquals("Find Course Id: ", 5, perms.size());
	}	

	@Test
	public void testSearchUserByNm() {
		String userNm = "wc2442";
		String permission = "role:ViewOnlyAdmin";
		List<HierarchyPerms> perms = dao.searchUserByNm(userNm, permission);
		Assert.assertEquals("Find Course Id: ", 5, perms.size());
	}	

}
