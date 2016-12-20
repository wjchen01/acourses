package org.wjchen.archivedcourse.models;

import java.util.Comparator;

public class AnnualPackComparator implements Comparator<AnnualPack> {

	@Override
	public int compare(AnnualPack o1, AnnualPack o2) {
		String c1 = o1.getYear();
		String c2 = o2.getYear();
		
		return c2.compareTo(c1);
	}

}
