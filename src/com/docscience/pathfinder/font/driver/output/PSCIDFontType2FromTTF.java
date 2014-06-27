package com.docscience.pathfinder.font.driver.output;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.FontException;
import com.docscience.pathfinder.font.driver.cid.CIDFontType2Dictionary;
import com.docscience.pathfinder.font.driver.cid.CIDSystemInfo;
import com.docscience.pathfinder.font.driver.ps.PSEncodingArray;
import com.docscience.pathfinder.font.driver.ps.PSFontDictionary;
import com.docscience.pathfinder.font.driver.ps.PSFontInfoDictionary;
import com.docscience.pathfinder.font.driver.ps.PSFontOutput;
import com.docscience.pathfinder.font.driver.ps.PSGlyphNames2Unicode;
import com.docscience.pathfinder.font.driver.ps.PSName;
import com.docscience.pathfinder.font.driver.ps.PSObject;
import com.docscience.pathfinder.font.driver.ps.PSString;
import com.docscience.pathfinder.font.driver.ttf.TTFFormatException;
import com.docscience.pathfinder.font.driver.ttf.TTFWrapper;
import com.docscience.pathfinder.font.driver.type42.Type42CharStringsDictionary;
import com.docscience.pathfinder.font.driver.type42.Type42GlyphDirectory;
import com.docscience.pathfinder.font.driver.type42.Type42SFNTExtractor;

/**
 * @author wxin
 *
 */
public class PSCIDFontType2FromTTF implements PSFontOutput {

	private TTFWrapper ttfWrapper;
	private String cidFontName;
	private boolean outputGlyphNames2Unicode;
	
	public PSCIDFontType2FromTTF(TTFWrapper ttfWrapper, String cidFontName) {
		this.ttfWrapper = ttfWrapper;
		this.cidFontName = cidFontName;
	}
	
	public void setOutputGlyphNames2Unicode(boolean b) {
		outputGlyphNames2Unicode = b;
	}
	
	@Override
	public PSFontDictionary getFontDictionary() throws TTFFormatException, IOException {
		CIDFontType2Dictionary fontDict = new CIDFontType2Dictionary();
		fontDict.setFontType(42);
		fontDict.setFontMatrix(new double[] {1.0, 0, 0, 1.0, 0, 0});
		fontDict.setFontBBox(new double[] {
				ttfWrapper.getHeadTable().getXMin(),
				ttfWrapper.getHeadTable().getYMin(),
				ttfWrapper.getHeadTable().getXMax(),
				ttfWrapper.getHeadTable().getYMax(),
		});
		fontDict.setCIDFontType(2);
		fontDict.setCIDFontName(cidFontName);
		fontDict.setCIDCount(65535);
		fontDict.setPaintType(CIDFontType2Dictionary.PAINTTYPE_FILL);
		fontDict.setGDBytes(2);
		
		CIDSystemInfo sysInfo = new CIDSystemInfo();
		sysInfo.setRegistry("Adobe");
		sysInfo.setOrdering("Identity");
		sysInfo.setSupplement(0);
		fontDict.setCIDSystemInfo(sysInfo);
		
		Type42CharStringsDictionary charStrings = new Type42CharStringsDictionary();
		charStrings.setGID(".notdef", 0);
		fontDict.setCharStrings(charStrings);
		
		PSEncodingArray encoding = new PSEncodingArray(1);
		encoding.setEncoding(0, ".notdef");
		fontDict.setEncoding(encoding);
		
		fontDict.setCIDMap(0);
		
		PSFontInfoDictionary fontInfo = new PSFontInfoDictionary();
		if (outputGlyphNames2Unicode) {
			PSGlyphNames2Unicode glyphNames2Unicode = new PSGlyphNames2Unicode();
			for (int c=0; c<65536; ++c) {
				int g = ttfWrapper.getGlyphID(c);
				if (g != 0) {
					glyphNames2Unicode.addUnicode(g, c);
				}
			}
			fontInfo.setGlyphNames2Unicode(glyphNames2Unicode);
		}
		fontDict.setFontInfo(fontInfo);
		
		Type42GlyphDirectory glyphDirectory = new Type42GlyphDirectory();
		glyphDirectory.setGlyphBytes(0, ttfWrapper.getGlyphBytes(0));
		fontDict.setGlyphDirectory(glyphDirectory);
		
		Type42SFNTExtractor sfntExtractor = new Type42SFNTExtractor(ttfWrapper);
		fontDict.setSfnts(sfntExtractor.getSFNTBytesArray());
		
		return fontDict;
	}

	@Override
	public int getCodePoint(int unicode) throws FontException, IOException {
		return ttfWrapper.getGlyphID(unicode);
	}

	@Override
	public int getGlyphID(int codepoint) throws FontException, IOException {
		return codepoint;
	}

	@Override
	public PSObject getGlyphPresentation(int glyphID) throws FontException,
			IOException {
		return new PSString(ttfWrapper.getGlyphBytes(glyphID));
	}

	@Override
	public PSString getShowString(String string) throws FontException,
			IOException {
		byte[] bytes = new byte[string.length() * 2];
		int bi = 0;
		for (int i=0; i<string.length(); ++i) {
			int c = string.charAt(i);
			int g = ttfWrapper.getGlyphID(c);
			bytes[bi++] = (byte) ((g >> 8) & 0xff);
			bytes[bi++] = (byte) (g & 0xff);
		}
		return new PSString(bytes, 0, bi);
	}

	@Override
	public PSName getGlyphName(int glyphID) throws FontException, IOException {
		return new PSName(Integer.toString(glyphID));
	}

}
