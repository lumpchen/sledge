/**
 * 
 */
package com.docscience.pathfinder.font.driver.otf;

import com.docscience.pathfinder.font.driver.otf.OTFGSubTable.LigatureSubst;
import com.docscience.pathfinder.font.driver.otf.OTFGSubTable.SingleSubst;

public class OTFGlyphSubstitute {
	
	private final OTFGSubTable gsubTable;
	@SuppressWarnings("unused")
	private final OTFGDefTable gdefTable;
	private OTFLangSysTable langSysTable;

	private OTFLookupTable[] isolTables;
	private OTFLookupTable[] initTables;
	private OTFLookupTable[] mediTables;
	private OTFLookupTable[] finaTables;
	private OTFLookupTable[] ligaTables;
	private OTFLookupTable[] rligTables;

	
	public OTFGlyphSubstitute(OTFGSubTable gsubTable, OTFGDefTable gdefTable) {
		this.gsubTable = gsubTable;
		this.gdefTable = gdefTable;
	}
	
	public boolean setScriptAndLangSys(int scriptTag, int langSysTag) {
		this.langSysTable = this.gsubTable.getLangSysTable(scriptTag, langSysTag);
		if (this.langSysTable != null) {
			this.isolTables = this.gsubTable.getLookupTables(this.langSysTable, OTFTypography.FEATURE_TAG_ISOL);
			this.initTables = this.gsubTable.getLookupTables(this.langSysTable, OTFTypography.FEATURE_TAG_INIT);
			this.mediTables = this.gsubTable.getLookupTables(this.langSysTable, OTFTypography.FEATURE_TAG_MEDI);
			this.finaTables = this.gsubTable.getLookupTables(this.langSysTable, OTFTypography.FEATURE_TAG_FINA);
			this.ligaTables = this.gsubTable.getLookupTables(this.langSysTable, OTFTypography.FEATURE_TAG_LIGA);
			this.rligTables = this.gsubTable.getLookupTables(this.langSysTable, OTFTypography.FEATURE_TAG_RLIG);
			return true;
		} else {
			return false;
		}
	}

	public int[] substituteLiga(int[] glyphs) {
		if (this.ligaTables == null) {
			return glyphs;
		}
		
		for (int i=0; i<this.ligaTables.length; ++i) {
			OTFLookupTable table = this.ligaTables[i];
			for (int j=0; j<table.getLookupSubTableCount(); ++j) {
				LigatureSubst st = (LigatureSubst) table.getLookupSubTable(j);
				glyphs = st.substitute(glyphs);
			}
		}
		
		return glyphs;
	}
	
	public int[] substituteRlig(int[] glyphs) {
		if (this.rligTables == null) {
			return glyphs;
		}
		
		for (int i=0; i<this.rligTables.length; ++i) {
			OTFLookupTable table = this.rligTables[i];
			for (int j=0; j<table.getLookupSubTableCount(); ++j) {
				LigatureSubst st = (LigatureSubst) table.getLookupSubTable(j);
				glyphs = st.substitute(glyphs);
			}
		}
		
		return glyphs;
	}
	
	public int substituteIsol(int glyph) {
		if (this.isolTables == null) {
			return glyph;
		}
		
		for (int i=0; i<this.isolTables.length; ++i) {
			OTFLookupTable table = this.isolTables[i];
			for (int j=0; j<table.getLookupSubTableCount(); ++j) {
				SingleSubst st = (SingleSubst) table.getLookupSubTable(j);
				if (st.getSubstitute(glyph) != glyph) {
					return st.getSubstitute(glyph);
				}
			}
		}
		
		return glyph;
	}
	
	public int substituteInit(int glyph) {
		if (this.initTables == null) {
			return glyph;
		}
		
		for (int i=0; i<this.initTables.length; ++i) {
			OTFLookupTable table = this.initTables[i];
			for (int j=0; j<table.getLookupSubTableCount(); ++j) {
				SingleSubst st = (SingleSubst) table.getLookupSubTable(j);
				if (st.getSubstitute(glyph) != glyph) {
					return st.getSubstitute(glyph);
				}
			}
		}
		
		return glyph;
	}
	
	public int substituteMedi(int glyph) {
		if (this.mediTables == null) {
			return glyph;
		}
		
		for (int i=0; i<this.mediTables.length; ++i) {
			OTFLookupTable table = this.mediTables[i];
			for (int j=0; j<table.getLookupSubTableCount(); ++j) {
				SingleSubst st = (SingleSubst) table.getLookupSubTable(j);
				if (st.getSubstitute(glyph) != glyph) {
					return st.getSubstitute(glyph);
				}
			}
		}
		
		return glyph;
	}
	 
	public int substituteFina(int glyph) {
		if (this.finaTables == null) {
			return glyph;
		}
		
		for (int i=0; i<this.finaTables.length; ++i) {
			OTFLookupTable table = this.finaTables[i];
			for (int j=0; j<table.getLookupSubTableCount(); ++j) {
				SingleSubst st = (SingleSubst) table.getLookupSubTable(j);
				if (st.getSubstitute(glyph) != glyph) {
					return st.getSubstitute(glyph);
				}
			}
		}
		
		return glyph;
	}
}