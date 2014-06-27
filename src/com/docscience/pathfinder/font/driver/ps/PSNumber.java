package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public abstract class PSNumber extends PSObject {

	public abstract int intValue();
	
	public abstract double doubleValue();

	@Override
	public boolean equals(Object that) {
		if (that == null) {
			return false;
		}
		if (this == that) {
			return true;
		}
		if (!(that instanceof PSNumber)) {
			return false;
		}
		return this.doubleValue() == ((PSNumber) that).doubleValue();
	}
	
	@Override
	public int hashCode() {
		return (new Double(doubleValue())).hashCode();
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
