package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFFormatException;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public abstract class OTFGCommonTable extends OTFTable {

    private int version;

    private OTFScriptListTable scriptListTable;
    
    private OTFFeatureListTable featureListTable;
    
    private OTFLookupListTable lookupListTable;

    public int getVersion() {
    	return this.version;
    }
    
    public OTFScriptListTable getScriptListTable() {
    	return this.scriptListTable;
    }
    
    public OTFFeatureListTable getFeatureListTable() {
    	return this.featureListTable;
    }
    
    public OTFLookupListTable getLookupListTable() {
    	return this.lookupListTable;
    }
    
    public OTFLangSysTable getLangSysTable(int scriptTag, int langSysTag) {
		OTFScriptTable scriptTable = this.getScriptListTable().getScriptTableByTag(scriptTag);
		if (scriptTable != null) {
			return scriptTable.getLangSysTableByTag(langSysTag);
		}
		return null;
    }
    
    public OTFFeatureTable[] getSupportedFeatures(OTFLangSysTable langSysTable) {
    	if (langSysTable == null) {
    		return new OTFFeatureTable[0];
    	}
       	OTFFeatureTable[] records = new OTFFeatureTable[langSysTable.getFeatureCount()];
    	for (int i=0; i<langSysTable.getFeatureCount(); ++i) {
    		int featureIndex = langSysTable.getFeatureIndex(i);
    		records[i] = this.getFeatureListTable().getFeatureTable(featureIndex); 
    	}
    	return records;
    }
    
    public OTFFeatureTable getRequiredFeature(OTFLangSysTable langSysTable) {
    	if (langSysTable == null) {
    		return null;
    	}
    	int featureIndex = langSysTable.getReqFeatureIndex();
    	if (featureIndex == OTFLangSysTable.NO_REQUIRED_FEATURE) {
    		return null;
    	} else {
    		return this.getFeatureListTable().getFeatureTable(featureIndex); 
    	}
    }
    
    public OTFFeatureTable getFeature(OTFLangSysTable langSysTable, int featureTag) {
    	OTFFeatureTable required = getRequiredFeature(langSysTable);
    	if (required != null && required.getFeatureTag() == featureTag) {
    		return required;
    	}
    	
    	OTFFeatureTable[] supported = getSupportedFeatures(langSysTable);
    	for (OTFFeatureTable record : supported) {
    		if (record.getFeatureTag() == featureTag) {
    			return record;
    		}
    	}
    	
    	return null;
    }
    
    public OTFLookupTable[] getLookupTables(OTFFeatureTable featureTable) {
    	if (featureTable == null) {
    		return new OTFLookupTable[0];
    	}
    	
    	OTFLookupTable[] tables = new OTFLookupTable[featureTable.getLookupCount()];
    	for (int i=0; i<tables.length; ++i) {
    		int lookupIndex = featureTable.getLookupIndex(i);
    		tables[i] = this.lookupListTable.getLookupTable(lookupIndex);
    	}
    	return tables;
    }
    
	public OTFLookupTable[] getLookupTables(OTFLangSysTable langSysTable, int featureTag) {
		return this.getLookupTables(this.getFeature(langSysTable, featureTag));
	}
    
	@Override
	public void read(long offset, long length, TTFRandomReader rd)
			throws TTFFormatException, IOException {
		rd.setPosition(offset);

		this.version = rd.readTTFLong();
        int scriptListOffset  = rd.readTTFUShort();
        int featureListOffset = rd.readTTFUShort();
        int lookupListOffset  = rd.readTTFUShort();

        rd.setPosition(offset + scriptListOffset);
        this.scriptListTable = new OTFScriptListTable();  
        this.scriptListTable.read(rd);
        
        rd.setPosition(offset + featureListOffset);
        this.featureListTable = new OTFFeatureListTable();
        this.featureListTable.read(rd);
        
        rd.setPosition(offset + lookupListOffset);
        this.lookupListTable = new OTFLookupListTable(getLookupSubTableReader());
        this.lookupListTable.read(rd);
	}

	protected abstract OTFLookupSubTableReader getLookupSubTableReader();
	
}
