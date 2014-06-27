package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public class PSSave extends PSCompositeObject {

	@Override
	public int getType() {
		return TYPE_SAVE;
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
