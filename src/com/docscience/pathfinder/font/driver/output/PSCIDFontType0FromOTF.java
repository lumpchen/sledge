package com.docscience.pathfinder.font.driver.output;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.FontException;
import com.docscience.pathfinder.font.driver.cff.CFFDictData;
import com.docscience.pathfinder.font.driver.cff.CFFFormatException;
import com.docscience.pathfinder.font.driver.cff.CFFWrapper;
import com.docscience.pathfinder.font.driver.cid.CIDFontType0Dictionary;
import com.docscience.pathfinder.font.driver.cid.CIDGlyphDirectory;
import com.docscience.pathfinder.font.driver.cid.CIDPrivateDictionary;
import com.docscience.pathfinder.font.driver.cid.CIDSystemInfo;
import com.docscience.pathfinder.font.driver.ps.PSArray;
import com.docscience.pathfinder.font.driver.ps.PSFontDictionary;
import com.docscience.pathfinder.font.driver.ps.PSFontInfoDictionary;
import com.docscience.pathfinder.font.driver.ps.PSFontOutput;
import com.docscience.pathfinder.font.driver.ps.PSGlyphNames2Unicode;
import com.docscience.pathfinder.font.driver.ps.PSName;
import com.docscience.pathfinder.font.driver.ps.PSObject;
import com.docscience.pathfinder.font.driver.ps.PSString;
import com.docscience.pathfinder.font.driver.ttf.TTFWrapper;
import com.docscience.pathfinder.font.driver.type1.Type1CharString;
import com.docscience.pathfinder.font.driver.type1.Type1FontDictionary;
import com.docscience.pathfinder.font.driver.type2.Type2CharString;

/**
 * @author wxin
 *
 */
public class PSCIDFontType0FromOTF implements PSFontOutput {

	private TTFWrapper ttfWrapper;
	private CFFWrapper cffWrapper;
	private String cidFontName;
	private boolean outputGlyphNames2Unicode;
	
	public PSCIDFontType0FromOTF(TTFWrapper ttfWrapper, String cidFontName) throws CFFFormatException, IOException {
		this.ttfWrapper = ttfWrapper;
		this.cffWrapper = ttfWrapper.getCFFWrapper();
		this.cidFontName = cidFontName;
	}
	
	public void setOutputGlyphNames2Unicode(boolean b) {
		outputGlyphNames2Unicode = b;
	}
	
	@Override
	public PSFontDictionary getFontDictionary() throws FontException,
			IOException {
		CIDFontType0Dictionary fontDict = new CIDFontType0Dictionary();
		fontDict.setCIDFontType(0);
		fontDict.setCIDFontName(cidFontName);
			
		CIDSystemInfo sysInfo = new CIDSystemInfo();
		if (cffWrapper.isCIDFont()) {
			sysInfo.setRegistry(cffWrapper.getRegistry());
			sysInfo.setOrdering(cffWrapper.getOrdering());
			sysInfo.setSupplement(cffWrapper.getSupplement());
		}
		else {
			sysInfo.setRegistry("Adobe");
			sysInfo.setOrdering("Identity");
			sysInfo.setSupplement(0);
		}
		fontDict.setCIDSystemInfo(sysInfo);
			
		fontDict.setFontBBox(cffWrapper.getFontBBox());
		if (cffWrapper.getFontMatrix() != null) {
			fontDict.setFontMatrix(cffWrapper.getFontMatrix());
		}
		
		fontDict.setFontInfo(getFontInfo());

		int[] uidBase = cffWrapper.getUIDBase();
		if (uidBase != null) {
			fontDict.setUIDBase(uidBase[0]);
		}
		int[] xuid = cffWrapper.getXUID();
		if (xuid != null) {
			fontDict.setXUID(xuid);
		}
		
		fontDict.setCIDCount(cffWrapper.getCIDCount());
		fontDict.setGDBytes(1);
		fontDict.setCIDMapOffset(0);
		fontDict.setFDArray(getFDArray());
		fontDict.setFDBytes(1);
		
		int[] paintType = cffWrapper.getPaintType();
		if (paintType != null) {
			fontDict.setPaintType(paintType[0]);
		}
		
		double[] strokeWidth = cffWrapper.getStrokeWidth();
		if (strokeWidth != null) {
			fontDict.setStrokeWidth(strokeWidth[0]);
		}
		
		CIDGlyphDirectory glyphDirectory = new CIDGlyphDirectory();
		glyphDirectory.setGlyphBytes(0, ((PSString) getGlyphPresentation(0)).getBytes());
		fontDict.setGlyphDirectory(glyphDirectory);
		
		fontDict.setGlyphData(new byte[0]);
		
		return fontDict;
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
		fontInfo.setFSType(4);
		
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
		return fontInfo;
	}

