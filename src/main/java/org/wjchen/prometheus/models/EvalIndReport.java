package org.wjchen.prometheus.models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "CMS_EVAL_IND_REPORTSDB")
public class EvalIndReport extends EvaluationReport implements Comparable<EvalIndReport> {

	private static final long serialVersionUID = 1L;
	
	private static final Map<String, String> formatMap = new HashMap<String, String>();
	static {
		formatMap.put("g", "Graph");
		formatMap.put("c", "Comments");
		formatMap.put("1p", "Concise");
	}
	
	@Column(name = "IND_TYPE")
	private String evalType;
	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CONTROL")
	private Faculty faculty;

	@Override
	public String filePath() {
		int begin = -1;
		if(formatType.equals("1p")) {
			begin = this.path.indexOf("onepage_eval");
		}
		else {
			begin = this.path.indexOf("ind_eval");
		}
		
		return this.path.substring(begin);
	}

	@Override
	public String formatName() {
		return formatMap.get(this.formatType);
	}

	@Override
	public String getUserName() {
		if(this.getEvalType().equalsIgnoreCase("core")) {
			return " ";
		}
		else {
			return this.getFaculty().getUserName();
		}
	}

	@Override
	public int compareTo(EvalIndReport other) {
		int compare = this.getEvalType().compareTo(other.getEvalType());
		if(compare == 0) {
			compare = this.getUserName().compareTo(other.getUserName());
			if(compare == 0) {
				compare = this.getFormatType().compareTo(other.getFormatType());
			}
		}
		return compare;
	}

}
