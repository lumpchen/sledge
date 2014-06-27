package com.docscience.pathfinder.font.driver.cff;

import java.io.IOException;

/**
 * @author wxin
 *
 */
public final class CFFSubrIndex extends CFFIndexData {
    
    private byte[][] subrBytes;
    private int charstringType;
    private int bias;

    public static int calcBias(int charstringType, int nSubrs) {
        assert(charstringType == 1 || charstringType == 2);
        if (charstringType == 1) {
            return 0;
        }
        else if (nSubrs < 1240) {
            return 107;
        }
        else if (nSubrs < 33900) {
            return 1131;
        }
        else {
            return 32768;
        }
    }
    
    public CFFSubrIndex() {
        this(2);
    }
    
    public CFFSubrIndex(int charstringType) {
        assert(charstringType == 1 || charstringType == 2);
        this.charstringType = charstringType;
    }
    
    public int getBias() {
        return bias;
    }

    public int getNumSubrs() {
        return subrBytes.length;
    }
    
    public byte[] getSubr(int index) {
        return subrBytes[index];
    }
    
    @Override
	public void read(long pos, CFFRandomReader rd) throws IOException, CFFFormatException {
        super.read(pos, rd);
        subrBytes = new byte[getNumData()][];
        for (int i=0; i<getNumData(); ++i) {
            int len = (int) getDataLength(i);
            subrBytes[i] = new byte[len];
            
            rd.setPosition(getDataPosition(i));
            rd.readBytes(subrBytes[i], 0, len);
        }
        bias = calcBias(charstringType, getNumData());
    }
    
}
