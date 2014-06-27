package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;
import java.util.ArrayList;

/**
 * post - PostScript
 *
 * <p>
 * This table contains additional information needed to use TrueType or
 * OpenTypeTM fonts on PostScript printers. This includes data for the FontInfo
 * dictionary entry and the PostScript names of all the glyphs. For more
 * information about PostScript names, see the Adobe document Unicode and Glyph
 * Names.
 * </p>
 *
 * <p>
 * Versions 1.0, 2.0, and 2.5 refer to TrueType fonts and OpenType fonts with
 * TrueType data. OpenType fonts with TrueType data may also use Version 3.0.
 * OpenType fonts with CFF data use Version 3.0 only.
 * </p>
 *
 * The table begins as follows:
 *
 * <pre>
 *  Type  Name               Description
 *  ----------------------------------------------------------------------------
 *  Fixed Version            0x00010000 for version 1.0
 *                           0x00020000 for version 2.0
 *                           0x00025000 for version 2.5 (deprecated)
 *                           0x00030000 for version 3.0
 *  Fixed italicAngle        Italic angle in counter-clockwise degrees from the
 *                           vertical. Zero for upright text, negative for text
 *                           that leans to the right (forward).
 *  FWord underlinePosition  This is the suggested distance of the top of the
 *                           underline from the baseline (negative values
 *                           indicate below baseline).
 *  FWord underlineThickness Suggested values for the underline thickness.
 *  ULONG isFixedPitch       Set to 0 if the font is proportionally spaced,
 *                           non-zero if the font is not proportionally spaced.
 *  ULONG minMemType42       Minimum memory usage when an OpenType font is
 *                           downloaded.
 *  ULONG maxMemType42       Maximum memory usage when an OpenType font is
 *                           downloaded.
 *  ULONG minMemType1        Minimum memory usage when an OpenType font is
 *                           downloaded as a Type 1 font.
 *  ULONG maxMemType1        Maximum memory usage when an OpenType font is
 *                           downloaded as a Type 1 font.
 *
 *  @author wxin
 *
 */
public final class TTFPostTable extends TTFTable {

    public static final int VERSION_1_0 = 0x00010000;

    public static final int VERSION_2_0 = 0x00020000;

    public static final int VERSION_2_5 = 0x00025000;

    public static final int VERSION_3_0 = 0x00030000;

    /**
     * @see {@link http://developer.apple.com/textfonts/TTRefMan/RM06/Chap6post.html}
     */
    public static final String[] BASIC_GLYPH_NAMES = { ".notdef", ".null",
            "nonmarkingreturn", "space", "exclam", "quotedbl", "numbersign",
            "dollar", "percent", "ampersand", "quotesingle", "parenleft",
            "parenright", "asterisk", "plus", "comma", "hyphen", "period",
            "slash", "zero", "one", "two", "three", "four", "five", "six",
            "seven", "eight", "nine", "colon", "semicolon", "less", "equal",
            "greater", "question", "at", "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z", "bracketleft", "backslash",
            "bracketright", "asciicircum", "underscore", "grave", "a", "b",
            "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
            "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "braceleft",
            "bar", "braceright", "asciitilde", "Adieresis", "Aring",
            "Ccedilla", "Eacute", "Ntilde", "Odieresis", "Udieresis", "aacute",
            "agrave", "acircumflex", "adieresis", "atilde", "aring",
            "ccedilla", "eacute", "egrave", "ecircumflex", "edieresis",
            "iacute", "igrave", "icircumflex", "idieresis", "ntilde", "oacute",
            "ograve", "ocircumflex", "odieresis", "otilde", "uacute", "ugrave",
            "ucircumflex", "udieresis", "dagger", "degree", "cent", "sterling",
            "section", "bullet", "paragraph", "germandbls", "registered",
            "copyright", "trademark", "acute", "dieresis", "notequal", "AE",
            "Oslash", "infinity", "plusminus", "lessequal", "greaterequal",
            "yen", "mu", "partialdiff", "summation", "product", "pi",
            "integral", "ordfeminine", "ordmasculine", "Omega", "ae", "oslash",
            "questiondown", "exclamdown", "logicalnot", "radical", "florin",
            "approxequal", "Delta", "guillemotleft", "guillemotright",
            "ellipsis", "nonbreakingspace", "Agrave", "Atilde", "Otilde", "OE",
            "oe", "endash", "emdash", "quotedblleft", "quotedblright",
            "quoteleft", "quoteright", "divide", "lozenge", "ydieresis",
            "Ydieresis", "fraction", "currency", "guilsinglleft",
            "guilsinglright", "fi", "fl", "daggerdbl", "periodcentered",
            "quotesinglbase", "quotedblbase", "perthousand", "Acircumflex",
            "Ecircumflex", "Aacute", "Edieresis", "Egrave", "Iacute",
            "Icircumflex", "Idieresis", "Igrave", "Oacute", "Ocircumflex",
            "apple", "Ograve", "Uacute", "Ucircumflex", "Ugrave", "dotlessi",
            "circumflex", "tilde", "macron", "breve", "dotaccent", "ring",
            "cedilla", "hungarumlaut", "ogonek", "caron", "Lslash", "lslash",
            "Scaron", "scaron", "Zcaron", "zcaron", "brokenbar", "Eth", "eth",
            "Yacute", "yacute", "Thorn", "thorn", "minus", "multiply",
            "onesuperior", "twosuperior", "threesuperior", "onehalf",
            "onequarter", "threequarters", "franc", "Gbreve", "gbreve",
            "Idotaccent", "Scedilla", "scedilla", "Cacute", "cacute", "Ccaron",
            "ccaron", "dcroat" };

