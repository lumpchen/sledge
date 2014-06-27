package com.docscience.pathfinder.font.driver.ps;

public class PSGlyphNames2Unicode implements PSStructure {

	PSDictionary psDict = new PSDictionary();
	
	@Override
	public PSObject getPSObject() {
		return psDict;
	}

	@Override
	public void setPSObject(PSObject psObject) {
		if (psObject.isDictionary()) {
			throw new IllegalArgumentException("psObject must be dictionary");
		}
		psDict = (PSDictionary) psObject;
	}

	public void addUnicode(int codepoint, int unicode) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) ((unicode >> 8) & 0xff);
		bytes[1] = (byte) (unicode & 0xff);
		psDict.put(PSInteger.valueOf(codepoint), new PSString(bytes, 0, 2));
	}
	
	public int getUnicode(int codepoint) {
		PSString hexString = (PSString) psDict.get(PSInteger.valueOf(codepoint));
		byte[] bytes = hexString.getBytes();
		return ((bytes[0] & 0x0f) << 8) | (bytes[1] & 0x0f);
	}
}
