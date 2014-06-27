package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public final class PSBoolean extends PSObject {

	public static final PSBoolean True = new PSBoolean(true);
	public static final PSBoolean False = new PSBoolean(false);
	
	private boolean value;
	
	private PSBoolean(boolean value) {
		this.value = value;
	}
	
	@Override
	public int getType() {
		return TYPE_BOOLEAN;
	}
	
	public boolean booleanValue() {
		return value;
	}
		
	@Override
	public boolean isExecutable() {
		return false;
	}

	@Override
	public void setExecutable(boolean value) {
		// do nothing here
	}

	@Override
	public String toString() {
		return value ? "true" : "false";
	}

	public static PSBoolean valueOf(boolean value) {
		return value ? True : False;
	}
	
}
