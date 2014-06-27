package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public class PSEncodingArray implements PSStructure {

	public static final int MIN_ENCODING = 0;
	public static final int MAX_ENCODING = 255;
	
	private PSArray psArray;
		
	public PSEncodingArray() {
		psArray = new PSArray(MAX_ENCODING + 1);
		for (int i=0; i<=MAX_ENCODING; ++i) {
			psArray.add(PSName._notdef);
		}
		psArray.setIndexFormat(true);
	}
	
	public PSEncodingArray(int size) {
		psArray = new PSArray(size);
		for (int i=0; i<size; ++i) {
			psArray.add(PSName._notdef);
		}
		psArray.setIndexFormat(true);
	}
	
	public void setEncoding(int i, String name) {
		psArray.set(i, new PSName(name));
	}
	
	public String getEncoding(int i) {
		return ((PSName) psArray.get(i)).getName();
	}
	
	@Override
	public PSObject getPSObject() {
		return psArray;
	}

	@Override
	public void setPSObject(PSObject psObject) {
		if (!psObject.isArray()) {
			throw new IllegalArgumentException("psObject must be array");
		}
		psArray = (PSArray) psObject;
	}

}
