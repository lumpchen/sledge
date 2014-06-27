package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public class OTFFeatureListTable {

    private OTFFeatureTable[] featureTables;
    
	public OTFFeatureTable getFeatureTable(int index) {
		return this.featureTables[index];
	}
	
	public int getNumFeatureTables() {
		return this.featureTables.length;
	}

	public void read(TTFRandomReader rd) throws IOException {
		long offset = rd.getPosition();
        int featureCount = rd.readTTFUShort();

        int[] featureTags = new int[featureCount];
        int[] featureOffsets = new int[featureCount];
        for (int i = 0; i < featureCount; i++) {
            featureTags[i] = rd.readTTFTag();
            featureOffsets[i] = rd.readTTFUShort();
        }
        
        this.featureTables = new OTFFeatureTable[featureCount];
        for (int i = 0; i < featureCount; i++) {
        	rd.setPosition(offset + featureOffsets[i]);
        	this.featureTables[i] = new OTFFeatureTable(featureTags[i]);
        	this.featureTables[i].read(rd);
        }
    }
}