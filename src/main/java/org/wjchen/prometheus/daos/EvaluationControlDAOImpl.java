package org.wjchen.prometheus.daos;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;

import org.wjchen.prometheus.models.EvaluationControl;

@Repository("org.wjchen.prometheus.daos.EvaluationControlDAO")
public class EvaluationControlDAOImpl extends GenericDAOImpl<EvaluationControl, Long> implements EvaluationControlDAO {

    @Autowired
    @Qualifier("org.wjchen.prometheus.SessionFactory")
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
            super.setSessionFactory(sessionFactory);
    }

}
