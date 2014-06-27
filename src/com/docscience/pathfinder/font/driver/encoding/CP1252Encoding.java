package com.docscience.pathfinder.font.driver.encoding;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wxin
 *
 */
public class CP1252Encoding implements Encoding {
	
	static final char[] codePoint2Unicode = new char[] {
			0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 
			0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 
			0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 
			0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 
			0x0020, 0x0021, 0x0022, 0x0023, 0x0024,	0x0025, 0x0026, 0x0027,
			0x0028, 0x0029, 0x002A, 0x002B, 0x002C,	0x002D, 0x002E, 0x002F,
			0x0030, 0x0031, 0x0032, 0x0033, 0x0034,	0x0035, 0x0036, 0x0037,
			0x0038, 0x0039, 0x003A, 0x003B, 0x003C,	0x003D, 0x003E, 0x003F,
			0x0040, 0x0041, 0x0042, 0x0043, 0x0044,	0x0045, 0x0046, 0x0047,
			0x0048, 0x0049, 0x004A, 0x004B, 0x004C,	0x004D, 0x004E, 0x004F,
			0x0050, 0x0051, 0x0052, 0x0053, 0x0054,	0x0055, 0x0056, 0x0057,
			0x0058, 0x0059, 0x005A, 0x005B, 0x005C,	0x005D, 0x005E, 0x005F,
			0x0060, 0x0061, 0x0062, 0x0063, 0x0064,	0x0065, 0x0066, 0x0067,
			0x0068, 0x0069, 0x006A, 0x006B, 0x006C,	0x006D, 0x006E, 0x006F,
			0x0070, 0x0071, 0x0072, 0x0073, 0x0074,	0x0075, 0x0076, 0x0077,
			0x0078, 0x0079, 0x007A, 0x007B, 0x007C,	0x007D, 0x007E, 0x007F,
			0x20AC, 0xFFFF, 0x201A, 0x0192, 0x201E,	0x2026, 0x2020, 0x2021,
			0x02C6, 0x2030, 0x0160, 0x2039, 0x0152,	0xFFFF, 0x017D, 0xFFFF,
			0xFFFF, 0x2018, 0x2019, 0x201C, 0x201D,	0x2022, 0x2013, 0x2014,
			0x02DC, 0x2122, 0x0161, 0x203A, 0x0153,	0xFFFF, 0x017E, 0x0178,
			0x00A0, 0x00A1, 0x00A2, 0x00A3, 0x00A4,	0x00A5, 0x00A6, 0x00A7,
			0x00A8, 0x00A9, 0x00AA, 0x00AB, 0x00AC,	0x00AD, 0x00AE, 0x00AF,
			0x00B0, 0x00B1, 0x00B2, 0x00B3, 0x00B4,	0x00B5, 0x00B6, 0x00B7,
			0x00B8, 0x00B9, 0x00BA, 0x00BB, 0x00BC,	0x00BD, 0x00BE, 0x00BF,
			0x00C0, 0x00C1, 0x00C2, 0x00C3, 0x00C4,	0x00C5, 0x00C6, 0x00C7,
			0x00C8, 0x00C9, 0x00CA, 0x00CB, 0x00CC,	0x00CD, 0x00CE, 0x00CF,
			0x00D0, 0x00D1, 0x00D2, 0x00D3, 0x00D4,	0x00D5, 0x00D6, 0x00D7,
			0x00D8, 0x00D9, 0x00DA, 0x00DB, 0x00DC,	0x00DD, 0x00DE, 0x00DF,
			0x00E0, 0x00E1, 0x00E2, 0x00E3, 0x00E4,	0x00E5, 0x00E6, 0x00E7,
			0x00E8, 0x00E9, 0x00EA, 0x00EB, 0x00EC,	0x00ED, 0x00EE, 0x00EF,
			0x00F0, 0x00F1, 0x00F2, 0x00F3, 0x00F4,	0x00F5, 0x00F6, 0x00F7,
			0x00F8, 0x00F9, 0x00FA, 0x00FB, 0x00FC,	0x00FD, 0x00FE, 0x00FF, };
	
