package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public class PSReal extends PSNumber {

	private double value;
	
	public PSReal(double value) {
		this.value = value;
	}

	@Override
	public int getType() {
		return TYPE_REAL;
	}

	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public int intValue() {
		return (int) value;
	}

	@Override
	public String toString() {
		return Double.toString(value);
	}
}
