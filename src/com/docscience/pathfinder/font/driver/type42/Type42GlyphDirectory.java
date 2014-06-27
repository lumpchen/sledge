package com.docscience.pathfinder.font.driver.type42;

import com.docscience.pathfinder.font.driver.ps.PSArray;
import com.docscience.pathfinder.font.driver.ps.PSDictionary;
import com.docscience.pathfinder.font.driver.ps.PSInteger;
import com.docscience.pathfinder.font.driver.ps.PSNull;
import com.docscience.pathfinder.font.driver.ps.PSObject;
import com.docscience.pathfinder.font.driver.ps.PSString;
import com.docscience.pathfinder.font.driver.ps.PSStructure;

/**
 * @author wxin
 *
 */
public class Type42GlyphDirectory implements PSStructure {

	private PSObject psObject;
	
	public Type42GlyphDirectory(int count) {
		PSArray array = new PSArray(count);
		for (int i=0; i<count; ++i) {
			array.add(PSNull.Null);
		}
		psObject = array;
	}
	
	public Type42GlyphDirectory() {
		psObject = new PSDictionary();
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
	
	public void setGlyphBytes(int glyphID, byte[] glyphBytes) {
		if (psObject.isArray()) {
			((PSArray) psObject).set(glyphID, new PSString(glyphBytes, 0, glyphBytes.length));
		}
		else {
			assert(psObject.isDictionary());
			((PSDictionary) psObject).put(PSInteger.valueOf(glyphID), new PSString(glyphBytes, 0, glyphBytes.length));
		}
	}

}
