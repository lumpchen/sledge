package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public class PSNull extends PSObject {

	public static final PSNull Null = new PSNull();
	
	private PSNull() {
		// do nothing here
	}
	
	@Override
	public int getType() {
		return TYPE_NULL;
	}
	
	@Override
	public String toString() {
		return "null";
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
