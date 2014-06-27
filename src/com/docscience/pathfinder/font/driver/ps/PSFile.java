package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public class PSFile extends PSCompositeObject {

	private boolean executable;

	@Override
	public int getType() {
		return TYPE_FILE;
	}

	@Override
	public boolean isExecutable() {
		return executable;
	}

	@Override
	public void setExecutable(boolean value) {
		this.executable = value;
	}

	@Override
	public String toString() {
		return "file";
	}
}
