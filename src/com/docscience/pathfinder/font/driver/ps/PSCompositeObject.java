package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public abstract class PSCompositeObject extends PSObject implements Cloneable {

	private PSAccess access;
	
	public PSAccess getAccess() {
		return access;
	}
	
	public void setAccess(PSAccess access) {
		this.access = access;
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			assert(false) : "never goes here";
		}
		return null;
	}
	
	@Override
	public final boolean isComposite() {
		return true;
	}
}
