package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public class PSPackedArray extends PSArray {

	// no special behavior now, just compatible with PS Specification
	
	@Override
	public int getType() {
		return TYPE_PACKEDARRAY;
	}
	
}
