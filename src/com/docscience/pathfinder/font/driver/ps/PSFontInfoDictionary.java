package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public class PSFontInfoDictionary implements PSStructure {

	protected PSDictionary psDict = new PSDictionary();
		
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

	public void setFamilyName(String value) {
		psDict.put(PSName.FamilyName, new PSString(value));
	}
	
	public void setFullName(String value) {
		psDict.put(PSName.FullName, new PSString(value)); 
	}
	
	public void setNotice(String value) {
		psDict.put(PSName.Notice, new PSString(value)); 
	}
	
	public void setWeight(String value) {
		psDict.put(PSName.Weight, new PSString(value));
	}
	
	public String getVersion() {
		PSString string = (PSString) psDict.get(PSName.version);
		if (string == null) {
			return null;
		}
		return string.getASCIIString();
	}
	
	public void setVersion(String value) {
		psDict.put(PSName.version, new PSString(value)); 
	}
	
	public void setItalicAngle(double value) {
		psDict.put(PSName.ItalicAngle, new PSReal(value));
	}
	
	public void setIsFixedPitch(boolean value) {
		psDict.put(PSName.isFixedPitch, PSBoolean.valueOf(value));
	}
	
	public void setUnderlinePosition(double value) {
		psDict.put(PSName.UnderlinePosition, new PSReal(value));
	}
	
	public void setUnderlineThickness(double value) {
		psDict.put(PSName.UnderlineThickness, new PSReal(value));
	}
	
	public void setGlyphNames2Unicode(PSGlyphNames2Unicode glyphNames2Unicode) {
		psDict.put(PSName.GlyphNames2Unicode, glyphNames2Unicode.getPSObject());
	}

	public void setFSType(int i) {
		psDict.put(PSName.FSType, PSInteger.valueOf(i));
	}

	public void setCopyright(String copyright) {
		psDict.put(PSName.Copyright, new PSString(copyright));
	}
}
