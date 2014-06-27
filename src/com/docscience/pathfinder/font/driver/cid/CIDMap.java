package com.docscience.pathfinder.font.driver.cid;

import com.docscience.pathfinder.font.driver.ps.PSArray;
import com.docscience.pathfinder.font.driver.ps.PSDictionary;
import com.docscience.pathfinder.font.driver.ps.PSInteger;
import com.docscience.pathfinder.font.driver.ps.PSObject;
import com.docscience.pathfinder.font.driver.ps.PSStructure;

/**
 * @author wxin
 *
 */
public class CIDMap implements PSStructure {
	
	private PSObject psObject;
	
	@SuppressWarnings("unused")
	private int gdBytes;

	public CIDMap(int cidcount, int gdBytes) {
		this.gdBytes = gdBytes;
		PSInteger zero = PSInteger.valueOf(0);
		PSArray array = new PSArray(cidcount);
		for (int i=0; i<cidcount; ++i) {
			array.add(zero);
		}
		this.psObject = array;
	}
	
	public CIDMap() {
		this.psObject = new PSDictionary();
	}
	
	@Override
	public PSObject getPSObject() {
		return psObject;
	}
	
	@Override
	public void setPSObject(PSObject psObject) {
		if (!psObject.isArray() && !psObject.isDictionary()){
			throw new IllegalArgumentException("psObject must be either array or dictionary");		
		}
		this.psObject = psObject;
	}

	public void addMapping(int cid, int gid) {
		if (psObject.isArray()) {
			((PSArray) psObject).set(cid, PSInteger.valueOf(gid));
		}
		else {
			assert(psObject.isDictionary());
			((PSDictionary) psObject).put(PSInteger.valueOf(cid), PSInteger.valueOf(gid));
		}
	}
}
