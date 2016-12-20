package org.wjchen.prometheus.logics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.wjchen.prometheus.daos.EvalCrsReportDAO;
import org.wjchen.prometheus.daos.EvalIndReportDAO;
import org.wjchen.prometheus.daos.EvaluationControlDAO;
import org.wjchen.prometheus.models.EvalCrsReport;
import org.wjchen.prometheus.models.EvalIndReport;
import org.wjchen.prometheus.models.EvaluationControl;
import org.wjchen.prometheus.models.EvaluationReport;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
@Service("org.wjchen.prometheus.logics.EvalReportsLogic")
@Transactional("prometheus")
public class EvalReportsLogicImpl implements EvalReportsLogic {

	@Autowired
	private EvalCrsReportDAO crsDao;
	
	@Autowired
	private EvalIndReportDAO indDao;
	
	@Autowired
	private EvaluationControlDAO controlDao;
	
	public void init() {
		log.info("init");
	}

	@Override
	public List<EvalCrsReport> getCrsReportById(Long id) {
		List<EvalCrsReport> evals = crsDao.searchByCourseId(id);
		Collections.sort(evals);

		return evals;
	}

	@Override
	public List<EvalIndReport> getIndReportById(Long id) {
		List<EvalIndReport> evals = indDao.searchByCourseId(id);
		Collections.sort(evals);
		
		return evals;
	}


	@Override
	public String getTitleById(Long id) {
		String title = null;
		
		EvaluationControl control = controlDao.find(id);
		if(control != null) {
			title = control.getTitle();
		}
		
		return title;
	}


	@Override
	public List<EvaluationReport> getById(Long id) {
		List<EvaluationReport> reports = new ArrayList<EvaluationReport>();
		try {
			List<EvalIndReport> indReports = indDao.searchByCourseId(id);
			reports.addAll(indReports);
			List<EvalCrsReport> crsReports = crsDao.searchByCourseId(id);
			reports.addAll(crsReports);
		}
		catch(Exception e) {
			log.error("Could not get evaluation reports for course id: " + id.toString());
		}
		return reports;
	}
	
}
