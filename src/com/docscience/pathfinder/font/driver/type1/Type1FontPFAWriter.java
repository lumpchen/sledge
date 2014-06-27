package com.docscience.pathfinder.font.driver.type1;

import java.io.IOException;
import java.io.OutputStream;

import com.docscience.pathfinder.font.driver.ps.PSObjectWriter;
import com.docscience.pathfinder.font.driver.ps.PSString;

/**
 * @author wxin
 *
 */
public class Type1FontPFAWriter {
	
	private Type1FontDictionary type1FontDict;
	
	public Type1FontPFAWriter(Type1FontDictionary type1FontDict) {
		this.type1FontDict = type1FontDict;
	}
	
	public void write(OutputStream stream) throws IOException {
		PSObjectWriter writer = new PSObjectWriter(stream);
		
		Type1FontPFBWriter pfbWriter = new Type1FontPFBWriter(type1FontDict);
		writer.write(pfbWriter.getPlainTextPortion1());
		
		PSString psString = new PSString(pfbWriter.getEncryptedPortion());
		writer.psObject(psString);
		
		writer.write(pfbWriter.getPlainTextPortion2());
	}
	
}
