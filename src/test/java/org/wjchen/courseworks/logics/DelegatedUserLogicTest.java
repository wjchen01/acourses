package org.wjchen.courseworks.logics;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:courseworks-components.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
									 DirtiesContextTestExecutionListener.class, 
									 TransactionalTestExecutionListener.class})
@TransactionConfiguration(defaultRollback=true)
@Transactional("courseworks")
public class DelegatedUserLogicTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private DelegatedUserLogic service;
	
	@Test
	public void testHasDelegatedAccess() {
		String uni = "wc2442";
		Assert.assertTrue("Has Delegated Access: ", service.hasDelegatedAccess(uni));
		uni = "my2248";
		Assert.assertFalse("Has Delegated Access: ", service.hasDelegatedAccess(uni));
	}

	@Test
	public void testIsGrantAccess() {
		String uni = "wc2442";
		String courseId = "SOCIS1000_001_2015_2";
		String[] perms = {"role:ViewOnlyAdmin"};
		Assert.assertTrue("Access SOCIS1000_001_2015_2: ", service.isGrantAccess(uni, courseId, perms));
	}
	
	@Test
	public void testIsGrantAccessFromSchoolAndSubject() {
		String uni = "aa2468";
		String[] allows = {"role:ViewOnlyAdmin", "role:EditAllAdmin"};
		Assert.assertFalse("Access PSYC (subject) in INTF (school) ", service.isGrantAccess(uni, "SPUB", "EPID", allows));
		Assert.assertFalse("Access PSYC (subject) in INTF (school) ", service.isGrantAccess(uni, "SPUB", "PUBH", allows));
		Assert.assertFalse("Access PSYC (subject) in INTF (school) ", service.isGrantAccess(uni, "SPUB", "EHSC", allows));
		Assert.assertFalse("Access PSYC (subject) in INTF (school) ", service.isGrantAccess(uni, "SPUB", "HPMN", allows));
		Assert.assertFalse("Access PSYC (subject) in INTF (school) ", service.isGrantAccess(uni, "SPUB", "SOSC", allows));
		Assert.assertFalse("Access PSYC (subject) in INTF (school) ", service.isGrantAccess(uni, "SPUB", "POPF", allows));
		Assert.assertFalse("Access PSYC (subject) in INTF (school) ", service.isGrantAccess(uni, "SPUB", "BIST", allows));
	}
	
}