	static final String[] codePoint2Name = new String[256];
	
	static final Map<Integer, Integer> unicode2CodePoint;

	private static final int NOT_DEFINED_CODEPOINT = 0xFFFF;
	
	static {
		assert(codePoint2Unicode.length == 256);
		assert(codePoint2Name.length == 256);
		
		codePoint2Name[32] = "space";
		codePoint2Name[33] = "exclam";
		codePoint2Name[34] = "quotedbl";
		codePoint2Name[35] = "numbersign";
		codePoint2Name[36] = "dollar";
		codePoint2Name[37] = "percent";
		codePoint2Name[38] = "ampersand";
		codePoint2Name[39] = "quotesingle";
		codePoint2Name[40] = "parenleft";
		codePoint2Name[41] = "parenright";
		codePoint2Name[42] = "asterisk";
		codePoint2Name[43] = "plus";
		codePoint2Name[44] = "comma";
		codePoint2Name[45] = "hyphen";
		codePoint2Name[46] = "period";
		codePoint2Name[47] = "slash";
		codePoint2Name[48] = "zero";
		codePoint2Name[49] = "one";
		codePoint2Name[50] = "two";
		codePoint2Name[51] = "three";
		codePoint2Name[52] = "four";
		codePoint2Name[53] = "five";
		codePoint2Name[54] = "six";
		codePoint2Name[55] = "seven";
		codePoint2Name[56] = "eight";
		codePoint2Name[57] = "nine";
		codePoint2Name[58] = "colon";
		codePoint2Name[59] = "semicolon";
		codePoint2Name[60] = "less";
		codePoint2Name[61] = "equal";
		codePoint2Name[62] = "greater";
		codePoint2Name[63] = "question";
		codePoint2Name[64] = "at";
		codePoint2Name[65] = "A";
		codePoint2Name[66] = "B";
		codePoint2Name[67] = "C";
		codePoint2Name[68] = "D";
		codePoint2Name[69] = "E";
		codePoint2Name[70] = "F";
		codePoint2Name[71] = "G";
		codePoint2Name[72] = "H";
		codePoint2Name[73] = "I";
		codePoint2Name[74] = "J";
		codePoint2Name[75] = "K";
		codePoint2Name[76] = "L";
		codePoint2Name[77] = "M";
		codePoint2Name[78] = "N";
		codePoint2Name[79] = "O";
		codePoint2Name[80] = "P";
		codePoint2Name[81] = "Q";
		codePoint2Name[82] = "R";
		codePoint2Name[83] = "S";
		codePoint2Name[84] = "T";
		codePoint2Name[85] = "U";
		codePoint2Name[86] = "V";
		codePoint2Name[87] = "W";
		codePoint2Name[88] = "X";
		codePoint2Name[89] = "Y";
		codePoint2Name[90] = "Z";
		codePoint2Name[91] = "bracketleft";
		codePoint2Name[92] = "backslash";
		codePoint2Name[93] = "bracketright";
		codePoint2Name[94] = "asciicircum";
		codePoint2Name[95] = "underscore";
		codePoint2Name[96] = "grave";
		codePoint2Name[97] = "a";
		codePoint2Name[98] = "b";
		codePoint2Name[99] = "c";
		codePoint2Name[100] = "d";
		codePoint2Name[101] = "e";
		codePoint2Name[102] = "f";
		codePoint2Name[103] = "g";
		codePoint2Name[104] = "h";
		codePoint2Name[105] = "i";
		codePoint2Name[106] = "j";
		codePoint2Name[107] = "k";
		codePoint2Name[108] = "l";
		codePoint2Name[109] = "m";
		codePoint2Name[110] = "n";
		codePoint2Name[111] = "o";
		codePoint2Name[112] = "p";
		codePoint2Name[113] = "q";
		codePoint2Name[114] = "r";
		codePoint2Name[115] = "s";
		codePoint2Name[116] = "t";
		codePoint2Name[117] = "u";
		codePoint2Name[118] = "v";
		codePoint2Name[119] = "w";
		codePoint2Name[120] = "x";
		codePoint2Name[121] = "y";
		codePoint2Name[122] = "z";
		codePoint2Name[123] = "braceleft";
		codePoint2Name[124] = "bar";
		codePoint2Name[125] = "braceright";
		codePoint2Name[126] = "asciitilde";
		codePoint2Name[127] = "delete";
		codePoint2Name[128] = "Euro";
		codePoint2Name[130] = "quotesinglbase";
		codePoint2Name[131] = "florin";
		codePoint2Name[132] = "quotedblbase";
		codePoint2Name[133] = "ellipsis";
		codePoint2Name[134] = "dagger";
		codePoint2Name[135] = "daggerdbl";
		codePoint2Name[136] = "circumflex";
		codePoint2Name[137] = "perthousand";
		codePoint2Name[138] = "Scaron";
		codePoint2Name[139] = "guilsinglleft";
		codePoint2Name[140] = "OE";
		codePoint2Name[142] = "Zcaron";
		codePoint2Name[145] = "quoteleft";
		codePoint2Name[146] = "quoteright";
		codePoint2Name[147] = "quotedblleft";
		codePoint2Name[148] = "quotedblright";
		codePoint2Name[149] = "bullet";
		codePoint2Name[150] = "endash";
		codePoint2Name[151] = "emdash";
		codePoint2Name[152] = "tilde";
		codePoint2Name[153] = "trademark";
		codePoint2Name[154] = "scaron";
		codePoint2Name[155] = "guilsinglright";
		codePoint2Name[156] = "oe";
		codePoint2Name[158] = "zcaron";
		codePoint2Name[159] = "Ydieresis";
		codePoint2Name[160] = "space";
		codePoint2Name[161] = "exclamdown";
		codePoint2Name[162] = "cent";
		codePoint2Name[163] = "sterling";
		codePoint2Name[164] = "currency";
		codePoint2Name[165] = "yen";
		codePoint2Name[166] = "brokenbar";
		codePoint2Name[167] = "section";
		codePoint2Name[168] = "dieresis";
		codePoint2Name[169] = "copyright";
		codePoint2Name[170] = "ordfeminine";
		codePoint2Name[171] = "guillemotleft";
		codePoint2Name[172] = "logicalnot";
		codePoint2Name[173] = "hyphen";
		codePoint2Name[174] = "registered";
		codePoint2Name[175] = "overscore";
		codePoint2Name[176] = "degree";
		codePoint2Name[177] = "plusminus";
		codePoint2Name[178] = "twosuperior";
		codePoint2Name[179] = "threesuperior";
		codePoint2Name[180] = "acute";
		codePoint2Name[181] = "mu1";
		codePoint2Name[182] = "paragraph";
		codePoint2Name[183] = "middot";
		codePoint2Name[184] = "cedilla";
		codePoint2Name[185] = "onesuperior";
		codePoint2Name[186] = "ordmasculine";
		codePoint2Name[187] = "guillemotright";
		codePoint2Name[188] = "onequarter";
		codePoint2Name[189] = "onehalf";
		codePoint2Name[190] = "threequarters";
		codePoint2Name[191] = "questiondown";
		codePoint2Name[192] = "Agrave";
		codePoint2Name[193] = "Aacute";
		codePoint2Name[194] = "Acircumflex";
		codePoint2Name[195] = "Atilde";
		codePoint2Name[196] = "Adieresis";
		codePoint2Name[197] = "Aring";
		codePoint2Name[198] = "AE";
		codePoint2Name[199] = "Ccedilla";
		codePoint2Name[200] = "Egrave";
		codePoint2Name[201] = "Eacute";
		codePoint2Name[202] = "Ecircumflex";
		codePoint2Name[203] = "Edieresis";
		codePoint2Name[204] = "Igrave";
		codePoint2Name[205] = "Iacute";
		codePoint2Name[206] = "Icircumflex";
		codePoint2Name[207] = "Idieresis";
		codePoint2Name[208] = "Eth";
		codePoint2Name[209] = "Ntilde";
		codePoint2Name[210] = "Ograve";
		codePoint2Name[211] = "Oacute";
		codePoint2Name[212] = "Ocircumflex";
		codePoint2Name[213] = "Otilde";
		codePoint2Name[214] = "Odieresis";
		codePoint2Name[215] = "multiply";
		codePoint2Name[216] = "Oslash";
		codePoint2Name[217] = "Ugrave";
		codePoint2Name[218] = "Uacute";
		codePoint2Name[219] = "Ucircumflex";
		codePoint2Name[220] = "Udieresis";
		codePoint2Name[221] = "Yacute";
		codePoint2Name[222] = "Thorn";
		codePoint2Name[223] = "germandbls";
		codePoint2Name[224] = "agrave";
		codePoint2Name[225] = "aacute";
		codePoint2Name[226] = "acircumflex";
		codePoint2Name[227] = "atilde";
		codePoint2Name[228] = "adieresis";
		codePoint2Name[229] = "aring";
		codePoint2Name[230] = "ae";
		codePoint2Name[231] = "ccedilla";
		codePoint2Name[232] = "egrave";
		codePoint2Name[233] = "eacute";
		codePoint2Name[234] = "ecircumflex";
		codePoint2Name[235] = "edieresis";
		codePoint2Name[236] = "igrave";
		codePoint2Name[237] = "iacute";
		codePoint2Name[238] = "icircumflex";
		codePoint2Name[239] = "idieresis";
		codePoint2Name[240] = "eth";
		codePoint2Name[241] = "ntilde";
		codePoint2Name[242] = "ograve";
		codePoint2Name[243] = "oacute";
		codePoint2Name[244] = "ocircumflex";
		codePoint2Name[245] = "otilde";
		codePoint2Name[246] = "odieresis";
		codePoint2Name[247] = "divide";
		codePoint2Name[248] = "oslash";
		codePoint2Name[249] = "ugrave";
		codePoint2Name[250] = "uacute";
		codePoint2Name[251] = "ucircumflex";
		codePoint2Name[252] = "udieresis";
		codePoint2Name[253] = "yacute";
		codePoint2Name[254] = "thorn";
		codePoint2Name[255] = "ydieresis";
		
		unicode2CodePoint = new HashMap<Integer, Integer>();
		for (int i=1; i<codePoint2Unicode.length; ++i) {
			int codePoint = i;
			if ((codePoint2Unicode[codePoint] & 0xFFFF) != NOT_DEFINED_CODEPOINT) {
				int unicode = codePoint2Unicode[i];
				unicode2CodePoint.put(new Integer(unicode), new Integer(codePoint));
			}
		}		
	}
	
	@Override
	public int getCodePoint(int unicode) {
		Integer i = unicode2CodePoint.get(new Integer(unicode));
		if (i != null) {
			return i.intValue();
		}
		else {
			return NOT_DEFINED_CODEPOINT;
		}
	}
	
	@Override
	public int getMaxCodePoint() {
		return 255;
	}

	@Override
	public int getMinCodePoint() {
		return 0;
	}
	
	@Override
	public boolean isDefinedCodePoint(int codePoint) {
		if ((codePoint2Unicode[codePoint] & 0xFFFF) == NOT_DEFINED_CODEPOINT) {
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public String getCharacterName(int codePoint) {
		if (isDefinedCodePoint(codePoint)) {
			assert(codePoint2Name[codePoint] != null) : "codePoint " + codePoint + " lose name defined";
			return codePoint2Name[codePoint];
		}
		return null;
	}

	@Override
	public String getGlyphName(int codepoint) {
		return getCharacterName(codepoint);
	}

	@Override
	public int getUnicode(int codePoint) {
		return codePoint2Unicode[codePoint];
	}

}
