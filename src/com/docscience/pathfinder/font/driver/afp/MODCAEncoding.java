package com.docscience.pathfinder.font.driver.afp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wxin
 *
 */
public class MODCAEncoding {

	public static final MODCAEncoding EBCDIC_CS103_CP500;
	
	static {
		EBCDIC_CS103_CP500 = new MODCAEncoding();
		EBCDIC_CS103_CP500.characterSetGID = 103;
		EBCDIC_CS103_CP500.codepageGID = 500;
		EBCDIC_CS103_CP500.defaultCharacterCodePoint = 0x40;
		EBCDIC_CS103_CP500.defaultCharacterUnicode = ' ';
		byte[] mapping = new byte[256];
		for (int i=0; i<mapping.length; ++i) {
			mapping[i] = 0;
		}
		mapping[0x40] = ' ';
		mapping[0x4A] = '[';
		mapping[0x4B] = '.';
		mapping[0x4C] = '<';
		mapping[0x4D] = '(';
		mapping[0x4E] = '+';
		mapping[0x4F] = '!';
		mapping[0x50] = '&';
		mapping[0x5A] = ']';
		mapping[0x5B] = '$';
		mapping[0x5C] = '*';
		mapping[0x5D] = ')';
		mapping[0x5E] = ';';
		mapping[0x5F] = '^';
		mapping[0x60] = '-';
		mapping[0x61] = '/';
		mapping[0x6B] = ',';
		mapping[0x6C] = '%';
		mapping[0x6D] = '_';
		mapping[0x6E] = '>';
		mapping[0x6F] = '?';
		mapping[0x79] = '`';
		mapping[0x7A] = ':';
		mapping[0x7B] = '#';
		mapping[0x7C] = '@';
		mapping[0x7D] = '\'';
		mapping[0x7E] = '=';
		mapping[0x7F] = '\"';
		mapping[0x81] = 'a';
		mapping[0x82] = 'b';
		mapping[0x83] = 'c';
		mapping[0x84] = 'd';
		mapping[0x85] = 'e';
		mapping[0x86] = 'f';
		mapping[0x87] = 'g';
		mapping[0x88] = 'h';
		mapping[0x89] = 'i';
		mapping[0x91] = 'j';
		mapping[0x92] = 'k';
		mapping[0x93] = 'l';
		mapping[0x94] = 'm';
		mapping[0x95] = 'n';
		mapping[0x96] = 'o';
		mapping[0x97] = 'p';
		mapping[0x98] = 'q';
		mapping[0x99] = 'r';
		mapping[0xA1] = '~';
		mapping[0xA2] = 's';
		mapping[0xA3] = 't';
		mapping[0xA4] = 'u';
		mapping[0xA5] = 'v';
		mapping[0xA6] = 'w';
		mapping[0xA7] = 'x';
		mapping[0xA8] = 'y';
		mapping[0xA9] = 'z';
		mapping[0xBB] = '|';
		mapping[0xC0] = '{';
		mapping[0xC1] = 'A';
		mapping[0xC2] = 'B';
		mapping[0xC3] = 'C';
		mapping[0xC4] = 'D';
		mapping[0xC5] = 'E';
		mapping[0xC6] = 'F';
		mapping[0xC7] = 'G';
		mapping[0xC8] = 'H';
		mapping[0xC9] = 'I';
		mapping[0xD0] = '}';
		mapping[0xD1] = 'J';
		mapping[0xD2] = 'K';
		mapping[0xD3] = 'L';
		mapping[0xD4] = 'M';
		mapping[0xD5] = 'N';
		mapping[0xD6] = 'O';
		mapping[0xD7] = 'P';
		mapping[0xD8] = 'Q';
		mapping[0xD9] = 'R';
		mapping[0xE0] = '\\';
		mapping[0xE2] = 'S';
		mapping[0xE3] = 'T';
		mapping[0xE4] = 'U';
		mapping[0xE5] = 'V';
		mapping[0xE6] = 'W';
		mapping[0xE7] = 'X';
		mapping[0xE8] = 'Y';
		mapping[0xE9] = 'Z';
		mapping[0xF0] = '0';
		mapping[0xF1] = '1';
		mapping[0xF2] = '2';
		mapping[0xF3] = '3';
		mapping[0xF4] = '4';
		mapping[0xF5] = '5';
		mapping[0xF6] = '6';
		mapping[0xF7] = '7';
		mapping[0xF8] = '8';
		mapping[0xF9] = '9';
		for (int i=0; i<mapping.length; ++i) {
			EBCDIC_CS103_CP500.encodeMap.put(new Integer(mapping[i]), new Integer(i));
			EBCDIC_CS103_CP500.decodeMap.put(new Integer(i), new Integer(mapping[i]));
		}
	}
	
	private int characterSetGID;
	private int codepageGID;
	private Map<Integer, Integer> decodeMap = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> encodeMap = new HashMap<Integer, Integer>();
	private int defaultCharacterCodePoint;
	private int defaultCharacterUnicode;
	
	public byte encode(char c) {
		Integer e = encodeMap.get(new Integer(c));
		if (e == null) {
			return (byte) defaultCharacterCodePoint;
		}
		else {
			return (byte) e.intValue();
		}
		
	}
	
	public char decode(int e) {
		Integer c = decodeMap.get(new Integer(e));
		if (c == null) {
			return (char) defaultCharacterUnicode;
		}
		else {
			return (char) c.intValue();
		}
	}
	
	public byte[] encode(String string, int limitLength) {
		if (limitLength > string.length()) {
			limitLength = string.length();
		}
		return encode(string.substring(0, limitLength));
	}
	
	public byte[] encode(String string) {
		byte[] data = new byte[string.length()];
		return encode(string, data, 0);
	}
	
	public byte[] encode(String string, byte[] data, int offset) {
		for (int i=0; i<string.length(); ++i) {
			char c = string.charAt(i);
			data[i + offset] = encode(c);
		}
		return data;
	}
	
	public String decode(byte[] data, int offset, int length) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<length; ++i) {
			int e = data[i + offset] & 0xff;
			sb.append(decode(e));
		}
		return sb.toString();
	}
	
	public int getDefaultCharacterUnicode() {
		return defaultCharacterUnicode;
	}
	
	public int getDefaultCharacterCodePoint() {
		return defaultCharacterCodePoint;
	}
	
	public int getGraphicsCharacterSetGID() {
		return characterSetGID;
	}
	
	public int getCodePageGID() {
		return codepageGID;
	}
}
