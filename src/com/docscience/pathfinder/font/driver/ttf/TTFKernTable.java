package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

public class TTFKernTable extends TTFTable {
    
    private int version;
    private TTFKernSubTable[] subTables;

    @Override
	public int getTag() {
        return TAG_KERN_TABLE;
    }

    @Override
	public void read(long offset, long length, TTFRandomReader rd)
            throws TTFFormatException, IOException {
        rd.setPosition(offset);
        version = rd.readTTFUShort();
        int numSubTables = rd.readTTFUShort();
        subTables = new TTFKernSubTable[numSubTables];
        
        long pos = offset + 4;
        for (int i=0; i<numSubTables; ++i) {
        	rd.setPosition(pos);
        	TTFKernSubTable subTable = new TTFKernSubTable();
        	subTable.read(rd);
            pos += subTable.getLength();
            subTables[i] = subTable;
        }
    }
    
    public int getVersion() {
        return version;
    }
    
    public int getNumKernSubTables() {
        return subTables.length;
    }
    
    public TTFKernSubTable getKernSubTable(int index) {
    	return subTables[index];
    }
    
}
