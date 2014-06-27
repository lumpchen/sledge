package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * @author wxin
 *
 */
public final class TTFFile {
    
    public static final int FILE_TAG_TTC = 0x74746366; // 'ttcf' in ttc file
    
    private TTCHeader ttcHeader;
    private TTFFontDirectory[] ttfFonts;
   
    public TTCHeader getTTCHeader() {
        return ttcHeader;
    }
       
    public int getNumFonts() {
        return ttfFonts.length;
    }
    
    public TTFFontDirectory getFontDirection(int i) {
        return ttfFonts[i];
    }
       
    public void read(TTFRandomReader rd) throws TTFFormatException, IOException {
        rd.setPosition(0);
        if (rd.readTTFTag() == FILE_TAG_TTC) {
            ttcHeader = new TTCHeader();
            rd.setPosition(0);
            ttcHeader.read(rd);
            
            int nFonts = ttcHeader.getNumFonts();
            assert(nFonts > 0);
            ttfFonts = new TTFFontDirectory[nFonts];
            for (int i=0; i<nFonts; ++i) {
                ttfFonts[i] = new TTFFontDirectory();
                rd.setPosition(ttcHeader.getFontOffset(i));
                ttfFonts[i].read(rd);
            }
        }
        else {
            ttcHeader = null;
            rd.setPosition(0);
            ttfFonts = new TTFFontDirectory[1];
            ttfFonts[0] = new TTFFontDirectory();
            ttfFonts[0].read(rd);
        }
    }
    
}
