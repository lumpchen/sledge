package com.docscience.pathfinder.font.driver.cff;

import java.io.IOException;

/**
 * @author wxin
 *
 */
public final class CFFFile {
    
    private CFFHeader header;
    private CFFNameIndex nameIndex;
    private CFFIndexData topIndex;
    private CFFStringIndex stringIndex;
    private CFFSubrIndex globalSubrIndex;
    
    public CFFHeader getHeader() {
        return header;
    }
    
    public CFFNameIndex getNameIndex() {
        return nameIndex;
    }
    
    public CFFIndexData getTopIndex() {
        return topIndex;
    }
    
    public CFFStringIndex getStringIndex() {
        return stringIndex;
    }
    
    public CFFSubrIndex getGlobalSubrIndex() {
        return globalSubrIndex;
    }
    
    public void read(CFFRandomReader rd) throws IOException, CFFFormatException {
        header = new CFFHeader();
        header.read(rd);
        if (header.getMajor() != 1) {
            throw new CFFFormatException("bad major version of cff header");
        }
        if (header.getMinor() != 0) {
            throw new CFFFormatException("bad minor version of cff header");
        }
        nameIndex = new CFFNameIndex();
        nameIndex.read(header.getHdrSize(), rd);
        topIndex = new CFFIndexData();
        topIndex.read(nameIndex.getNextPosition(), rd);
        stringIndex = new CFFStringIndex();
        stringIndex.read(topIndex.getNextPosition(), rd);
        globalSubrIndex = new CFFSubrIndex();
        globalSubrIndex.read(stringIndex.getNextPosition(), rd);
    }
    
}
