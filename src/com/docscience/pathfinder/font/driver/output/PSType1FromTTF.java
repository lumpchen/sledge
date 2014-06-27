package com.docscience.pathfinder.font.driver.output;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.FontException;
import com.docscience.pathfinder.font.driver.encoding.Encoding;
import com.docscience.pathfinder.font.driver.ps.PSEncodingArray;
import com.docscience.pathfinder.font.driver.ps.PSFontDictionary;
import com.docscience.pathfinder.font.driver.ps.PSFontInfoDictionary;
import com.docscience.pathfinder.font.driver.ps.PSFontOutput;
import com.docscience.pathfinder.font.driver.ps.PSGlyphNames2Unicode;
import com.docscience.pathfinder.font.driver.ps.PSName;
import com.docscience.pathfinder.font.driver.ps.PSObject;
import com.docscience.pathfinder.font.driver.ps.PSString;
import com.docscience.pathfinder.font.driver.ttf.TTFEncoding;
import com.docscience.pathfinder.font.driver.ttf.TTFFormatException;
import com.docscience.pathfinder.font.driver.ttf.TTFGlyphException;
import com.docscience.pathfinder.font.driver.ttf.TTFPostTable;
import com.docscience.pathfinder.font.driver.ttf.TTFWrapper;
import com.docscience.pathfinder.font.driver.type1.Type1CharString;
import com.docscience.pathfinder.font.driver.type1.Type1FontDictionary;
import com.docscience.pathfinder.font.driver.type1.Type1PrivateDictionary;
import com.docscience.pathfinder.font.shared.GlyphPath;
import com.docscience.pathfinder.font.shared.GlyphPathIterator;

/**
 * @author wxin
 *
 */
public class PSType1FromTTF implements PSFontOutput {

	private int unitBase = -1;
	private TTFWrapper ttfWrapper;
	private Encoding encoding;
	private boolean outputGlyphNames2Unicode;
	
	public PSType1FromTTF(TTFWrapper ttfWrapper, Encoding encoding) {
		this.ttfWrapper = ttfWrapper;
		this.encoding = encoding;
	}
	
	public void setUnitBase(int unitBase) {
		this.unitBase = unitBase;
	}
	
	public void setOutputGlyphNames2Unicode(boolean b) {
		outputGlyphNames2Unicode = b;
	}
	
	private double scale(double value) throws TTFFormatException, IOException {
		if (unitBase == -1) {
			return value;
		}
		else {
			return value / ttfWrapper.getHeadTable().getUnitsPerEm() * unitBase;
		}
	}
		
	@Override
	public int getCodePoint(int unicode) throws FontException, IOException {
		int c = encoding.getCodePoint(unicode);
		if (encoding.isDefinedCodePoint(c)) {
			return c;
		}
		else {
			return 0;
		}
	}

	@Override
	public PSFontDictionary getFontDictionary() throws FontException,
			IOException {
		Type1FontDictionary fontDict = new Type1FontDictionary();
		fontDict.setFontType(1);
		TTFWrapper.Name[] names = ttfWrapper.getNames(TTFEncoding.NAME_POSTSCRIPT_FONT_NAME);
		if (names != null) {
			fontDict.setFontName(names[0].getUnicode());
		}
		
		double matrix;
		if (unitBase == -1) {
			matrix = 1.0 / ttfWrapper.getHeadTable().getUnitsPerEm();
		}
		else {
			matrix = 1.0 / unitBase;
		}
		fontDict.setFontMatrix(new double[]{matrix, 0, 0, matrix, 0, 0});
		
		fontDict.setFontBBox(new double[] {
				scale(ttfWrapper.getHeadTable().getXMin()),
				scale(ttfWrapper.getHeadTable().getYMin()),
				scale(ttfWrapper.getHeadTable().getXMax()),
				scale(ttfWrapper.getHeadTable().getYMax()),
		});
		
		fontDict.setFontInfo(getFontInfo());
		fontDict.setPrivate(getPrivate());

		PSEncodingArray psEncoding = new PSEncodingArray();
		for (int cp = encoding.getMinCodePoint(); cp <= encoding.getMaxCodePoint(); ++cp) {
			if (encoding.isDefinedCodePoint(cp)) {
				psEncoding.setEncoding(cp, encoding.getGlyphName(cp));
			}
			else {
				psEncoding.setEncoding(cp, ".notdef");
			}
		}
		fontDict.setEncoding(psEncoding);
		
		fontDict.setPaintType(0);
		
		fontDict.addCharString(".notdef", getType1CharStringBytes(0));
		for (int cp = encoding.getMinCodePoint(); cp <= encoding.getMaxCodePoint(); ++cp) {
			if (encoding.isDefinedCodePoint(cp)) {
				int ttfGID = toTTFGlyphID(cp);
				fontDict.addCharString(encoding.getGlyphName(cp), getType1CharStringBytes(ttfGID));
			}
		}
		
		return fontDict;
	}
	
	private Type1PrivateDictionary getPrivate() {
		Type1PrivateDictionary privDict = new Type1PrivateDictionary();
		privDict.defineRD();
		privDict.defineND();
		privDict.defineNP();
		privDict.defineMinFeature();
		privDict.definePassword();
		privDict.setLenIV(4); // 4 bytes before charstring
		privDict.setBlueValues(new int[] {-15, 0});
		return privDict;
	}

