package com.docscience.pathfinder.font.driver.cid;

import com.docscience.pathfinder.font.driver.ps.PSInteger;
import com.docscience.pathfinder.font.driver.ps.PSName;
import com.docscience.pathfinder.font.driver.type42.Type42FontDictionary;

/**
 * @author wxin
 *
 */
public class CIDFontType2Dictionary extends Type42FontDictionary implements CIDFontDictionary {

	@Override
	public void setCIDFontType(int value) {
		psDict.put(PSName.CIDFontType, PSInteger.valueOf(value));
	}
	
	@Override
	public void setCIDFontName(String name) {
		psDict.put(PSName.CIDFontName, new PSName(name));
	}
	
	@Override
	public void setCIDSystemInfo(CIDSystemInfo info) {
		psDict.put(PSName.CIDSystemInfo, info.getPSObject());
	}
		
	@Override
	public void setUIDBase(int value) {
		psDict.put(PSName.UIDBase, PSInteger.valueOf(value));
	}

	public void setCIDMap(CIDMap value) {
		psDict.put(PSName.CIDMap, value.getPSObject());
	}
	
	public void setCIDMap(int value) {
		psDict.put(PSName.CIDMap, PSInteger.valueOf(value));
	}
	
	public void setGDBytes(int gdBytes) {
		psDict.put(PSName.GDBytes, PSInteger.valueOf(gdBytes));
	}

	public void setCIDCount(int i) {
		psDict.put(PSName.CIDCount, PSInteger.valueOf(i));
	}
	
}
