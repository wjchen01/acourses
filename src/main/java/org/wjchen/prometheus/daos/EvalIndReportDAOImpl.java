package org.wjchen.prometheus.daos;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import org.wjchen.prometheus.models.EvalIndReport;

@Repository("org.wjchen.prometheus.daos.EvalIndReportDAO")
public class EvalIndReportDAOImpl extends GenericDAOImpl<EvalIndReport, Long> implements EvalIndReportDAO {

    @Autowired
    @Qualifier("org.wjchen.prometheus.SessionFactory")
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
            super.setSessionFactory(sessionFactory);
    }

	public List<EvalIndReport> searchByCourseId(Long id) {
		Search search = new Search(EvalIndReport.class);
		Filter filter = Filter.equal("course.id", id);
		search.addFilter(filter);
		
		return this.search(search);
	}

}
