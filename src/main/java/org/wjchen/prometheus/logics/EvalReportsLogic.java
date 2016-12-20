package org.wjchen.prometheus.logics;

import java.util.List;

import org.wjchen.prometheus.models.EvalCrsReport;
import org.wjchen.prometheus.models.EvalIndReport;
import org.wjchen.prometheus.models.EvaluationReport;


public interface EvalReportsLogic {

	public List<EvaluationReport> getById(Long id);
	public List<EvalCrsReport> getCrsReportById(Long id);
	public List<EvalIndReport> getIndReportById(Long id);	
	public String getTitleById(Long id);
	
}
