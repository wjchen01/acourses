package org.wjchen.archivedcourse.models;

import java.util.Comparator;

public class CoursePackComparator implements Comparator<CoursePack> {

	@Override
	public int compare(CoursePack o1, CoursePack o2) {
		String c1 = o1.getUniqueId();
		String c2 = o2.getUniqueId();
		
		return c1.compareTo(c2);
	}

}
