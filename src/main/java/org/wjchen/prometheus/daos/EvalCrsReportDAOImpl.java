package org.wjchen.prometheus.daos;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import org.wjchen.prometheus.models.EvalCrsReport;

@Repository("org.wjchen.prometheus.daos.EvalCrsReportDAO")
public class EvalCrsReportDAOImpl extends GenericDAOImpl<EvalCrsReport, Long> implements EvalCrsReportDAO {

    @Autowired
    @Qualifier("org.wjchen.prometheus.SessionFactory")
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
    	super.setSessionFactory(sessionFactory);
    }

	public List<EvalCrsReport> searchByCourseId(Long id) {
		Search search = new Search(EvalCrsReport.class);
		Filter filter = Filter.equal("course.id", id);
		search.addFilter(filter);
		
		return this.search(search);
	}
	
}
