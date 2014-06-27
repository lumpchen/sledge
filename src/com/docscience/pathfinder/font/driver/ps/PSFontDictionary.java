package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public class PSFontDictionary implements PSStructure {
	
	protected PSDictionary psDict = new PSDictionary();
	
	private PSFontInfoDictionary psFontInfoDict;
	
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
	
	public int getFontType() {
		PSObject obj = psDict.get(PSName.FontType);
		return ((PSInteger) obj).intValue();
	}

	public void setFontType(int fontType) {
		psDict.put(PSName.FontType, PSInteger.valueOf(fontType));
	}
	
	public void setFontMatrix(double[] matrix) {
		assert(matrix.length == 6);
		psDict.put(PSName.FontMatrix, PSArray.convert(matrix));
	}

	public String getFontName() {
		PSName name = (PSName) psDict.get(PSName.FontName);
		if (name == null) {
			return null;
		}
		return name.getName();
	}
	
	public void setFontName(String name) {
		psDict.put(PSName.FontName, new PSName(name));
	}
	
	public PSFontInfoDictionary getFontInfo() {
		if (psFontInfoDict == null && psDict.contains(PSName.FontInfo)) {
			psFontInfoDict = new PSFontInfoDictionary();
			psFontInfoDict.setPSObject(psDict.get(PSName.FontInfo));
		}
		return psFontInfoDict;
	}
	
	public void setFontInfo(PSFontInfoDictionary fontInfo) {
		psFontInfoDict = fontInfo;
		psDict.put(PSName.FontInfo, fontInfo.getPSObject());
	}
	
	public void setLanguageLevel(int level) {
		psDict.put(PSName.LanguageLevel, PSInteger.valueOf(level));
	}
	
	public void setWMode(int writeMode) {
		psDict.put(PSName.WMode, PSInteger.valueOf(writeMode));
	}

	public void setFontID(PSFontID fontID) {
		psDict.put(PSName.FontID, fontID);
	}

	public void setEncoding(PSEncodingArray encoding) {
		psDict.put(PSName.Encoding, encoding.getPSObject());
	}
	
	public void setFontBBox(double[] bbox) {
		assert(bbox.length == 4);
		psDict.put(PSName.FontBBox, PSArray.convert(bbox));
	}
	
	public void setUniqueID(int uid) {
		psDict.put(PSName.UniqueID, PSInteger.valueOf(uid));
	}
	
	public void setXUID(int[] xuid) {
		PSArray array = new PSArray(xuid.length);
		for (int i=0; i<xuid.length; ++i) {
			array.add(PSInteger.valueOf(xuid[i]));
		}
		psDict.put(PSName.XUID, array);
	}
	
}
