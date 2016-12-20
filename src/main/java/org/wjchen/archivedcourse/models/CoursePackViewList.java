package org.wjchen.archivedcourse.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CoursePackViewList implements Serializable {

	private static final long serialVersionUID = 711040995201113475L;

	@Getter @Setter
	public class AnnualPackPredicate implements Predicate<AnnualPack> {

		private String year;
		
		public AnnualPackPredicate(String year) {
			super();
			this.year = year;
		}
		
		@Override
		public boolean apply(AnnualPack pack) {
			return this.getYear().equals(pack.getYear());
		}
		
	}
	private List<AnnualPack> annualPacks;

	public CoursePackViewList() {
		annualPacks = new ArrayList<AnnualPack>();
	}
	
	public void addCourses(List<CoursePack> additions) {			
		// group by years
		for(CoursePack pack : additions) {
			String year = pack.getYear();
			if(year == null) {
				continue;
			}

			AnnualPack yearPack = Iterables.find(annualPacks, new AnnualPackPredicate(year), null);
			if(yearPack == null) {
				yearPack = new AnnualPack(year);
				annualPacks.add(yearPack);
				Collections.sort(annualPacks, new AnnualPackComparator());
			}
			yearPack.addCoursePack(pack);
		}				
	}

	public void clear() {
		this.getAnnualPacks().clear();
	}
	
	public int hasCourses() {
		if(annualPacks.size() > 0) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
}
