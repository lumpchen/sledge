package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFFormatException;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public class OTFLookupListTable {
	
	private OTFLookupSubTableReader subTableFactory;
	private OTFLookupTable[] lookupTables;
	
	public OTFLookupListTable(OTFLookupSubTableReader subTableFactory) {
		this.subTableFactory = subTableFactory;
	}
	
    public void read(TTFRandomReader rd) throws IOException, TTFFormatException {
    	long offset = rd.getPosition();
    	
        int lookupCount = rd.readTTFUShort();
        int[] lookupOffsets = new int[lookupCount];
        for (int i = 0; i < lookupCount; i++) {
            lookupOffsets[i] = rd.readTTFUShort();
        }
        
        lookupTables = new OTFLookupTable[lookupCount];
        for (int i = 0; i < lookupCount; i++) {
        	rd.setPosition(offset + lookupOffsets[i]);
        	lookupTables[i] = new OTFLookupTable(this.subTableFactory);
        	lookupTables[i].read(rd);
        }
    }

    public int getNumLookupTables() {
    	return this.lookupTables.length;
    }
    
	public OTFLookupTable getLookupTable(int lookupIndex) {
		return this.lookupTables[lookupIndex];
	}
	
}