package com.docscience.pathfinder.font.driver.cid;

import com.docscience.pathfinder.font.driver.ps.PSDictionary;
import com.docscience.pathfinder.font.driver.ps.PSInteger;
import com.docscience.pathfinder.font.driver.ps.PSName;
import com.docscience.pathfinder.font.driver.ps.PSObject;
import com.docscience.pathfinder.font.driver.ps.PSString;
import com.docscience.pathfinder.font.driver.ps.PSStructure;

/**
 * @author wxin
 *
 */
public class CIDSystemInfo implements PSStructure {

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
	
	public void setRegistry(String string) {
		psDict.put(PSName.Registry, new PSString(string));
	}
	
	public void setOrdering(String string) {
		psDict.put(PSName.Ordering, new PSString(string));
	}
	
	public void setSupplement(int value) {
		psDict.put(PSName.Supplement, PSInteger.valueOf(value));
	}

}
 