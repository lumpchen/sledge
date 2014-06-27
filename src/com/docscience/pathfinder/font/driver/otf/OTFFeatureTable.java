package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFHelper;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public class OTFFeatureTable {
    
	private int featureTag;
	
	private String featureTagString;

	private int featureParams; // reserved. UINT

    private int[] lookupIndexes;
    
    public OTFFeatureTable(int tag) {
    	this.featureTag = tag;
    	this.featureTagString = TTFHelper.decomposeTag(tag);
	}
    
    public int getFeatureTag() {
    	return featureTag;
    }
    
    public String getFeatureTagString() {
    	return featureTagString;
    }

	public int getFeatureParams() {
    	return featureParams;
    }
    
    public int getLookupCount() {
    	return this.lookupIndexes.length;
    }
    
    public int getLookupIndex(int index) {
    	return this.lookupIndexes[index];
    }

	public void read(TTFRandomReader rd) throws IOException {
		featureParams = rd.readTTFOffset();
		int lookupCount = rd.readTTFUShort();
		this.lookupIndexes = new int[lookupCount];
		for (int i=0; i<lookupCount; ++i) {
			this.lookupIndexes[i] = rd.readTTFUShort();
		}
	}
}