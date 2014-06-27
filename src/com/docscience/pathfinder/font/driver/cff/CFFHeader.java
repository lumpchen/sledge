package com.docscience.pathfinder.font.driver.cff;

import java.io.IOException;

/**
 * @author wxin
 *
 */
public final class CFFHeader {
    
    private int major;
    private int minor;
    private int hdrSize;
    private int offSize;
    
    public final int getHdrSize() {
        return hdrSize;
    }

    public final int getMajor() {
        return major;
    }

    public final int getMinor() {
        return minor;
    }

    public final int getOffSize() {
        return offSize;
    }

    public void read(CFFRandomReader rd) throws IOException, CFFFormatException {
        major = rd.readCFFCard8();
        minor = rd.readCFFCard8();
        hdrSize = rd.readCFFCard8();
        offSize = rd.readCFFOffSize();
        if (offSize < 1 || offSize > 4) {
            throw new CFFFormatException("invalid offSize data", rd.getPosition() - 1);
        }
    }
    
}
