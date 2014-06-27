package com.docscience.pathfinder.font.driver.cff;

import java.io.IOException;

/**
 * The FDSelect associates an FD (Font DICT) with a glyph by specifying an FD
 * index for that glyph. The FD index is used to access one of the Font DICTs
 * stored in the Font DICT INDEX.
 * 
 * @author wxin
 * 
 */
public final class CFFFDSelect {

    private int[] fdIndexs;
    
    public CFFFDSelect(int nGlyphs) {
        fdIndexs = new int[nGlyphs];
    }
    
    public int getNumGlyphs() {
        return fdIndexs.length;
    }
    
    public int getFDIndex(int gid) {
        return fdIndexs[gid];
    }
    
    /**
     * @param pos
     * @param rd
     * @throws IOException
     * @throws CFFFormatException
     */
    public void read(long pos, CFFRandomReader rd) throws IOException, CFFFormatException {
        rd.setPosition(pos);
        int format = rd.readCFFCard8();
        if (format == 0) {
            for (int i=0; i<fdIndexs.length; ++i) {
                fdIndexs[i] = rd.readCFFCard8();
            }
        }
        else if (format == 3) {
            int nRanges = rd.readCFFCard16();
            int[] ranges = new int[nRanges * 2];
            for (int i=0; i<nRanges; ++i) {
                ranges[i * 2] = rd.readCFFCard16();
                ranges[i * 2 + 1] = rd.readCFFCard8();
            }
            int sentinel = rd.readCFFCard16();
            for (int i=0; i<nRanges; ++i) {
                int first = ranges[i * 2];
                int fd    = ranges[i * 2 + 1];
                int last;
                if (i == nRanges - 1) {
                    last = sentinel;
                }
                else {
                    last = ranges[i * 2 + 2];                    
                }
                for (int j=first; j<last; j++) {
                    fdIndexs[j] = fd;
                }
            }
        }
        else {
            throw new CFFFormatException("invalid format for FDSelect", rd.getPosition() - 1);
        }
    }

}
