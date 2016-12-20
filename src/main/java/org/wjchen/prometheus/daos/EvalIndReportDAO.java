package org.wjchen.prometheus.daos;

import java.util.List;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;

import org.wjchen.prometheus.models.EvalIndReport;

public interface EvalIndReportDAO extends GenericDAO<EvalIndReport, Long> {
	public List<EvalIndReport> searchByCourseId(Long id);
}
