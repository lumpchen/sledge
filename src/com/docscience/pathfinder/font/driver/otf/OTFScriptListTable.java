package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFFormatException;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public class OTFScriptListTable {

    private OTFScriptTable[] scriptTables;
        
    public int getNumScriptTables() {
    	return this.scriptTables.length;
    }
    
    public OTFScriptTable getScriptTable(int index) {
    	return this.scriptTables[index];
    }
    
    public OTFScriptTable getScriptTableByTag(int scriptTag) {
    	for (int i=0; i<this.scriptTables.length; ++i) {
    		if (this.scriptTables[i].getScriptTag() == scriptTag) {
    			return this.scriptTables[i];
    		}
    	}
    	return null;
    }
    
	public void read(TTFRandomReader rd)
			throws TTFFormatException, IOException {
		long offset = rd.getPosition();
        int scriptCount = rd.readTTFUShort();
        
        int[] scriptTags = new int[scriptCount];
        int[] scriptOffsets = new int[scriptCount];
        for (int i = 0; i < scriptCount; i++) {
            scriptTags[i] = rd.readTTFTag();
            scriptOffsets[i] = rd.readTTFUShort();
        }
        
        this.scriptTables = new OTFScriptTable[scriptCount];
        for (int i = 0; i < scriptCount; i++) {
        	rd.setPosition(offset + scriptOffsets[i]);
        	this.scriptTables[i] = new OTFScriptTable(scriptTags[i]);
        	this.scriptTables[i].read(rd);
        }
	}
}