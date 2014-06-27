package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFHelper;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public class OTFLangSysTable {
    
	public static final int NO_REQUIRED_FEATURE = 0xFFFF;
	
	private int langSysTag;
	
	private String langSysTagString;
	
	private int lookupOrder;

    private int reqFeatureIndex;

    private int[] featureIndexes;

    public OTFLangSysTable(int tag) {
    	this.langSysTag = tag;
    	this.langSysTagString = TTFHelper.decomposeTag(tag);
	}
    
    public int getLangSysTag() {
    	return this.langSysTag;
    }
    
    public String getLangSysTagString() {
    	return this.langSysTagString;
    }

	public int getLookupOrder() {
		return this.lookupOrder;
	}

	public int getReqFeatureIndex() {
		return this.reqFeatureIndex;
	}

	public int getFeatureCount() {
		return this.featureIndexes.length;
	}
	
	public int getFeatureIndex(int index) {
		return this.featureIndexes[index];
	}

	public void read(TTFRandomReader reader) throws IOException {
        this.lookupOrder = reader.readTTFUShort();
        this.reqFeatureIndex = reader.readTTFUShort();
        
        int featureCount = reader.readTTFUShort();
        this.featureIndexes = new int[featureCount];
        for (int i = 0; i < featureCount; i++) {
            this.featureIndexes[i] = reader.readTTFUShort();
        }
    }
}