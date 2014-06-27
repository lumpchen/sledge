package com.docscience.pathfinder.font.driver.cid;

import com.docscience.pathfinder.font.driver.ps.PSArray;
import com.docscience.pathfinder.font.driver.ps.PSInteger;
import com.docscience.pathfinder.font.driver.ps.PSName;
import com.docscience.pathfinder.font.driver.type1.Type1PrivateDictionary;

/**
 * @author wxin
 *
 */
public class CIDPrivateDictionary extends Type1PrivateDictionary {

	public void setSubrCount(int value) {
		psDict.put(PSName.SubrCount, PSInteger.valueOf(value));
	}
	
	public void setSDBytes(int value) {
		psDict.put(PSName.SDBytes, PSInteger.valueOf(value));
	}
	
	public void setSubrMapOffset(int value) {
		psDict.put(PSName.SubrMapOffset, PSInteger.valueOf(value));	
	}

	public void defineProc(PSName name, PSArray proc) {
		psDict.put(name, proc);
	}
	
}
