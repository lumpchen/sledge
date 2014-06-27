package com.docscience.pathfinder.font.driver.cff;

import java.io.IOException;

/**
 * @author wxin
 *
 */
public class CFFIndexData {
    
    private long nextPosition;
    private long[] dataPositions;
    
    /**
     * Get position of the next table.
     * 
     * @return position value.
     */
    public long getNextPosition() {
        return nextPosition;
    }
    
    public int getNumData() {
        return dataPositions == null ? 0 : dataPositions.length;
    }
    
    public long getDataPosition(int i) {
        return dataPositions[i];
    }
    
    public long getDataLength(int i) {
        if (i == dataPositions.length - 1) {
            return nextPosition - dataPositions[i];
        }
        return dataPositions[i + 1] - dataPositions[i];
    }
    
    public void read(long pos, CFFRandomReader rd) throws IOException, CFFFormatException {
        rd.setPosition(pos);
        int count = rd.readCFFCard16();
        if (count == 0) {
            nextPosition = rd.getPosition();
            return;
        }
        int offSize = rd.readCFFOffSize();
        long base = rd.getPosition();
        dataPositions = new long[count];
        switch(offSize) {
        case 1:
            base += count;
            for (int i=0; i<count; ++i) {
                dataPositions[i] = rd.readCFFOffset1() + base;
            }
            nextPosition = rd.readCFFOffset1() + base;
            break;
        case 2:
            base += (count + 1) * 2 - 1;
            for (int i=0; i<count; ++i) {
                dataPositions[i] = rd.readCFFOffset2() + base;
            }
            nextPosition = rd.readCFFOffset2() + base;
            break;
        case 3:
            base += (count + 1) * 3 - 1;
            for (int i=0; i<count; ++i) {
                dataPositions[i] = rd.readCFFOffset3() + base;
            }
            nextPosition = rd.readCFFOffset3() + base;
            break;
        case 4:
            base += (count + 1) * 4 - 1;
            for (int i=0; i<count; ++i) {
                dataPositions[i] = rd.readCFFOffset4() + base;
            }
            nextPosition = rd.readCFFOffset4() + base;
            break;
        default:
            throw new CFFFormatException("invalid offSize data", rd.getPosition() - 1);
        }
    }
    
}
