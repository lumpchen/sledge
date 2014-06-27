package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public class PSMark extends PSObject {

	public static PSMark Mark = new PSMark();
	
	private PSMark() {
		// do nothing here
	}
	
	@Override
	public int getType() {
		return TYPE_MARK;
	}
	
	@Override
	public String toString() {
		return "mark";
	}

	@Override
	public boolean isExecutable() {
		return false;
	}

	@Override
	public void setExecutable(boolean value) {
		// do nothing here
	}

}
