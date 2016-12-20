package org.wjchen.courseworks.daos;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;

import org.wjchen.courseworks.models.SakaiUser;

public interface SakaiUserDAO extends GenericDAO<SakaiUser, String> {
	
	public SakaiUser findByUserId(String userId);
	public SakaiUser findByUserNm(String userName);
	public SakaiUser searchByUserNm(String userName);
	
}
