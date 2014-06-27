package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Font Directory
 * 
 * <pre> 
 * Type    Name           Description
 * ---------------------------------------------------------------
 * Fixed   sfnt version   0x00010000 for version 1.0.
 * USHORT  numTables      Number of tables.
 * USHORT  searchRange    16 x (Maximum power of 2 <= numTables).
 * USHORT  entrySelector  Log2(maximum power of 2 <= numTables).
 * USHORT  rangeShift     NumTables x 16-searchRange.
 * </pre>
 *   
 * @author wxin
 * 
 */
public final class TTFFontDirectory {
    
    public static final int TAG_OTF_TTF_WINDOWS    = 0x00010000; // sfnt version 1.0 in ttf file or otf file with truetype font
    public static final int TAG_OTF_TTF_MACHINTOSH = 0x74727565; // 'ture'
    public static final int TAG_OTF_CFF = 0x4F54544F; // 'OTTO'
    
    private int tag;
    private int numTables;
    private int searchRange;
    private int entrySelector;
    private int rangeShift;
    private Map<Integer, Integer> tableMap = new HashMap<Integer, Integer>();
    
    private TTFTableDirectory[] tableDirectorys;

    public boolean withTTFOutline() {
        return tag == TAG_OTF_TTF_WINDOWS || tag == TAG_OTF_TTF_MACHINTOSH;
    }
    
    public boolean withCFFOutline() {
        return tag == TAG_OTF_CFF;
    }
    
    public int getTag() {
        return tag;
    }
    
    public int getNumTables() {
        return numTables;
    }
    
    public TTFTableDirectory getTableDirectory(int i) {
        return tableDirectorys[i];
    }
    
    public int getSearchRange() {
        return searchRange;
    }
    
    public int getEntrySelector() {
        return entrySelector;
    }

    public int getRangeShift() {
        return rangeShift;
    }
    
    public boolean hasTable(int tag) {
        return tableMap.containsKey(new Integer(tag));
    }
    
    public TTFTableDirectory getTableDirectoryByTag(int tag) {
        Integer i = tableMap.get(new Integer(tag));
        if (i == null) {
            return null;
        }
        else {
            return tableDirectorys[i.intValue()];
        }
    }

    public void read(TTFRandomReader rd) throws TTFFormatException, IOException {
        tag = rd.readTTFTag();
        if (tag != TAG_OTF_TTF_WINDOWS && tag != TAG_OTF_TTF_MACHINTOSH && tag != TAG_OTF_CFF) {
            throw new TTFFormatException("bad snft version", rd.getPosition() - 4);
        }
        numTables = rd.readTTFUShort();
        searchRange = rd.readTTFUShort();
        entrySelector = rd.readTTFUShort();
        rangeShift = rd.readTTFUShort();
        tableDirectorys = new TTFTableDirectory[numTables];
        for (int i=0; i<numTables; ++i) {
            tableDirectorys[i] = new TTFTableDirectory();
            tableDirectorys[i].read(rd);
            tableMap.put(new Integer(tableDirectorys[i].getTag()), new Integer(i));
        }
    }
}
