package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author wxin
 *
 */
public abstract class TTFCMapEncoding implements Serializable {
    
	private static final long serialVersionUID = -2704531692520506934L;

	public static final int CMAP_FORMAT_0 = 0;
    public static final int CMAP_FORMAT_2 = 2;
    public static final int CMAP_FORMAT_4 = 4;
    public static final int CMAP_FORMAT_6 = 6;
    public static final int CMAP_FORMAT_8 = 8;
    public static final int CMAP_FORMAT_10 = 10;
    public static final int CMAP_FORMAT_12 = 12;
    
    /**
     * @return first char code
     */
    public abstract int getFirstChar();
    
    /**
     * @return last char code
     */
    public abstract int getLastChar();
    
    /**
     * @return format tag
     */
    public abstract int getFormat();
    
    /**
     * @param offset
     * @param rd
     * @throws TTFFormatException
     * @throws IOException
     */
    public abstract void read(long offset, TTFRandomReader rd) throws TTFFormatException, IOException;
    
    /**
     * This interface map unicode value to glyphID. We use integer code for 
     * UCS-4 compatible, even it's not used yet.
     * 
     * @param c character code be decoded.
     * @return glyph index in font.
     */
    public abstract int getGlyphID(int code);
    
    /**
     * TTFCMapEncoding Factory. 
     * 
     * @param offset
     * @param rd
     * @return
     * @throws TTFFormatException
     * @throws IOException
     */
    public static TTFCMapEncoding readCMapEncoding(long offset, TTFRandomReader rd) throws TTFFormatException, IOException {
        assert(offset > 0);
        TTFCMapEncoding encoding = null;
        rd.setPosition(offset);
        switch(rd.readTTFUShort()) {
        case CMAP_FORMAT_0:
            encoding = new TTFCMapEncodingFormat0();
            break;
        case CMAP_FORMAT_2:
            encoding = new TTFCMapEncodingFormat2();
            break;            
        case CMAP_FORMAT_4:
            encoding = new TTFCMapEncodingFormat4();
            break;
        case CMAP_FORMAT_6:
            encoding = new TTFCMapEncodingFormat6();
            break;
        case CMAP_FORMAT_8:
            encoding = new TTFCMapEncodingFormat8();
            break;
        case CMAP_FORMAT_10:
            encoding = new TTFCMapEncodingFormat10();
            break;
        case CMAP_FORMAT_12:
            encoding = new TTFCMapEncodingFormat12();
            break;
        default:
            throw new TTFFormatException("bad cmap format type", rd.getPosition() - 2);
        }
        encoding.read(offset, rd);
        return encoding;
    }
    
}
