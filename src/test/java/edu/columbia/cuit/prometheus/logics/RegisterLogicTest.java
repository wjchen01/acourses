package org.wjchen.prometheus.logics;

import java.util.Set;

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

import org.wjchen.prometheus.models.CourseDetail;
import org.wjchen.prometheus.models.CourseInfo;
import org.wjchen.prometheus.models.Faculty;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:prometheus-components.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
									 DirtiesContextTestExecutionListener.class, 
									 TransactionalTestExecutionListener.class})
@TransactionConfiguration(defaultRollback=true)
@Transactional("prometheus")
public class RegisterLogicTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private RegisterLogic service;
	
	@Test
	public void testGetCourseByFaculty() {
		String userName = "amo1";
		Set<CourseInfo> courses = service.getCourseByUserName(userName);
		Assert.assertEquals(userName + " course count: ", 142, courses.size());
		
		courses.clear();
		String lastName = "Oakley";
		courses = service.getCourseByLastName(lastName);
		Assert.assertEquals(lastName + " course count: ", 142, courses.size());
	}

	@Test
	public void testFacultyByCourse() {
		String uniqueId = "RELIG9502_006_2002_1";
		Set<Faculty> faculties = service.getFacultyByCourseId(uniqueId);
		Assert.assertEquals("amo1 course count: ", 1, faculties.size());
		
		CourseInfo course = service.getCourseByCourseId(uniqueId);
		CourseDetail detail = course.getDetail();
		Assert.assertEquals("Year: ", "2002", detail.getYear());
		Assert.assertEquals("Year: ", "1", detail.getSemester());		
	}

}
