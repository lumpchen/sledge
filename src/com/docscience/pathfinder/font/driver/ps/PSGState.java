package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public class PSGState extends PSCompositeObject {

	@Override
	public int getType() {
		return TYPE_GSTATE;
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
