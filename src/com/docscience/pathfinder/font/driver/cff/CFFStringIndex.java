package com.docscience.pathfinder.font.driver.cff;

import java.io.IOException;

/**
 * @author wxin
 * 
 */
public final class CFFStringIndex extends CFFIndexData {

    private static final String[] STD_STRINGS = { ".notdef", "space", "exclam",
            "quotedbl", "numbersign", "dollar", "percent", "ampersand",
            "quoteright", "parenleft", "parenright", "asterisk", "plus",
            "comma", "hyphen", "period", "slash", "zero", "one", "two",
            "three", "four", "five", "six", "seven", "eight", "nine", "colon",
            "semicolon", "less", "equal", "greater", "question", "at", "A",
            "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "bracketleft", "backslash", "bracketright", "asciicircum",
            "underscore", "quoteleft", "a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
            "v", "w", "x", "y", "z", "braceleft", "bar", "braceright",
            "asciitilde", "exclamdown", "cent", "sterling", "fraction", "yen",
            "florin", "section", "currency", "quotesingle", "quotedblleft",
            "guillemotleft", "guilsinglleft", "guilsinglright", "fi", "fl",
            "endash", "dagger", "daggerdbl", "periodcentered", "paragraph",
            "bullet", "quotesinglbase", "quotedblbase", "quotedblright",
            "guillemotright", "ellipsis", "perthousand", "questiondown",
            "grave", "acute", "circumflex", "tilde", "macron", "breve",
            "dotaccent", "dieresis", "ring", "cedilla", "hungarumlaut",
            "ogonek", "caron", "emdash", "AE", "ordfeminine", "Lslash",
            "Oslash", "OE", "ordmasculine", "ae", "dotlessi", "lslash",
            "oslash", "oe", "germandbls", "onesuperior", "logicalnot", "mu",
            "trademark", "Eth", "onehalf", "plusminus", "Thorn", "onequarter",
            "divide", "brokenbar", "degree", "thorn", "threequarters",
            "twosuperior", "registered", "minus", "eth", "multiply",
            "threesuperior", "copyright", "Aacute", "Acircumflex", "Adieresis",
            "Agrave", "Aring", "Atilde", "Ccedilla", "Eacute", "Ecircumflex",
            "Edieresis", "Egrave", "Iacute", "Icircumflex", "Idieresis",
            "Igrave", "Ntilde", "Oacute", "Ocircumflex", "Odieresis", "Ograve",
            "Otilde", "Scaron", "Uacute", "Ucircumflex", "Udieresis", "Ugrave",
            "Yacute", "Ydieresis", "Zcaron", "aacute", "acircumflex",
            "adieresis", "agrave", "aring", "atilde", "ccedilla", "eacute",
            "ecircumflex", "edieresis", "egrave", "iacute", "icircumflex",
            "idieresis", "igrave", "ntilde", "oacute", "ocircumflex",
            "odieresis", "ograve", "otilde", "scaron", "uacute", "ucircumflex",
            "udieresis", "ugrave", "yacute", "ydieresis", "zcaron",
            "exclamsmall", "Hungarumlautsmall", "dollaroldstyle",
            "dollarsuperior", "ampersandsmall", "Acutesmall",
            "parenleftsuperior", "parenrightsuperior", "twodotenleader",
            "onedotenleader", "zerooldstyle", "oneoldstyle", "twooldstyle",
            "threeoldstyle", "fouroldstyle", "fiveoldstyle", "sixoldstyle",
            "sevenoldstyle", "eightoldstyle", "nineoldstyle", "commasuperior",
            "threequartersemdash", "periodsuperior", "questionsmall",
            "asuperior", "bsuperior", "centsuperior", "dsuperior", "esuperior",
            "isuperior", "lsuperior", "msuperior", "nsuperior", "osuperior",
            "rsuperior", "ssuperior", "tsuperior", "ff", "ffi", "ffl",
            "parenleftinferior", "parenrightinferior", "Circumflexsmall",
            "hyphensuperior", "Gravesmall", "Asmall", "Bsmall", "Csmall",
            "Dsmall", "Esmall", "Fsmall", "Gsmall", "Hsmall", "Ismall",
            "Jsmall", "Ksmall", "Lsmall", "Msmall", "Nsmall", "Osmall",
            "Psmall", "Qsmall", "Rsmall", "Ssmall", "Tsmall", "Usmall",
            "Vsmall", "Wsmall", "Xsmall", "Ysmall", "Zsmall", "colonmonetary",
            "onefitted", "rupiah", "Tildesmall", "exclamdownsmall",
            "centoldstyle", "Lslashsmall", "Scaronsmall", "Zcaronsmall",
            "Dieresissmall", "Brevesmall", "Caronsmall", "Dotaccentsmall",
            "Macronsmall", "figuredash", "hypheninferior", "Ogoneksmall",
            "Ringsmall", "Cedillasmall", "questiondownsmall", "oneeighth",
            "threeeighths", "fiveeighths", "seveneighths", "onethird",
            "twothirds", "zerosuperior", "foursuperior", "fivesuperior",
            "sixsuperior", "sevensuperior", "eightsuperior", "ninesuperior",
            "zeroinferior", "oneinferior", "twoinferior", "threeinferior",
            "fourinferior", "fiveinferior", "sixinferior", "seveninferior",
            "eightinferior", "nineinferior", "centinferior", "dollarinferior",
            "periodinferior", "commainferior", "Agravesmall", "Aacutesmall",
            "Acircumflexsmall", "Atildesmall", "Adieresissmall", "Aringsmall",
            "AEsmall", "Ccedillasmall", "Egravesmall", "Eacutesmall",
            "Ecircumflexsmall", "Edieresissmall", "Igravesmall", "Iacutesmall",
            "Icircumflexsmall", "Idieresissmall", "Ethsmall", "Ntildesmall",
            "Ogravesmall", "Oacutesmall", "Ocircumflexsmall", "Otildesmall",
            "Odieresissmall", "OEsmall", "Oslashsmall", "Ugravesmall",
            "Uacutesmall", "Ucircumflexsmall", "Udieresissmall", "Yacutesmall",
            "Thornsmall", "Ydieresissmall", "001.000", "001.001", "001.002",
            "001.003", "Black", "Bold", "Book", "Light", "Medium", "Regular",
            "Roman", "Semibold" };

    public static final int STD_STRINGS_NUM = STD_STRINGS.length;
    
    private String[] strTable;

    public int getNumStrings() {
        return strTable.length;
    }

    public String getString(int index) {
        return strTable[index];
    }
    
    public String getStringBySID(int sid) {
        if (sid < STD_STRINGS.length) {
            return STD_STRINGS[sid];
        }
        else {
            return strTable[sid - STD_STRINGS.length];
        }
    }

    @Override
	public void read(long pos, CFFRandomReader rd) throws IOException,
            CFFFormatException {
        super.read(pos, rd);
        byte[] tmp = null;
        strTable = new String[getNumData()];
        for (int i = 0; i < getNumData(); ++i) {
            int len = (int) getDataLength(i);

            if (tmp == null || tmp.length < len) {
                tmp = new byte[len];
            }

            rd.setPosition(getDataPosition(i));
            rd.readBytes(tmp, 0, len);

            strTable[i] = new String(tmp, 0, len, "ASCII");
        }
    }

}
