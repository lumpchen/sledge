package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public class PSAccess {
	
	private static final String ACCESS_UNLIMITED = "Unlimited";
	private static final String ACCESS_READONLY = "ReadOnly";
	private static final String ACCESS_EXECUTEONLY = "ExecuteOnly";
	private static final String ACCESS_NONE = "None";

	public static final PSAccess AccessUnlimited = new PSAccess(ACCESS_UNLIMITED);
	public static final PSAccess AccessReadOnly  = new PSAccess(ACCESS_READONLY);
	public static final PSAccess AccessExecuteOnly = new PSAccess(ACCESS_EXECUTEONLY);
	public static final PSAccess AccessNone = new PSAccess(ACCESS_NONE);
	
	private String access;
	
	private PSAccess(String accessUnlimited2) {
		access = accessUnlimited2;
	}
	
	@Override
	public String toString() {
		return access;
	}
}
