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

import org.wjchen.courseworks.models.SakaiUser;
import org.wjchen.courseworks.models.UserDetail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:courseworks-components.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
									 DirtiesContextTestExecutionListener.class, 
									 TransactionalTestExecutionListener.class})
@TransactionConfiguration(defaultRollback=true)
@Transactional("courseworks")
public class SakaiUserDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private SakaiUserDAO dao;
		
	@Test
	public void testDao() {
		Search search = new Search(SakaiUser.class);		
		Filter restrict = Filter.equal("userNm", "wc2442");
		search.addFilter(restrict);

		List<SakaiUser> users = dao.search(search);
		Assert.assertEquals("Find Course Id: ", 1, users.size());		
		SakaiUser user = users.get(0);
		Assert.assertNotNull(user);
		if(user != null) {		
			UserDetail detail = user.getDetail();
			Assert.assertNotNull(detail);
			if(detail != null) {
				Assert.assertEquals("Last Name", "Chen", detail.getLastName());
				Assert.assertEquals("First Name", "Wen", detail.getFirstName());
			}
		}
	}

	@Test
	public void testfindByUserId() {
		String userId = "77bed6ec-60ec-4535-9f53-37eba6898a45";
		SakaiUser user = dao.findByUserId(userId);
		Assert.assertNotNull(user);
		if(user != null) {
			UserDetail detail = user.getDetail();
			Assert.assertNotNull(detail);
			if(detail != null) {
				Assert.assertEquals("Last Name", "Chen", detail.getLastName());
				Assert.assertEquals("First Name", "Wen", detail.getFirstName());
			}
		}
	}
	
	@Test
	public void testfindByUserNm() {
		String userNm = "wc2442";
		SakaiUser user = dao.findByUserNm(userNm);
		Assert.assertNotNull(user);
		if(user != null) {
			UserDetail detail = user.getDetail();
			Assert.assertNotNull(detail);
			if(detail != null) {
				Assert.assertEquals("Last Name", "Chen", detail.getLastName());
				Assert.assertEquals("First Name", "Wen", detail.getFirstName());
			}
		}		
	}

}
