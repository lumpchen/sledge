package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * Abstract description of TrueType Table.
 * 
 * @author wxin
 *
 */
abstract public class TTFTable {
    
    public static final int TAG_ACNT_TABLE = TTFHelper.composeTag("acnt"); // (accent attachment) table
    public static final int TAG_AVAR_TABLE = TTFHelper.composeTag("avar"); // (axis variation) table
    public static final int TAG_BDAT_TABLE = TTFHelper.composeTag("bdat"); // (bitmap data) table
    public static final int TAG_BHED_TABLE = TTFHelper.composeTag("bhed"); // (bitmap font header) table
    public static final int TAG_BLOC_TABLE = TTFHelper.composeTag("bloc"); // (bitmap location) table
    public static final int TAG_BSLN_TABLE = TTFHelper.composeTag("bsln"); // (baseline) table
    public static final int TAG_CMAP_TABLE = TTFHelper.composeTag("cmap"); // (character code mapping) table
    public static final int TAG_CVAR_TABLE = TTFHelper.composeTag("cvar"); // (CVT variation) table
    public static final int TAG_CVT__TABLE = TTFHelper.composeTag("cvt "); // (control value) table
    public static final int TAG_EBSC_TABLE = TTFHelper.composeTag("EBSC"); // (embedded bitmap scaling control) table
    public static final int TAG_FDSC_TABLE = TTFHelper.composeTag("fdsc"); // (font descriptor) table
    public static final int TAG_FEAT_TABLE = TTFHelper.composeTag("feat"); // (layout feature) table
    public static final int TAG_FMTX_TABLE = TTFHelper.composeTag("fmtx"); // (font metrics) table
    public static final int TAG_FPGM_TABLE = TTFHelper.composeTag("fpgm"); // (font program) table
    public static final int TAG_FVAR_TABLE = TTFHelper.composeTag("fvar"); // (font variation) table
    public static final int TAG_GASP_TABLE = TTFHelper.composeTag("gasp"); // (grid-fitting and scan-conversion procedure) table
    public static final int TAG_GLYF_TABLE = TTFHelper.composeTag("glyf"); // (glyph outline) table
    public static final int TAG_GVAR_TABLE = TTFHelper.composeTag("gvar"); // (glyph variation) table
    public static final int TAG_HDMX_TABLE = TTFHelper.composeTag("hdmx"); // (horizontal device metrics) table
    public static final int TAG_HEAD_TABLE = TTFHelper.composeTag("head"); // (font header) table
    public static final int TAG_HHEA_TABLE = TTFHelper.composeTag("hhea"); // (horizontal header) table
    public static final int TAG_HMTX_TABLE = TTFHelper.composeTag("hmtx"); // (horizontal metrics) table
    public static final int TAG_HSTY_TABLE = TTFHelper.composeTag("hsty"); // (horizontal style) table
    public static final int TAG_JUST_TABLE = TTFHelper.composeTag("just"); // (justification) table
    public static final int TAG_KERN_TABLE = TTFHelper.composeTag("kern"); // (kerning) table
    public static final int TAG_LCAR_TABLE = TTFHelper.composeTag("lcar"); // (ligature caret) table
    public static final int TAG_LOCA_TABLE = TTFHelper.composeTag("loca"); // (glyph location) table
    public static final int TAG_MAXP_TABLE = TTFHelper.composeTag("maxp"); // (maximum profile) table
    public static final int TAG_MORT_TABLE = TTFHelper.composeTag("mort"); // (metamorphosis) table
    public static final int TAG_MORX_TABLE = TTFHelper.composeTag("morx"); // (extended metamorphosis) table
    public static final int TAG_NAME_TABLE = TTFHelper.composeTag("name"); // (name) table
    public static final int TAG_OPBD_TABLE = TTFHelper.composeTag("opbd"); // (optical bounds) table
    public static final int TAG_OS_2_TABLE = TTFHelper.composeTag("OS/2"); // (compatibility) table
    public static final int TAG_POST_TABLE = TTFHelper.composeTag("post"); // (glyph name and PostScript compatibility) table
    public static final int TAG_PREP_TABLE = TTFHelper.composeTag("prep"); // (control value program) table
    public static final int TAG_PROP_TABLE = TTFHelper.composeTag("prop"); // (properties) table
    public static final int TAG_TRAK_TABLE = TTFHelper.composeTag("trak"); // (tracking) table
    public static final int TAG_VHEA_TABLE = TTFHelper.composeTag("vhea"); // (vertical header) table
    public static final int TAG_VMTX_TABLE = TTFHelper.composeTag("vmtx"); // (vertical metrics) table
    public static final int TAG_ZAPF_TABLE = TTFHelper.composeTag("Zapf"); // (glyph reference) table
    public static final int TAG_CFF__TABLE = TTFHelper.composeTag("CFF "); // (cff data) table
    public static final int TAG_PCLT_TABLE = TTFHelper.composeTag("PCLT"); // (pclt data) table
    
    public abstract int getTag();
    
    public abstract void read(long offset, long length, TTFRandomReader rd) throws TTFFormatException, IOException ;
}
