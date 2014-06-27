package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public class PSOperator extends PSObject {

	private String name;
	
	public PSOperator(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	@Override
	public int getType() {
		return TYPE_OPERATOR;
	}

	@Override
	public boolean isExecutable() {
		return true;
	}

	@Override
	public void setExecutable(boolean value) {
		// do nothing here
	}

	@Override
	public String toString() {
		return name;
	}
}