	private PSFontInfoDictionary getFontInfo() throws TTFFormatException, IOException {
		PSFontInfoDictionary fontInfo = new PSFontInfoDictionary();
		TTFWrapper.Name[] names = null;
		names = ttfWrapper.getNames(TTFEncoding.NAME_FONT_FAMILY);
		if (names != null) {
			fontInfo.setFamilyName(names[0].getUnicode());			
		}
		names = ttfWrapper.getNames(TTFEncoding.NAME_FONT_FULL_NAME);
		if (names != null) {
			fontInfo.setFullName(names[0].getUnicode());
		}
		names = ttfWrapper.getNames(TTFEncoding.NAME_COPYRIGHT_NOTICE);
		if (names != null) {
			fontInfo.setNotice(names[0].getUnicode());
		}
		names = ttfWrapper.getNames(TTFEncoding.NAME_VERSION_STRING);
		if (names != null) {
			fontInfo.setVersion(names[0].getUnicode());
		}
		TTFPostTable postTable = ttfWrapper.getPostTable();
		if (postTable != null) {
			fontInfo.setItalicAngle(postTable.getItalicAngle());
			fontInfo.setIsFixedPitch(postTable.getIsFixedPitch());
			fontInfo.setUnderlinePosition(scale(postTable.getUnderlinePosition()));
			fontInfo.setUnderlineThickness(scale(postTable.getUnderlineThickness()));
		}
		if (outputGlyphNames2Unicode) {
			PSGlyphNames2Unicode glyphNames2Unicode = new PSGlyphNames2Unicode();
			for (int i = encoding.getMinCodePoint(); i <= encoding.getMaxCodePoint(); ++i) {
				if (encoding.isDefinedCodePoint(i)) {
					glyphNames2Unicode.addUnicode(i, encoding.getUnicode(i));
				}
			}
			fontInfo.setGlyphNames2Unicode(glyphNames2Unicode);
		}
		return fontInfo;
	}

	private int toTTFGlyphID(int codepoint) {
		int unicode = encoding.getUnicode(codepoint);
		int ttfGID = ttfWrapper.getGlyphID(unicode);
		return ttfGID;
	}

	private byte[] getType1CharStringBytes(int ttfGID) throws TTFGlyphException, TTFFormatException, IOException {
		Type1CharString t1cs = new Type1CharString();
		
		int sbx = (int) scale(ttfWrapper.getHmtxTable().getLeftSideBearing(ttfGID));
		int wx  = (int) scale(ttfWrapper.getHmtxTable().getAdvanceWidth(ttfGID));
		t1cs.hsbw(sbx, wx);
		
		GlyphPath ttfPath = ttfWrapper.getGlyphPath(ttfGID, true);
		double factor = 1;
		if (unitBase != -1) {
			factor = unitBase / (double) ttfWrapper.getHeadTable().getUnitsPerEm();
		}
	
		GlyphPathIterator pitr = ttfPath.getGlyphPathIterator(factor, 0, 0, factor, 0, 0);
		double[] coords = new double[6];
		int x = sbx, y = 0;
		while (!pitr.isDone()) {
			int type = pitr.currentSegment(coords);
			switch(type) {
			case GlyphPathIterator.SEG_MOVETO:
				t1cs.rmoveto((int) coords[0] - x, (int) coords[1] - y);
				x = (int) coords[0];
				y = (int) coords[1];
				break;
			case GlyphPathIterator.SEG_LINETO:
				t1cs.rlineto((int) coords[0] - x, (int) coords[1] - y);
				x = (int) coords[0];
				y = (int) coords[1];
				break;
			case GlyphPathIterator.SEG_CURVETO:
                int dx1 = (int) (coords[0] - x);
                int dy1 = (int) (coords[1] - y);
                int dx2 = (int) (coords[2] - coords[0]);
                int dy2 = (int) (coords[3] - coords[1]);
                int dx3 = (int) (coords[4] - coords[2]);
                int dy3 = (int) (coords[5] - coords[3]);
                t1cs.rrcurveto(dx1, dy1, dx2, dy2, dx3, dy3);
                x = (int) (coords[4]);
                y = (int) (coords[5]);
				break;
			case GlyphPathIterator.SEG_CLOSE:
				t1cs.closepath();
				break;
			default:
				assert(false) : "never goes here";
			}
			pitr.next();
		}
		
		t1cs.endchar();
		
		return t1cs.getEncryptedBytes(new byte[] {0, 0, 0, 0});
	}

	@Override
	public int getGlyphID(int codepoint) throws FontException, IOException {
		// for type1 font glyphID is always same as code point.
		return codepoint;
	}
	
	@Override
	public PSName getGlyphName(int glyphID) {
		return new PSName(encoding.getGlyphName(glyphID));
	}

	@Override
	public PSObject getGlyphPresentation(int glyphID) throws FontException,
			IOException {
		return new PSString(getType1CharStringBytes(toTTFGlyphID(glyphID)));
	}

	@Override
	public PSString getShowString(String string) throws FontException,
			IOException {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<string.length(); ++i) {
			sb.append((char) getCodePoint(string.charAt(i)));
		}
		return new PSString(sb.toString());
	}

}