    private int version;

    private int italicAngle;

    private short underlinePosition;

    private short underlineThickness;

    private boolean isFixedPitch;

    private long minMemType42;

    private long maxMemType42;

    private long minMemType1;

    private long maxMemType1;

    private int numGlyphs = 0;

    private int[] glyphNameIndex;
    private String[] psGlyphNames;
    
    @SuppressWarnings("unused")
	private int highestGlyphNameIndex;

    public final boolean getIsFixedPitch() {
        return isFixedPitch;
    }

    public final int getItalicAngle() {
        return italicAngle;
    }

    public final long getMaxMemType1() {
        return maxMemType1;
    }

    public final long getMaxMemType42() {
        return maxMemType42;
    }

    public final long getMinMemType1() {
        return minMemType1;
    }

    public final long getMinMemType42() {
        return minMemType42;
    }

    public final short getUnderlinePosition() {
        return underlinePosition;
    }

    public final short getUnderlineThickness() {
        return underlineThickness;
    }

    public final int getVersion() {
        return version;
    }

    @Override
	public int getTag() {
        return TAG_POST_TABLE;
    }

    public int getNumGlyphs() {
        return numGlyphs;
    }

    public String getGlyphName(int glyphID) {
        if (glyphID >= numGlyphs)
             return null;

        switch(version) {
        case VERSION_1_0:
            return BASIC_GLYPH_NAMES[glyphID];
        case VERSION_2_0:
            if(glyphNameIndex[glyphID]>=0&&glyphNameIndex[glyphID]<=257){
                return BASIC_GLYPH_NAMES[glyphNameIndex[glyphID]];
            }else if(glyphNameIndex[glyphID] > 257&&glyphNameIndex[glyphID]<32768){
                return psGlyphNames[glyphNameIndex[glyphID] - 258];
            }else{
                return BASIC_GLYPH_NAMES[0];
            }
        case VERSION_2_5:
            //TODO: ADD CODE HERE
            return null;
        case VERSION_3_0:
            return BASIC_GLYPH_NAMES[0];
        default:
            assert(false) : "never goes here";
            return null;
        }
    }

    @Override
	public void read(long offset, long length, TTFRandomReader rd)
            throws TTFFormatException, IOException {
        rd.setPosition(offset);
        version = rd.readTTFFixed();
        if (version != VERSION_1_0 && version != VERSION_2_0
                && version != VERSION_2_5 && version != VERSION_3_0) {
            throw new TTFFormatException("bad version of post table", rd.getPosition() - 4);
        }
        italicAngle = rd.readTTFFixed();
        underlinePosition = rd.readTTFFWord();
        underlineThickness = rd.readTTFFWord();
        isFixedPitch = (rd.readTTFULong() != 0);
        minMemType42 = rd.readTTFULong();
        maxMemType42 = rd.readTTFULong();
        minMemType1 = rd.readTTFULong();
        maxMemType1 = rd.readTTFULong();
        switch (version) {
            case VERSION_1_0:
                numGlyphs = 258;
                assert (numGlyphs == BASIC_GLYPH_NAMES.length);
                break;
            case VERSION_2_0:
                numGlyphs = rd.readTTFUShort();
                glyphNameIndex = new int[numGlyphs];
                for (int i = 0; i < numGlyphs; i++) {
                    int index = rd.readTTFUShort();
                    glyphNameIndex[i] = index;
                }
                ArrayList<String> nameList=new ArrayList<String>();
                while (rd.getPosition()<offset+length) {
                    int len = rd.readTTFByte();
                    byte[] buf = new byte[len];
                    rd.readBytes(buf);
                    nameList.add(new String(buf));
                }
                psGlyphNames = nameList.toArray(new String[nameList.size()]);
                break;
            case VERSION_2_5:
                // TODO: DO THIS LATER
                break;
            case VERSION_3_0:
                break;
            default:
                assert (false) : "never goes here";
        }
    }

}
