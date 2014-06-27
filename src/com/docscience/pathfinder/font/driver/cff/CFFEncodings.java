package com.docscience.pathfinder.font.driver.cff;

import java.io.IOException;

/**
 * Enclose two type of mapping, one (Codes) for map GID to code, another (Supps)
 * for map code to glyph name ID (SID).
 * 
 * @author wxin
 * 
 */
public final class CFFEncodings {

    private static final int[] STANDARD_ENCODING_ARRAY = { 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
            19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52,
            53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69,
            70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86,
            87, 88, 89, 90, 91, 92, 93, 94, 95, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108,
            109, 110, 0, 111, 112, 113, 114, 0, 115, 116, 117, 118, 119, 120,
            121, 122, 0, 123, 0, 124, 125, 126, 127, 128, 129, 130, 131, 0,
            132, 133, 0, 134, 135, 136, 137, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 138, 0, 139, 0, 0, 0, 0, 140, 141, 142, 143, 0, 0,
            0, 0, 0, 144, 0, 0, 0, 145, 0, 0, 146, 147, 148, 149, 0, 0, 0, 0 };

    private static final int[] EXPERT_ENCODING_ARRAY = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 229, 230, 0, 231, 232, 233, 234, 235, 236, 237, 238, 13, 14, 15,
            99, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 27, 28, 249,
            250, 251, 252, 0, 253, 254, 255, 256, 257, 0, 0, 0, 258, 0, 0, 259,
            260, 261, 262, 0, 0, 263, 264, 265, 0, 266, 109, 110, 267, 268,
            269, 0, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281,
            282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294,
            295, 296, 297, 298, 299, 300, 301, 302, 303, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 304, 305, 306, 0, 0, 307, 308, 309, 310, 311, 0,
            312, 0, 0, 312, 0, 0, 314, 315, 0, 0, 316, 317, 318, 0, 0, 0, 158,
            155, 163, 319, 320, 321, 322, 323, 324, 325, 0, 0, 326, 150, 164,
            169, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338,
            339, 340, 341, 342, 343, 344, 345, 346, 347, 348, 349, 350, 351,
            352, 353, 354, 355, 356, 357, 358, 359, 360, 361, 362, 363, 364,
            365, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377,
            378 };

    public static final CFFEncodings StandardEncoding = new CFFEncodings(
            STANDARD_ENCODING_ARRAY, CFFCharsets.ISOAdobeCharsets);

    public static final CFFEncodings ExpertEncoding = new CFFEncodings(
            EXPERT_ENCODING_ARRAY, CFFCharsets.ExpertCharsets);

    private int[] codes;
    private boolean predefined = false;
    private CFFCharsets charsets;

    private CFFEncodings(int[] codes, CFFCharsets charsets) {
        assert(codes.length == 256);
        this.codes = codes;
        this.charsets = charsets;
        this.predefined = true;
    }

    public CFFEncodings(CFFCharsets charsets) {
        codes = new int[256];
        for (int i = 0; i < codes.length; ++i) {
            codes[i] = 0;
        }
        this.charsets = charsets;
    }
    
    public int getMaxCode() {
        return codes.length;
    }
    
    public int getGlyphNameSID(int code) {
        return codes[code];
    }
    
    /**
     * @param pos
     * @param rd
     * @throws IOException
     * @throws CFFFormatException
     */
    public void read(long pos, CFFRandomReader rd) throws IOException,
            CFFFormatException {
        if (predefined) {
            throw new IllegalStateException("predefined encoding could not change");
        }

        rd.setPosition(pos);
        int format = rd.readCFFCard8();
        if ((format & 0x7f) == 0) {
            int nCodes = rd.readCFFCard8();
            for (int gid = 0; gid < nCodes; ++gid) {
                codes[rd.readCFFCard8()] = charsets.getGlyphNameID(gid);
            }
        } 
        else if ((format & 0x7f) == 1) {
            int nRanges = rd.readCFFCard8();
            int gid = 0;
            for (int i = 0; i < nRanges; ++i) {
                int first = rd.readCFFCard8();
                int nLeft = rd.readCFFCard8();
                for (int j = first; j < first + nLeft + 1; ++j) {
                    codes[j] = charsets.getGlyphNameID(gid++);
                }
            }
        } 
        else {
            throw new CFFFormatException("invalid format for encodings", rd
                    .getPosition() - 1);
        }

        if ((format & 0x80) != 0) {
            int nSupps = rd.readCFFCard8();
            for (int i = 0; i < nSupps; ++i) {
                int c = rd.readCFFCard8();
                int s = rd.readCFFSID();
                codes[c] = s;
            }
        }
    }

}
