package com.docscience.pathfinder.font.driver.cff;

import java.io.IOException;

/**
 * @author wxin
 *
 */
public final class CFFNameIndex extends CFFIndexData {

    private String[] nameTable;
    
    public int getNumNames() {
        return nameTable.length;
    }
    
    public String getName(int index) {
        return nameTable[index];
    }
    
    @Override
	public void read(long pos, CFFRandomReader rd) throws IOException, CFFFormatException {
        super.read(pos, rd);
        byte[] tmp = null;
        nameTable = new String[getNumData()];
        for (int i=0; i<getNumData(); ++i) {
            int len = (int) getDataLength(i);
            
            if (tmp == null || tmp.length < len) {
                tmp = new byte[len];
            }
            
            rd.setPosition(getDataPosition(i));
            rd.readBytes(tmp, 0, len);
            
            nameTable[i] = new String(tmp, 0, len, "ASCII");
        }
    }    
    
}
