package com.docscience.pathfinder.font.driver.output;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.FontException;
import com.docscience.pathfinder.font.driver.cff.CFFDictData;
import com.docscience.pathfinder.font.driver.cff.CFFFormatException;
import com.docscience.pathfinder.font.driver.cff.CFFWrapper;
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
import com.docscience.pathfinder.font.driver.ttf.TTFWrapper;
import com.docscience.pathfinder.font.driver.type1.Type1CharString;
import com.docscience.pathfinder.font.driver.type1.Type1FontDictionary;
import com.docscience.pathfinder.font.driver.type1.Type1PrivateDictionary;
import com.docscience.pathfinder.font.driver.type2.Type2CharString;
import com.docscience.pathfinder.font.driver.type2.Type2CharStringException;

/**
 * @author wxin
 *
 */
public class PSType1FromOTF implements PSFontOutput {

	private TTFWrapper ttfWrapper;
	private CFFWrapper cffWrapper;
	private Encoding encoding;
	private boolean outputGlyphNames2Unicode;
	
	public PSType1FromOTF(TTFWrapper ttfWrapper, Encoding encoding) throws CFFFormatException, IOException {
		this.ttfWrapper = ttfWrapper;
		this.cffWrapper = ttfWrapper.getCFFWrapper();
		this.encoding = encoding;
	}
	
	public void setOutputGlyphNames2Unicode(boolean b) {
		outputGlyphNames2Unicode = b;
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

		fontDict.setFontBBox(cffWrapper.getFontBBox());
		if (cffWrapper.getFontMatrix() != null) {
			fontDict.setFontMatrix(cffWrapper.getFontMatrix());
		}
		else {
			fontDict.setFontMatrix(new double[]{0.001, 0, 0, 0.001, 0, 0});
		}
		
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

	private int toTTFGlyphID(int codepoint) {
		int unicode = encoding.getUnicode(codepoint);
		int ttfGID = ttfWrapper.getGlyphID(unicode);
		return ttfGID;
	}

	private Type1PrivateDictionary getPrivate() {
		Type1PrivateDictionary privDict = new Type1PrivateDictionary();
		privDict.defineRD();
		privDict.defineND();
		privDict.defineNP();
		privDict.defineMinFeature();
		privDict.definePassword();
		privDict.setLenIV(4); // 4 bytes before charstring
		
		CFFDictData pd = cffWrapper.getPrivateDicts()[0];
        CFFDictData.Value v;
        v = pd.get(CFFDictData.Key.KEY_BLUEVALUES);
        if (v != null) {
        	privDict.setBlueValues(toIntArray(v.arrayValue()));
        }
        v = pd.get(CFFDictData.Key.KEY_OTHERBLUES);
        if (v != null) {
        	privDict.setBlueValues(toIntArray(v.arrayValue()));
        }
        v = pd.get(CFFDictData.Key.KEY_FAMILYBLUES);
        if (v != null) {
        	privDict.setFamilyBlues(toIntArray(v.arrayValue()));
        }
        v = pd.get(CFFDictData.Key.KEY_FAMILYOTHERBLUES);
        if (v != null) {
        	privDict.setFamilyOtherBlues(toIntArray(v.arrayValue()));
        }
        v = pd.get(CFFDictData.Key.KEY_STEMSNAPH);
        if (v != null) {
        	privDict.setStemSnapH(v.arrayValue());
        }
        v = pd.get(CFFDictData.Key.KEY_STEMSNAPV);
        if (v != null) {
        	privDict.setStemSnapV(v.arrayValue());
        }
        v = pd.get(CFFDictData.Key.KEY_BLUEFUZZ);
        if (v != null) {
        	privDict.setBlueFuzz((int) v.doubleValue());
        }
        v = pd.get(CFFDictData.Key.KEY_BLUESCALE);
        if (v != null) {
        	privDict.setBlueScale(v.doubleValue());
        }
        v = pd.get(CFFDictData.Key.KEY_BLUESHIFT);
        if (v != null) {
        	privDict.setBlueShift((int) v.doubleValue());
        }
        v = pd.get(CFFDictData.Key.KEY_STDHW);
        if (v != null) {
        	privDict.setStdHW(new double[]{v.doubleValue()});
        }
        v = pd.get(CFFDictData.Key.KEY_STDVW);
        if (v != null) {
        	privDict.setStdVW(new double[]{v.doubleValue()});
        }
        v = pd.get(CFFDictData.Key.KEY_LANGUAGEGROUP);
        if (v != null) {
        	privDict.setLanguageGroup(v.intValue());
        }
        v = pd.get(CFFDictData.Key.KEY_EXPANSIONFACTOR);
        if (v != null) {
        	privDict.setExpansionFactor(v.doubleValue());
        }
        v = pd.get(CFFDictData.Key.KEY_FORCEBOLD);
        if (v != null) {
        	privDict.setForceBold(v.booleanValue());
        }
		return privDict;
	}

	private PSFontInfoDictionary getFontInfo() {
		PSFontInfoDictionary fontInfo = new PSFontInfoDictionary();
		if (cffWrapper.getFamilyName() != null) {
			fontInfo.setFamilyName(cffWrapper.getFamilyName());
		}
		if (cffWrapper.getFullName() != null) {
			fontInfo.setFullName(cffWrapper.getFullName());
		}
		if (cffWrapper.getNotice() != null) {
			fontInfo.setNotice(cffWrapper.getNotice());
		}
		if (cffWrapper.getWeight() != null) {
			fontInfo.setWeight(cffWrapper.getWeight());
		}
		if (cffWrapper.getVersion() != null) {
			fontInfo.setVersion(cffWrapper.getVersion());
		}
		if (cffWrapper.getCopyright() != null) {
			fontInfo.setCopyright(cffWrapper.getCopyright());
		}
		if (cffWrapper.getIsFixedPitch() != null) {
			fontInfo.setIsFixedPitch(cffWrapper.getIsFixedPitch()[0]);
		}
		if (cffWrapper.getItalicAngle() != null) {
			fontInfo.setItalicAngle(cffWrapper.getItalicAngle()[0]);
		}
		if (cffWrapper.getUnderlinePosition() != null) {
			fontInfo.setUnderlinePosition(cffWrapper.getUnderlinePosition()[0]);
		}
		if (cffWrapper.getUnderlineThickness() != null) {
			fontInfo.setUnderlineThickness(cffWrapper.getUnderlineThickness()[0]);
		}
//		fontInfo.setFSType(4); // useless in type 1
		
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

	private int[] toIntArray(double[] dArray) {
		int[] iArray = new int[dArray.length];
		for (int i=0; i<iArray.length; ++i) {
			iArray[i] = (int) dArray[i];
		}
		return iArray;
	}
	
	@Override
	public int getGlyphID(int codepoint) throws FontException, IOException {
		return codepoint;
	}

	@Override
	public PSName getGlyphName(int glyphID) throws FontException, IOException {
		return new PSName(encoding.getGlyphName(glyphID));
	}
	
	private byte[] getType1CharStringBytes(int ttfGlyphID) throws Type2CharStringException {
		Type2CharString t2cs = cffWrapper.getType2CharString(ttfGlyphID);
		Type1CharString t1cs = cffWrapper.toType1CharString(t2cs, cffWrapper.getCFFFID(ttfGlyphID));
		return t1cs.getEncryptedBytes(new byte[] {0, 0, 0, 0});
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
