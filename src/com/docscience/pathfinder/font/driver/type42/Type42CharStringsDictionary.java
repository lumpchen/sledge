package com.docscience.pathfinder.font.driver.type42;

import com.docscience.pathfinder.font.driver.ps.PSDictionary;
import com.docscience.pathfinder.font.driver.ps.PSInteger;
import com.docscience.pathfinder.font.driver.ps.PSName;
import com.docscience.pathfinder.font.driver.ps.PSObject;
import com.docscience.pathfinder.font.driver.ps.PSStructure;

/**
 * @author wxin
 *
 */
public class Type42CharStringsDictionary implements PSStructure {

	private PSDictionary psDict = new PSDictionary();
		
	@Override
	public PSObject getPSObject() {
		return psDict;
	}
	
	@Override
	public void setPSObject(PSObject psObject) {
		if (!psObject.isDictionary()) {
			throw new IllegalArgumentException("psObject must be dictionary");
		}
		psDict = (PSDictionary) psObject;
	}

	public void setGID(String name, int gid) {
		psDict.put(new PSName(name), PSInteger.valueOf(gid));
	}
	
	public int  getGID(String name) {
		return ((PSInteger) psDict.get(new PSName(name))).intValue();
	}
}
