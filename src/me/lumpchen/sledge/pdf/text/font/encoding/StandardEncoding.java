package me.lumpchen.sledge.pdf.text.font.encoding;

import java.util.HashMap;
import java.util.Map;

public class StandardEncoding implements Encoding {
	
	private static Map<Integer, String> code2name = new HashMap<Integer, String>();
	private static Map<String, Integer> name2code = new HashMap<String, Integer>();
	static {
		addCharacterEncoding( 65, "A" );
		addCharacterEncoding( 225, "AE" );
		addCharacterEncoding( 66, "B" );
		addCharacterEncoding( 67, "C" );
		addCharacterEncoding( 68, "D" );
		addCharacterEncoding( 69, "E" );
		addCharacterEncoding( 70, "F" );
		addCharacterEncoding( 71, "G" );
		addCharacterEncoding( 72, "H" );
		addCharacterEncoding( 73, "I" );
		addCharacterEncoding( 74, "J" );
		addCharacterEncoding( 75, "K" );
		addCharacterEncoding( 76, "L" );
		addCharacterEncoding( 232, "Lslash" );
		addCharacterEncoding( 77, "M" );
		addCharacterEncoding( 78, "N" );
		addCharacterEncoding( 79, "O" );
		addCharacterEncoding( 234, "OE" );
		addCharacterEncoding( 233, "Oslash" );
		addCharacterEncoding( 80, "P" );
		addCharacterEncoding( 81, "Q" );
		addCharacterEncoding( 82, "R" );
		addCharacterEncoding( 83, "S" );
		addCharacterEncoding( 84, "T" );
		addCharacterEncoding( 85, "U" );
		addCharacterEncoding( 86, "V" );
		addCharacterEncoding( 87, "W" );
		addCharacterEncoding( 88, "X" );
		addCharacterEncoding( 89, "Y" );
		addCharacterEncoding( 90, "Z" );
		addCharacterEncoding( 97, "a" );
		addCharacterEncoding( 194, "acute" );
		addCharacterEncoding( 241, "ae" );
		addCharacterEncoding( 38, "ampersand" );
		addCharacterEncoding( 94, "asciicircum" );
		addCharacterEncoding( 126, "asciitilde" );
		addCharacterEncoding( 42, "asterisk" );
		addCharacterEncoding( 64, "at" );
		addCharacterEncoding( 98, "b" );
		addCharacterEncoding( 92, "backslash" );
		addCharacterEncoding( 124, "bar" );
		addCharacterEncoding( 123, "braceleft" );
		addCharacterEncoding( 125, "braceright" );
		addCharacterEncoding( 91, "bracketleft" );
		addCharacterEncoding( 93, "bracketright" );
		addCharacterEncoding( 198, "breve" );
		addCharacterEncoding( 183, "bullet" );
		addCharacterEncoding( 99, "c" );
		addCharacterEncoding( 207, "caron" );
		addCharacterEncoding( 203, "cedilla" );
		addCharacterEncoding( 162, "cent" );
		addCharacterEncoding( 195, "circumflex" );
		addCharacterEncoding( 58, "colon" );
		addCharacterEncoding( 44, "comma" );
		addCharacterEncoding( 168, "currency" );
		addCharacterEncoding( 100, "d" );
		addCharacterEncoding( 178, "dagger" );
		addCharacterEncoding( 179, "daggerdbl" );
		addCharacterEncoding( 200, "dieresis" );
		addCharacterEncoding( 36, "dollar" );
		addCharacterEncoding( 199, "dotaccent" );
		addCharacterEncoding( 245, "dotlessi" );
		addCharacterEncoding( 101, "e" );
		addCharacterEncoding( 56, "eight" );
		addCharacterEncoding( 188, "ellipsis" );
		addCharacterEncoding( 208, "emdash" );
		addCharacterEncoding( 177, "endash" );
		addCharacterEncoding( 61, "equal" );
		addCharacterEncoding( 33, "exclam" );
		addCharacterEncoding( 161, "exclamdown" );
		addCharacterEncoding( 102, "f" );
		addCharacterEncoding( 174, "fi" );
		addCharacterEncoding( 53, "five" );
		addCharacterEncoding( 175, "fl" );
		addCharacterEncoding( 166, "florin" );
		addCharacterEncoding( 52, "four" );
		addCharacterEncoding( 164, "fraction" );
		addCharacterEncoding( 103, "g" );
		addCharacterEncoding( 251, "germandbls" );
		addCharacterEncoding( 193, "grave" );
		addCharacterEncoding( 62, "greater" );
		addCharacterEncoding( 171, "guillemotleft" );
		addCharacterEncoding( 187, "guillemotright" );
		addCharacterEncoding( 172, "guilsinglleft" );
		addCharacterEncoding( 173, "guilsinglright" );
		addCharacterEncoding( 104, "h" );
		addCharacterEncoding( 205, "hungarumlaut" );
		addCharacterEncoding( 45, "hyphen" );
		addCharacterEncoding( 105, "i" );
		addCharacterEncoding( 106, "j" );
		addCharacterEncoding( 107, "k" );
		addCharacterEncoding( 108, "l" );
		addCharacterEncoding( 60, "less" );
		addCharacterEncoding( 248, "lslash" );
		addCharacterEncoding( 109, "m" );
		addCharacterEncoding( 197, "macron" );
		addCharacterEncoding( 110, "n" );
		addCharacterEncoding( 57, "nine" );
		addCharacterEncoding( 35, "numbersign" );
		addCharacterEncoding( 111, "o" );
		addCharacterEncoding( 250, "oe" );
		addCharacterEncoding( 206, "ogonek" );
		addCharacterEncoding( 49, "one" );
		addCharacterEncoding( 227, "ordfeminine" );
		addCharacterEncoding( 235, "ordmasculine" );
		addCharacterEncoding( 249, "oslash" );
		addCharacterEncoding( 112, "p" );
		addCharacterEncoding( 182, "paragraph" );
		addCharacterEncoding( 40, "parenleft" );
		addCharacterEncoding( 41, "parenright" );
		addCharacterEncoding( 37, "percent" );
		addCharacterEncoding( 46, "period" );
		addCharacterEncoding( 180, "periodcentered" );
		addCharacterEncoding( 189, "perthousand" );
		addCharacterEncoding( 43, "plus" );
		addCharacterEncoding( 113, "q" );
		addCharacterEncoding( 63, "question" );
		addCharacterEncoding( 191, "questiondown" );
		addCharacterEncoding( 34, "quotedbl" );
		addCharacterEncoding( 185, "quotedblbase" );
		addCharacterEncoding( 170, "quotedblleft" );
		addCharacterEncoding( 186, "quotedblright" );
		addCharacterEncoding( 96, "quoteleft" );
		addCharacterEncoding( 39, "quoteright" );
		addCharacterEncoding( 184, "quotesinglbase" );
		addCharacterEncoding( 169, "quotesingle" );
		addCharacterEncoding( 114, "r" );
		addCharacterEncoding( 202, "ring" );
		addCharacterEncoding( 115, "s" );
		addCharacterEncoding( 167, "section" );
		addCharacterEncoding( 59, "semicolon" );
		addCharacterEncoding( 55, "seven" );
		addCharacterEncoding( 54, "six" );
		addCharacterEncoding( 47, "slash" );
		addCharacterEncoding( 32, "space" );
		addCharacterEncoding( 163, "sterling" );
		addCharacterEncoding( 116, "t" );
		addCharacterEncoding( 51, "three" );
		addCharacterEncoding( 196, "tilde" );
		addCharacterEncoding( 50, "two" );
		addCharacterEncoding( 117, "u" );
		addCharacterEncoding( 95, "underscore" );
		addCharacterEncoding( 118, "v" );
		addCharacterEncoding( 119, "w" );
		addCharacterEncoding( 120, "x" );
		addCharacterEncoding( 121, "y" );
		addCharacterEncoding( 165, "yen" );
		addCharacterEncoding( 122, "z" );
		addCharacterEncoding( 48, "zero" );
	}
	
	public StandardEncoding() {
	}
	
	static void addCharacterEncoding(int code, String name) {
		code2name.put(code, name);
		name2code.put(name, code);
	}

	@Override
	public String getCharacterName(int code) {
		return code2name.get(code);
	}

	@Override
	public int getCharacterCode(String name) {
		return name2code.get(name);
	}
	
}
