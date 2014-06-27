package com.docscience.pathfinder.font.driver.ttf;

/**
 * @author wxin
 *
 */
public abstract class TTFHelper {
      
    /**
     * Construct a tag integer from a ascii string.
     * 
     * @param string must be present by ascii value 32-126.
     * @return composed integer.
     */
    public static int composeTag(String string) {
        int tag = 0;
        char[] chars = string.toCharArray();
        assert(chars.length == 4);
        assert(chars[0] >= 32 && chars[0] <= 126);
        assert(chars[1] >= 32 && chars[1] <= 126);
        assert(chars[2] >= 32 && chars[2] <= 126);
        assert(chars[3] >= 32 && chars[3] <= 126);
        tag = (chars[0] << 24) 
            + (chars[1] << 16) 
            + (chars[2] << 8) 
            +  chars[3];
        return tag;
    }

    /**
     * Parse a tag integer generate a ascii string represent.
     * 
     * @param tag integer value.
     * @return ascii string.
     */
    public static String decomposeTag(int tag) {
        char[] chars = new char[4];
        chars[0] = (char) ((tag >> 24) & 0x0ff);
        chars[1] = (char) ((tag >> 16) & 0x0ff);
        chars[2] = (char) ((tag >> 8)  & 0x0ff);
        chars[3] = (char) (tag & 0x0ff);
        return new String(chars);
    }
    
}
