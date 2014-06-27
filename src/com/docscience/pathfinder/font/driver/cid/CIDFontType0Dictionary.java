package com.docscience.pathfinder.font.driver.cid;

import com.docscience.pathfinder.font.driver.ps.PSArray;
import com.docscience.pathfinder.font.driver.ps.PSFontDictionary;
import com.docscience.pathfinder.font.driver.ps.PSInteger;
import com.docscience.pathfinder.font.driver.ps.PSName;
import com.docscience.pathfinder.font.driver.ps.PSString;
import com.docscience.pathfinder.font.driver.type1.Type1FontDictionary;

/**
 * @author wxin
 *
 */
public class CIDFontType0Dictionary extends Type1FontDictionary implements CIDFontDictionary {

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

	public void setCIDCount(int value) {
		psDict.put(PSName.CIDCount, PSInteger.valueOf(value));
	}
	
	public void setGDBytes(int gdBytes) {
		psDict.put(PSName.GDBytes, PSInteger.valueOf(gdBytes));
	}

	public void setCIDMapOffset(int value) {
		psDict.put(PSName.CIDMapOffset, PSInteger.valueOf(value));
	}
	
	public void setFDArray(PSArray array) {
		psDict.put(PSName.FDArray, array);
	}

	public void setFDBytes(int value) {
		psDict.put(PSName.FDBytes, PSInteger.valueOf(value));
	}

	public void setGlyphData(byte[] value) {
		psDict.put(PSName.GlyphData, new PSString(value));
	}
	
	public void addFontDictionary(PSFontDictionary fd) {
		PSArray array = (PSArray) psDict.get(PSName.FDArray);
		if (array == null) {
			array = new PSArray();
			psDict.put(PSName.FDArray, array);
		}
		array.add(fd.getPSObject());
	}

	public void setGlyphDirectory(CIDGlyphDirectory glyphDirectory) {
		psDict.put(PSName.GlyphDirectory, glyphDirectory.getPSObject());
	}
	
}
