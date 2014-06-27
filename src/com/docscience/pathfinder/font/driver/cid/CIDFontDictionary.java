package com.docscience.pathfinder.font.driver.cid;

import com.docscience.pathfinder.font.driver.ps.PSObject;

/**
 * @author wxin
 *
 */
public interface CIDFontDictionary {

	public void setCIDFontType(int value);
	
	public void setCIDFontName(String name);
	
	public void setCIDSystemInfo(CIDSystemInfo info);
		
	public void setUIDBase(int value);

	public PSObject getPSObject();
	
	public void setPSObject(PSObject psObject);
	
}
