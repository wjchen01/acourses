package org.wjchen.archivedcourse.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Collections;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(of={"year"})
public class AnnualPack implements Serializable {

	private static final long serialVersionUID = 1L;

	String year;
	List<CoursePack> coursePacks;
	
	public AnnualPack(String year) {
		this.year = year;
		coursePacks = new ArrayList<CoursePack>();
	}
	
	public void addCoursePack(CoursePack coursePack) {
		if(coursePack.getYear().equals(this.getYear())) {
			if(!coursePacks.contains(coursePack)) {
				coursePacks.add(coursePack);
				Collections.sort(coursePacks, new CoursePackComparator());
			}
		}
	}
	
}
