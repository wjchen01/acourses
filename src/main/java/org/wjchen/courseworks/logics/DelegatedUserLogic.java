package org.wjchen.courseworks.logics;

public interface DelegatedUserLogic {
	
//	public SakaiUser getUserByUni(String uni, String...perms);
//	public SakaiUser getUserByUid(String uid, String...perms);
	
	public boolean hasDelegatedAccess(String uni);
	public boolean isGrantAccess(String uni, String courseId, String...allows);
	public boolean isGrantAccess(String uni, String school, String subject, String...allows);

}
