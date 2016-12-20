package org.wjchen.prometheus.models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "CMS_EVAL_REPORTSDB")
public class EvalCrsReport extends EvaluationReport implements Comparable<EvalCrsReport> {
	
	private static final long serialVersionUID = 1L;
	
	private static final Map<String, String> formatMap = new HashMap<String, String>();
	static {
		formatMap.put("g", "Graph");
		formatMap.put("c", "Comments");
		formatMap.put("s", "Statistics");
		formatMap.put("t", "Theme");
	}
	
	@Override
	public String filePath() {
		return "eval/" + this.path;
	}

	@Override
	public String formatName() {
		return formatMap.get(this.formatType);
	}

	@Override
	public String getUserName() {
		return " ";
	}
	
	@Override
	public String getEvalType() {
		return "Course";
	}

	@Override
	public int compareTo(EvalCrsReport other) {
		return this.getFormatType().compareTo(other.getFormatType());
	}
	
}
