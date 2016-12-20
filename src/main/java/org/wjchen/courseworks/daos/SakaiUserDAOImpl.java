package org.wjchen.courseworks.daos;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import org.wjchen.courseworks.models.SakaiUser;

@Repository("org.wjchen.courseworks.daos.SakaiUserDAO")
public class SakaiUserDAOImpl extends GenericDAOImpl<SakaiUser, String> implements SakaiUserDAO {
	
    @Autowired
    @Qualifier("org.wjchen.courseworks.SessionFactory")
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
            super.setSessionFactory(sessionFactory);
    }

    @Override
    public SakaiUser findByUserId(String userId) {
		return this.find(userId);
	}
	
    @Override
    public SakaiUser searchByUserNm(String userName) {
		Search search = new Search(SakaiUser.class);
		Filter filter = Filter.like("userNm", '%' + userName + '%');
		search.addFilter(filter);
		
		List<SakaiUser> users = this.search(search);
		if(users.size() > 0) {
			return users.get(0);
		}
		else {
			return null;
		}
	}

    @Override
    public SakaiUser findByUserNm(String userName) {
		Search search = new Search(SakaiUser.class);
		Filter filter = Filter.equal("userNm", userName);
		search.addFilter(filter);    	
		
		List<SakaiUser> users = this.search(search);
		
		if(users.size() > 0) {
			return users.get(0);
		}
		else {
			return null;
		}
    }
    
}
