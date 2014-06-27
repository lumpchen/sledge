package com.docscience.pathfinder.font.driver.otf;

import com.docscience.pathfinder.font.driver.ttf.TTFHelper;
import com.docscience.pathfinder.font.driver.ttf.TTFTable;

public abstract class OTFTable extends TTFTable {
	
    public static final int TAG_BASE_TABLE = TTFHelper.composeTag("BASE"); // Baseline data 
    public static final int TAG_GDEF_TABLE = TTFHelper.composeTag("GDEF"); // Glyph definition data 
    public static final int TAG_GPOS_TABLE = TTFHelper.composeTag("GPOS"); // Glyph positioning data 
    public static final int TAG_GSUB_TABLE = TTFHelper.composeTag("GSUB"); // Glyph substitution data 
    public static final int TAG_JSTF_TABLE = TTFHelper.composeTag("JSTF"); // Justification data 
    public static final int TAG_DSIG_TABLE = TTFHelper.composeTag("DSIG"); // Digital signature

}