	private PSArray getFDArray() {
		PSArray array = new PSArray();
		if (cffWrapper.isCIDFont()) {
			for (int i=0; i<cffWrapper.getFDArray().length; ++i) {
				CFFDictData fd = cffWrapper.getFDArray()[i];
				CFFDictData.Value v;
				
				Type1FontDictionary t1font = new Type1FontDictionary();
				t1font.setFontType(1);
				t1font.setPaintType(0);
				v = fd.get(CFFDictData.Key.KEY_FONTNAME);
				t1font.setFontName(cffWrapper.getStrings().getStringBySID(v.intValue()));
				
				v = fd.get(CFFDictData.Key.KEY_FONTMATRIX);
				if (v == null) {
					t1font.setFontMatrix(new double[]{0.001, 0, 0, 0.001, 0, 0});
				}
				else {
					t1font.setFontMatrix(v.arrayValue());
				}
				t1font.setPrivate(getPrivateDictionary(i));
				
				array.add(t1font.getPSObject());
			}
		}
		else {
			Type1FontDictionary t1font = new Type1FontDictionary();
			t1font.setFontType(1);
			t1font.setPaintType(0);
			t1font.setFontName(cidFontName + "-Sub");
			double[] m = cffWrapper.getFontMatrix();
			if (m == null) {
				t1font.setFontMatrix(new double[]{0.001, 0, 0, 0.001, 0, 0});
			}
			else {
				t1font.setFontMatrix(m);
			}
			t1font.setPrivate(getPrivateDictionary(0));
			array.add(t1font.getPSObject());
		}
		return array;
	}

	private CIDPrivateDictionary getPrivateDictionary(int fdIndex) {
		CIDPrivateDictionary dict = new CIDPrivateDictionary();
		if (cffWrapper.isCIDFont()) {
			dict.setSubrMapOffset(0);
			dict.setSDBytes(1);
			if (cffWrapper.getLocalSubrsArray()[fdIndex] == null) {
				dict.setSubrCount(0);
			}
			else {
				dict.setSubrCount(cffWrapper.getLocalSubrsArray()[fdIndex].getNumSubrs());
			}
		}
		else {
			dict.defineRD();
			dict.defineRDAlter();
			dict.defineND();
			dict.defineNDAlter();
			dict.defineNP();
			dict.defineNPAlter();
		}
		
		dict.definePassword();
		dict.defineMinFeature();
		
		CFFDictData pd = cffWrapper.getPrivateDicts()[fdIndex];
        CFFDictData.Value v;
        v = pd.get(CFFDictData.Key.KEY_BLUEVALUES);
        if (v != null) {
        	dict.setBlueValues(toIntArray(v.arrayValue()));
        }
        v = pd.get(CFFDictData.Key.KEY_OTHERBLUES);
        if (v != null) {
        	dict.setBlueValues(toIntArray(v.arrayValue()));
        }
        v = pd.get(CFFDictData.Key.KEY_FAMILYBLUES);
        if (v != null) {
        	dict.setFamilyBlues(toIntArray(v.arrayValue()));
        }
        v = pd.get(CFFDictData.Key.KEY_FAMILYOTHERBLUES);
        if (v != null) {
        	dict.setFamilyOtherBlues(toIntArray(v.arrayValue()));
        }
        v = pd.get(CFFDictData.Key.KEY_STEMSNAPH);
        if (v != null) {
        	dict.setStemSnapH(v.arrayValue());
        }
        v = pd.get(CFFDictData.Key.KEY_STEMSNAPV);
        if (v != null) {
        	dict.setStemSnapV(v.arrayValue());
        }
        v = pd.get(CFFDictData.Key.KEY_BLUEFUZZ);
        if (v != null) {
        	dict.setBlueFuzz((int) v.doubleValue());
        }
        v = pd.get(CFFDictData.Key.KEY_BLUESCALE);
        if (v != null) {
        	dict.setBlueScale(v.doubleValue());
        }
        v = pd.get(CFFDictData.Key.KEY_BLUESHIFT);
        if (v != null) {
        	dict.setBlueShift((int) v.doubleValue());
        }
        v = pd.get(CFFDictData.Key.KEY_STDHW);
        if (v != null) {
        	dict.setStdHW(new double[]{v.doubleValue()});
        }
        v = pd.get(CFFDictData.Key.KEY_STDVW);
        if (v != null) {
        	dict.setStdVW(new double[]{v.doubleValue()});
        }
        v = pd.get(CFFDictData.Key.KEY_LANGUAGEGROUP);
        if (v != null) {
        	dict.setLanguageGroup(v.intValue());
        }
        v = pd.get(CFFDictData.Key.KEY_EXPANSIONFACTOR);
        if (v != null) {
        	dict.setExpansionFactor(v.doubleValue());
        }
        v = pd.get(CFFDictData.Key.KEY_FORCEBOLD);
        if (v != null) {
        	dict.setForceBold(v.booleanValue());
        }
        
		return dict;
	}
	
	private int[] toIntArray(double[] dArray) {
		int[] iArray = new int[dArray.length];
		for (int i=0; i<iArray.length; ++i) {
			iArray[i] = (int) dArray[i];
		}
		return iArray;
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
		Type2CharString t2cs = cffWrapper.getType2CharString(glyphID);
		return convertCharString(t2cs, cffWrapper.getCFFFID(glyphID));
	}

	private PSString convertCharString(Type2CharString t2cs, int fid) {
		Type1CharString t1cs = cffWrapper.toType1CharString(t2cs, fid);
		byte[] t1csBytes = t1cs.getEncryptedBytes(new byte[] {0, 0, 0, 0});
		byte[] temp = new byte[t1csBytes.length + 1];
		temp[0] = (byte) fid;
		System.arraycopy(t1csBytes, 0, temp, 1, temp.length - 1);
		return new PSString(temp, 0, temp.length);
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
