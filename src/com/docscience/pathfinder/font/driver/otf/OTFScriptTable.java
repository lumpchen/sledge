package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFHelper;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public class OTFScriptTable {
	
	private int scriptTag;
	
	private String scriptTagString;
    
	private OTFLangSysTable defaultLangSysTable;
	
    private OTFLangSysTable[] langSysTables;
    
    public OTFScriptTable(int tag) {
    	this.scriptTag = tag;
    	this.scriptTagString = TTFHelper.decomposeTag(tag);
	}
    
    public int getScriptTag() {
    	return this.scriptTag;
    }
    
    public String getScriptTagString() {
    	return this.scriptTagString;
    }

	public OTFLangSysTable getDefaultLangSysTable() {
    	return this.defaultLangSysTable;
    }
    
    public int getNumLangSysTables() {
    	return this.langSysTables.length;
    }
    
    public OTFLangSysTable getLangSysTable(int index) {
    	return this.langSysTables[index];
    }
    
    public OTFLangSysTable getLangSysTableByTag(int langSysTag) {
    	for (int i=0; i<this.langSysTables.length; ++i) {
    		if (this.langSysTables[i].getLangSysTag() == langSysTag) {
    			return this.langSysTables[i];
    		}
    	}
    	return this.defaultLangSysTable;
    }
    
    public void read(TTFRandomReader rd) throws IOException {
        long offset = rd.getPosition();

        int defaultLangSysOffset = rd.readTTFUShort();
        int langSysCount = rd.readTTFUShort();

        int[] langSysOffsets = new int[langSysCount];
        int[] langSysTags = new int[langSysCount];
        for (int i = 0; i < langSysCount; i++) {
        	langSysTags[i] = rd.readTTFTag();
        	langSysOffsets[i] = rd.readTTFUShort();
        }

        this.langSysTables = new OTFLangSysTable[langSysCount];
        for (int i = 0; i < langSysCount; i++) {
            rd.setPosition(offset + langSysOffsets[i]);
            this.langSysTables[i] = new OTFLangSysTable(langSysTags[i]);
            this.langSysTables[i].read(rd);
        }
        
        if (defaultLangSysOffset > 0) {
        	rd.setPosition(offset + defaultLangSysOffset);
        	this.defaultLangSysTable = new OTFLangSysTable(-1);
        	this.defaultLangSysTable.read(rd);
        }
    }
}