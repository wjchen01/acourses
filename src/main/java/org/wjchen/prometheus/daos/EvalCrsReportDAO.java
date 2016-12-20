package org.wjchen.prometheus.daos;

import java.util.List;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;

import org.wjchen.prometheus.models.EvalCrsReport;

public interface EvalCrsReportDAO extends GenericDAO<EvalCrsReport, Long> {
	public List<EvalCrsReport> searchByCourseId(Long id);
}
