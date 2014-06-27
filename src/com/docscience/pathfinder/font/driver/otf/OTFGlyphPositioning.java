package com.docscience.pathfinder.font.driver.otf;

import com.docscience.pathfinder.font.driver.otf.OTFGPosTable.PairPosSubst;

public class OTFGlyphPositioning {
    
    private final OTFGPosTable gposTable;
    private OTFLangSysTable langSysTable;

    private OTFLookupTable[] kernTables;
    
    public OTFGlyphPositioning(OTFGPosTable gposTable) {
        this.gposTable = gposTable;
    }
    
    public boolean setScriptAndLangSys(int scriptTag, int langSysTag) {
        this.langSysTable = this.gposTable.getLangSysTable(scriptTag, langSysTag);
        if (this.langSysTable != null) {
            this.kernTables = this.gposTable.getLookupTables(this.langSysTable, OTFTypography.FEATURE_TAG_KERN);
            return true;
        } else {
            return false;
        }
    }
    
    public int getKernByGlyphPair(int firstGlyph, int secondGlyph) {
        if (this.kernTables == null) {
            return 0;
        }
        for (int i = 0; i < this.kernTables.length; ++i) {
            OTFLookupTable table = this.kernTables[i];
            for (int j = 0; j < table.getLookupSubTableCount(); ++j) {
                PairPosSubst st = (PairPosSubst) table.getLookupSubTable(j);
                if(st != null){
                    int kern = st.getKernByGlyphPair(firstGlyph, secondGlyph);
                    if(kern != Integer.MAX_VALUE){
                        return kern;
                    }
                }
            }
        }
        return 0;
    }
}
